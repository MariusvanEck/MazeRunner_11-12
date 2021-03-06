package mazerunner;

import javax.media.opengl.GL;

import model.Model;
import model.TexturedModel;

public class End extends GameObject implements VisibleObject {
	
	// model
	private static final String modelFileLocation = "models/the_end/the_end.obj";
	private static final String textureFileLocation = "models/the_end/the_end.png";
	private static TexturedModel texturedModel;
	
	private int orientation;

	public End(GL gl, int x, int z) {
		super(x*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2, 0, z*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2);
		this.orientation = -90;
		if (gl != null) {
			texturedModel = new TexturedModel(gl, new Model(modelFileLocation, 1f), textureFileLocation);
		}
	}
	
	/**
	 * Load the model
	 */
	public static void loadModel(GL gl) {
		texturedModel = new TexturedModel(gl, new Model(modelFileLocation, 1f),textureFileLocation);
	}
	
	/**
	 * Display
	 */
	public void display(GL gl){
		texturedModel.render(gl, orientation, locationX, locationY, locationZ);
	}
}