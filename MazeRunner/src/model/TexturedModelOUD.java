package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import de.matthiasmann.twl.utils.PNGDecoder;//use TWL's png decoder

	// Baseer code op:
	// https://github.com/demoscenepassivist/SocialCoding/tree/master/code_demos_jogamp/src/framework/util
	// ga uit van vertexBuffer, zie TextureUtils.java voor textureloading
	// https://docs.google.com/file/d/0B9hhZie2D-fEMzNjNDBmMjUtNGQzNS00ODY0LWJkYmUtNDM5Y2I5ODc0Y2Nj/edit?hl=en  <-lijkt de oplossing die ik zoek!

public class TexturedModel {
	// private int texture;
	private int shaderProgramHandle;
	private int vboVertexHandle[] = new int[1];
	private int vboNormalHandle[] = new int [1];
	private int vboTexCoordHandle[] = new int [1];
	private int vertexShaderHandle;
	private int fragmentShaderHandle;
	private int diffuseModifierUniform;
	private Texture texture;
	private int objectlist;
	private int[] tex;
	
	private  Model m;
	
	public TexturedModel(GL gl,Model model){
		this.m = model;
		//this.loadTexture(gl);
		setupTextures(gl, "models/Lambent_Male/Lambent_Male_D.png");
		this.setUpVBOs(gl);
		setUpLighting(gl);
		
		//this.setupShaders(gl);
	}
	
	public static int setupTextures(GL gl, String filename){
		//acquire the id
		int[] id=new int[1];
		gl.glGenTextures(1, id, 0);
		try{
			InputStream in=new FileInputStream(filename);//open and read the file
			PNGDecoder decoder=new PNGDecoder(in);//hand it to the decoder
			//setup ByteBuffer and decode png file to it
			ByteBuffer 
			data=ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
			decoder.decode(data, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
			data.rewind();
			//bind buffer to the id and set some parameters
			gl.glBindTexture(gl.GL_TEXTURE_2D, id[0]);
			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
			gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, gl.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, gl.GL_RGBA, gl.GL_UNSIGNED_BYTE, data);
			gl.glPixelStorei(gl.GL_UNPACK_ALIGNMENT, 4);
		}
		catch(FileNotFoundException e){
			System.out.println("Could not find " + filename);
		}
		catch(IOException e){
			System.out.println("Error decoding file " + filename);	
		}
		return id[0];
	
	 }
	
	public void loadTexture(GL gl){
		try {
			texture = TextureIO.newTexture(new File("models/Lambent_Male/Lambent_Male_D.tga"), false);
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	protected void finalize(GL gl) throws Throwable{
		try{
			this.cleanUp(gl);
		}finally{
			super.finalize();
		}
	}
	
	public void setUpVBOs(GL gl){
		gl.glGenBuffers(1,vboVertexHandle,0);
		gl.glGenBuffers(1,vboNormalHandle,0);
		gl.glGenBuffers(1,vboTexCoordHandle,0);
		
		if(!m.isLoaded()) // draw nothing if there is no model loaded, so stop init VBO
			return;
		
		int sizeOfBuffers = m.faces.size() *36; // 3*3*4
		FloatBuffer vertices = reserveData(sizeOfBuffers); // allocate memory for buffer
		FloatBuffer normals = reserveData(sizeOfBuffers); // allocate memory for buffer
		FloatBuffer texcoords = reserveData(sizeOfBuffers); // allocate memory for buffer
		
		for(Face face : m.faces){
			vertices.put(asFloats(m.vertices.get((int) face.vertex.x-1)));
			vertices.put(asFloats(m.vertices.get((int) face.vertex.y-1)));
			vertices.put(asFloats(m.vertices.get((int) face.vertex.z-1)));
			
			texcoords.put(asFloats(m.texcoords.get((int) face.texcoord.x-1)));
			texcoords.put(asFloats(m.texcoords.get((int) face.texcoord.y-1)));
			texcoords.put(asFloats(m.texcoords.get((int) face.texcoord.z-1)));
			
			normals.put(asFloats(m.normals.get((int) face.normals.x-1)));
			normals.put(asFloats(m.normals.get((int) face.normals.y-1)));
			normals.put(asFloats(m.normals.get((int) face.normals.z-1)));
		}
		// make the buffers readable for GL
		vertices.flip();
		normals.flip();
		//texcoords.flip();
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboVertexHandle[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,sizeOfBuffers,vertices, GL.GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER,vboNormalHandle[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,sizeOfBuffers,normals, GL.GL_STATIC_DRAW);
		
		// TODO loading & binding the textures
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER,vboTexCoordHandle[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,sizeOfBuffers,texcoords, GL.GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0); // unbind the buffer
	}
	
	public void setupShaders(GL gl){
		shaderProgramHandle = gl.glCreateProgram();
		vertexShaderHandle = gl.glCreateShader(GL.GL_VERTEX_SHADER);
		fragmentShaderHandle = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);
		
		String vertexShaderSource = new String();
		String fragmentShaderSource = new String();
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("src/model/shader.vert"));
			String line;
			
			while((line = reader.readLine()) != null){
				vertexShaderSource += line;
				vertexShaderSource += "\n";
			}
			reader.close();
			
		}catch(IOException e){
			System.err.println("Vertex shader wassn't loaded properly.");
			return;
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("src/model/shader.frag"));
			String line;
			
			while((line = reader.readLine()) != null){
				fragmentShaderSource += line;
				fragmentShaderSource += "\n";
			}
			reader.close();
			
		}catch(IOException e){
			System.err.println("Fragment shader wassn't loaded properly.");
			return;
		}
		IntBuffer temp = IntBuffer.allocate(1);
		gl.glShaderSource(vertexShaderHandle, 1, new String[]{vertexShaderSource},null);
		gl.glCompileShader(vertexShaderHandle);
		
