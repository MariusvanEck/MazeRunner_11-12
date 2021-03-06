package mazerunner;

import gamestate.UserInput;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.media.opengl.GL;

import loot.Sword;

public class EnemyAI implements VisibleObject{
	
	private Maze maze;				// the maze
	private Player player;			// the player
	
	// The enemies
	private static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private Iterator<Enemy> it;
	
	private ArrayList<Point> memory;		// the enemies' combined (swarm) memory
	private Enemy enemy;					// current enemy (for updating)
	private EnemyControl control;			// current control (for updating)
	private Random rnd;						// random number generation
	
	/**
	 * In the EnemyAI constructor the enemies are initialised and the maze and player are set
	 * 
	 * @param player	the player
	 * @param maze		the maze
	 */
	public EnemyAI(GL gl, Player player, Maze maze) {
		this.maze = maze;
		this.player = player;
		this.memory = new ArrayList<Point>();
		this.rnd = new Random();
	}
	
	/*
	 * **********************************************
	 * *					update					*
	 * **********************************************
	 */
	
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
			
			// when the enemy has 0 health remove and continue
			if (enemy.getHitpoints() <= 0) {it.remove();}
			
			// when the enemy hit a wall add an in between target//
			if(enemy.hasHitWall() && control.targets.size() < 2) {
				Location inBetweenTarget = 
						avoidWall(enemy, control.targets.get(control.targets.size()-1), 0.2);
				control.targets.add(0, inBetweenTarget);}
			
			// Update enemy Aggro
			boolean wasPlayerVisible = enemy.isPlayerVisible();
			boolean isPlayerVisible = derivePlayerVisible();			
			updateEnemyAgro(isPlayerVisible, wasPlayerVisible, deltaTime);
			
			// if the target list is empty get a new target
			if (control.targets.isEmpty()) {
				nextTarget(enemy, control, memory);
				enemy.setMemory(maze.currentGridPoint(enemy));}
			
			// damage the player with the enemies weapon
			if (control.getAngleToRotate() < 15)
				enemy.getWeapon().doDamage(player);
			
