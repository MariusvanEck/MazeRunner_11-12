package mazerunner;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * TEMP AI CLASS
 * @author Marius
 *
 */
public class EnemyAI{
	
	private Maze maze;
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private Iterator<Enemy> it;
	private ArrayList<Point> memory;
	private Enemy enemy;
	private EnemyControl control;
	
	/**
	 * In the EnemyAI constructor the enemies are initialised and the maze and player are set
	 * 
	 * @param player	the player
	 * @param maze		the maze
	 */
	public EnemyAI(Player player, Maze maze) {
		this.maze = maze;
		this.player = player;
		this.memory = new ArrayList<Point>();
		
		this.enemies = new ArrayList<Enemy>();
		enemies.add(new Enemy(2, 0, 4, 180, 50,	null, "models/test.obj"));
	}
	
	/**
	 * loops over all the enemies to set the right EnemyControl parameters, 
	 * update the EnemyControls
	 */
	public void update(int deltaTime) {
		// get the current iterator over the enemy
		it = enemies.iterator();
		
		// loop over the enemies
		while (it.hasNext()) {
			enemy = it.next();
			control = (EnemyControl) enemy.getControl();
			
			// when the enemy hit a wall add an in between target//
			if(enemy.hasHitWall() && control.targets.size() < 2) {
				Location inBetweenTarget = 
						maze.avoidWall(enemy, control.targets.get(control.targets.size()-1), 0.2);
				control.targets.add(0, inBetweenTarget);}
			
			boolean wasPlayerVisible = enemy.isPlayerVisible();
			boolean isPlayerVisible = derivePlayerVisible();			
			
			if (isPlayerVisible) {
				// if the player is and was visible update the targets player location
				// (last member of targets arraylist)
				if (wasPlayerVisible) {control.updateMainTarget(player.locationX, player.locationZ);}
				// if the player is visible and was invisible update the main target
				else {control.updateMainTarget(player.locationX, player.locationZ);}}
			
			else if (!isPlayerVisible){
				// if the player has just become invisible start the "aggro" timer
				if (wasPlayerVisible) {
					enemy.setTimePassed(deltaTime);}
				
				// if the player stays invisible increment the "aggro" timer
				else if (enemy.getTimePassed() > 0 && enemy.getTimePassed() < 3000) {
					enemy.setTimePassed(enemy.getTimePassed() + deltaTime);
					//after a certain time set the next target (lose "aggro")
					if (enemy.getTimePassed() >= 3000) {
						maze.nextTarget(enemy, control, memory);
						enemy.setTimePassed(0);
						enemy.setMemory(maze.currentGridPoint(enemy));}}}
			
			// If an enemy reached the next location in the list, delete this location 
			// and add to the global memory
			if (enemy.atTarget(Maze.SQUARE_SIZE/100d)) {
				control.targets.remove(0);
				memory.add(0, maze.currentGridPoint(enemy));}
			
			// if the target list is empty get a new target
			if (control.targets.isEmpty()) {
				maze.nextTarget(enemy, control, memory);
				enemy.setMemory(maze.currentGridPoint(enemy));}
			
			// update the Enemy's control
			control.update();
		}
		
	}

	/**
	 * derivePlayerVisible() checks for the current enemy if it can see the player
	 * 
	 * @return
	 */
	public boolean derivePlayerVisible() {
		// is the player in the enemy's viewing cone
		boolean inCone = enemy.derivePlayerInCone(player);
		// is the player close to the enemy
		boolean toClose = (enemy.distanceTo(player) < Maze.SQUARE_SIZE);
		
		// is the player behind a wall
		boolean behindWall = maze.isVisionBlocked(player.locationX, player.locationZ, 
				enemy.locationX, enemy.locationZ);;
		// is the player visible
		boolean playerVisible = (inCone || toClose) && !behindWall;
				
		// set and return
		enemy.setPlayerVisible(playerVisible);
		return playerVisible;
	}
	
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
}
