package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

// Need to use GL VBO for better performance
public class Model {
	protected List<Vector3f> vertices = new ArrayList<Vector3f>();
	protected List<Vector3f> normals = new ArrayList<Vector3f>();
	protected List<Face> faces = new ArrayList<Face>();
	
	private boolean loaded = false;
	
	public Model(){}
	
	public Model(String file){
		this.load(file);
	}
	
	public void addVertice(Vector3f add){
		vertices.add(add);
	}
	public void addNormal(Vector3f add){
		normals.add(add);
	}
	public void addFace(Face add){
		faces.add(add);
	}
	
	
	public boolean isLoaded(){return loaded;}
	
	public void load(String file){
		try{
			Model temp = OBJLoader.loadModel(new File(file));
			vertices = temp.vertices;
			normals = temp.normals;
			faces = temp.faces;
			loaded = true;
		}catch(FileNotFoundException e){
			loaded = false;
			System.err.println("File not found!");
		}catch(IOException e){
			loaded = false;
			System.out.println("Read error!");
		}
	}
	
}
