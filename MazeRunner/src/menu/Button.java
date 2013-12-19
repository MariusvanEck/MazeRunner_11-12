package menu;

import javax.media.opengl.GL;
import com.sun.opengl.util.texture.Texture;

public class Button extends MenuObject{
	private Texture texture;
	
	/**
	 * Button constructor
	 * @param text The text on the button
	 * @param gl The GL to draw to
	 * @param minX The left x
	 * @param maxX the right x
	 * @param minY the bottom y
	 * @param maxY the top y
	 */
	public Button(int minX,int maxX,int minY,int maxY, Texture texture){
		super(minX,maxX,minY,maxY);
		this.texture = texture;
	}
	
	/**
	 * Draw the button
	 */
	public void display(GL gl){
		
		if (texture != null){
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			texture.enable();
			texture.bind();
			//White background color for normal texture view
			gl.glColor3f(255/255f, 255/255f, 255/255f);
			if(selected){
				gl.glColor3f(128/255f, 128/255f, 128/255f);
			}
			gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0,0);
				gl.glVertex2f(minX, maxY);
				gl.glTexCoord2f(1,0);
				gl.glVertex2f(maxX, maxY);
				gl.glTexCoord2f(1,1);
				gl.glVertex2f(maxX, minY);
				gl.glTexCoord2f(0,1);
				gl.glVertex2f(minX, minY);
			gl.glEnd();
			texture.disable();
		}
	}
	public void update(int minX,int maxX,int minY, int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
}
