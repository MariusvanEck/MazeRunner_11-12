package loot;

import gamestate.Sound;

import java.util.Calendar;

import javax.media.opengl.GL;

import mazerunner.Creature;
import mazerunner.Maze;
import mazerunner.Player;
import model.Model;
import model.TexturedModel;

public class Sword extends Weapon{
	
	private int downTime = 1000;							// the weapon down time in ms
	private Long timeDoneLastDamage;						// last time the stick was swung
	private int damage = 50;								// the sticks damage output
	private double range = .5;								// the damage range for the stick in units of SQUARE_SIZE
	private int counter;									// counter counts time since sword was swung in ms
	private boolean animating = false;						// boolean check for in animation			
	
	// the model
	private TexturedModel model;
	private final static String modelFileLocation = "models/Killer_Frost_Ice_Sword/Killer_Frost_Ice_Sword.obj",
								textureFileLocation = "models/Killer_Frost_Ice_Sword/Killer_Frost_Ice_Sword_D.tga";
	
	// the sounds
	private final static Sound 	hit = new Sound("sword-hit.wav"),
								miss = new Sound("sword-miss.wav");
	
	/**
	 * Sword update function
	 */
	public void update(int deltaTime, Player player) {
		// Set the sword location 
		setWieldX(player.getLocationX() + (Maze.SQUARE_SIZE/5)*Math.cos(Math.toRadians(player.getHorAngle())));
		setWieldY(player.getLocationY() - Maze.SQUARE_SIZE/10);
		setWieldZ(player.getLocationZ() - (Maze.SQUARE_SIZE/5)*Math.sin(Math.toRadians(player.getHorAngle())));
		
		// calculate the sword angles
		double weaponAngleX = 20+player.getVerAngle();
		double weaponAngleY = player.getHorAngle();
		double weaponAngleZ = 90;

		// animation
		if (animating) {
			if (counter < 200) {
				weaponAngleX -= 15*Math.sin((counter/200d)*Math.PI);
				weaponAngleZ += 360*(counter/200d);
				counter += deltaTime;
			}
			else {
				animating = false;
				counter = 0;
			}
		}
		
		// set the angles
		setAngleX(weaponAngleX);
		setAngleY(weaponAngleY); // sideways, without effects
		setAngleZ(weaponAngleZ); //
		
		
	}
	
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
			animating = true;
			
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
