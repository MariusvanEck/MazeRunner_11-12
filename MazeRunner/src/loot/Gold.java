package loot;

import javax.media.opengl.GL;

public class Gold extends Loot {
	
	private static final String	modelFileLocation = "models/gold/gold.obj",
								textureFileLocation = "models/gold/gold.png";
	private double angle;
	
	/**
	 * Food constructor
	 * @param x 					The x-coordinate of the location 
	 * @param y 					The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param hpIncrease 			The HP that will be regenerated
	 * @param modelFileLocation		The location of the model file
	 */
	public Gold(GL gl, double x, double z){
		super(gl, x, 0, z, 0.25f, modelFileLocation, textureFileLocation);
		angle = Math.random()*360;
	}
	
	/**
	 * the display function should not be used and render(gl,angle) instead
	 */
	public void display(GL gl){
		if (model == null) {
			System.err.println("food model is null");
		}
		else 
			model.render(gl, angle, locationX, locationY, locationZ);
	}
	
	
}
