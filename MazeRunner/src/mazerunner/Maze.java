package mazerunner;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.opengl.GL;

import cast.Cast;
import cast.InvalidByteArraySize;

import com.sun.opengl.util.texture.Texture;

import database.DataBase;

/**
 * Maze represents the maze used by MazeRunner.
 */
public class Maze implements VisibleObject {
	
	private int mazeSize;
	private int levelSize;
	public final static double SQUARE_SIZE = 5;
	
	private HashMap<String,Texture> textures;			// reference to the texture hashmap
	private Texture currentTexture;						// specifies the current texture
	private int currentLevelIndex = 0;						// specifies the currentLevel

	private ArrayList<int[][]> levels;
	private int[][] currentLevel;
	private ArrayList<Stair> stairs;
	
	// old constructor
	public Maze(GL gl,String file,HashMap<String,Texture> textures){
		this.textures = textures;
		this.levels = new ArrayList<int[][]>();
		this.stairs = new ArrayList<Stair>();
		
		this.load(gl, file);
	}
	
	public Maze(GL gl, DataBase dataBase, String name, HashMap<String,Texture> textures){
		this.textures = textures;
		this.levels = new ArrayList<int[][]>();
		this.stairs = new ArrayList<Stair>();
		
		this.load(gl,dataBase,name);
	}
	
	private void load(GL gl,DataBase dataBase,String name){ // moet nog testen of hij werkt
		try{
			byte[] b = dataBase.getMap(name);
			if((b.length % 4) != 0)
				throw new IOException("Corrupted data");
			
			byte[] temp = new byte[4];
			temp[0] = b[0];
			temp[1] = b[1];
			temp[2] = b[2];
			temp[3] = b[3];
			levelSize = Cast.byteArrayToInt(temp);
			temp[0] = b[4];
			temp[1] = b[5];
			temp[2] = b[6];
			temp[3] = b[7];
			mazeSize = Cast.byteArrayToInt(temp);
			
			
			this.currentLevel = new int[mazeSize][mazeSize];
			for(int y = 0; y < levelSize;y++){
				for(int z = 0; z < mazeSize; z++){
					for(int x = 0; x < mazeSize; x++){
						temp[0] = b[8+x+z*mazeSize+y*mazeSize*mazeSize];
						temp[1] = b[9+x+z*mazeSize+y*mazeSize*mazeSize];
						temp[2] = b[10+x+z*mazeSize+y*mazeSize*mazeSize];
						temp[3] = b[11+x+z*mazeSize+y*mazeSize*mazeSize];
						currentLevel[x][z] = Cast.byteArrayToInt(temp);
					}
				}
				levels.add(currentLevel);
			}
			
			
		}catch(IOException e){
			System.err.println("Maze: " + e.getMessage());
		}catch( InvalidByteArraySize e){
			System.err.println("Maze: " + e.getMessage());
		}
		
		getStairs(gl);
		
		
		
	}
	
	private void getStairs(GL gl){
		for(int y = 0; y < levelSize; y++){
			for(int z = 0; z < mazeSize; z++){
				for(int x = 0; x < mazeSize;x++){
					//finding stairs and orientation
					if(levels.get(y)[x][z] == 11){
						//WEST
						if (levels.get(y)[x+1][z] == 13){
							stairs.add(new Stair(gl,y,x*Maze.SQUARE_SIZE,z*Maze.SQUARE_SIZE,(x+1)*Maze.SQUARE_SIZE,z*Maze.SQUARE_SIZE,"models\\stairs.obj"));
						}
						//EAST
						else if (levels.get(y)[x-1][z] == 13){
							stairs.add(new Stair(gl,y,(x+1)*Maze.SQUARE_SIZE,(z+1)*Maze.SQUARE_SIZE,x*Maze.SQUARE_SIZE,(z+1)*Maze.SQUARE_SIZE,"models\\stairs.obj"));
						}
						//SOUTH
						else if (levels.get(y)[x][z-1] == 13){
							stairs.add(new Stair(gl,y,x*Maze.SQUARE_SIZE,(z+1)*Maze.SQUARE_SIZE,x*Maze.SQUARE_SIZE,z*Maze.SQUARE_SIZE,"models\\stairs.obj"));
						}
						//NORTH
						else if (levels.get(y)[x][z+1] == 13){
							stairs.add(new Stair(gl,y,(x+1)*Maze.SQUARE_SIZE,z*Maze.SQUARE_SIZE,(x+1)*Maze.SQUARE_SIZE,(z+1)*Maze.SQUARE_SIZE,"models\\stairs.obj"));
						}
					}
				}
			}
		}
	}
	
