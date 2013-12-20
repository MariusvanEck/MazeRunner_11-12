package mazerunner;

import javax.media.opengl.GL;

/**
 * Represents an ingame wall block
 */
public class Wall extends GameObject implements VisibleObject{

	private static final float size = (float) Maze.SQUARE_SIZE;			// wall size
	private static final float s = size/2;
	
	/**
	 * create a new wall object on the index coordinates
	 */
	public Wall(int x, int z) {
		super((x + .5)*Maze.SQUARE_SIZE, .5*Maze.SQUARE_SIZE, (z + .5)*Maze.SQUARE_SIZE);
	}

	/**
	 * draw function
	 */
	@Override
	public void display(GL gl) {
		gl.glPushMatrix();
		gl.glTranslated(locationX, locationY, locationZ);
	        gl.glBegin(GL.GL_QUADS);
		        // Front Face
	        	gl.glNormal3f(0, 0, 1);
		        gl.glTexCoord2f(0, 0); gl.glVertex3f(-s, -s,  s);  // Bottom Left Of The Texture and Quad
		        gl.glTexCoord2f(1, 0); gl.glVertex3f( s, -s,  s);  // Bottom Right Of The Texture and Quad
		        gl.glTexCoord2f(1, 1); gl.glVertex3f( s,  s,  s);  // Top Right Of The Texture and Quad
		        gl.glTexCoord2f(0, 1); gl.glVertex3f(-s,  s,  s);  // Top Left Of The Texture and Quad
		        // Back Face
		        gl.glNormal3f(0, 0, -1);
		        gl.glTexCoord2f(1, 0); gl.glVertex3f(-s, -s, -s);  // Bottom Right Of The Texture and Quad
		        gl.glTexCoord2f(1, 1); gl.glVertex3f(-s,  s, -s);  // Top Right Of The Texture and Quad
		        gl.glTexCoord2f(0, 1); gl.glVertex3f( s,  s, -s);  // Top Left Of The Texture and Quad
		        gl.glTexCoord2f(0, 0); gl.glVertex3f( s, -s, -s);  // Bottom Left Of The Texture and Quad
		        // Right face
		        gl.glNormal3f(1, 0, 0);
		        gl.glTexCoord2f(1, 0); gl.glVertex3f( s, -s, -s);  // Bottom Right Of The Texture and Quad
		        gl.glTexCoord2f(1, 1); gl.glVertex3f( s,  s, -s);  // Top Right Of The Texture and Quad
		        gl.glTexCoord2f(0, 1); gl.glVertex3f( s,  s,  s);  // Top Left Of The Texture and Quad
		        gl.glTexCoord2f(0, 0); gl.glVertex3f( s, -s,  s);  // Bottom Left Of The Texture and Quad
		        // Left Face
		        gl.glNormal3f(-1, 0, 0);
		        gl.glTexCoord2f(0, 0); gl.glVertex3f(-s, -s, -s);  // Bottom Left Of The Texture and Quad
		        gl.glTexCoord2f(1, 0); gl.glVertex3f(-s, -s,  s);  // Bottom Right Of The Texture and Quad
		        gl.glTexCoord2f(1, 1); gl.glVertex3f(-s,  s,  s);  // Top Right Of The Texture and Quad
		        gl.glTexCoord2f(0, 1); gl.glVertex3f(-s,  s, -s);  // Top Left Of The Texture and Quad
	        gl.glEnd();
        gl.glPopMatrix();
	}

}
