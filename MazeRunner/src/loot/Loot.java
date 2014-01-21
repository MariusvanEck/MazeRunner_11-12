package loot;

import javax.media.opengl.GL;

import mazerunner.GameObject;
import mazerunner.Maze;
import mazerunner.VisibleObject;
import model.Model;
import model.TexturedModel;

public abstract class Loot extends GameObject implements VisibleObject {
	protected TexturedModel model;
	
	/**
	 * Loot constructor.
	 * 
	 * @param x						The x-coordinate of the location
	 * @param y						The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param modelFileLocation		The location of the model file
	 */
	public Loot(GL gl, int x, int z, float scale, String modelFileLocation, String textureFileLocation){
		super(x*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2, Maze.SQUARE_SIZE/2, z*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2);
		if(modelFileLocation != null)
			model = new TexturedModel(gl,new Model(modelFileLocation,scale), textureFileLocation);
	}
	
	/**
	 * Loot constructor with explicit coordinates
	 */
	public Loot(GL gl, double x, double y, double z, float scale, String modelFileLocation, String textureFileLocation){
		super(x*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2, y*Maze.SQUARE_SIZE, z*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2);
		if(modelFileLocation != null)
			model = new TexturedModel(gl,new Model(modelFileLocation,scale), textureFileLocation);
	}
	
	/**
	 * Loot constructor without coordinates
	 */
	public Loot(GL gl, String modelFileLocation, String textureFileLocation){
		if(modelFileLocation != null)
			model = new TexturedModel(gl,new Model(modelFileLocation,.25f), textureFileLocation);
	}
	
	/**
	 * Display
	 */
	public void render(GL gl, double angle){
		model.render(gl, angle, locationX, locationY, locationZ);
	}
}
