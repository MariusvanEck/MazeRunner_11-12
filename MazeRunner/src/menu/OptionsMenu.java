package menu;

import java.io.File;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class OptionsMenu extends MenuObject{
	
	private Button buttons[];
	private float[] buttonColor = {1,0,0};
	
	public static final byte BACK = 0;
	
	private Texture[] textures;
	
	/**
	 * Constructor creates menu objects
	 */
	public OptionsMenu(int minX, int maxX, int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		textures = new Texture[1];
		
		loadTextures();
		
		buttons = new Button[1];
		buttons[0] = new Button(minX,maxX,minY,minY+(maxY-minY)/2, textures[0]);		//Back
	}
	
	public void loadTextures(){
		try {
			textures[0] = TextureIO.newTexture(new File("textures\\back.png"), false);
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
			return BACK;
		return -1;
	}
	
	public void reshape(int minX, int maxX, int minY, int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		
		buttons[0].update(minX,maxX,minY,minY+(maxY-minY)/2);
	}
	
	/**
	 * Draw the menu
	 */
	public void display(GL gl) {		
		for(int i=0; i<buttons.length; i++)
			buttons[i].display(gl);
	}
	
	/**
	* This method is used to check if and what is selected
	**/
	public void update(int x, int y){
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);
		}
		
		// set selected button to true
		switch(getButton(x,y)){
		case BACK: 		buttons[BACK].setSelected(true);		break;}
	}
}
