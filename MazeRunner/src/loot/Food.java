package loot;

import javax.media.opengl.GL;

import mazerunner.Creature;
import mazerunner.Maze;

public class Food extends Loot {
	private int hpIncrease;
	private final static String 	modelFileLocation = "models/food/food.obj",
									textureFileLocation = "models/food/food.png";
	
	
	/**
	 * Food constructor
	 * @param x 					The x-coordinate of the location 
	 * @param y 					The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param hpIncrease 			The HP that will be regenerated
	 * @param modelFileLocation		The location of the model file
	 */
	public Food(GL gl, int x, int z, int hpIncrease){
		super(gl, x, z, 0.10f, modelFileLocation, textureFileLocation);
		setLocationY(getLocationY() - Maze.SQUARE_SIZE/3);
		this.hpIncrease = hpIncrease;
	}
	
	/**
	 * Gives the HP to a creature
	 * @param the creature to give the HP to
	 */
	public void increaseHP(Creature creature){
		creature.addHP(hpIncrease);
	}

	/**
	 * the display function
	 */
	public void display(GL gl) {
		if (model == null) {
			System.err.println("food model is null");
		}
		else 
			model.render(gl, 0, locationX, locationY, locationZ);
	}
}
