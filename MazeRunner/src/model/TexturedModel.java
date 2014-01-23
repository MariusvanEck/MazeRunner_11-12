/*
 * This is based on tutorials from Oscar Veerhoek.
 * The tutorials were written in LWJGL so needed to be converted
 * to JOGL. 
 */
package model;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;


public class TexturedModel {
	private int shaderProgramHandle;
	private int vboVertexHandle[] = new int[1];
	private int vboNormalHandle[] = new int [1];
	private int vboTexCoordHandle[] = new int [1];
	private int vertexShaderHandle;
	private int fragmentShaderHandle;
	private Texture texture;
	private  Model m;
	private GL gl;
	
	/**
	 * Constructs a TexturedModel
	 * @param gl					GL used to access openGL functions
	 * @param model					A Model
	 * @param textureFileLocation	The file location of the texture
	 */
	public TexturedModel(GL gl, Model model, String textureFileLocation){
		this.m = model;
		this.gl = gl;
		this.setUpVBOs();
		this.setUpLighting();
		this.loadTexture(textureFileLocation);
	}
	/**
	 * Deallocate the memory used by openGL 
	 * @throws Throwable
	 */
	@Override
	protected void finalize() throws Throwable{
		try{
			this.cleanUp();
		}finally{
			super.finalize();
		}
	}
	/**
	 * Setup the VBO's so openGL will be able to render the model
	 */
	private void setUpVBOs(){
		gl.glGenBuffers(1,vboVertexHandle,0);
		gl.glGenBuffers(1,vboNormalHandle,0);
		gl.glGenBuffers(1,vboTexCoordHandle,0);
		
		if(!m.isLoaded()) // draw nothing if there is no model loaded, so stop init VBO
			return;
		
		int sizeOfBuffers = m.faces.size() * 36; // 3*3*4
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
		texcoords.flip();
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboVertexHandle[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,sizeOfBuffers,vertices, GL.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER,vboNormalHandle[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,sizeOfBuffers,normals, GL.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER,vboTexCoordHandle[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,sizeOfBuffers,texcoords, GL.GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0); // unbind the buffer
	}
	
	/**
	 * Load the textures		
	 * @param textureFileLocation	The location of the texture file
	 */
	private void loadTexture(String textureFileLocation){
		if (textureFileLocation == null)
			return;
		
		try {
			texture = TextureIO.newTexture(new File(textureFileLocation), true);
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}	
	}
	
	/**
	 * Used to cleanup the memory allocated by openGL
	 */
	public void cleanUp(){
		gl.glDeleteProgram(shaderProgramHandle);
		
		gl.glDeleteBuffers(1,vboVertexHandle,0);
		gl.glDeleteBuffers(1,vboNormalHandle,0);
		gl.glDeleteBuffers(1, vboTexCoordHandle, 0);
		
		gl.glDeleteShader(vertexShaderHandle);
		gl.glDeleteShader(fragmentShaderHandle);
	}
	
	/**
	 * Setup the lighting so the model will be visible 
	 */
	private void setUpLighting(){
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
	
	/**
	 * Render the model
	 * @param gl			The gl used for the display
	 * @param angleX		rotation angle round the X-axis
	 * @param angleY		rotation angle round the Y-axis
	 * @param angleZ		rotation angle round the Z-axis
	 * @param x				Location x	where the model should appear
	 * @param y				Location y	where the model should appear
	 * @param z				Location z	where the model should appear
	 */
	public void render(		GL gl, double angleX, double angleY, double angleZ, 
							double x, double y, double z) {
		if(!m.isLoaded()) // there is nothing to render
			return;
		gl.glPushMatrix();
		
			gl.glTranslated(x, y, z);

			gl.glRotated(angleY, 0, 1, 0);
			gl.glRotated(angleX, 1, 0, 0);
			gl.glRotated(angleZ, 0, 0, 1);
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboVertexHandle[0]);
			gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0L);
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboNormalHandle[0]);
			gl.glNormalPointer(GL.GL_FLOAT, 0, 0L);
			
			if(texture != null){
				texture.enable();
				gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureObject());
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboTexCoordHandle[0]);//
				gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0);//
				gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
			}
			
			gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
		
			gl.glDrawArrays(GL.GL_TRIANGLES, 0, m.faces.size()*3);
			
			gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
			
			if(texture != null){
				gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
				texture.disable();
			}
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0); // unbind the buffer
			gl.glUseProgram(0); // unbind the program
			
		gl.glPopMatrix();	
	}
	/**
	 * Render the model
	 * @param gl			The gl used for the display
	 * @param angleY		rotation angle round the Y-axis
	 * @param x				Location x	where the model should appear
	 * @param y				Location y	where the model should appear
	 * @param z				Location z	where the model should appear
	 */
	public void render(GL gl, double angleY, double x, double y, double z){
		render(gl, 0, angleY, 0, x, y, z);
	}
	/**
	 * convert a vector2f to a float[]
	 * @param v		the vector to be converted
	 * @return		the float[]
	 */
	private float[] asFloats(Vector2f v){
		return new float[]{v.x,v.y};
	}
	/**
	 * convert a vector3f to a float[]
	 * @param v		the vector to be converted
	 * @return		the float[]
	 */
	private float[] asFloats(Vector3f v){
		return new float[]{v.x,v.y,v.z};
	}
	/**
	 * Convert different float values to a FloatBuffer
	 * @param values	the float values
	 * @return			FloatBuffer
	 */
	private FloatBuffer asFloatBuffer(float... values){
		FloatBuffer res = reserveData(values.length*4); // 4 bytes per float
		res.put(values);
		res.flip();
		return res;
	}
	
	/**
	 * Create a FloatBuffer with the specified size
	 * @param size	The size of the FloatBuffer in bytes 
	 * @return
	 */
	private FloatBuffer reserveData(int size){
		ByteBuffer byteBuff = ByteBuffer.allocateDirect(size); // allocate memory
		byteBuff.order(ByteOrder.nativeOrder()); // use the device hardware's native byte order
		FloatBuffer data = byteBuff.asFloatBuffer(); // create a floating point buffer from the ByteBuffer
		return data;
	}
	
	
}
