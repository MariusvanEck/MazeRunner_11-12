package mazerunner;

import gamestate.GameStateManager;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.opengl.GL;
import javax.swing.JFileChooser;

import loot.*;
import trap.*;

import com.sun.opengl.util.texture.Texture;

import database.DataBase;

/**
 * Maze represents the maze used by MazeRunner.
 */
public class Maze implements VisibleObject {
	
	private int mazeSize;							// The amount of squares (wall / no wall)
	private int numLevels;							// The amount of different levels
	public final static double SQUARE_SIZE = 5;		// scale factor for displaying
	
	private HashMap<String,Texture> textures;			// reference to the texture hashmap
	private Texture currentTexture;						// specifies the current texture
	
	private int currentLevelIndex = 0;					// specifies the currentLevel
	private ArrayList<int[][]> levels;					// the levels in an arraylist
	private int[][] currentLevel;						// the current level
	
	// the object lists to display
	private ArrayList<Stair> stairs;
	private ArrayList<Floor> floors;
	private ArrayList<Roof> roofs;
	private ArrayList<Wall> walls;
	private HashMap<Point,SlidingWall> slidingWalls;
	private End end;
	
	
	
	/**
	 * The Maze Constructor loads the map from the DataBase into the memory
	 * @param gl			GL for rendering the models
	 * @param player		Player so the traps can react when a player is in range, and deal damage to the player
	 * @param dataBase		DataBase is needed to load from
	 * @param name			String Name of the Map  which will be loaded
	 * @param textures		HashMap<> Contains the needed textures
	 */
	public Maze(GL gl, DataBase dataBase, String name, HashMap<String,Texture> textures){
		this.textures = textures;
		this.levels = new ArrayList<int[][]>();
		
		this.load(gl,dataBase,name);
	}
	
	
	/*
	 * **********************************************
	 * *			   load functions	 			*
	 * **********************************************
	 */
	
	/**
	 * Load a maze from the database
	 */
	private void load(GL gl,DataBase dataBase,String name){
		mazeSize = dataBase.getMazeSize(name);
		numLevels = dataBase.getNumLevels(name);
		
		for(int i = 0; i < numLevels; i++)
			levels.add(dataBase.getMap(name, i));
	}
	
	/**
	 * Get the object of the current level and fill the arraylists with active objects
	 */
	private void loadCurrentLevelObjects(GL gl) {
		
		stairs = new ArrayList<Stair>();
		roofs = new ArrayList<Roof>();
		walls = new ArrayList<Wall>();
		slidingWalls = new HashMap<Point,SlidingWall>();
		floors = new ArrayList<Floor>();
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		ArrayList<Trap> traps = new ArrayList<Trap>();
		ArrayList<Loot> loot = new ArrayList<Loot>();
		
		for (int x=0; x<mazeSize; x++) {
			for (int z=0; z<mazeSize; z++) {
				// check for a wall
				if (currentLevel[x][z] == 1) 
					walls.add(new Wall(x, z));
				
				// add other objects
				else {
					// roof and floor
					roofs.add(new Roof(x, z));
					floors.add(new Floor(x, z));
					
					if (currentLevel[x][z] != 0) {
						// check for stairs
						if (currentLevel[x][z]%11 == 0) {
							if(currentLevel[x][z+1] != 0 && currentLevel[x][z+1]%13 == 0) stairs.add(new Stair(gl,x+1,z,180)); 		// N
							else if(currentLevel[x+1][z] != 0 && currentLevel[x+1][z]%13 == 0) stairs.add(new Stair(gl,x,z,270)); 		// E
							else if(currentLevel[x][z-1] != 0 && currentLevel[x][z-1]%13 == 0) stairs.add(new Stair(gl,x,z+1,0)); 		// S
							else if(currentLevel[x-1][z] != 0 && currentLevel[x-1][z]%13 == 0) stairs.add(new Stair(gl,x+1,z+1,90)); 	// W
						}
				
						// check for enemies and add to the AI
						if (currentLevel[x][z]%23 == 0) 
							enemies.add(new Enemy(gl, x, z, 0));
						
						// check for Food
						if(currentLevel[x][z]%19 == 0)
							loot.add(new Food(gl, x, z, 10));
						
						// check for Gold1
						if(currentLevel[x][z]%2 == 0)
							loot.add(new Gold(gl, x-.25, z-.25));
						
						// check for Gold2
						if(currentLevel[x][z]%3 == 0)
							loot.add(new Gold(gl, x+.25, z-.25));
						
						// check for Gold3
						if(currentLevel[x][z]%5 == 0)
							loot.add(new Gold(gl, x-.25, z+.25));
						
						// check for Gold4
						if(currentLevel[x][z]%7 == 0)
							loot.add(new Gold(gl, x+.25, z+.25));
						
						// check for sliding walls {
						if (currentLevel[x][z]%37 == 0)
							slidingWalls.put(new Point(x,z), new SlidingWall(x,z));
						
						// check for Traps
						// Trap N
						if(currentLevel[x][z]%41 == 0)
							traps.add(new ProjectileTrap(gl,this,x,z,'N',0.01));
						// Trap E
						else if(currentLevel[x][z]%43 == 0)
							traps.add(new ProjectileTrap(gl,this,x,z,'E',0.01));
						// Trap S
						else if(currentLevel[x][z]%47 == 0)
							traps.add(new ProjectileTrap(gl,this,x,z,'S',0.01));
						// Trap W
						else if(currentLevel[x][z]%53 == 0)
							traps.add(new ProjectileTrap(gl,this,x,z,'W',0.01));
						
						// check for end
						if (currentLevel[x][z]%59 == 0) {
							end = new End(gl, x, z);
						}
					}
				}
			}
		}
		
		EnemyAI.setEnemies(enemies);
		TrapController.setTraps(traps);
		LootController.setLootList(loot);
	}
	
	
	/*
	 * **********************************************
	 * *			  		Checks					*
	 * **********************************************
	 */
	
