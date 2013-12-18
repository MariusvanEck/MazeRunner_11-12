package loot;

import mazerunner.GameObject;
import mazerunner.VisibleObject;
import model.Model;

public abstract class Loot extends GameObject implements VisibleObject {
	protected double x,y,z;
	protected Model model;
	
	/**
	 * Loot constructor.
	 * 
	 * @param x						The x-coordinate of the location
	 * @param y						The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param modelFileLocation		The location of the model file
	 */
	public Loot(double x,double y,double z,String modelFileLocation){
		super(x, y, z);
		if(modelFileLocation != null)
			model.load(modelFileLocation);
	}
	
	
}