			// update the Enemy's control
			control.setPlayerVisible(isPlayerVisible);
			control.update();
		}
		
		// swing the sword
		if (UserInput.wasMousePressed()) {
			if(((Sword) player.getWeapon()).swingSword(enemies)) {
				enemy.hit();
			}
		}
		
		// reset wasMousePressed
		UserInput.resetMousePressed();
	}

	/**
	 * Update the enemy's targets according to the current aggresive state tov the player
	 */
	private void updateEnemyAgro(boolean ipv, boolean wpv, int deltaTime) {
		
		
		if (ipv) {
			// if the player is and was visible update the targets player location
			// (last member of targets arraylist)
			if (wpv) {control.updateMainTarget(player.locationX, player.locationZ);}
			// if the player is visible and was invisible update the main target
			else {control.updateMainTarget(player.locationX, player.locationZ);}}
			
		else if (!ipv){
			// if the player has just become invisible start the "aggro" timer
			if (wpv) {
				enemy.setTimePassed(deltaTime);}
			
			// if the player stays invisible increment the "aggro" timer
			else if (enemy.getTimePassed() > 0 && enemy.getTimePassed() < 3000) {
				enemy.setTimePassed(enemy.getTimePassed() + deltaTime);
				//after a certain time set the next target (lose "aggro")
				if (enemy.getTimePassed() >= 3000) {
					nextTarget(enemy, control, memory);
					enemy.setTimePassed(0);
					enemy.setMemory(maze.currentGridPoint(enemy));}}
			
			// If an enemy reached the next location in the list, delete this location 
			// and add to the global memory
			if (enemy.atTarget(Maze.SQUARE_SIZE/100d)) {
				control.targets.remove(0);
				memory.add(0, maze.currentGridPoint(enemy));}}
	}
	
	/**
	 * derivePlayerVisible() checks for the current enemy if it can see the player
	 * 
	 * @return
	 */
	private boolean derivePlayerVisible() {
		// is the player in the enemy's viewing cone
		boolean inCone = enemy.derivePlayerInCone(player);
		// is the player close to the enemy
		boolean toClose = (enemy.distanceTo(player) < Maze.SQUARE_SIZE);
		// is the player behind a wall
		boolean behindWall = maze.isVisionBlocked(player, enemy);
		// is the player visible
		boolean playerVisible = (inCone || toClose) && !behindWall;
				
		// set and return
		enemy.setPlayerVisible(playerVisible);
		return playerVisible;
	}
	
	/**
	 * returns an in between location to avoid a wall in the way of an enemy's target
	 */
	private Location avoidWall(Enemy enemy, Location target, double objectSize) {
		
		double xSign = Math.signum(target.locationX - enemy.locationX);
		double zSign = Math.signum(target.locationZ - enemy.locationZ);
		
		double x = enemy.locationX - objectSize*xSign*Maze.SQUARE_SIZE; 
		double z = enemy.locationZ-objectSize*zSign*Maze.SQUARE_SIZE;
		
		// wall in the x direction
		if (maze.isWall(x + xSign*Maze.SQUARE_SIZE, z, 0)) {
			if(zSign > 0) z = (maze.convertToGridZ(z)+ 1.05 + objectSize)*Maze.SQUARE_SIZE;
			else if (zSign < 0) z = (maze.convertToGridZ(z) - 0.05 - objectSize)*Maze.SQUARE_SIZE;
			x = enemy.locationX - xSign*0.01*Maze.SQUARE_SIZE;}
		
		// wall in the z direction
		else if (maze.isWall(x, z + zSign*Maze.SQUARE_SIZE, 0)) {
			if(xSign > 0) x = (maze.convertToGridX(x)+ 1.05 + objectSize)*Maze.SQUARE_SIZE;
			else if (xSign < 0) x = (maze.convertToGridX(x) - 0.05 - objectSize)*Maze.SQUARE_SIZE;
			z = enemy.locationZ - zSign*0.01*Maze.SQUARE_SIZE;}
		
		return new Location(x, z);
	}
	
	/**
	 * Sets the next target for an enemy when the player is not visible
	 * randomly
	 */
	private void nextTarget(Enemy enemy, EnemyControl control, ArrayList<Point> memory) {
		
		// the possible locations list for the enemy
		ArrayList<Point> possibleLocations = new ArrayList<Point>();
		
		// the grid location of the enemy
		Point currentLocation = new Point(maze.convertToGridX(enemy.locationX), maze.convertToGridZ(enemy.locationZ));
		int x = currentLocation.x;
		int z = currentLocation.y;
		
		// initialise the factors array [posX, negX, posZ, negZ] => [0, 1, 2, 3]
		double[] factors = new double[4];
		
		for(int i=0; i<factors.length; i++) {
			possibleLocations.add(null);}
		
		// add the possible locations
		if (!maze.isWall(x+1, z)) {possibleLocations.set(0, new Point(x+1, z)); factors[0] = 1;}
		if (!maze.isWall(x-1, z)) {possibleLocations.set(1, new Point(x-1, z)); factors[1] = 1;}
		if (!maze.isWall(x, z+1)) {possibleLocations.set(2, new Point(x, z+1)); factors[2] = 1;}
		if (!maze.isWall(x, z-1)) {possibleLocations.set(3, new Point(x, z-1)); factors[3] = 1;}
		
		// count the number of possible locations
		int numPl = 0;
		for (int i=0; i<factors.length; i++) {
			numPl += factors[i];}
		
		// set the previous location factor to zero if there are multiple locations
		if (numPl > 1 && possibleLocations.contains(enemy.getMemory()))
			factors[possibleLocations.indexOf(enemy.getMemory())] = 0;
		
		// double the factor for walking straight
		Point straight = new Point(currentLocation); 
		straight.translate(x - enemy.getMemory().x, z - enemy.getMemory().y);
		if (possibleLocations.contains(straight))
			factors[possibleLocations.indexOf(straight)] *= 2;
		
		// multiply the factor for a point contained in the memory by (1 + memoryIndex)/(MAZE_SIZE^2)
		for (int i=0; i<possibleLocations.size(); i++) {
			if (memory.contains(possibleLocations.get(i))) {
				factors[i] *= (1 + memory.indexOf(possibleLocations.get(i)))/Math.pow(maze.getMazeSize(), 2);}}
		
		// make factors cumulative and normalize
		for (int i=1; i<factors.length; i++) {
			factors[i] += factors[i-1];}
		for (int i=0; i<factors.length; i++) {
			factors[i] /= factors[factors.length-1];}
		
		// pick randomly from the possible locations and set
		double random = rnd.nextDouble();
		
		int nextLocationIndex = 0;
		for (int i=0; i<factors.length; i++) {
			if (random <= factors[i]) {
				nextLocationIndex = i;
				break;}}
		
		
		Point nextLocation = possibleLocations.get(nextLocationIndex);
		if (nextLocation != null) {
			control.setTarget( 	((double) nextLocation.x + 0.5) * Maze.SQUARE_SIZE,
								((double) nextLocation.y + 0.5) * Maze.SQUARE_SIZE);}
		else {
			control.setTarget(enemy.locationX, enemy.locationZ);}
	}
	
	
	/*
	 * **********************************************
	 * *			setters and getters				*
	 * **********************************************
	 */
	
	/**
	 * Get the ArrayList with all the enemies associated with this AI
	 */ 
	public static ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	
	/**
	 * Set the ArrayList with all the enemies associated with this AI
	 */ 
	public static void setEnemies(ArrayList<Enemy> enemies) {
		EnemyAI.enemies = enemies;
	}

	/**
	 * Display the enemies
	 */
	@Override
	public void display(GL gl) {
		for (Enemy enemy : enemies) {
			enemy.display(gl);
		}
	}
}
