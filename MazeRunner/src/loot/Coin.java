package loot;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class Coin extends Loot {
	
	/**
	 * Food constructor
	 * @param x 					The x-coordinate of the location 
	 * @param y 					The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param hpIncrease 			The HP that will be regenerated
	 * @param modelFileLocation		The location of the model file
	 */
	public Coin(GL gl,double x, double y, double z, String modelFileLocation,String textureFileLocation){
		super(gl,x, y, z, modelFileLocation, textureFileLocation);
	}
	
	/**
	 * the display function should not be used and render(gl,angle) instead
	 */
	public void display(GL gl){
		GLUT glut = new GLUT();
		
		// Set color and material.
		float wallColour[] = { 0f, 1f, 0f, 0f };						// green
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);		// Set the materials
		
		// push matrix
		gl.glPushMatrix();
	
		// translate and scale to correct location
		gl.glTranslated(x, y, z);
	
		// TEMP: draw a cube
		glut.glutSolidCone(10,10,10,10);
	
		// pop matrix
		gl.glPopMatrix();
	}
	
	
}
