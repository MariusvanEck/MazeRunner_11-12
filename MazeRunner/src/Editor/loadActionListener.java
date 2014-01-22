package Editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import database.DataBase;


/**
 * Used to load a Map to the dataBase
 */
public class loadActionListener implements ActionListener {
	private Editor editor;
	private DataBase dataBase;
	public loadActionListener(Editor editor){
		this.editor = editor; 
		this.dataBase = new DataBase();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int nlevels = dataBase.getNumLevels(editor.getMapName());
		int mazeX = dataBase.getMazeSize(editor.getMapName());
		
		int[][] firstLevel = dataBase.getMap(editor.getMapName(), 0);
		Level.updateMazeX(mazeX);
		editor.levels = new Level[nlevels];
            
		for (int b = 0; b < nlevels; b++){
			editor.levels[b] = new Level(mazeX,mazeX);
		}
               
		editor.level = editor.levels[0];
               
            
		editor.level.setX(mazeX);
		editor.level.setY(mazeX);
            
		int n = 0;
		int[][] nextLevel;
		editor.levels[n].setLevel(firstLevel);
		for ( n=1; n<nlevels; n++){
			nextLevel = dataBase.getMap(editor.getMapName(), n);
			if (nextLevel != null) {
				editor.levels[n].setLevel(nextLevel);
			}
			else break;
		}
		editor.mirror();
		editor.getLoadFrame().dispose();
	}

}
