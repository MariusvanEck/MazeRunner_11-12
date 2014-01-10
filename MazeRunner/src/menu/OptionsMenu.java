package menu;

import gamestate.Sound;

import java.io.File;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class OptionsMenu extends MenuObject{
	
	private Button[] buttons;
	private Slider[] sliders;
	public static final byte BACK = 0;
	public static final byte VOLUME = 1;
	
	private Texture[] textures;
	private Texture[] sliderTextures;
	
	/**
	 * Constructor creates menu objects
	 */
	public OptionsMenu(int minX, int maxX, int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		textures = new Texture[1];
		sliderTextures = new Texture[3];
		loadTextures();
		
		buttons = new Button[1];
		sliders = new Slider[1];
		buttons[0] = new Button(minX,maxX,minY,minY+(maxY-minY)/3, textures[0]);		//Back
		sliders[0] = new Slider(minX, maxX, minY + 2*(maxY-minY)/3, maxY, sliderTextures);
	}
	
	public void loadTextures(){
		try {
			textures[0] = TextureIO.newTexture(new File("textures\\back.png"), false);
			sliderTextures[0] = TextureIO.newTexture(new File("textures\\slider1.png"), false);
			sliderTextures[1] = TextureIO.newTexture(new File("textures\\slider2.png"), false);
			sliderTextures[2] = TextureIO.newTexture(new File("textures\\volume.png"), false);
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
		else if (sliders[0].minX < x && x < sliders[0].maxX && sliders[0].minY < y && y < sliders[0].minY + (sliders[0].maxY - sliders[0].minY)/2)
			return VOLUME;
		return -1;
	}
	
	public void reshape(int minX, int maxX, int minY, int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		
		buttons[0].update(minX, maxX, minY, minY+(maxY-minY)/3);
		sliders[0].update(minX, maxX, minY + 2*(maxY-minY)/3, maxY);
	}
	
	/**
	 * Draw the menu
	 */
	public void display(GL gl) {		
		for(int i=0; i<buttons.length; i++)
			buttons[i].display(gl);
		for(int i=0; i<sliders.length; i++)
			sliders[i].display(gl);	
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

	/**
	 * This method sets the volume
	 */
	public void setVolume(int x) {
		float volumeFraction = ((float) (x - sliders[0].minX)) / ((float) (sliders[0].maxX - sliders[0].minX));
		
		System.out.println(volumeFraction);
		
		Sound.setVolume(volumeFraction);
		sliders[0].setFraction(volumeFraction);
	}
}
