package loot;

import gamestate.Sound;

import java.util.Calendar;

import javax.media.opengl.GL;

import mazerunner.Creature;
import model.Model;
import model.TexturedModel;

public class Sword extends Weapon{
	
	private int downTime = 1000;							// the weapon down time in ms
	private Long timeDoneLastDamage;						// last time the stick was swung
	private int damage = 50;								// the sticks damage output
	private double range = .5;								// the damage range for the stick in units of SQUARE_SIZE
	
	// the model
	private TexturedModel model;
	private final static String modelFileLocation = "models/Killer_Frost_Ice_Sword/Killer_Frost_Ice_Sword.obj",
								textureFileLocation = "models/Killer_Frost_Ice_Sword/Killer_Frost_Ice_Sword_D.tga";
	
	// the sounds
	private final static Sound 	hit = new Sound("sword-hit.wav"),
								miss = new Sound("sword-miss.wav");
	
	/**
	 * Create a new sword
	 */
	public Sword(GL gl) {
		super(gl, modelFileLocation, textureFileLocation);
		
		// set the model
		if(modelFileLocation != null && textureFileLocation != null){
			model = new TexturedModel(gl,new Model(modelFileLocation,0.05f),textureFileLocation);
		}
	}
	
	/**
	 * Display function
	 */
	@Override
	public void display(GL gl) {
		if (model != null)
			model.render(gl, angleX, angleY, angleZ, wieldX, wieldY, wieldZ);
	}

	/**
	 * Swing the sword
	 */
	public boolean swingSword (Creature creature, boolean incone) {
		if ((timeDoneLastDamage == null || 
			Calendar.getInstance().getTimeInMillis() - timeDoneLastDamage > downTime)) {
			timeDoneLastDamage = Calendar.getInstance().getTimeInMillis();
			
			if (creature.near(getCreature(), range) && incone) {
				doDamage(creature);
				return true;
			}
			else {
				miss.play();
			}
		}
		
		return false;
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
