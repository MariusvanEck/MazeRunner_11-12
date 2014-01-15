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
	
	
	public LevelSelector(int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		DataBase dataBase = new DataBase();
		
		names = dataBase.getMapNames();
		
		buttons = new Button[names.length+1];
		for(int i = 0; i < names.length; i++){
				buttons[i] = new Button(minX,maxX,minY,maxY,names[i]);
				System.out.println(names[i]);
			}
		try {
			back = TextureIO.newTexture(new File("textures\\back.png"), false);
		} catch (GLException | IOException e) {
			System.err.println("LevelSelector: Error while loading back texture\n\t"+e.getMessage());
		}
		buttons[names.length] = new Button(minX,maxX,minY,maxY,back);
	}
	
	
	

	@Override
	public void display(GL gl) {
		for(int i = 0; i < buttons.length; i++)
			buttons[i].display(gl);
	}
	
	
	public int getButton(int x,int y){
		for(int i = 0; i < buttons.length; i++){
			if(buttons[i].minX < x && x < buttons[i].maxX && buttons[i].minY < y && y < buttons[i].maxY)
				return i;
		}
		return -1;
	}
	
	
	public void reshape(int minX, int maxX,int minY,int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		for(int i = 0; i < buttons.length;i++)
			buttons[i].update(minX,maxX,minY+(maxY-minY)*(buttons.length-1-i)/(buttons.length+1),minY+(maxY-minY)*(buttons.length-i)/(buttons.length+1));
	}
	



	public void update(int x,int y) {
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		int index = -1;
		if((index =getButton(x,y)) != -1)
			buttons[index].setSelected(true);
	}
	
	
	public String getName(int index){
		if(index < names.length)
			return names[index];
		if(index == names.length)
			return "back";
		return null;
	}
	
	
}
