package trap;

import java.util.ArrayList;

import javax.media.opengl.GL;

import mazerunner.Creature;
import mazerunner.Enemy;
import mazerunner.Maze;
import mazerunner.Player;

public class ProjectileTrap extends Trap {
	private Projectile projectile = null;
	private boolean triggered;
	private Maze maze;
	private int lastUpdate = 0;
	private char direction;
	private double angle;
	
	private static String modelFileLocation = "models/trap/trapbase.obj";
	private static String textureFileLocation = "models/trap/trapbase.png";
	
	/**
	 * @param gl			GL for the rendering
	 * @param player		The Player who can activate the trap
	 * @param maze			The maze
	 * @param x				The trap coordX
	 * @param y				The trap coordY
	 * @param z				The trap coordZ
	 * @param activationX	The activation coordX
	 * @param activationY	The activation coordY (as in wich lvl)
	 * @param activationZ	The activation coordZ
	 * @param direction		The direction the Projectile goes (N,E,S,W)
	 * @param speed			The speed relative to the player
	 */
	public ProjectileTrap(GL gl,Maze maze,double x,double z,char direction,double speed){
		super(gl,x,z,modelFileLocation,textureFileLocation);
		this.maze = maze;
		this.direction = direction;
		
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
	
	public void update(int deltaTime, Player player, ArrayList<Enemy> enemies){
		
		// create list with all the creatures
		@SuppressWarnings("unchecked")
		ArrayList<Creature> creatures = (ArrayList<Creature>) enemies.clone();
		creatures.add(player);
		
		// update triggered
		for (Creature creature : creatures) {
			switch(direction){
				case 'N':
					if(creature.getLocationX() <= locationX && nearAxis(locationX,creature.getLocationX(),0.20) && !maze.isVisionBlocked(this, creature))
						this.triggered = true;
					break;
				case 'E':
					if(creature.getLocationZ() >= locationZ && nearAxis(locationZ,creature.getLocationZ(),0.20) && !maze.isVisionBlocked(this, creature))
						this.triggered = true;
					break;
				case 'S':
					if(creature.getLocationX() >= locationX && nearAxis(locationX,creature.getLocationX(),0.20) && !maze.isVisionBlocked(this, creature))
						this.triggered = true;
					break;
				case 'W':
					if(creature.getLocationZ() <= locationZ && nearAxis(locationZ,creature.getLocationZ(),0.20) && !maze.isVisionBlocked(this, creature))
						this.triggered = true;
					break;
			}
			
			if (triggered) break;
		}
		
		if (triggered) {
		
			double x = projectile.getX();
			double z = projectile.getZ();
			projectile.update(deltaTime);
			
			if(maze.isWall(projectile.getX(),projectile.getZ(),Math.abs(x-projectile.getX())) || maze.isStair(projectile.getX(), projectile.getZ(),Math.abs(z-projectile.getZ())))
					projectile.setLocation(this.locationX, this.locationY, this.locationZ);
			
			
			for (Creature creature : creatures) {
				if(projectile.near(creature, Maze.SQUARE_SIZE/16)){
					if(lastUpdate > 500){
				
						creature.removeHP(projectile.getDamage());
						if (creature instanceof Enemy) {
							((Enemy)creature).hit();
						}
						
						lastUpdate = 0;
					}
					projectile.setLocation(this.locationX, this.locationY, this.locationZ); // so the projectile doesn't go trough a creature
				}
				else {
					lastUpdate += deltaTime;
				}
			}
		}
	}
	
	@Override
	public void display(GL gl) {
		model.render(gl, angle, locationX, locationY, locationZ);
		
		if(triggered)
			projectile.display(gl);
	}
	
	private double distanceToAxis(double axis,double pos){
		return Math.abs(axis-pos);
	}
	
	private boolean nearAxis(double axis,double pos,double near){
		return (distanceToAxis(axis,pos) < near);
	}
	
}
