package Editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import database.DataBase;

public class saveActionListener implements ActionListener {
	private Editor editor;
	public saveActionListener(Editor editor){
		this.editor = editor; 
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		editor.resetSize();
		
		editor.mirror();
		editor.saveToDataBase(editor.getMapName(), new DataBase());
		editor.mirror();
		editor.getSaveFrame().dispose();
	}

}
