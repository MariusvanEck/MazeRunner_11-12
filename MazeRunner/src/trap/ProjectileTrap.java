package trap;

import java.util.ArrayList;

import javax.media.opengl.GL;

import mazerunner.Creature;
import mazerunner.Enemy;
import mazerunner.GameObject;
import mazerunner.Maze;
import mazerunner.Player;
import model.Model;
import model.TexturedModel;

public class ProjectileTrap extends GameObject implements Trap{
	
	private Projectile projectile;			// the associated projectile
	private boolean triggered;				// boolean storing if the trap is active
	private Maze maze;						// a reference to the maze
	
	private int Counter = 0;				// counts the time since done damage	
	
	// trap orientation
	private char direction;		
	private double angle;
	
	// the model for the trap
	private TexturedModel model;			
	private static String 	modelFileLocation = "models/trap/trapbase.obj",
							textureFileLocation = "models/trap/trapbase.png";
	
	/**
	 * @param gl			GL for the rendering
	 * @param maze			The maze
	 * @param x				The trap coordX
	 * @param y				The trap coordY
	 * @param z				The trap coordZ
	 * @param direction		The direction the Projectile goes (N,E,S,W)
	 * @param speed			The projectile speed
	 */
	public ProjectileTrap(GL gl, Maze maze, double x, double z, char direction, double speed){
		super(	x*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2,
				Maze.SQUARE_SIZE/4, 
				z*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2);
		
		this.model = new TexturedModel(gl, new Model(modelFileLocation, 0.05f), textureFileLocation);
		this.maze = maze;
		this.direction = direction;
		
		// set the correct angle
		switch(direction){
			case 'N':
			case 'n':
				angle = 90;
				break;
			case 'E':
			case 'e':
				angle = 0;
				break;
			case 'S':
			case 's':
				angle = 270;
				break;
			case 'W':
			case 'w':
				angle = 180;
		}
		
		
		projectile = new Projectile(gl,locationX,locationY,locationZ,direction,speed);
		this.triggered = false;
	}
	
	/**
	 * updates the trap
	 */
	public void update(int deltaTime, Player player, ArrayList<Enemy> enemies){
		
		// create list with all the creatures
		@SuppressWarnings("unchecked")
		ArrayList<Creature> creatures = (ArrayList<Creature>) enemies.clone();
		creatures.add(player);
		
		// update triggered
		for (Creature creature : creatures) {
			switch(direction){
				case 'N':
					if(creature.getLocationZ() <= locationZ && nearAxis(locationX,creature.getLocationX(),0.20) && !maze.isVisionBlocked(this, creature))
						this.triggered = true;
					break;
				case 'E':
					if(creature.getLocationX() >= locationX && nearAxis(locationZ,creature.getLocationZ(),0.20) && !maze.isVisionBlocked(this, creature))
						this.triggered = true;
					break;
				case 'S':
					if(creature.getLocationZ() >= locationZ && nearAxis(locationX,creature.getLocationX(),0.20) && !maze.isVisionBlocked(this, creature))
						this.triggered = true;
					break;
				case 'W':
					if(creature.getLocationX() <= locationX && nearAxis(locationZ,creature.getLocationZ(),0.20) && !maze.isVisionBlocked(this, creature))
						this.triggered = true;
					break;
			}
			
			if (triggered) break;
		}
		
		if (triggered) {
		
			double x = projectile.getLocationX();
			double z = projectile.getLocationZ();
			projectile.update(deltaTime);
			
			if(		maze.isWall(projectile.getLocationX(),projectile.getLocationZ(),Math.abs(x-projectile.getLocationX())) || 
					maze.isStair(projectile.getLocationX(), projectile.getLocationZ(),Math.abs(z-projectile.getLocationZ())))
					projectile.setLocation(this.locationX, this.locationY, this.locationZ);
			
			
			for (Creature creature : creatures) {
				if(projectile.near(creature, Maze.SQUARE_SIZE/16)){
					if(Counter > 500){
				
						creature.removeHP(projectile.getDamage());
						if (creature instanceof Enemy) {
							((Enemy)creature).hit();
						}
						
						Counter = 0;
					}
					projectile.setLocation(this.locationX, this.locationY, this.locationZ); // so the projectile doesn't go trough a creature
				}
				else {
					Counter += deltaTime;
				}
			}
		}
	}
	
	/**
	 * display the trap
	 */
	public void display(GL gl) {
		model.render(gl, angle, locationX, locationY, locationZ);
		
		if(triggered)
			projectile.display(gl);
	}

	/**
	 * Checks if a creature is near the axis of the trap
	 */
	private boolean nearAxis(double axis,double pos,double near){
		return (Math.abs(axis-pos) < near);
	}
	
}
