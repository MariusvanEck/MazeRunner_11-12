package loot;

import gamestate.Sound;

import java.util.Calendar;

import javax.media.opengl.GL;

import mazerunner.Creature;
import model.Model;
import model.TexturedModel;

public class Sword extends Weapon{
	
	//private final static String modelFileLocation = null; 		// specify the model file here
	//private final static String textureFileLocation = null; 		// specify the model file here
	private TexturedModel model;									// the model of the Sword
	private final static Sound hit = new Sound("sword-hit.wav");		// the hitting sound
	private final static Sound miss = new Sound("sword-miss.wav");
	
	private int downTime = 1000;							// the weapon down time in ms
	private Long timeDoneLastDamage;						// last time the stick was swung
	private int damage = 50;								// the sticks damage output
	private double range = 1;								// the damage range for the stick in units of SQUARE_SIZE
	
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
		if (model != null)
			model.render(gl, angleX, angleY, angleZ, wieldX, wieldY, wieldZ);
	}

	public void swingSword (Creature creature, boolean incone) {
		if ((timeDoneLastDamage == null || 
			Calendar.getInstance().getTimeInMillis() - timeDoneLastDamage > downTime)) {
			timeDoneLastDamage = Calendar.getInstance().getTimeInMillis();
			
			if (creature.near(getCreature(), range) && incone) {
				doDamage(creature);
			}
			else {
				miss.play();
			}
		}
	}
	
	/**
	 * The doDamage function of sword
	 * 
	 * Does damage if the creature is near and then goes on cooldown for downTime (in ms)
	 */
	@Override
	public void doDamage(Creature creature) {
			creature.removeHP(damage);
			hit.play();
	}

}
