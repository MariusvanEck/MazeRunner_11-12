package menu;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import database.DataBase;

public class LevelSelector extends MenuObject{
	private Button buttons[];
	private String names[];
	private Texture back;
	private Texture arrowU,arrowD;
	private int index = 0;
	
	
	public LevelSelector(int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		DataBase dataBase = new DataBase();
		
		names = dataBase.getMapNames();
		
		
		try {
			back = TextureIO.newTexture(new File("textures/back.png"), false);
			arrowU = TextureIO.newTexture(new File("textures/Alfabet/arrowU.png"), false);
			arrowD = TextureIO.newTexture(new File("textures/Alfabet/arrowD.png"), false);
		} catch (GLException e) {
			System.err.println("LevelSelector: Error while loading back texture\n\t"+e.getMessage());
		}catch( IOException e){
			System.err.println("LevelSelector: Error while loading back texture\n\t"+e.getMessage());
		}
		
		buttons = new Button[names.length+3]; // also for back, and the arrows < >
		buttons[0] = new Button(minX,maxX,minY+(minY+maxY)/5,minY+(minY+maxY)*2/5,arrowU);
		for(int i = 1; i < names.length+1; i++){
				buttons[i] = new Button(minX,maxX,minY+(minY+maxY)*2/5,minY+(minY+maxY)*3/5,names[i-1]);
			}
		
		buttons[names.length+1] = new Button(minX,maxX,minY+(minY+maxY)*3/5,minY+(minY+maxY)*4/5,arrowD);
		buttons[names.length+2] = new Button(minX,maxX,minY,maxY,back);
	}
	
	
	

	@Override
	public void display(GL gl) {
		buttons[0].display(gl);
		buttons[index+1].display(gl);
		buttons[names.length+1].display(gl);
		buttons[names.length+2].display(gl);
	}
	
	
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
	



	public void update(int x,int y) {
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		int index = -1;
		if((index = getButton(x,y)) != -1)
			buttons[index].setSelected(true);
	}
	
	
	public String getName(int index){
		if(index == 0)
			return"up";
		if(index < names.length+1)
			return names[index-1];
		if(index == names.length+1)
			return "down";
		if(index == names.length+2)
			return "back";
		return null;
	}
	
	public void goUp(){
		if(index > 0)
			index--;
		else
			index = names.length-1;
	}
	public void goDown(){
		if(index < names.length-1)
			index++;
		else
			index = 0;
	}
	
}