		gl.glGetShaderiv(vertexShaderHandle, GL.GL_COMPILE_STATUS, temp);
		if(temp.get(0) == GL.GL_FALSE) {
			System.err.println("Vertex shader wasn't able to be compiled corectly.");
			return;}
		
		gl.glShaderSource(fragmentShaderHandle,1,new String[]{fragmentShaderSource},null);
		gl.glCompileShader(fragmentShaderHandle);
		
		gl.glGetShaderiv(fragmentShaderHandle, GL.GL_COMPILE_STATUS, temp);
		if(temp.get(0) == GL.GL_FALSE) {
			System.err.println("Fragment shader wasn't able to be compiled corectly.");
			return;}
		
		gl.glAttachShader(shaderProgramHandle, vertexShaderHandle);
		gl.glAttachShader(shaderProgramHandle, fragmentShaderHandle);
		
		gl.glLinkProgram(shaderProgramHandle);
		gl.glValidateProgram(shaderProgramHandle);
		
		diffuseModifierUniform = gl.glGetUniformLocation(shaderProgramHandle, "diffuseIntensityModifier");
		
		setUpLighting(gl);
	}
	
	public void cleanUp(GL gl){
		gl.glDeleteProgram(shaderProgramHandle);
		
		gl.glDeleteBuffers(1,vboVertexHandle,0);
		gl.glDeleteBuffers(1,vboNormalHandle,0);
		
		gl.glDeleteShader(vertexShaderHandle);
		gl.glDeleteShader(fragmentShaderHandle);
	}
	
	private void setUpLighting(GL gl){
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{0.5f,0.5f,0.05f,1f}));
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, asFloatBuffer(new float[]{0,0,0,1f}));
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_BACK);
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_DIFFUSE);
	}
		
	public void render(GL gl, double angle,double x,double y,double z){
		if(!m.isLoaded()) // there is nothing to render
			return;
		
//		this.objectlist = gl.glGenLists(1);
//
//        gl.glNewList(objectlist, GL.GL_COMPILE);
//        gl.glColor3f(1, 1, 1);
//        texture.enable();
//        texture.bind();
//        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
//        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
//        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
//        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		
		gl.glPushMatrix();
		
			gl.glTranslated(x, y, z);
			gl.glRotated(angle, 0, 1, 0);
			//gl.glUseProgram(shaderProgramHandle);
			//gl.glUniform1f(diffuseModifierUniform, 1.0f);
			//gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 10.0f);
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboVertexHandle[0]);
			gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboNormalHandle[0]);
			gl.glNormalPointer(GL.GL_FLOAT, 0, 0L);
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboTexCoordHandle[0]);
			gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0);
			
			gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
			gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
			//gl.glEnable(GL.GL_TEXTURE_2D);			
		
			//gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 10f);
			//texture.enable();
			//gl.glEnable(GL.GL_TEXTURE_2D);
			//gl.glColor3i(1, 1, 1);
			//texture.bind();
			gl.glDrawArrays(GL.GL_TRIANGLES, 0, m.faces.size()*3);
			//texture.disable();

			gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
			gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
			//gl.glDisable(GL.GL_TEXTURE_2D);
			//gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0); // unbind the buffer
			//gl.glUseProgram(0); // unbind the program
			//texture.disable();
			
		gl.glPopMatrix();
		
//		//texture.disable();
//		gl.glEndList();
//		
//		gl.glCallList(objectlist);
		
	}
	
	private float[] asFloats(Vector3f v){
		return new float[]{v.x,v.y,v.z};
	}
	
	private float[] asFloats(Vector2f v){
		return new float[]{v.x,v.y};
	}
	
	private FloatBuffer asFloatBuffer(float... values){
		FloatBuffer res = reserveData(values.length*4); // 4 bytes per float
		res.put(values);
		res.flip();
		return res;
	}
	
	private FloatBuffer reserveData(int size){
		ByteBuffer byteBuff = ByteBuffer.allocateDirect(size); // allocate memory
		byteBuff.order(ByteOrder.nativeOrder()); // use the device hardware's native byte order
		FloatBuffer data = byteBuff.asFloatBuffer(); // create a floating point buffer from the ByteBuffer
		return data;
	}
	
	
}
