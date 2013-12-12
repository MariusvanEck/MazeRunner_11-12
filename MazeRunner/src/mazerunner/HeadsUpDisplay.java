package mazerunner;

import gamestate.GameStateManager;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class HeadsUpDisplay {

	private Player player;
	private int time;				// time since game start
	private String timeString;		// time in string format
	private int sw, sh;
	
	/**
	 * Sets up a new heads up display for the game and sets the time to 0
	 */
	public HeadsUpDisplay(Player player) {
		// set time to 0
		time = 0;
		this.player = player;
		sw = GameStateManager.screenWidth;
		sh = GameStateManager.screenHeight;
	}
	
	/**
	 * updates the HUD
	 */
	public void update(int deltaTime) {
		// update time
		time += deltaTime;
		timeString = Integer.toString(time/60000) + ":" + String.format("%02d",(time%60000)/1000);
		
		// update screenSize
		sw = GameStateManager.screenWidth;
		sh = GameStateManager.screenHeight;
	}
	
	/**
	 * display the time passed since game start and the health bar
	 */
	public void display(GL gl) {
		// draw time box
		drawTimeBox(gl);
		
	    // draw health bar
	    drawHealthBar(gl);
	}
	
	private void drawTimeBox(GL gl) {
		GLUT glut = new GLUT();
		
		// draw box
		int length = glut.glutBitmapLength(GLUT.BITMAP_9_BY_15, timeString);
		gl.glPushMatrix();
		gl.glColor4f(0f, 0f, 0f, 1f);
		gl.glLoadIdentity();
		gl.glBegin(GL.GL_QUADS);
			gl.glVertex2d(18, sh-31);
			gl.glVertex2d(22 + length, sh-31);
			gl.glVertex2d(22 + length, sh-18);
			gl.glVertex2d(18, sh-18);
		gl.glEnd();
		gl.glPopMatrix();
		
		// draw time string
		gl.glPushMatrix();
		gl.glLoadIdentity();
        	gl.glColor4f(0f, 1f, 0f, 1f); // set the color
        	gl.glRasterPos2i(20, sh - 29); // set the string position (left top)
        	glut.glutBitmapString(GLUT.BITMAP_9_BY_15, timeString); // draw the string
        gl.glPopMatrix();
	}

	public void drawHealthBar(GL gl)  {
		 
		double frac = player.hitpoints/Creature.MAX_HP;
		int health = (int) Math.floor(((double) (2*sw/3-2))*frac);
		    // draw health bar
		    gl.glPushMatrix();
		    gl.glLoadIdentity();
		    gl.glColor4f(0, 0, 0, 1);
		    gl.glBegin(GL.GL_QUADS);
		    	gl.glVertex2d(sw/6, 	20);
		    	gl.glVertex2d(5*sw/6, 	20);
		    	gl.glVertex2d(5*sw/6, 	20 + sh/30);
		    	gl.glVertex2d(sw/6, 	20 + sh/30);
		    gl.glEnd();
		    gl.glColor4f(1, 0, 0, 1);
		    gl.glBegin(GL.GL_QUADS);
			    gl.glVertex2d(sw/6 + 2, 		22);
		    	gl.glVertex2d(sw/6 + health, 	22);
		    	gl.glVertex2d(sw/6 + health, 	18 + sh/30);
		    	gl.glVertex2d(sw/6 + 2, 		18 + sh/30);
		    gl.glEnd();
		    gl.glPopMatrix();
	}
}
