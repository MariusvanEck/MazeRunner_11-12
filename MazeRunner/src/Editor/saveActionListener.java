package Editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import database.DataBase;


/**
 * Used to save a Map to the dataBase
 */
public class saveActionListener implements ActionListener {
	private Editor editor;
	public saveActionListener(Editor editor){
		this.editor = editor; 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		editor.resetSize();
		
		editor.saveToDataBase(editor.getMapName(), new DataBase());
		editor.getSaveFrame().dispose();
	}

}
