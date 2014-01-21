package menu;

import java.io.File;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class QuitMenu extends MenuObject{
	
	// this menu's buttons
	private Button buttons[];
	
	// this menu's options
	public static final byte YES = 0;
	public static final byte NO = 1;
	
	// the textures
	private Texture[] textures;
	
	/**
	 * Constructor creates menu objects
	 */
	public QuitMenu(int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		textures = new Texture[3];
		
		loadTextures();
		
		buttons = new Button[2];
		buttons[0] = new Button(minX,maxX,minY+(maxY-minY)/4,minY+(maxY-minY)/2, textures[0]);		//Yes
		buttons[1] = new Button(minX,maxX,minY,minY+(maxY-minY)/4, textures[1]);					//No
		
	}
	
	/**
	 * Load the textures
	 */
	public void loadTextures(){
		try {
			textures[0] = TextureIO.newTexture(new File("textures/Menu/yes.png"), false);
			textures[1] = TextureIO.newTexture(new File("textures/Menu/no.png"), false);
			textures[2] = TextureIO.newTexture(new File("textures/Menu/sure.png"), false);
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Returns the value of the button hovered over
	 */
	public int getButton(int x,int y){
		if(buttons[0].minX < x && x < buttons[0].maxX && buttons[0].minY < y && y < buttons[0].maxY)
			return YES;
		if(buttons[1].minX < x && x < buttons[1].maxX && buttons[1].minY < y && y < buttons[1].maxY)
			return NO;
		return -1;
	}
	
	/**
	 * Reshape
	 */
	public void reshape(int minX, int maxX,int minY,int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		
		buttons[0].update(minX,maxX,minY+(maxY-minY)/4,minY+(maxY-minY)/2);
		buttons[1].update(minX,maxX,minY,minY+(maxY-minY)/4);
	}
	
	/**
	 * Draw the menu
	 */
	public void display(GL gl){
		
		//Drawing the question
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		textures[2].enable();
		textures[2].bind();
		//White background color for normal texture view
		gl.glColor3f(255/255f, 255/255f, 255/255f);
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0,0);
			gl.glVertex2f(minX-(maxX-minX), maxY+((maxX-minX)/2));
			gl.glTexCoord2f(1,0);
			gl.glVertex2f(maxX+(maxX-minX), maxY+((maxX-minX)/2));
			gl.glTexCoord2f(1,1);
			gl.glVertex2f(maxX+(maxX-minX), maxY);
			gl.glTexCoord2f(0,1);
			gl.glVertex2f(minX-(maxX-minX), maxY);
		gl.glEnd();
		textures[2].disable();
		
		for(int i=0; i<buttons.length; i++)
			buttons[i].display(gl);
	}
	
	/**
	* This methode is used to check if and what is selected
	**/
	public void update(int x, int y){
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		// set selected button to true
		switch(getButton(x,y)){
		case YES: 		buttons[YES].setSelected(true);		break;
		case NO: 		buttons[NO].setSelected(true);		break;}
	}

}