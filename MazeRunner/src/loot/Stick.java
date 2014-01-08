package loot;

import gamestate.Sound;

import java.util.Calendar;

import javax.media.opengl.GL;

import mazerunner.Creature;

public class Stick extends Weapon{

	private final static String modelFileLocation = null; 		// specify the model file here
	private final static String textureFileLocation = null; 		// specify the model file here
	private final static Sound hit = new Sound("stick.wav");	// the hitting sound
	
	private int downTime = 1000;							// the weapon down time in ms
	private Long timeDoneLastDamage;						// last time the stick was swung
	private int damage = 10;								// the sticks damage output
	private double range = .2;								// the damage range for the stick in units of SQUARE_SIZE
	
	/**
	 * Constructor for stick with location (as loot)
	 * @param x			x-coordinate
	 * @param y			y-coordinate
	 * @param z			z-coordinate
	 */
	public Stick(GL gl,double x, double y, double z){
		super(gl,x, y, z, modelFileLocation,textureFileLocation);
	}
	
	/**
	 * Constructor for stick without location (associated with a creature)
	 */
	public Stick(GL gl) {
		super(gl,modelFileLocation);
	}
	
	/**
	 * The doDamage function of stick
	 * 
	 * Does damage if the creature is near and then goes on cooldown for downTime (in ms)
	 */
	@Override
	public void doDamage(Creature creature) {
		
		// do damage to the creature if there has not been done any damage or 
		// the time since damage was done > downTime and the creature is close
		if ((timeDoneLastDamage == null || 
			Calendar.getInstance().getTimeInMillis() - timeDoneLastDamage > downTime) &&
			creature.near(getCreature(), range)) 
		{
			timeDoneLastDamage = Calendar.getInstance().getTimeInMillis();
			creature.removeHP(damage);
			hit.play();}
	}
	
	/**
	 * The display function of stick for displaying as loot, i.e.
	 * if isEquipped results in false
	 */
	public void display(GL gl){
		
		
		if (!isEquipped()) {
			
		}
	}
}
