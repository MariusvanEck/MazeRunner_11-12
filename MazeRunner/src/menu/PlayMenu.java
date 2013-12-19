package menu;

import java.io.File;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class PlayMenu extends MenuObject{
	private Button buttons[];
	
	public static final byte NEW = 0;
	public static final byte CONTINUE = 1;
	public static final byte BACK = 2;
	
	private Texture[] textures;
	
	
	/**
	 * Constructor creates menu objects
	 */
	public PlayMenu(int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		textures = new Texture[3];
		
		loadTextures();
		
		buttons = new Button[3];
		buttons[0] = new Button(minX,maxX,minY+(maxY-minY)*2/3,maxY, textures[0]);						//New
		buttons[1] = new Button(minX,maxX,minY+(maxY-minY)/3,minY+(maxY-minY)*2/3, textures[1]);		//Continue
		buttons[2] = new Button(minX,maxX,minY,minY+(maxY-minY)/3, textures[2]);							//Back
	}
	
	public void loadTextures(){
		try {
			textures[0] = TextureIO.newTexture(new File("textures\\new.png"), false);
			textures[1] = TextureIO.newTexture(new File("textures\\continue.png"), false);
			textures[2] = TextureIO.newTexture(new File("textures\\back.png"), false);
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
			return NEW;
		if(buttons[1].minX < x && x < buttons[1].maxX && buttons[1].minY < y && y < buttons[1].maxY)
			return CONTINUE;
		if(buttons[2].minX < x && x < buttons[2].maxX && buttons[2].minY < y && y < buttons[2].maxY)
			return BACK;
		return -1;
	}
	
	/**
	 * Draw the menu
	 */
	public void display(GL gl){
		for(int i=0; i<buttons.length; i++)
			buttons[i].display(gl);
	}
	
	public void reshape(int minX, int maxX,int minY,int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		
		buttons[0].update(minX,maxX,minY+(maxY-minY)*2/3,maxY);
		buttons[1].update(minX,maxX,minY+(maxY-minY)/3,minY+(maxY-minY)*2/3);
		buttons[2].update(minX,maxX,minY,minY+(maxY-minY)/3);
	}
	
	
	/**
	 * This methode is used to check if and what is selected
	 */
	public void update(int x,int y){
		
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		// set selected button to true
		switch(getButton(x,y)){
		case NEW: 		buttons[NEW].setSelected(true);		break;
		case CONTINUE: 		buttons[CONTINUE].setSelected(true);		break;
		case BACK:		buttons[BACK].setSelected(true);		break;}
	}
}
