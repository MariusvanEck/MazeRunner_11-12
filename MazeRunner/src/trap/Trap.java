package trap;

import javax.media.opengl.GL;

import mazerunner.Creature;
import mazerunner.GameObject;
import mazerunner.Maze;
import model.Model;
import model.TexturedModel;

public abstract class Trap extends GameObject{
	protected TexturedModel model;
	
	public Trap(GL gl,double x,double z,String modelFileLocation,String textureFileLocation){
		super(x*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2,Maze.SQUARE_SIZE/4,z*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2);
		
		this.model = new TexturedModel(gl,new Model(modelFileLocation, 0.05f),textureFileLocation);
	}
	
	public abstract void display(GL gl);
	
	public abstract void update(int deltaTime,Creature creature);
	
}