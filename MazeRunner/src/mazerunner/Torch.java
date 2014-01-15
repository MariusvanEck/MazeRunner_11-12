package mazerunner;

import javax.media.opengl.GL;

import model.Model;
import model.TexturedModel;

public class Torch extends GameObject implements VisibleObject {
	
	private static final String modelFileLocation = "models/torch/torch.obj";
	private static final String textureFileLocation = "models/textures/brick1.jpg";
	private static TexturedModel texturedModel;


	public Torch(int x, int z) {
		super(x*Maze.SQUARE_SIZE, 0, z*Maze.SQUARE_SIZE);
	}
	
	public static void loadModel(GL gl) {
		texturedModel = new TexturedModel(gl, new Model(modelFileLocation, 1f),textureFileLocation);
	}
	
	public void display(GL gl){
		texturedModel.render(gl, 0, locationX, locationY, locationZ);
	}
}
