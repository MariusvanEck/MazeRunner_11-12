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
		editor.resetSize();
		
		editor.loadFromDataBase(editor.getMapName(), dataBase);
		
		
		editor.getLoadFrame().dispose();
	}

}