	/**
	 * isWall(int x, int y, int z) checks for a wall.
	 * <p>
	 * It returns whether maze[x][z][y] contains a 1.
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param y		the y-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a wall at maze[x][z]
	 */
	public boolean isWall( int x, int z ) {
		if( x >= 0 && x < mazeSize && z >= 0 && z < mazeSize ) {
			if (currentLevel[x][z] == 1 || currentLevel[x][z] == 13) return true;
			else if (currentLevel[x][z] == 37) {
				if (slidingWalls.get(new Point(x,z)).isWall()) return true;
			}
		}
		return false;
	}
	
	/**
	 * isStair(int x,int y,int z) checks for a stair
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param y		the y-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a stair at maze[x][z][y]
	 */
	public boolean isStair(int x ,int z) {
		if(x >= 0 && x < mazeSize && z >= 0 && z < mazeSize)
			return currentLevel[x][z] == 11;
		return false;
	}
	
	/**
	 * Checks if there is a wall on the line between the two points specified
	 * 
	 * @param x1	x-coordinate of the first point
	 * @param y1	y-coordinate of the first point
	 * @param x2	x-coordinate of the second point
	 * @param y2	y-coordinate of the second point
	 */
	public boolean isVisionBlocked(GameObject GameObject1,
			GameObject GameObject2) {
		
		double x1 = GameObject1.getLocationX();
		double x2 = GameObject2.getLocationX();
		double z1 = GameObject1.getLocationZ();
		double z2 = GameObject2.getLocationZ();
		
		// sort the points so that X2 > X1
		if (x1 > x2) {
			double Xtemp = x1, Ztemp = z1;
			x1 = x2; z1 = z2;
			x2 = Xtemp; z2 = Ztemp;}
		
		// get the current and final grid position and calculate the direction of z
		double zDir = Math.signum(z2-z1);
		double currentXGrid = convertToGridX(x1);
		double currentZGrid = convertToGridZ(z1);
		double finalXGrid = convertToGridX(x2);
		double finalZGrid = convertToGridZ(z2);
		
		// normalise the points
		x1 = x1/SQUARE_SIZE; x2 = x2/SQUARE_SIZE;
		z1 = z1/SQUARE_SIZE; z2 = z2/SQUARE_SIZE;
		
		// get the line Equation Z = a*X + b
		double a = (z2-z1)/(x2-x1);
		double b = z1 - a*x1;
		
		while (true) {
			// if the current grid position is a wall, return true
			if (isWall((int) currentXGrid, (int) currentZGrid)) return true;
			
			// if the current grid point is the same as the final grid position, break
			if (currentXGrid == finalXGrid && currentZGrid == finalZGrid) break;
			
			// if the line passes to the grid next grid point in the positive x-direction move over there
			// else move 1 grid point in the z direction
			double zMoved = a*(currentXGrid + 1d) + b;
			if (zMoved >= currentZGrid && zMoved <= (currentZGrid + 1)) currentXGrid++;
			else currentZGrid += zDir;}
		
		return false;
	}
	