	// old loader
	private void load(GL gl, String file){
		try{     
			FileInputStream fmaze = new FileInputStream(file);
			ObjectInputStream omaze = new ObjectInputStream(fmaze);
			
			levelSize = (Integer) omaze.readObject();
			mazeSize = (Integer) omaze.readObject();
	      
			for (int i=0; i<levelSize; i++) {
				levels.add((int[][]) omaze.readObject());
				for (int z = 0; z < mazeSize; z++){
					for(int x = 0; x < mazeSize;x++){
						//finding stairs and orientation
						if(levels.get(i)[x][z] == 11){
							//WEST
							if (levels.get(i)[x+1][z] == 13){
								stairs.add(new Stair(gl,i,x*Maze.SQUARE_SIZE,z*Maze.SQUARE_SIZE,(x+1)*Maze.SQUARE_SIZE,z*Maze.SQUARE_SIZE,"models\\stairs.obj"));
							}
							//EAST
							else if (levels.get(i)[x-1][z] == 13){
								stairs.add(new Stair(gl,i,(x+1)*Maze.SQUARE_SIZE,(z+1)*Maze.SQUARE_SIZE,x*Maze.SQUARE_SIZE,(z+1)*Maze.SQUARE_SIZE,"models\\stairs.obj"));
							}
							//SOUTH
							else if (levels.get(i)[x][z-1] == 13){
								stairs.add(new Stair(gl,i,x*Maze.SQUARE_SIZE,(z+1)*Maze.SQUARE_SIZE,x*Maze.SQUARE_SIZE,z*Maze.SQUARE_SIZE,"models\\stairs.obj"));
							}
							//North
							else if (levels.get(i)[x][z+1] == 13){
								stairs.add(new Stair(gl,i,(x+1)*Maze.SQUARE_SIZE,z*Maze.SQUARE_SIZE,(x+1)*Maze.SQUARE_SIZE,(z+1)*Maze.SQUARE_SIZE,"models\\stairs.obj"));
							}
						}
					}
				}
			}
	  
			currentLevel = levels.get(currentLevelIndex);
			omaze.close();
			
			
			
			
		}
        catch(Exception ex){ex.printStackTrace();}
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
	public boolean isWall( int x, int z )
	{
		if( x >= 0 && x < mazeSize && z >= 0 && z < mazeSize )
			return currentLevel[x][z] == 1;
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
	 * isStair(int x,int y,int z) checks for a stair
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param y		the y-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a stair at maze[x][z][y]
	 */
	public boolean isStair(int x ,int z){
		if(x >= 0 && x < mazeSize && z >= 0 && z < mazeSize)
			return currentLevel[x][z] == 11 || currentLevel[x][z] == 13;
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
		return 	isWall(gXmin, gZmin) || isWall(gXmax, gZmax) ||
				isWall(gXmin, gZmax) || isWall(gXmax, gZmin) ||
				isWall(gXmin, gZmin) || isWall(gXmax, gZmax) ||
				isWall(gXmin, gZmax) || isWall(gXmax, gZmin);
	}
	
	/**
	 * isStair(double x, double z) checks for a stair in a square with edge 0.1*SQUARE_SIZE around the location
	 * by converting the double values to integer coordinates.
	 * <p>
	 * This method first converts the x and z to values that correspond with the grid 
	 * defined by maze[][]. Then it calls upon isStair(int, int) to check for a stair.
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a stair at maze[x][z][y]
	 */
	public boolean isStair(double x, double z){
		int gXmin = convertToGridX(x-0.1*SQUARE_SIZE);
		int gXmax = convertToGridX(x+0.1*SQUARE_SIZE);
		int gZmin = convertToGridZ(z-0.1*SQUARE_SIZE);
		int gZmax = convertToGridZ(z+0.1*SQUARE_SIZE);
		
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
	 * Sets the specified location to wall or no wall
	 * 
	 * @param x			x-coordinate
	 * @param z			z-coordinate
	 * @param isWall 	true for wall, false for no wall
	 */
	public synchronized void editMaze(int x, int y,int z, boolean isWall) {
		currentLevel[x][z] = isWall? 1 : 0;
	}
	
	
	/*
	 * **********************************************
	 * *			Drawing functions 				*
	 * **********************************************
	 */
	
	/**
	 * maze display function
	 */
	@Override
	public void display(GL gl) {
		// Set the materials
		float colour[] = { 1f, 1f, 1f, 1f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, colour, 0);	
		
		//// walls ////
		bindCurrentTexture("wall"); // bind wall texture
		for(int i=0; i<mazeSize; i++) {
	        for(int j=0; j<mazeSize; j++) {
	        	
	        	gl.glPushMatrix();	// go to the current maze location and push
	            gl.glTranslated( i * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2, j * SQUARE_SIZE + SQUARE_SIZE / 2 );
				
	            if (isWall(i,j)) {
			    	drawTexturedWall(gl, (float) SQUARE_SIZE);}
	            
	            gl.glPopMatrix();}} // pop
		currentTexture.disable(); // disable wall texture
		
		//// floors ////
		bindCurrentTexture("floor"); // bind floor texture
		for(int i=0; i<mazeSize; i++) {
	        for(int j=0; j<mazeSize; j++) {
	        	
	        	gl.glPushMatrix();	// go to the current maze location and push
	            gl.glTranslated( i * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2, j * SQUARE_SIZE + SQUARE_SIZE / 2 );
				
	            if (!isWall(i,j)) {
			    	drawTexturedFloor(gl, (float) SQUARE_SIZE);}
	            
	            gl.glPopMatrix();}} // pop
		currentTexture.disable(); // disable floor texture
		
		//// roofs ////
		bindCurrentTexture("floor"); // bind roof texture
		for(int i=0; i<mazeSize; i++) {
	        for(int j=0; j<mazeSize; j++) {
	        	
	        	gl.glPushMatrix();	// go to the current maze location and push
	            gl.glTranslated( i * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2, j * SQUARE_SIZE + SQUARE_SIZE / 2 );
	            gl.glRotated(180, 0, 0, 1);
	            
	            if (!isWall(i,j)) {
			    	drawTexturedFloor(gl, (float) SQUARE_SIZE);}
	            
	            gl.glPopMatrix();}} // pop
		currentTexture.disable(); // disable roof texture
		
		//// stairsL ////
/*		bindCurrentTexture("stairL"); // bind stairL texture
		for(int i=0; i<MAZE_SIZE; i++) {
	        for(int j=0; j<MAZE_SIZE; j++) {
	        	
	        	gl.glPushMatrix();	// go to the current maze location and push
	            gl.glTranslated( i * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2, j * SQUARE_SIZE + SQUARE_SIZE / 2 );
	            
	            if (isStair(i,j)) {
			    	drawTexturedStairL(gl, (float) SQUARE_SIZE);}
	            
	            gl.glPopMatrix();}} // pop
		currentTexture.disable(); // disable stairL texture
*/
		for(Stair stair : stairs){
			if(stair.getLowerY() == currentLevelIndex)
				stair.display(gl);
		}
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
	
	/**
	 * drawTexturedStairL(gl, size) draws the lower part of the stairs
	 * around the current point, applying the enabled texture
	 */
	public static void drawTexturedStairL(GL gl, float size) {
        float s = size/2;
		
        gl.glBegin(GL.GL_QUADS);
	        // Top Face
        	gl.glNormal3f(0, 0, 1);
	        gl.glTexCoord2f(1, 1); gl.glVertex3f(-s, -s,  s);  // Bottom Left Of The Texture and Quad
	        gl.glTexCoord2f(1, 0); gl.glVertex3f( s, -s,  s);  // Bottom Right Of The Texture and Quad
	        gl.glTexCoord2f(0, 0); gl.glVertex3f( s,  0,  -s);  // Top Right Of The Texture and Quad
	        gl.glTexCoord2f(0, 1); gl.glVertex3f( -s,  0,  -s);  // Top Left Of The Texture and Quad
        gl.glEnd();
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
	
	
	public int getMazeSize(){
		return mazeSize;
	}
	public int getLevelSize(){
		return levelSize;
	}
	
	public int[][] getMaze(int i){
		return levels.get(i);
	}
	
	public void lvlToString(){
		for(int[][] level:levels){
			for(int i = 0; i < levelSize; i++){
				for(int j = 0; j < levelSize; j++){
					System.out.print(level[j][i]);
					System.out.print(' ');
				}
				System.out.print('\n');
			}
		}
	}
}
