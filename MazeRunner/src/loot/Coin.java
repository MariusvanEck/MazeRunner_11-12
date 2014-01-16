package loot;

import javax.media.opengl.GL;

public class Coin extends Loot {
	
	private static final String	modelFileLocation = "models/box.obj",
								textureFileLocation = null;
	
	/**
	 * Food constructor
	 * @param x 					The x-coordinate of the location 
	 * @param y 					The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param hpIncrease 			The HP that will be regenerated
	 * @param modelFileLocation		The location of the model file
	 */
	public Coin(GL gl, int x, int z){
		super(gl, x, z, 0.25f,modelFileLocation, textureFileLocation);
	}
	
	/**
	 * the display function should not be used and render(gl,angle) instead
	 */
	public void display(GL gl){
		if (model == null) {
			System.err.println("food model is null");
		}
		else 
			model.render(gl, 0, locationX, locationY, locationZ);
	}
	
	
}
