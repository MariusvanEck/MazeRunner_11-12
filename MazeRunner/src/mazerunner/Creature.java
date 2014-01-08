package mazerunner;

import javax.media.opengl.GL;

import loot.Weapon;
import model.Model;
import model.TexturedModel;

/**
 * Class for living creatures Player, Enemy
 */
public abstract class Creature extends GameObject {
	
	private double horAngle;						// the horizontal angle
	private int hitpoints;							// the number of hitpoints
	public int maxHP;								// the maximum number of hitpoints
	
	private Weapon weapon;							// the weapon of the creature
	private TexturedModel texturedModel;			// the model of the Creature
	
	/**
	 * The Creature constructor.
	 * 
	 * @param x					The x coordinate of the location
	 * @param y					The y coordinate of the location
	 * @param z					The z coordinate of the location
	 * @param hitpoints			The amount of hitpoints (HP) that the creature has
	 * @param weapon			The weapon the creature has (null if no weapon)
	 * @param modelFileLocation	The location of the model (.obj) file
	 */
	public Creature(GL gl,double x, double y, double z,int hitpoints,Weapon weapon,String modelFileLocation, String textureFileLocation){
		super(x,y,z);
		this.hitpoints = hitpoints;
		
		// set the weapon and associate the enemy with it
		this.weapon = weapon;
		if (weapon != null)
			weapon.setCreature(this);
		
		// set the model
		if(modelFileLocation != null) // if Creature has no model the string will be null
			texturedModel = new TexturedModel(gl,new Model(modelFileLocation,0.75f),textureFileLocation);
	}
	
	
	/*
	 * **********************************************
	 * *				HP functions				*
	 * **********************************************
	 */
	
	/**
	 * Get current the HP
	 * @return the current hp
	 */
	public double getHP(){return hitpoints;}
	
	/**
	 * Add HP
	 * @param add the amount to add
	 * @return true if addHP was successful false otherwise
	 */
	public boolean addHP(int add){
		if(add <= 0 || hitpoints == maxHP)
			return false;
		if(hitpoints + add <= maxHP)
			hitpoints += add;
		else
			hitpoints = maxHP;
		return true;
	}
	
	/**
	 * Remove HP
	 * @param remove amount to remove
	 */
	public void removeHP(int remove){
		if (hitpoints - remove > 0)
			hitpoints -= remove;
		else 
			hitpoints = 0;
	}

	
	/*
	 * **********************************************
	 * *			Getters and setters				*
	 * **********************************************
	 */

	/**
	 * Get the creatures horizontal angle
	 */
	protected double getHorAngle() {
		return horAngle;
	}

	/**
	 * Set the creatures horizontal angle
	 */
	protected void setHorAngle(double horAngle) {
		this.horAngle = horAngle;
	}
	
	/**
	 * Get the weapon
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**
	 * set the weapon
	 */
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	/**
	 * Get the textured model
	 */
	public TexturedModel getTexturedModel() {
		return texturedModel;
	}

	/**
	 * Set the textured model
	 */
	public void setTexturedModel(TexturedModel texturedModel) {
		this.texturedModel = texturedModel;
	}

	/**
	 * Get the hitpoints
	 */
	public int getHitpoints() {
		return hitpoints;
	}

	/**
	 * Set the hitpoints
	 */
	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}
}
