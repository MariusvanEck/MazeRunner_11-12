package menu;

import java.io.File;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import database.DataBase;

public class LevelSelector extends MenuObject{
	
	private Button buttons[]; 			// the menu buttons
	private String names[];				// the map names
	private Texture back;
	private Texture arrowU, arrowD;
	private int index = 0;
	
	/**
	 * Constructor creates a new levelselector
	 */
	public LevelSelector(int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		// get the map names
		DataBase dataBase = new DataBase();
		names = dataBase.getMapNames();
		
		// load the textures
		try {
			back = TextureIO.newTexture(new File("textures/Menu/back.png"), false);
			arrowU = TextureIO.newTexture(new File("textures/Alfabet/arrowU.png"), false);
			arrowD = TextureIO.newTexture(new File("textures/Alfabet/arrowD.png"), false);
		}
		catch (Exception e) {e.printStackTrace();}
		
		buttons = new Button[names.length+3]; 	// also for back, and the arrows
		
		// up button
		buttons[0] = new Button(minX,maxX,minY+(minY+maxY)/5,minY+(minY+maxY)*2/5,arrowU);
		
		// map buttons
		for(int i = 1; i < names.length+1; i++){
				buttons[i] = new Button(minX,maxX,minY+(minY+maxY)*2/5,minY+(minY+maxY)*3/5,names[i-1]);
		}
		
		// down button
		buttons[names.length+1] = new Button(minX,maxX,minY+(minY+maxY)*3/5,minY+(minY+maxY)*4/5,arrowD);
		
		// back button
		buttons[names.length+2] = new Button(minX,maxX,minY,maxY,back);
	}
	
	/**
	 * Display the levelSelector
	 */
	@Override
	public void display(GL gl) {
		buttons[0].display(gl);
		buttons[index+1].display(gl);
		buttons[names.length+1].display(gl);
		buttons[names.length+2].display(gl);
	}
	
	/**
	 * Get the selected button
	 */
	public int getButton(int x,int y){
		if(buttons[0].minX < x && x < buttons[0].maxX && buttons[0].minY < y && y < buttons[0].maxY)
			return 0;
		if(buttons[names.length+1].minX < x && x < buttons[names.length+1].maxX && buttons[names.length+1].minY < y && y < buttons[names.length+1].maxY)
			return names.length+1;
		if(buttons[names.length+2].minX < x && x < buttons[names.length+2].maxX && buttons[names.length+2].minY < y && y < buttons[names.length+2].maxY)
			return names.length+2;
		if(buttons[index+1].minX < x && x < buttons[index+1].maxX && buttons[index+1].minY < y && y < buttons[index+1].maxY)
			return index+1;
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
		buttons[0].update(minX, maxX, minY+(maxY-minY)*4/5,maxY);
		for(int i = 1; i < names.length+1;i++)
			buttons[i].update(minX,maxX,minY+(maxY-minY)*3/5,minY+(maxY-minY)*4/5);
		buttons[names.length+1].update(minX, maxX, minY+(maxY-minY)*2/5,minY+(maxY-minY)*3/5);
		buttons[names.length+2].update(minX, maxX,minY+(maxY-minY)/5,minY+(maxY-minY)*2/5);
	}
	
	/**
	 * update the level selector
	 */
	public void update(int x,int y) {
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		int index = -1;
		if((index = getButton(x,y)) != -1)
			buttons[index].setSelected(true);
	}
	
	/**
	 * Get the selected button name
	 */
	public String getName(int index){
		if(index == 0)
			return "up";
		if(index > 0 && index < names.length+1){
			return names[index-1];
		}
		if(index == names.length+1)
			return "down";
		if(index == names.length+2)
			return "back";
		return null;
	}
	
	/**
	 * Go the previous map (arrow up)
	 */
	public void goUp(){
		if(index > 0)
			index--;
		else
			index = names.length-1;
	}
	
	/**
	 * Go to the next map (arrow down)
	 */
	public void goDown(){
		if(index < names.length-1)
			index++;
		else
			index = 0;
	}
	
}
