package loot;

import javax.media.opengl.GL;

import mazerunner.Creature;

public class Sword extends Weapon{
	
	public Sword(GL gl, double x, double y, double z, String modelFileLocation,String textureFileLocation) {
		super(gl, x, y, z, modelFileLocation, textureFileLocation);
	}

	private final static String modelFileLocation = null; 		// specify the model file here
	private final static String textureFileLocation = null; 		// specify the model file here
	
	@Override
	public void display(GL gl) {
		model.render(gl, 0, 13, 2, 13);
	}

	@Override
	public void doDamage(Creature creature) {
		// TODO Auto-generated method stub	
	}

}
