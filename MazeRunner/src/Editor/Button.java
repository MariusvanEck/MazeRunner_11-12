package Editor;

import java.awt.event.MouseEvent;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;

public class Button {
	
	private float x;
	private float y;
	private float sizex;
	private float sizey;
	private float R;
	private float G;
	private float B;
	private float O;
	protected boolean selected;
	private String text;
	private Texture texture;
	
	/**
	 * the button constructor
	 * @param gl		GL for drawing
	 * @param x			the x-location
	 * @param y			the y-location
	 * @param sizex		the x-size
	 * @param sizey		the y-size
	 * @param R			the color -R
	 * @param G			the color -G
	 * @param B			the color -B
	 * @param O			the color -opacity
	 * @param text		the text string
	 */
	public Button(GL gl, float x, float y, float sizex, float sizey, float R, float G, float B, float O, String text){
		this.x = x;
		this.y = y;
		this.sizex = sizex;
		this.sizey = sizey;
		this.R = R;
		this.G = G;
		this.B = B;
		this.O = O;
		this.text = text;
	}
	/**
	 * The draw method
	 * @param gl	GL to draw
	 */
	public void draw(GL gl){
		
		gl.glLineWidth(1);
		//Step 1: Draw the button
		gl.glColor4f(R,G,B,O);
		boxOnScreen(gl,x,y,sizex,sizey);
	
		//Step 2: Draw text on the button
		gl.glColor3f(1, 1, 1);
		gl.glPushMatrix();
		GLUT glut = new GLUT();
		float width = glut.glutStrokeLengthf(GLUT.STROKE_ROMAN, text); // the width of the text-string in gl coordinations
		float borderGap = 10; // Gap between the button border and the text
		gl.glTranslatef(x+borderGap, y+2.5f*borderGap, 0); // Translation to the button
		gl.glScalef((sizex-borderGap*2)/width, (sizey-borderGap*5)/100f, 1f); // Text scale to the button size
		glut.glutStrokeString(GLUT.STROKE_ROMAN, text); // Draws the text
		gl.glPopMatrix();
		//End of drawing the text
		
		//Step 3: check if the button is selected and if so, draw a border around the button
		if (selected){
			gl.glColor3f(1, 0, 0);
			gl.glLineWidth(7);
			boxLine(gl,x,y,sizex,sizey);
		}
	}
	
	/**
	 * Box selection line
	 * @param gl		GL for the openGL methods
	 * @param x			the x-location
	 * @param y			the y-location
	 * @param sizex		the x-size
	 * @param sizey		the y-size
	 */
	public void boxLine(GL gl, float x, float y, float sizex, float sizey){
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + sizex, y);
		gl.glVertex2f(x + sizex, y + sizey);
		gl.glVertex2f(x, y + sizey);
		gl.glEnd();
	}
	
	/**
	 * Box draw
	 * @param gl		GL for the openGL methods
	 * @param x			the x-location
	 * @param y			the y-location
	 * @param sizex		the x-size
	 * @param sizey		the y-size
	 */
	public void boxOnScreen(GL gl, float x, float y, float sizex, float sizey) {
		
		if (texture != null) {
			texture.enable();
			texture.bind();
		}
		
		gl.glBegin(GL.GL_POLYGON);
		gl.glTexCoord2f(0,1);
		gl.glVertex2f(x, y);
		gl.glTexCoord2f(1,1);
		gl.glVertex2f(x + sizex, y);
		gl.glTexCoord2f(1,0);
		gl.glVertex2f(x + sizex, y + sizey);
		gl.glTexCoord2f(0,0);
		gl.glVertex2f(x, y + sizey);
		gl.glEnd();
		
		if (texture != null) {
			texture.disable();
		}
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	public void mouseReleased(MouseEvent me) {/*NOT USED*/}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getSizex() {
		return sizex;
	}

	public float getSizey() {
		return sizey;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	

}
