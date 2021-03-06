package mazerunner;

import java.awt.Point;
import java.util.Calendar;

import javax.media.opengl.GL;

import loot.Stick;
import model.Model;
import model.TexturedModel;

/**
 * Enemy represents enemies in the game
 * 
 * @author Marius
 */
public class Enemy extends Creature implements VisibleObject{
	
	private final int rotationSpeed = 3;	// The enemy's rotation speed			
	private static double maxSpeed = 0.008;	// the maximum speed for the enemy
	private double speed = maxSpeed;		// the enemy's speed
	
	private EnemyControl control;		// the enemies control
	private boolean playerVisible;		// is true if the player is currently visible
	private boolean hitWall;			// is true if the enemy hit a wall
	private int TimePassed;				// integer used for keeping track of the "aggro" time
										// after a player disappears
	private long timeHit;
	
	private Point memory; 				// holds the point previously visited in grid coordinates
	
	// main model
	private static String 	modelFileLocation = "models/Lambent_Male/Lambent_Male.obj",
							textureFileLocation = "models/Lambent_Male/Lambent_Male_D.tga";
	
	// hit model
	private static TexturedModel hitModel;
	private final String hitTextureFileLocation = "models/Lambent_Male/Lambent_Male_D_Blood.png";
	
	/**
	 * The Enemy constructor.
	 * 
	 * @param x					The x-grid location
	 * @param y					The y-grid location
	 * @param z					The z-grid location
	 * @param h					The horizontal angle of the orientation in degrees
	 * @param hp				The amount of HP that the enemy has
	 * @param weapon			The weapon of the Enemy
	 * @param modelFileLocation The location of the model file
	 */
	public Enemy(GL gl, int x, int z, double horAngle) {
		super(gl,x*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2, 
				0, 
				z*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2, 
				100, new Stick(gl), 
				modelFileLocation, textureFileLocation);
		
		setHorAngle(horAngle);
		setMaxHP(100);
		
		// create and set a control
		setControl(new EnemyControl());
		((EnemyControl)getControl()).setEnemy(this);
		
		// set the hit model
		if(modelFileLocation != null && gl != null){ // if Creature has no model the string will be null
			System.err.println("ja");		
			hitModel = new TexturedModel(gl,new Model(modelFileLocation,0.75f), hitTextureFileLocation);
		
		}
		// initialise the memory
		memory = new Point(x, z);
	}
 
	
	/*
	 * **********************************************
	 * *			Drawing functions 				*
	 * **********************************************
	 */
	
	/**
	 * enemy display function 
	 * draws the model of the enemy
	 */
	@Override
	public void display(GL gl) { 
		if (getTexturedModel() == null || hitModel == null)
			System.out.println("null");
		else if (Calendar.getInstance().getTimeInMillis() - timeHit < 250)
			hitModel.render(gl, getHorAngle()-180,locationX,locationY,locationZ);
		else
			getTexturedModel().render(gl, getHorAngle()-180,locationX,locationY,locationZ);
	}

	
	/*
	 * **********************************************
	 * *					update					*
	 * **********************************************
	 */
	
	/**
	 * 
	 * @param deltaTime The time in milliseconds since the last update.
	 */
	public void update(int deltaTime) {
		
		// rotate the enemy, according to control
		setHorAngle(GameObject.normaliseAngle(getHorAngle() + rotationSpeed*control.dX));
		
		// move the enemy, according to control
		if (control.moveDirection != null) {
			locationX -= speed*deltaTime*
					Math.sin(Math.toRadians((control.moveDirection + getHorAngle())));
			locationZ -= speed*deltaTime*
					Math.cos(Math.toRadians((control.moveDirection + getHorAngle())));}
	}

	
	/*
	 * **********************************************
	 * *				miscelanous					*
	 * **********************************************
	 */
	
	/**
	 * Checks if the player is in the current enemy viewing cone
	 */
	public boolean derivePlayerInCone(Player player) {
		// find the angle to rotate
		double playerAngle = Math.toDegrees(
			Math.atan2(locationX - player.locationX, locationZ - player.locationZ));
		int angleToRotate = (int) Math.floor(playerAngle - getHorAngle());
		angleToRotate = (int) GameObject.normaliseAngle(angleToRotate);
		
		if (Math.abs(angleToRotate) < 45) return true;
		else return false;
	}
	
	/**
	 * check if the enemy has reached its current target
	 */
	public boolean atTarget(double margin) {
		return control.atTarget(margin);
	}
	
	/**
	 * sets an enemy hit
	 */
	public void hit() {
		timeHit = Calendar.getInstance().getTimeInMillis();
	}
	
	/*
	 * **********************************************
	 * *			getters and setters				*
	 * **********************************************
	 */

	protected double getSpeed() {
		return speed;
	}

	protected void setSpeed(double speedFactor) {
		if(speedFactor >= 0 && speedFactor <= 1)
			this.speed = speedFactor*maxSpeed;
		else if (speedFactor > 1)
			this.speed = maxSpeed;
		else
			this.speed = 0;
	}
	
	public boolean isPlayerVisible() {
		return playerVisible;
	}
	
	public void setPlayerVisible(boolean playerVisible) {
		this.playerVisible = playerVisible;
	}

	public Point getMemory() {
		return memory;
	}

	public void setMemory(Point memory) {
		this.memory = memory;
	}

	public boolean hasHitWall() {
		return hitWall;
	}

	public void setHitWall(boolean hitWall) {
		this.hitWall = hitWall;
	}

	public int getTimePassed() {
		return TimePassed;
	}

	public void setTimePassed(int timePassed) {
		TimePassed = timePassed;
	}

	protected EnemyControl getControl() {
		return control;
	}

	protected void setControl(EnemyControl control) {
		this.control = control;	
	}
	
	public static void setMaxSpeed (double maxSpeedFraction) {
		maxSpeed = 0.011*maxSpeedFraction;
	}
}