	/**
	 * isWall(double x, double z, double objectSize) checks for a wall in a square with edge 
	 * objectSize*SQUARE_SIZE around the location by converting the double values to integer 
	 * coordinates.
	 * <p>
	 * This method first converts the x and z to values that correspond with the grid 
	 * defined by maze[][][]. Then it calls upon isWall(int, int, int) to check for a wall.
	 * 
	 * @param x					the x-coordinate of the location to check
	 * @param z					the z-coordinate of the location to check
	 * @param objectSize		size of the square to check
	 * @return					whether there is a wall at maze[x][z] at the current level
	 */
	public boolean isWall(double x, double z, double objectSize) {
		int gXmin = convertToGridX(x-objectSize*SQUARE_SIZE);
		int gXmax = convertToGridX(x+objectSize*SQUARE_SIZE);
		int gZmin = convertToGridZ(z-objectSize*SQUARE_SIZE);
		int gZmax = convertToGridZ(z+objectSize*SQUARE_SIZE);
		return 	isWall(gXmin, gZmin) || isWall(gXmax, gZmax) ||
				isWall(gXmin, gZmax) || isWall(gXmax, gZmin) ||
				isWall(gXmin, gZmin) || isWall(gXmax, gZmax) ||
				isWall(gXmin, gZmax) || isWall(gXmax, gZmin);
	}
	
	/**
	 * isStair(double x, double z, double objectSize) checks for a stair in a square with edge 
	 * objectSize*SQUARE_SIZE around the location by converting the double values to integer 
	 * coordinates.
	 * <p>
	 * This method first converts the x and z to values that correspond with the grid 
	 * defined by maze[][]. Then it calls upon isStair(int, int) to check for a stair.
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a stair at maze[x][z][y]
	 */
	public boolean isStair(double x, double z, double objectSize) {
		int gXmin = convertToGridX(x-objectSize*SQUARE_SIZE);
		int gXmax = convertToGridX(x+objectSize*SQUARE_SIZE);
		int gZmin = convertToGridZ(z-objectSize*SQUARE_SIZE);
		int gZmax = convertToGridZ(z+objectSize*SQUARE_SIZE);
		
		return 	isStair(gXmin,gZmin) || isStair(gXmax,gZmax) ||
				isStair(gXmin,gZmax) || isStair(gXmax,gZmin) ||
				isStair(gXmin,gZmin) || isStair(gXmax,gZmax) ||
				isStair(gXmin,gZmax) || isStair(gXmax,gZmin);			
	}
	
	
	/*
	 * **********************************************
	 * *			   GridConversion	 			*
	 * **********************************************
	 */
	
	/**
	 * getCurrentGridPoint takes a gameObject and returns a Point object with the 
	 * coordinates of the current grid point
	 */
	public Point currentGridPoint(GameObject gameObject) {
		return new Point(convertToGridX(gameObject.locationX), convertToGridZ(gameObject.locationZ));
	}
	
	/**
	 * Converts the double x-coordinate to its correspondent integer coordinate.
	 * @param x		the double x-coordinate
	 * @return		the integer x-coordinate
	 */
	public int convertToGridX( double x )
	{
		return (int)Math.floor( x / SQUARE_SIZE );
	}
	
	/**
	 * Converts the double x-coordinate to its correspondent integer coordinate.
	 * @param y		the double y-coordinate
	 * @return		the integer y-coordinate
	 */
	public int convertToGridY(double y){
		return (int)Math.floor( y / SQUARE_SIZE );
	}
	
	/**
	 * Converts the double z-coordinate to its correspondent integer coordinate.
	 * @param z		the double z-coordinate
	 * @return		the integer z-coordinate
	 */
	public int convertToGridZ(double z)
	{
		return (int)Math.floor( z / SQUARE_SIZE );
	}
	
	
	/*
	 * **********************************************
	 * *				 miscelanous				*
	 * **********************************************
	 */

