package mazerunner;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;

/**
 * Maze represents the maze used by MazeRunner.
 */
public class Maze implements VisibleObject {
	
	public static double MAZE_SIZE = 10;
	public static double LEVEL_SIZE = 1;
	public final static double SQUARE_SIZE = 5;
	
	private HashMap<String,Texture> textures;			// reference to the texture hashmap
	private Texture currentTexture;						// specifies the current texture
	private int currentLevel = 0;						// specifies the currentLevel
	
	private Random rnd = new Random();					// random generator

	private ArrayList<int[][]> maze;
	private int[][] level;
	
	public void load(String file){
		try{     
			FileInputStream fmaze = new FileInputStream(file);
			ObjectInputStream omaze = new ObjectInputStream(fmaze);
	      
			LEVEL_SIZE = (int) omaze.readObject();
			MAZE_SIZE = (int) omaze.readObject();
	      
			maze = new ArrayList<int[][]>();
			for (int i=0; i<LEVEL_SIZE; i++) {
				maze.add((int[][]) omaze.readObject());}
	  
			level = maze.get(currentLevel);
			omaze.close();    
		}
        catch(Exception ex){ex.printStackTrace();}
}
	
	
	/*
	 * **********************************************
	 * *			  Checks for Objects			*
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
	public boolean isWall( int x, int z )
	{
		if( x >= 0 && x < MAZE_SIZE && z >= 0 && z < MAZE_SIZE )
			return level[x][z] == 1;
		else
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
	public boolean isVisionBlocked(double x1, double z1,
			double x2, double z2) {
		
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
	 * isStair(int x,int y,int z) checks for a stair
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param y		the y-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a stair at maze[x][z][y]
	 */
	public boolean isStair(int x,int y,int z){
		if(x >= 0 && x < MAZE_SIZE && y >= 0 && y < LEVEL_SIZE && z >= 0 && z < MAZE_SIZE)
			return level[x][z] == 11;
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
	public boolean isWall(double x, double z, double objectSize)
	{
		int gXmin = convertToGridX(x-objectSize*SQUARE_SIZE);
		int gXmax = convertToGridX(x+objectSize*SQUARE_SIZE);
		int gZmin = convertToGridZ(z-objectSize*SQUARE_SIZE);
		int gZmax = convertToGridZ(z+objectSize*SQUARE_SIZE);
		return 	isWall(gXmin,gZmin) || isWall(gXmax, gZmax) ||
				isWall(gXmin, gZmax) || isWall(gXmax, gZmin) ||
				isWall(gXmin, gZmin) || isWall(gXmax, gZmax) ||
				isWall(gXmin, gZmax) || isWall(gXmax, gZmin);
	}
	
	/**
	 * isStair(double x, double z) checks for a stair in a square with edge 0.1*SQUARE_SIZE around the location
	 * by converting the double values to integer coordinates.
	 * <p>
	 * This method first converts the x and z to values that correspond with the grid 
	 * defined by maze[][]. Then it calls upon isStair(int, int, int) to check for a stair.
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param y		the y-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a stair at maze[x][z][y]
	 */
	public boolean isStair(double x,double y,double z){
		int gXmin = convertToGridX(x-0.1*SQUARE_SIZE);
		int gXmax = convertToGridX(x+0.1*SQUARE_SIZE);
		int gYmin = convertToGridX(y-0.1*SQUARE_SIZE);
		int gYmax = convertToGridX(y+0.1*SQUARE_SIZE);
		int gZmin = convertToGridZ(z-0.1*SQUARE_SIZE);
		int gZmax = convertToGridZ(z+0.1*SQUARE_SIZE);
		
		return 	isStair(gXmin,gYmin,gZmin) || isStair(gXmax,gYmin,gZmax) ||
				isStair(gXmin,gYmin,gZmax) || isStair(gXmax,gYmin,gZmin) ||
				isStair(gXmin,gYmax,gZmin) || isStair(gXmax,gYmax,gZmax) ||
				isStair(gXmin,gYmax,gZmax) || isStair(gXmax,gYmax,gZmin);
				
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
	private int convertToGridX( double x )
	{
		return (int)Math.floor( x / SQUARE_SIZE );
	}
	
	/**
	 * Converts the double x-coordinate to its correspondent integer coordinate.
	 * @param y		the double y-coordinate
	 * @return		the integer y-coordinate
	 */
	@SuppressWarnings("unused")
	private int convertToGridY(double y){
		return (int)Math.floor( y / SQUARE_SIZE );
	}
	
	/**
	 * Converts the double z-coordinate to its correspondent integer coordinate.
	 * @param z		the double z-coordinate
	 * @return		the integer z-coordinate
	 */
	private int convertToGridZ( double z )
	{
		return (int)Math.floor( z / SQUARE_SIZE );
	}
	
	
	/*
	 * **********************************************
	 * *				 miscelanous				*
	 * **********************************************
	 */
	
	/**
	 * Sets the specified location to wall or no wall
	 * 
	 * @param x			x-coordinate
	 * @param z			z-coordinate
	 * @param isWall 	true for wall, false for no wall
	 */
	public synchronized void editMaze(int x, int y,int z, boolean isWall) {
		level[x][z] = isWall? 1 : 0;
	}
	
	/**
	 * Sets the next target for an enemy when the player is not visible
	 * randomly
	 */
	public void nextTarget(Enemy enemy, EnemyControl control, ArrayList<Point> memory) {
		
		//TODO verwijder alle testprints over nextTarget
		
		// the possible locations list for the enemy
		ArrayList<Point> possibleLocations = new ArrayList<Point>();
		
		// the grid location of the enemy
		Point currentLocation = new Point(convertToGridX(enemy.locationX), convertToGridZ(enemy.locationZ));
		int x = currentLocation.x;
		int z = currentLocation.y;
		
//		System.out.println("----nextTarget----");
//		System.out.println("currentLocation: " + currentLocation);
//		System.out.println("memory: " + memory);
		
		// initialise the factors array [posX, negX, posZ, negZ] => [0, 1, 2, 3]
		double[] factors = new double[4];
		
		for(int i=0; i<factors.length; i++) {
			possibleLocations.add(null);}
		
		// add the possible locations
		if (!isWall(x+1, z)) {possibleLocations.set(0, new Point(x+1, z)); factors[0] = 1;}
		if (!isWall(x-1, z)) {possibleLocations.set(1, new Point(x-1, z)); factors[1] = 1;}
		if (!isWall(x, z+1)) {possibleLocations.set(2, new Point(x, z+1)); factors[2] = 1;}
		if (!isWall(x, z-1)) {possibleLocations.set(3, new Point(x, z-1)); factors[3] = 1;}
		
//		System.out.println("factors after wallcheck: " + Arrays.toString(factors));
		
		// count the number of possible locations
		int numPl = 0;
		for (int i=0; i<factors.length; i++) {
			numPl += factors[i];}
		
		System.out.println("number of possible locations: " + numPl);
		
		// set the previous location factor to zero if there are multiple locations
		if (numPl > 1 && possibleLocations.contains(enemy.getMemory()))
			factors[possibleLocations.indexOf(enemy.getMemory())] = 0;
		
//		System.out.println("factors after set previous to zero: " + Arrays.toString(factors));
		
		// double the factor for walking straight
		Point straight = new Point(currentLocation); 
		straight.translate(x - enemy.getMemory().x, z - enemy.getMemory().y);
		if (possibleLocations.contains(straight))
			factors[possibleLocations.indexOf(straight)] *= 2;
		
//		System.out.println("factors after walk straight: " + Arrays.toString(factors));
		
		// multiply the factor for a point contained in the memory by (1 + memoryIndex)/(MAZE_SIZE^2)
		for (int i=0; i<possibleLocations.size(); i++) {
			if (memory.contains(possibleLocations.get(i))) {
				factors[i] *= (1 + memory.indexOf(possibleLocations.get(i)))/Math.pow(MAZE_SIZE, 2);}}
		
//		System.out.println("factors after memorycheck: " + Arrays.toString(factors));
		
		// make factors cumulative and normalize
		for (int i=1; i<factors.length; i++) {
			factors[i] += factors[i-1];}
		for (int i=0; i<factors.length; i++) {
			factors[i] /= factors[factors.length-1];}
		
//		System.out.println("factors after making cumulative and norm: " + Arrays.toString(factors));

		
		// pick randomly from the possible locations and set
		double random = rnd.nextDouble();
		
//		System.out.println("random number: " + random);
		
		int nextLocationIndex = 0;
		for (int i=0; i<factors.length; i++) {
			if (random <= factors[i]) {
				nextLocationIndex = i;
				break;}}
		
		System.out.println("nextLocationIndex: " + nextLocationIndex);
		
		Point nextLocation = possibleLocations.get(nextLocationIndex);
		control.setTarget( 	((double) nextLocation.x + 0.5) * SQUARE_SIZE,
							((double) nextLocation.y + 0.5) * SQUARE_SIZE);
		
//		System.out.println("----nextTarget----");
	}
	
	/**
	 * returns an in between location to avoid a wall in the way of an enemy's target
	 */
	public Location avoidWall(Enemy enemy, Location target, double objectSize) {
		
		double xSign = Math.signum(target.locationX - enemy.locationX);
		double zSign = Math.signum(target.locationZ - enemy.locationZ);
		
		double x = enemy.locationX - objectSize*xSign*SQUARE_SIZE; 
		double z = enemy.locationZ-objectSize*zSign*SQUARE_SIZE;
		
		// wall in the x direction
		if (isWall(x + xSign*SQUARE_SIZE, z, 0)) {
			if(zSign > 0) z = (convertToGridZ(z)+ 1.05 + objectSize)*SQUARE_SIZE;
			else if (zSign < 0) z = (convertToGridZ(z) - 0.05 - objectSize)*SQUARE_SIZE;
			x = enemy.locationX;}
		
		// wall in the z direction
		else if (isWall(x, z + zSign*SQUARE_SIZE, 0)) {
			if(xSign > 0) x = (convertToGridX(x)+ 1.05 + objectSize)*SQUARE_SIZE;
			else if (xSign < 0) x = (convertToGridX(x) - 0.05 - objectSize)*SQUARE_SIZE;
			z = enemy.locationZ;}
		
		return new Location(x, z);
	}
	
	
	/*
	 * **********************************************
	 * *			Drawing functions 				*
	 * **********************************************
	 */
	
	/**
	 * maze display function
	 */
	public void display(GL gl) {
		
		// Set the materials
		float colour[] = { 1f, 1f, 1f, 1f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, colour, 0);	
		
		//// walls ////
		bindCurrentTexture("wall"); // bind wall texture
		for(int i=0; i<MAZE_SIZE; i++) {
	        for(int j=0; j<MAZE_SIZE; j++) {
	        	
	        	gl.glPushMatrix();	// go to the current maze location and push
	            gl.glTranslated( i * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2, j * SQUARE_SIZE + SQUARE_SIZE / 2 );
				
	            if (isWall(i,j)) {
			    	drawTexturedWall(gl, (float) SQUARE_SIZE);}
	            
	            gl.glPopMatrix();}} // pop
		currentTexture.disable(); // disable wall texture
		
		//// floors ////
		bindCurrentTexture("floor"); // bind floor texture
		for(int i=0; i<MAZE_SIZE; i++) {
	        for(int j=0; j<MAZE_SIZE; j++) {
	        	
	        	gl.glPushMatrix();	// go to the current maze location and push
	            gl.glTranslated( i * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2, j * SQUARE_SIZE + SQUARE_SIZE / 2 );
				
	            if (!isWall(i,j)) {
			    	drawTexturedFloor(gl, (float) SQUARE_SIZE);}
	            
	            gl.glPopMatrix();}} // pop
		currentTexture.disable(); // disable floor texture
		
		//// roofs ////
		bindCurrentTexture("floor"); // bind roof texture
		for(int i=0; i<MAZE_SIZE; i++) {
	        for(int j=0; j<MAZE_SIZE; j++) {
	        	
	        	gl.glPushMatrix();	// go to the current maze location and push
	            gl.glTranslated( i * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2, j * SQUARE_SIZE + SQUARE_SIZE / 2 );
	            gl.glRotated(180, 0, 0, 1);
	            
	            if (!isWall(i,j)) {
			    	drawTexturedFloor(gl, (float) SQUARE_SIZE);}
	            
	            gl.glPopMatrix();}} // pop
		currentTexture.disable(); // disable roof texture
	}
	
	/**
	 * drawTexturedwall(gl, size) draws 4 panels of a cube with specified size 
	 * around the current point, applying the enabled texture
	 */
	public static void drawTexturedWall(GL gl, float size) {
        float s = size/2;
		
        gl.glBegin(GL.GL_QUADS);
	        // Front Face
        	gl.glNormal3f(0, 0, 1);
	        gl.glTexCoord2f(0, 0); gl.glVertex3f(-s, -s,  s);  // Bottom Left Of The Texture and Quad
	        gl.glTexCoord2f(1, 0); gl.glVertex3f( s, -s,  s);  // Bottom Right Of The Texture and Quad
	        gl.glTexCoord2f(1, 1); gl.glVertex3f( s,  s,  s);  // Top Right Of The Texture and Quad
	        gl.glTexCoord2f(0, 1); gl.glVertex3f(-s,  s,  s);  // Top Left Of The Texture and Quad
	        // Back Face
	        gl.glNormal3f(0, 0, -1);
	        gl.glTexCoord2f(1, 0); gl.glVertex3f(-s, -s, -s);  // Bottom Right Of The Texture and Quad
	        gl.glTexCoord2f(1, 1); gl.glVertex3f(-s,  s, -s);  // Top Right Of The Texture and Quad
	        gl.glTexCoord2f(0, 1); gl.glVertex3f( s,  s, -s);  // Top Left Of The Texture and Quad
	        gl.glTexCoord2f(0, 0); gl.glVertex3f( s, -s, -s);  // Bottom Left Of The Texture and Quad
	        // Right face
	        gl.glNormal3f(1, 0, 0);
	        gl.glTexCoord2f(1, 0); gl.glVertex3f( s, -s, -s);  // Bottom Right Of The Texture and Quad
	        gl.glTexCoord2f(1, 1); gl.glVertex3f( s,  s, -s);  // Top Right Of The Texture and Quad
	        gl.glTexCoord2f(0, 1); gl.glVertex3f( s,  s,  s);  // Top Left Of The Texture and Quad
	        gl.glTexCoord2f(0, 0); gl.glVertex3f( s, -s,  s);  // Bottom Left Of The Texture and Quad
	        // Left Face
	        gl.glNormal3f(-1, 0, 0);
	        gl.glTexCoord2f(0, 0); gl.glVertex3f(-s, -s, -s);  // Bottom Left Of The Texture and Quad
	        gl.glTexCoord2f(1, 0); gl.glVertex3f(-s, -s,  s);  // Bottom Right Of The Texture and Quad
	        gl.glTexCoord2f(1, 1); gl.glVertex3f(-s,  s,  s);  // Top Right Of The Texture and Quad
	        gl.glTexCoord2f(0, 1); gl.glVertex3f(-s,  s, -s);  // Top Left Of The Texture and Quad
        gl.glEnd();
	}

	/**
	 * drawTexturedFloor(gl, size) draws a floor panel, applying the enabled texture
	 */
	public static void drawTexturedFloor(GL gl, float size) {
		float s = size/2;
		
		gl.glBegin(GL.GL_QUADS);
			gl.glNormal3f(0, 1, 0);
		    gl.glTexCoord2f(0f, 1f); gl.glVertex3f(-s,  -s, -s);  // Top Left Of The Texture and Quad
		    gl.glTexCoord2f(0f, 0f); gl.glVertex3f(-s,  -s,  s);  // Bottom Left Of The Texture and Quad
		    gl.glTexCoord2f(1f, 0f); gl.glVertex3f( s,  -s,  s);  // Bottom Right Of The Texture and Quad
		    gl.glTexCoord2f(1f, 1f); gl.glVertex3f( s,  -s, -s);  // Top Right Of The Texture and Quad
	    gl.glEnd();
	}

	
	/*
	 * **********************************************
	 * *			getters and setters 			*
	 * **********************************************
	 */
	
	/**
	 * get the current active level
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	/**
	 * set the current active level
	 * @param currentLevel
	 */
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
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
}
