package trap;

import javax.media.opengl.GL;

import mazerunner.GameObject;
import mazerunner.Player;
import model.Model;
import model.TexturedModel;

public abstract class Trap extends GameObject{
	protected double activationX,activationY,activationZ; // the location that the trap activates
	protected Player player;
	protected TexturedModel model;
	
	public Trap(GL gl,Player player,double x,double y,double z,double activationX,double activationY,double activationZ,String modelFileLocation,String textureFileLocation){
		this.player = player;
		this.locationX = x;
		this.locationY = y;
		this.locationZ = z;
		this.activationX = activationX;
		this.activationY = activationY;
		this.activationZ = activationZ;
		this.model = new TexturedModel(gl,new Model(modelFileLocation,.5f),textureFileLocation);
	}
	
	public abstract void display(GL gl);
	
	public abstract void update(int deltaTime);
	
}