package mazerunner;

import gamestate.UserInput;

import javax.media.opengl.GL;

import loot.Weapon;

/**
 * Player represents the actual player in MazeRunner.
 * <p>
 * This class extends GameObject to take advantage of the already implemented location 
 * functionality. Furthermore, it also contains the orientation of the Player, ie. 
 * where it is looking at and the player's speed. 
 * <p>
 * For the player to move, a reference to a Control object can be set, which can then
 * be polled directly for the most recent input. 
 * <p>
 * All these variables can be adjusted freely by MazeRunner. They could be accessed
 * by other classes if you pass a reference to them, but this should be done with 
 * caution.
 * 
 * @author Bruno Scheele
 *
 */
public class Player extends Creature {
	
	private double verAngle;		// verticle angle
	private double speed;			// speed
	private double rotationSpeed;	// turning speed
	private Control control;		// the control
	private int score;				// the score
	double t = 0;					// variable for head bobbing
	private double counter = 0;
	
	private String name = "Player";
	
	/**
	 * The Player constructor.
	 * @param x			The x-coordinate of the location
	 * @param y			The y-coordinate of the location
	 * @param z			The z-coordinate of the location
	 * @param h			The horizontal angle of the orientation in degrees
	 * @param v			The vertical angle of the orientation in degrees
	 * @param hitpoints	The hit points (HP) of the player
	 * @param weapon	The weapon equipped by the player (null if not equipped)
	 */
	public Player(GL gl, double x,double y,double z,double h,double v,int hitpoints,Weapon weapon){
		super(gl,x,y,z,hitpoints,weapon,null,null);
		setMaxHP(200);
		setHorAngle(h);
		setVerAngle(v);
		speed = 0.01;
		rotationSpeed = 1;
		score = 0;
	}
	
	
	/*
	 * **********************************************
	 * *					update					*
	 * **********************************************
	 */
	
	/**
	 * Updates the physical location and orientation of the player
	 * @param deltaTime The time in milliseconds since the last update.
	 */
	public void update(int deltaTime) {
		if (control != null) {
			// update control
			control.update();
			
			// rotate the player, according to control
			setHorAngle(getHorAngle() - rotationSpeed*control.dX);
			if(getVerAngle() - rotationSpeed*control.dY < 90 && getVerAngle() - rotationSpeed*control.dY > -90)
				setVerAngle(getVerAngle() - rotationSpeed*control.dY);
			
			// move the player, according to control
			if (control.moveDirection != null) {
				locationX -= speed*deltaTime*
						Math.sin((getHorAngle() + control.moveDirection)*(Math.PI/180));
				locationZ -= speed*deltaTime*
						Math.cos((getHorAngle() + control.moveDirection)*(Math.PI/180));
				
				// make the camera bob
				t = t + ((double) deltaTime)/1000d;
				setVerAngle(getVerAngle() + 0.4*Math.sin(10*t));
				setHorAngle(getHorAngle() + 0.2*Math.sin(5*t));
				if(t > 5){t = 0;}
			}
			

			if (UserInput.wasJumped()) {
				double jumplength = 40;
				if(counter<2*Math.sqrt(2)){
					counter += 2*Math.sqrt(2)/jumplength;
					locationY = 4.5 - Math.pow((counter-Math.sqrt(2)),2);
				}
				else{
					locationY = 2.5;
					counter = 0;
					UserInput.resetJumped();
				}
			}
			
			
		}
	}

	
	/*
	 * **********************************************
	 * *			Setters and Getters				*
	 * **********************************************
	 */
	
	/**
	 * Sets the Control object that will control the player's motion
	 * <p>
	 * The control must be set if the object should be moved.
	 * @param input
	 */
	public void setControl(Control control)
	{
		this.control = control;
	}
	
	/**
	 * Gets the Control object currently controlling the player
	 * @return
	 */
	public Control getControl()
	{
		return control;
	}

	/**
	 * Returns the vertical angle of the orientation.
	 * @return the verAngle
	 */
	public double getVerAngle() {
		return verAngle;
	}

	/**
	 * Sets the vertical angle of the orientation.
	 * @param verAngle the verAngle to set
	 */
	public void setVerAngle(double verAngle) {
		this.verAngle = verAngle;
	}
	
	/**
	 * Returns the speed.
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed.
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	/**
	 * Get the player name 
	 * @return
	 */
	public String getName(){return name;}
	
	/**
	 * Get the score
	 */
	public int getScore(){return score;}
	
	/**
	 * Add to the score
	 * @param add
	 */
	public void addScore(int add){
		if(add > 0)
			score+= add;
	}
	public void setName(String name){
		this.name = name;
	}
}
