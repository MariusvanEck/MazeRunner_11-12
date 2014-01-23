/*
 * This is based on tutorials from Oscar Veerhoek.
 * The tutorials were written in LWJGL so needed to be converted
 * to JOGL. 
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import mazerunner.Maze;

public class Model {
	protected List<Vector3f> vertices = new ArrayList<Vector3f>();
	protected List<Vector3f> normals = new ArrayList<Vector3f>();
	protected List<Vector2f> texcoords = new ArrayList<Vector2f>();
	protected List<Face> faces = new ArrayList<Face>();
	private float scale = 0;
	
	private boolean loaded = false;
	
	/**
	 * Model with default parameters
	 * This object will be as good as empty
	 */
	public Model(){}
	
	/**
	 * Model with non-default parameters
	 * @param file		The file location of the models .obj-file
	 * @param scale		The scale that should be used while rendering
	 */
	public Model(String file,float scale){
		this.scale = scale;
		this.load(file);
	}
	/**
	 * Add a vertex
	 * @param add	the vertex to be added
	 */
	public void addVertex(Vector3f add){
		vertices.add(add);
	}
	/**
	 * Add a normal
	 * @param add	The normal to be added
	 */
	public void addNormal(Vector3f add){
		normals.add(add);
	}
	/**
	 * Add a texture coordinate
	 * @param add	The coordinate to be added
	 */
	public void addTexCoord(Vector2f add){
		texcoords.add(add);
	}
	/**
	 * Add a face
	 * @param add	The face to be added
	 */
	public void addFace(Face add){
		faces.add(add);
	}
	
	/**
	 * Checks if there is a loaded model present
	 * @return	returns true if there is a loaded model present, false otherwise.
	 */
	public boolean isLoaded(){return loaded;}
	
	/**
	 * Load a model from file
	 * @param file	the File location
	 */
	public void load(String file){
		try{
			Model temp = OBJLoader.loadModel(new File(file));
			vertices = temp.vertices;
			// Determine the height of the model
			float min=0,max=0;
			for(Vector3f vec3 : vertices){
				if(min > vec3.y)
					min = vec3.y;
				else if(max < vec3.y)
					max = vec3.y;
			}
			// rescale the model
			scale *= Maze.SQUARE_SIZE/(max-min);
			for(Vector3f vec3 : vertices)
				vec3.scale(scale);
			
			texcoords = temp.texcoords;
			normals = temp.normals;
			faces = temp.faces;
			loaded = true;
		}catch(FileNotFoundException e){
			loaded = false;
			System.err.println("Model: File not found!");
		}catch(IOException e){
			loaded = false;
			System.err.println("Model: Read error!");
		}
	}
	
}
