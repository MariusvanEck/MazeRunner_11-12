package menu;

import javax.media.opengl.GL;

import database.DataBase;

public class LevelSelector extends MenuObject{
	private Button buttons[];
	
	
	public LevelSelector(int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		DataBase dataBase = new DataBase();
		
		String names[] = dataBase.getMapNames();
		
		buttons = new Button[names.length];
		for(int i = 0; i < names.length; i++){
				buttons[i] = new Button(minX,maxX,minY,maxY,names[i]);
			}
		}
	
	
	

	@Override
	public void display(GL gl) {
		
	}
	
	
	
}
