package loot;

import mazerunner.Creature;

public abstract class Weapon extends Loot {
	private int damage;
	boolean equipped;
	
	/**
	 * Weapon constructor
	 * @param x 					The x-coordinate of the location 
	 * @param y 					The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param damage				The amount of damage to be dealt
	 * @param equipped				Is this weapon equipped by a creature
	 * @param modelFileLocation		The location of the model file
	 */
	public Weapon(double x, double y, double z, int damage, boolean equipped, String modelFileLocation){
		super(x,y,z,modelFileLocation);
		this.damage = damage;
		this.equipped = equipped;
	}
	
	/**
	 * doDamage
	 * @return The damage to be dealt to the enemy creature
	 */
	public void doDamage(Creature creature){
		creature.removeHP(damage);
	}
	
	/**
	 * Give state of equipped
	 * @return equipped
	 */
	public boolean getEquipped(){return equipped;}
	
	/**
	 * Set Equipped
	 * @param set The state of equipped that you want
	 */
	public void setEquipped(boolean set){
		equipped = set;
	}
}
