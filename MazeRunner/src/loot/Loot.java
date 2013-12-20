package loot;

import javax.media.opengl.GL;

import mazerunner.GameObject;
import mazerunner.VisibleObject;
import model.Model;
import model.TexturedModel;

public abstract class Loot extends GameObject implements VisibleObject {
	protected double x,y,z;
	protected TexturedModel model;
	
	/**
	 * Loot constructor.
	 * 
	 * @param x						The x-coordinate of the location
	 * @param y						The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param modelFileLocation		The location of the model file
	 */
	public Loot(GL gl,double x,double y,double z,String modelFileLocation){
		super(x, y, z);
		if(modelFileLocation != null)
			model = new TexturedModel(gl,new Model(modelFileLocation,.25f));
	}
	
	/**
	 * Loot constructor without location (associated with creature)
	 */
	public Loot(GL gl,String modelFileLocation) {
		super();
		if(modelFileLocation != null)
			model = new TexturedModel(gl,new Model(modelFileLocation,.25f));
	}
	
	public void render(GL gl,double angle){
		model.render(gl, angle, x, y, z);
	}
}
