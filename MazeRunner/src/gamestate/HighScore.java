package gamestate;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import menu.Button;
import database.Scores;

public class HighScore {
	private Button[] name;
	private Button[] score;
	private Texture texture;
	
	/**
	 * Contstructor for the HighScore
	 * @param scores	The scores data
	 * @param minX		The minX for display
	 * @param maxX		The maxX for display
	 * @param minY		The minY for display
	 * @param maxY		The maxY for display
	 */
	public HighScore(Scores scores,int minX,int maxX,int minY, int maxY){
		name = new Button[scores.size()];
		score = new Button[scores.size()];
			try {
				texture = TextureIO.newTexture(new File("textures/menu/Background.png"), false);
			} catch (GLException e) {
				System.err.println("HighScore: \n\t" + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("HighScore: \n\t" + e.getMessage());
				e.printStackTrace();
			}
		
		
		for(int i = 0; i < scores.size(); i++){
			name[i] = new Button(minX,maxX/2,minY+(maxY-minY)/11*i,(maxY-minY)/11*(i+1),scores.names.get(i));
			String s_score = "" + scores.scores.get(i);
			score[i] = new Button(maxX/2,maxX,minY+(maxY-minY)/11*i,(maxY-minY)/11*(i+1),s_score);
		}
		
		
		
	}
	/**
	 * Update Score, used only for display
	 * Should be called when window is resized
	 * @param minX		The minX for display
	 * @param maxX		The maxX for display
	 * @param minY		The minY for display
	 * @param maxY		The maxY for display
	 */
	public void update(int minX,int maxX,int minY,int maxY){
		for(int i = 0; i < name.length; i++){
			name[i].update(minX,maxX/2,minY+(maxY-minY)/11*i,(maxY-minY)/11*(i+1));
			score[i].update(maxX/2,maxX,minY+(maxY-minY)/11*i,(maxY-minY)/11*(i+1));
		}
	}
	/**
	 * Display the HighScore
	 * @param gl
	 */
	public void display(GL gl){
		texture.enable();
		texture.bind();
		
		gl.glColor3f(255/255f, 255/255f, 255/255f);
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0,0);
		gl.glVertex2f(0, GameStateManager.screenHeight);
		gl.glTexCoord2f(1,0);
		gl.glVertex2f(GameStateManager.screenWidth, GameStateManager.screenHeight);
		gl.glTexCoord2f(1,1);
		gl.glVertex2f(GameStateManager.screenWidth, 0);
		gl.glTexCoord2f(0,1);
		gl.glVertex2f(0, 0);
		gl.glEnd();
		texture.disable();
		
		for(Button b:name)
			b.display(gl);
		for(Button b:score)
			b.display(gl);
	}
	
}
