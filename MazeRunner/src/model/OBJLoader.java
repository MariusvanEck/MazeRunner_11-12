/*
 * This is based on tutorials from Oscar Veerhoek.
 * The tutorials were written in LWJGL so needed to be converted
 * to JOGL. 
 */
package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class OBJLoader {
	/**
	 * Load a model from a .obj-file
	 * @param f							The File location
	 * @return							Returns a Model
	 * @throws FileNotFoundException	Thrown when the file is not found
	 * @throws IOException				Thrown when the file is not readable
	 */
	public static Model loadModel(File f)throws FileNotFoundException,IOException{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		Model m = new Model();
		
		String line;
		
		while((line = reader.readLine()) != null){
			// add the vertices
			if(line.startsWith("v ")){
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.addVertex(new Vector3f(x,y,z));
			}
			// add the normals
			else if(line.startsWith("vn ")){
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.addNormal(new Vector3f(x,y,z));
			}
			// add the texture coordinates
			else if(line.startsWith("vt ")){
				float u = Float.valueOf(line.split(" ")[1]);
				float v = Float.valueOf(line.split(" ")[2]);
				m.addTexCoord(new Vector2f(u,v));
			}
			// add the faces
			else if(line.startsWith("f ")){
				float x = Float.valueOf(line.split(" ")[1].split("/")[0]);
				float y = Float.valueOf(line.split(" ")[2].split("/")[0]);
				float z = Float.valueOf(line.split(" ")[3].split("/")[0]);
				Vector3f vertexIndices = new Vector3f(x,y,z);
				
				x = Float.valueOf(line.split(" ")[1].split("/")[1]);
				y = Float.valueOf(line.split(" ")[2].split("/")[1]);
				z = Float.valueOf(line.split(" ")[3].split("/")[1]);
				Vector3f texcoordIndices = new Vector3f(x,y,z);
				
				x = Float.valueOf(line.split(" ")[1].split("/")[2]);
				y = Float.valueOf(line.split(" ")[2].split("/")[2]);
				z = Float.valueOf(line.split(" ")[3].split("/")[2]);
				Vector3f normalIndices = new Vector3f(x,y,z);
				
				m.addFace(new Face(vertexIndices,texcoordIndices,normalIndices));
			}
		}
		reader.close();
		return m;
	}
}
