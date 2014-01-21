package mazerunner;

import gamestate.GameStateManager;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class HeadsUpDisplay {

	private Player player;			// the player
	private int time;				// time since game start
	private String timeString;		// time in string format
	private int sw, sh;				// the screen width and height
	
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
	
	
	/*
	 * **********************************************
	 * *					update					*
	 * **********************************************
	 */
	
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

	
	/*
	 * **********************************************
	 * *				draw functions				*
	 * **********************************************
	 */
	
	/**
	 * display the time passed since game start and the health bar
	 */
	public void display(GL gl) {

		// draw focus area
		drawFocusArea(gl, 1.2);
		
		// draw time box
		drawTimeBox(gl);
		// draw score box
		drawScoreBox(gl);
		
	    // draw health bar
	    drawHealthBar(gl);
	}
	
	/**
	 * rad gives the normalised radius of the focus area, 1 means half the screen width
	 */
	private void drawFocusArea(GL gl, double rad) {
		gl.glPushMatrix();
		gl.glBegin(GL.GL_TRIANGLE_FAN);
			gl.glColor4f(0f, 0f, 0f, 0f);
			gl.glVertex2d(sw/2, sh/2);
			gl.glColor4f(0f, 0f, 0f, 1f);
			for(int i=-180; i<=180; i++) {
				gl.glVertex2d(	sw/2 + rad*(sw/2)*Math.cos(Math.toRadians(i)), 
								sh/2 + rad*(sw/2)*Math.sin(Math.toRadians(i)));}
		gl.glEnd();
		
		gl.glColor4f(0f, 0f, 0f, 1f);
		gl.glBegin(GL.GL_TRIANGLE_FAN);
			gl.glVertex2d(0, 0);
			gl.glVertex2d(0, sh/2);
			for(int i=-180; i<=-90; i++) {
				gl.glVertex2d(	sw/2 + rad*(sw/2)*Math.cos(Math.toRadians(i)), 
								sh/2 + rad*(sw/2)*Math.sin(Math.toRadians(i)));}
			gl.glVertex2d(sw/2, 0);
		gl.glEnd();
		gl.glBegin(GL.GL_TRIANGLE_FAN);
			gl.glVertex2d(sw, 0);
			gl.glVertex2d(sw/2, 0);
			for(int i=-90; i<=0; i++) {
				gl.glVertex2d(	sw/2 + rad*(sw/2)*Math.cos(Math.toRadians(i)), 
								sh/2 + rad*(sw/2)*Math.sin(Math.toRadians(i)));}
			gl.glVertex2d(sw, sh/2);
		gl.glEnd();
		gl.glBegin(GL.GL_TRIANGLE_FAN);
			gl.glVertex2d(sw, sh);
			gl.glVertex2d(sw, sh/2);
			for(int i=0; i<=90; i++) {
				gl.glVertex2d(	sw/2 + rad*(sw/2)*Math.cos(Math.toRadians(i)), 
								sh/2 + rad*(sw/2)*Math.sin(Math.toRadians(i)));}
			gl.glVertex2d(sw/2, sh);
		gl.glEnd();
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glVertex2d(0, sh);
		gl.glVertex2d(sw/2, sh);
			for(int i=90; i<=180; i++) {
				gl.glVertex2d(	sw/2 + rad*(sw/2)*Math.cos(Math.toRadians(i)), 
								sh/2 + rad*(sw/2)*Math.sin(Math.toRadians(i)));}
			gl.glVertex2d(0, sh/2);
		gl.glEnd();
		gl.glPopMatrix();
		
	}

	/**
	 * Draw the time
	 * @param gl
	 */
	private void drawTimeBox(GL gl) {
		GLUT glut = new GLUT();
		
		// draw box
		int length = glut.glutBitmapLength(GLUT.BITMAP_9_BY_15, timeString);
		gl.glPushMatrix();
		gl.glColor4f(0f, .2f, 0f, 1f);
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
	
	/**
	 * Draw the score
	 */
	private void drawScoreBox(GL gl){
		GLUT glut = new GLUT();
		String scoreString = "" + player.getScore();
		int length = glut.glutBitmapLength(GLUT.BITMAP_9_BY_15, scoreString);
		gl.glPushMatrix();
		gl.glColor4f(0f, .2f, 0f, 1f);
		gl.glLoadIdentity();
		gl.glBegin(GL.GL_QUADS);
			gl.glVertex2d(18, sh-38);
			gl.glVertex2d(22 + length, sh-38);
			gl.glVertex2d(22 + length, sh-50);
			gl.glVertex2d(18, sh-50);
		gl.glEnd();
		gl.glPopMatrix();
		
		// draw time string
		gl.glPushMatrix();
		gl.glLoadIdentity();
        	gl.glColor4f(0f, 1f, 0f, 1f); // set the color
        	gl.glRasterPos2i(20, sh - 49); // set the string position (left top)
        	glut.glutBitmapString(GLUT.BITMAP_9_BY_15, scoreString); // draw the string
        gl.glPopMatrix();
	}

	/**
	 * Draw the health bar
	 * @param gl
	 */
	public void drawHealthBar(GL gl)  {
		 
		double frac = (double)player.getHitpoints() / (double)player.getMaxHP();
		int health = (int) Math.floor(( (2*(double)sw/3-2))*frac);
		    // draw health bar
		    gl.glPushMatrix();
		    gl.glLoadIdentity();
		    gl.glColor4f(0f, .2f, 0f, 1f);
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
	
	/**
	 * Get the time
	 */
	public int getTime(){
		return time;
	}
}
