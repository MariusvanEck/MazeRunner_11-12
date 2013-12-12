package gamestate;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

/**
 * Class for the pause functionality of the game (PAUSE)
 */
public abstract class Pause {
	
	private static String pauseString = "PAUSE";
	
	/**
	  * Displays pause on the screen when the game is paused
	  */
	protected static void display(GL gl, int screenWidth) {
        GLUT glut = new GLUT();
        
        // draw pause box
        gl.glPushMatrix();
        gl.glColor4f(0, 0, 0, 1);
        gl.glBegin(GL.GL_QUADS);
        	gl.glVertex2d(0, 18);
        	gl.glVertex2d(screenWidth, 18);
        	gl.glVertex2d(screenWidth, 31);
        	gl.glVertex2d(0, 31);
        gl.glEnd();
        
       	// draw pause string
        gl.glPushMatrix();
	        int length = glut.glutBitmapLength(GLUT.BITMAP_9_BY_15, pauseString);
	        gl.glColor4f(0f, 1f, 0f, 1f);
	        gl.glRasterPos2i(screenWidth/2 - length/2, 20);
	        glut.glutBitmapString(GLUT.BITMAP_9_BY_15, pauseString);
	    gl.glPopMatrix();
	}
}
