package mazerunner;

import javax.media.opengl.GL;

import model.Model;
import model.TexturedModel;

public class Stair extends GameObject implements VisibleObject {
	
	private static final String modelFileLocation = "models/stairs/stairs.obj";
	private static final String textureFileLocation = "models/textures/brick1.jpg";
	private static TexturedModel texturedModel;
	
	private int orientation;

	public Stair(int x, int z, int orientation) {
		super(x*Maze.SQUARE_SIZE, 0, z*Maze.SQUARE_SIZE);
		this.orientation = orientation;
	}
	
	public static void loadModel(GL gl) {
		texturedModel = new TexturedModel(gl, new Model(modelFileLocation, 1f),textureFileLocation);
	}
	
	public void display(GL gl){
		texturedModel.render(gl, orientation, locationX, locationY, locationZ);
	}
}
