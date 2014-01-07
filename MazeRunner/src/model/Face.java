package model;

import javax.vecmath.Vector3f;

public class Face {
	public Vector3f vertex = new Vector3f();;
	public Vector3f normals = new Vector3f();
	public Vector3f texcoord = new Vector3f();
	
	public Face(Vector3f vertex, Vector3f texcoord, Vector3f normals){
		this.vertex = vertex;
		this.normals = normals;
		this.texcoord = texcoord;
	}
	
}