	/**
	 * load a new level of the maze
	 */
	public void changeLevel(int i, GL gl) {
		if (i < numLevels) {
			setCurrentLevel(i);
			loadCurrentLevelObjects(gl);}
	}
	
	/**
	 * Pick a file from the mazes directory
	 */
	public static String selectMaze(GameStateManager gsm) {
		JFileChooser fileChooser = new JFileChooser("mazes");
		
		int action = fileChooser.showOpenDialog(gsm);
		if (action == JFileChooser.APPROVE_OPTION) {
			String fileString = fileChooser.getSelectedFile().getName();
			fileString = fileString.substring(0, fileString.length() - 5);
			return fileString;}
		else
			return null;
	}
	
	/**
	 * update the sliding walls
	 */
	public void updateSlidingWalls(int deltaTime, Player player) {
		for (SlidingWall sw : slidingWalls.values()) {
			sw.update(deltaTime, player);
		}
	}
	
	/**
	 * Check if the player is at the end
	 */
	public boolean atEnd(Player player) {
		if (end != null) {
			if (player.near(end, .5)) {
				return true;
			}
		}
		return false;
	}
	
	
	/*
	 * **********************************************
	 * *			Drawing function				*
	 * **********************************************
	 */
	
	/**
	 * maze display function
	 */
	@Override
	public void display(GL gl) {
				
		//// walls ////
		bindCurrentTexture("wall"); // bind wall texture
		for (Wall w : walls) {
			w.display(gl);}
		for (SlidingWall sw : slidingWalls.values()) {
			sw.display(gl);}
		currentTexture.disable(); // disable wall texture
		
		//// floors ////
		bindCurrentTexture("floor"); // bind floor texture
		for(Floor f : floors) {
			f.display(gl);}
		for (SlidingWall sw : slidingWalls.values()) {
			sw.displayFloor(gl);}
		currentTexture.disable(); // disable floor texture
		
		//// roofs ////
		bindCurrentTexture("roof"); // bind roof texture
		for(Roof r : roofs) {
			r.display(gl);}
		currentTexture.disable(); // disable roof texture
	
		//// stairs ////
		for(Stair s : stairs){
				s.display(gl);}
		
		//// end ////
		if (end != null) {
			end.display(gl);
		}
	}
	

	/*
	 * **********************************************
	 * *			getters and setters 			*
	 * **********************************************
	 */
	
	/**
	 * get the current active level index
	 */
	public int getCurrentLevel() {
		return currentLevelIndex;
	}
	
	/**
	 * set the current active level
	 * @param currentLevel
	 */
	public void setCurrentLevel(int currentLevelIndex) {
		this.currentLevelIndex = currentLevelIndex;
		currentLevel = levels.get(currentLevelIndex);
		
	}
	
	/**
	 * set the texture hashmap
	 */
	public void setTextures(HashMap<String,Texture> textures) {
		this.textures = textures;
	}

	/**
	 * set the currentTexture and bind
	 */
	private void bindCurrentTexture(String textureName) {
		currentTexture = textures.get(textureName);
		currentTexture.enable();
		currentTexture.bind();
	}
	
	/** 
	 * get the maze size
	 */
	public int getMazeSize(){
		return mazeSize;
	}
	
	/** 
	 * get the level size
	 */
	public int getLevelSize(){
		return numLevels;
	}
	
	/**
	 * get a certain level
	 */
	public int[][] getLevel(int i){
		return levels.get(i);
	}
	
	/**
	 * print the levels
	 */
	public void lvlToString(){
		System.out.println("Maze: ");
		for(int[][] level:levels){
			for(int i = 0; i < mazeSize; i++){
				for(int j = 0; j < mazeSize; j++){
					System.out.print(level[j][i]);
					System.out.print(' ');
				}
				System.out.print('\n');
			}
		}
	}

	/**
	 * Get all the maze objects
	 */
	public ArrayList<Object> getAllMazeObjects(){
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.add(stairs);
		objects.add(floors);
		objects.add(roofs);
		objects.add(walls);
		objects.add(slidingWalls);
		objects.add(end);
		
		return objects;
	}
}
