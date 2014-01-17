package loot;

import javax.media.opengl.GL;

import mazerunner.Creature;
import model.Model;
import model.TexturedModel;

public class Sword extends Weapon{
	
	//private final static String modelFileLocation = null; 		// specify the model file here
	//private final static String textureFileLocation = null; 		// specify the model file here
	private TexturedModel model;									// the model of the Sword
	
	//constructor for wielding or placing the sword
	public Sword(GL gl, String modelFileLocation, String textureFileLocation) {
		super(gl, modelFileLocation, textureFileLocation);
		
		// set the model
		if(modelFileLocation != null && textureFileLocation != null){
			model = new TexturedModel(gl,new Model(modelFileLocation,0.05f),textureFileLocation);
		}
	}
	
	@Override
	public void display(GL gl) {
		model.render(gl, angleX, angleY, angleZ, wieldX, wieldY, wieldZ);
	}

	@Override
	public void doDamage(Creature creature) {
		// TODO Auto-generated method stub
		
	}

}
