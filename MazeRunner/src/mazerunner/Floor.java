package mazerunner;

import javax.media.opengl.GL;

/**
 * Represents an ingame wall block
 */
public class Floor extends GameObject implements VisibleObject{

	private static final float size = (float) Maze.SQUARE_SIZE;			// floor size
	private static final float s = size/2;
	
	/**
	 * create a new floor object on the index coordinates
	 */
	public Floor(int x, int z) {
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
				gl.glNormal3f(0, 1, 0);
			    gl.glTexCoord2f(0f, 1f); gl.glVertex3f(-s,  -s, -s);  // Top Left Of The Texture and Quad
			    gl.glTexCoord2f(0f, 0f); gl.glVertex3f(-s,  -s,  s);  // Bottom Left Of The Texture and Quad
			    gl.glTexCoord2f(1f, 0f); gl.glVertex3f( s,  -s,  s);  // Bottom Right Of The Texture and Quad
			    gl.glTexCoord2f(1f, 1f); gl.glVertex3f( s,  -s, -s);  // Top Right Of The Texture and Quad
		    gl.glEnd();
        gl.glPopMatrix();
	}
}
