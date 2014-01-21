package model;

import javax.vecmath.Vector3f;

public class Face {
	public Vector3f vertex = new Vector3f();
	public Vector3f normals = new Vector3f();
	public Vector3f texcoord = new Vector3f();
	
	/**
	 * Face is used to render a model with faces
	 * @param vertex		the vertex
	 * @param texcoord		the texture coordinate by the vertex
	 * @param normals		the normal to the face
	 */
	public Face(Vector3f vertex, Vector3f texcoord, Vector3f normals){
		this.vertex = vertex;
		this.normals = normals;
		this.texcoord = texcoord;
	}
	
}
