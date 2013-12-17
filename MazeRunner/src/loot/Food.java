package loot;

import gamestate.GameStateManager;

import javax.media.opengl.GL;

import mazerunner.Creature;

import com.sun.opengl.util.GLUT;

public class Food extends Loot {
	private int hpIncrease;
	
	/**
	 * Food constructor
	 * @param x 					The x-coordinate of the location 
	 * @param y 					The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param hpIncrease 			The HP that will be regenerated
	 * @param modelFileLocation		The location of the model file
	 */
	public Food(double x, double y, double z, int hpIncrease, String modelFileLocation){
		super(x,y,z,modelFileLocation);
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
	public void display(){
		GL gl = GameStateManager.gl;
		GLUT glut = new GLUT();
		
		
		// Set color and material.
		float wallColour[] = { 0f, 1f, 0f, 0f };						// green
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);	// Set the materials

		// push matrix
		gl.glPushMatrix();
	
		// translate and scale to correct location
		gl.glTranslated(x, y, z);
		gl.glScaled(.5, .5, .5);
		
	
		// TEMP: draw a cube
		glut.glutSolidCube(2);
	
		// pop matrix
		gl.glPopMatrix();
	}
	
	
}
