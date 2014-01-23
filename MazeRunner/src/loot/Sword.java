package loot;

import gamestate.Sound;

import java.util.ArrayList;
import java.util.Calendar;

import javax.media.opengl.GL;

import mazerunner.Creature;
import mazerunner.Enemy;
import mazerunner.GameObject;
import mazerunner.Maze;
import mazerunner.Player;
import model.Model;
import model.TexturedModel;

public class Sword extends Weapon{
	
	private int downTime = 1000;							// the weapon down time in ms
	private Long timeDoneLastDamage;						// last time the stick was swung
	private int damage = 50;								// the sticks damage output
	private double range = .5;								// the damage range for the stick in units of SQUARE_SIZE
	private int counter = 0;									// counter counts time since sword was swung in ms
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
		setWieldX(player.getLocationX() + (Maze.SQUARE_SIZE/5)*Math.cos(Math.toRadians(player.getHorAngle()+30)));
		setWieldY(player.getLocationY() - Maze.SQUARE_SIZE/10 - 0.3);
		setWieldZ(player.getLocationZ() - (Maze.SQUARE_SIZE/5)*Math.sin(Math.toRadians(player.getHorAngle()+30)));
		
		// calculate the sword angles
		double weaponAngleX = 30+0.5*player.getVerAngle();
		double weaponAngleY = player.getHorAngle()-10;
		double weaponAngleZ = 90;

		// animation
		if (animating) {
			double weaponspeed = 700;
			double raise = 3; // less is higher
			if (counter < weaponspeed) {
				if(counter<weaponspeed/raise){
					weaponAngleY -= 30*Math.sin((counter/((raise-1)*weaponspeed/raise))*Math.PI);
				}
				else{
					weaponAngleY += 30*Math.sin(((counter-weaponspeed/raise)/((raise-1)*weaponspeed/raise))*Math.PI);
				}
				if(counter<weaponspeed/10){
					weaponAngleX += 30*Math.sin((counter/((raise-1)*weaponspeed/raise))*Math.PI);
				}
				else{
					weaponAngleX -= 30*Math.sin(((counter-weaponspeed/raise)/((raise-1)*weaponspeed/raise))*Math.PI);
				}
				weaponAngleZ = 45;
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
	public boolean swingSword (ArrayList<Enemy> enemies) {
		
		if ((timeDoneLastDamage == null || 
			Calendar.getInstance().getTimeInMillis() - timeDoneLastDamage > downTime)) {
			timeDoneLastDamage = Calendar.getInstance().getTimeInMillis();
			animating = true;
			
			for (Enemy enemy : enemies) {
				double enemyAngle = (180/Math.PI)*
						Math.atan2(getCreature().getLocationX() - enemy.getLocationX(), 
								getCreature().getLocationZ() - enemy.getLocationZ());

				boolean incone = Math.abs(enemyAngle - GameObject.normaliseAngle(getCreature().getHorAngle())) < 45;
				if (enemy.near(getCreature(), range) && incone) {
					doDamage(enemy);
					return true;
				}
			}
			miss.play();
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
