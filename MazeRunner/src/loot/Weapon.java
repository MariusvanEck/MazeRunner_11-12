package loot;

import mazerunner.Creature;

public abstract class Weapon extends Loot {
	
	private Creature creature;
	
	/**
	 * Weapon constructor
	 * @param x 					The x-coordinate of the location 
	 * @param y 					The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param damage				The amount of damage to be dealt
	 * @param equipped				Is this weapon equipped by a creature
	 * @param modelFileLocation		The location of the model file
	 */
	public Weapon(double x, double y, double z, String modelFileLocation){
		super(x, y, z, modelFileLocation);
	}
	
	/**
	 * Constructor for stick without location belonging to a creature
	 */
	public Weapon(String modelFileLocation) {
		super(modelFileLocation);
	}
	
	/**
	 * doDamage
	 * @return The damage to be dealt to the enemy creature
	 */
	public abstract void doDamage(Creature creature);
	
	/**
	 * Set the associated creature
	 */
	public void setCreature(Creature creature) {
		this.creature = creature;
	}

	/**
	 * get the associated creature
	 */
	public Creature getCreature() {
		return creature;
	}
	
	/**
	 * check if the weapon is equiped (has an associated creature)
	 */
	public boolean isEquipped() {
		return getCreature() != null;
	}
}
