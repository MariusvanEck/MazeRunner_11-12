package loot;

import javax.media.opengl.GL;

import mazerunner.Creature;
import mazerunner.VisibleObject;

public abstract class Weapon extends Loot implements VisibleObject{
	
	private Creature creature;
	protected double wieldX;
	protected double wieldY;
	protected double wieldZ;
	protected double angleX;
	protected double angleY;
	protected double angleZ;
	
	/**
	 * Weapon constructor
	 * @param wieldx 				The x-coordinate of the weapon location 
	 * @param wieldy 				The y-coordinate of the weapon location
	 * @param wieldz				The z-coordinate of the weapon location
	 * @param damage				The amount of damage to be dealt
	 * @param equipped				Is this weapon equipped by a creature
	 * @param modelFileLocation		The location of the model file
	 * @param textureFileLocation	The location of the texture file
	 */
	public Weapon(GL gl, String modelFileLocation, String textureFileLocation, double wieldx,double wieldy,double wieldz, double AngleY){
		super(gl, modelFileLocation, textureFileLocation);
	}
	
	/**
	 * Constructor for stick without location belonging to a creature
	 */
	public Weapon(GL gl,String modelFileLocation,String textureFileLocation) {
		super(gl,modelFileLocation,textureFileLocation);
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

	public void setWieldX(double wieldX) {
		this.wieldX = wieldX;
	}

	public void setWieldY(double wieldY) {
		this.wieldY = wieldY;
	}

	public void setWieldZ(double wieldZ) {
		this.wieldZ = wieldZ;
	}

	public void setAngleX(double angleX) {
		this.angleX = angleX;
	}

	public void setAngleY(double angleY) {
		this.angleY = angleY;
	}

	public void setAngleZ(double angleZ) {
		this.angleZ = angleZ;
	}
}
