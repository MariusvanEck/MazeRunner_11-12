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
        
       	// get the string pixel length
        int length = glut.glutBitmapLength(GLUT.BITMAP_9_BY_15, pauseString);
        
        // set the color
        gl.glColor4f(0f, 1f, 0f, 1f);
        
        // set the string position (middle bottom)
        gl.glRasterPos2i(screenWidth/2 - length/2, 20);
        
        // draw the string
        glut.glutBitmapString(GLUT.BITMAP_9_BY_15, pauseString);
	}
}
