/**
 * The Editor allows a user to create maps with various levels- and map-sizes.
 */
package Editor;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;

public class Level {
	
	protected int[][] level;
	private int x;
	private int y;
	protected float buttonSize;
	protected int lineWidth = 2;
	private static Texture[] textureMaze;
	private int nTiles = 198; 					//this is the number of different tiles currently present in the maze
	private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static int screenHeight =  gd.getDisplayMode().getHeight();
	private static int screenWidth =  gd.getDisplayMode().getWidth();
	private static float mazeL = ((screenWidth-screenHeight)/3*2);					//Left bound of mazeDrawingWindow
	private static int mazeX = 20;
	private float alpha = 1;
    static int primeNumbers[] = new int[100];
	
   /**
    * creates a level with borders at the edges of the matrix
    * @param x		the x coordinate of the level size
    * @param y		the y coordinate of the level size
    */
	public Level(int x, int y){
		this.x = x;
		this.y = y;
		level = new int [x][y];
		for (int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){
				level[i][j] = 0;	
				if(i == 0 || i == x-1 || j == 0 || j == y-1){
					level[i][j] = 1;
				}
			}
		}
	}
		
	/**
	 * draws the level on the specified location
	 * @param gl		The gl is used to call openGL methods and to draw on the canvas
	 * @param startx	The x coordinate of the start position
	 * @param starty	The y coordinate of the start position
	 * @param width		The width of the  level draw area
	 * @param height	The height of the level draw area
	 */
	public void draw(GL gl, float startx, float starty, float width, float height){
				
		buttonSize = height/y;
		for(int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){
				alpha = 1;
				boolean prime = false;
				
				//Drawing the floor color
				//Also be sure to maintain the background color of the floor for specific textures, making it not white
				if ((check(i,j,0) && textureMaze[0] == null) || check(i,j,2) || check(i,j,3) || check(i,j,5)
						|| check(i,j,7) || check(i,j,97)){		
					gl.glColor4f(150/255f, 73/255f, 37/255f, 1);
				}
				
 				//Drawing the floor color before a texture is applied
 				gl.glBegin(GL.GL_QUADS);
				gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j+1)*buttonSize);
				gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j+1)*buttonSize);
				gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j)*buttonSize);
				gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j)*buttonSize);
 				gl.glEnd();
		
				//Drawing the wall color
				if(check(i,j,1) && textureMaze[1] == null){
					gl.glColor4f(87/255f, 84/255f, 83/255f, 1);
				}
				
				//Drawing the voids, setting tile to black
				if(check(i,j,17)){
					gl.glColor3f(0/255f, 0/255f, 0/255f);
				}
				
				//drawing textures if present
				for (int k = 1; k < nTiles; k++){
					if ((check(i,j,k))){
						if (textureMaze[k] != null) {
							gl.glEnable(GL.GL_BLEND);
							gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
							textureMaze[k].enable();
							textureMaze[k].bind();
							//White background color for normal texture view
							if(prime){
								alpha = 0.5f;
							}
							gl.glColor4f(255/255f, 255/255f, 255/255f,alpha);
							prime = true;
						}

						int[] a = new int[2];	a[0] = 0; a[1] = 0;
						int[] b = new int[2];	b[0] = 1; b[1] = 0;
						int[] c = new int[2];	c[0] = 1; c[1] = 1;
						int[] d = new int[2];	d[0] = 0; d[1] = 1;
						
						//If the stairs are not facing from left to the right we need to adjust the direction of the textures
						
						//Start Drawing the stairs from right to left
						if ((check(i,j,11) && check(i-1,j,13)) || (check(i,j,13) && check(i+1,j,11))){	
							a[0] = 1; a[1] = 0;
							b[0] = 0; b[1] = 0;
							c[0] = 0; c[1] = 1;
							d[0] = 1; d[1] = 1;
						}
						
						//Start Drawing the stairs from top to bottom
						if ((check(i,j,11) && check(i,j-1,13)) || (check(i,j,13) && check(i,j+1,11))){	
							a[0] = 0; a[1] = 0;
							b[0] = 0; b[1] = 1;
							c[0] = 1; c[1] = 1;
							d[0] = 1; d[1] = 0;
						}
						
						//Start Drawing the stairs from bottom to top
						if ((check(i,j,11) && check(i,j+1,13)) || (check(i,j,13) && check(i,j-1,11))){	
							a[0] = 1; a[1] = 1;
							b[0] = 1; b[1] = 0;
							c[0] = 0; c[1] = 0;
							d[0] = 0; d[1] = 1;
						}
						
						//normal texture drawing:
						gl.glBegin(GL.GL_QUADS);
						gl.glTexCoord2f(a[0],a[1]);
						gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j+1)*buttonSize);
						gl.glTexCoord2f(b[0],b[1]);
						gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j+1)*buttonSize);
						gl.glTexCoord2f(c[0],c[1]);
						gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j)*buttonSize);
						gl.glTexCoord2f(d[0],d[1]);
						gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j)*buttonSize);
						gl.glEnd();
						
						if (textureMaze[k] != null) {
							textureMaze[k].disable();
						}
						
					}
				}
				//Drawing the voids, drawing a red cross
				if(check(i,j,17)){
					gl.glColor3f(255/255f, 0/255f, 0/255f);
					gl.glLineWidth(3);
					gl.glBegin(GL.GL_LINES);
					gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j+1)*buttonSize);		
					gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j)*buttonSize);
					gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j+1)*buttonSize);
					gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j)*buttonSize);
					gl.glEnd();
				}
			}
		}
		
		//The active grid cursor
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		int xm = (int) b.getX();
		int ym = (int) b.getY();
		float squareX = (float) Math.floor(((xm - (mazeL)) / buttonSize));
		float squareY = (float) Math.floor(((screenHeight - ym)/buttonSize));
		if(squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			//Fourth argument = transparency (1f = 100% 0f = 0%)
			gl.glColor4f(255/255f, 255/255f, 0/255f, 0.5f);
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex2f(startx+((width-x*buttonSize)/2)+squareX*buttonSize, (squareY+1)*buttonSize);
			gl.glVertex2f(startx+((width-x*buttonSize)/2)+(squareX+1)*buttonSize, (squareY+1)*buttonSize);
			gl.glVertex2f(startx+((width-x*buttonSize)/2)+(squareX+1)*buttonSize, (squareY)*buttonSize);
			gl.glVertex2f(startx+((width-x*buttonSize)/2)+squareX*buttonSize, (squareY)*buttonSize);
			gl.glEnd();
		}

		//set the LineWidth and the line color of the grid
		gl.glLineWidth(lineWidth);
		gl.glColor3f(255/255f, 255/255f, 255/255f);
		
		//vertical lines of the grid
		for(int i = 0; i <= x; i++){
			gl.glBegin(GL.GL_LINES);
			gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, 0);
			gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, height);
			gl.glEnd();
		}
		
		//horizontal lines of the grid
		for(int i = 0; i <= y; i++){
			gl.glBegin(GL.GL_LINES);
			gl.glVertex2f(startx+((width-x*buttonSize)/2), i*buttonSize);
			gl.glVertex2f(startx+width-((width-x*buttonSize)/2), i*buttonSize);
			gl.glEnd();
		}
	}
	
	/**
	 * Level to string
	 */
	public  String toString(){
		String res = new String();
		for (int j = 0; j < y; j++){
			for(int i = 0; i < x; i++){
				res = res + level[i][j] + " ";
			}
			res = res +"\n";
		}
		return res;
	}
	
	/**
	 * Set the level
	 * @param level	int[][] level to be set
	 */
	public void setLevel(int[][] level) {
		this.level = level;
	}
	/**
	 * Set the x
	 * @param x	the x to be set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * Set the y
	 * @param y the y to be set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * Get this level
	 * @return	int[][] level
	 */
	public int[][] getLevel() {
		return level;
	}
	/**
	 * Set a specific point
	 * @param x		x location
	 * @param y		y location
	 * @param set	value to be set
	 */
	public void set(int x,int y,int set){
		level[x][y] = set;
	}
	/**
	 * Get a specific point
	 * @param x	x location
	 * @param y	y location
	 * @return	returns the value at (x,y) in the matrix
	 */
	public int get(int x,int y){
		return level[x][y];
	}
	/**
	 * Gives back x
	 * @return returns x
	 */
	public int getX() {
		return x;
	}
	/**
	 * Gives back y
	 * @return returns y
	 */
	public int getY() {
		return y;
	}
	/**
	 * Make a array of primenumbers
	 */
	public void primes(){
		// Initialize array of the first 100 prime numbers
        int index = 0;
        while(index<100){
            for (int i = 2; i < 100*10; i++){
                boolean primeNum = true;
                for(int j=2; j<i; j++){
                    if (i%j==0){
                        primeNum = false;
                    }
                }
                if (primeNum){
                    primeNumbers[index] = i;
                    index++;
                    if(index==100){
                        break;
                    }
                }
            }
        }
	}
	
	/**
	 * Checks if the given value is present on i an j in the level matrix
	 * @param i			i location
	 * @param j			j location
	 * @param value		The value to be checked
	 * @return			true if value is present at the location, false otherwise.
	 */
	boolean check(int i, int j, int value){
		int index = 0;
		int number = level[i][j];
		int objects[] = new int[10];
        // Input is larger than the maximum value, so the value given will miss factors and thus objects
        if(value>=Integer.MAX_VALUE){
            System.err.println("Error: Value is larger than Integer.MAX_VALUE.");
            objects[0] = -1;
            return false;
        }
        else if(number == 1){
        	objects[0] = 1;
        }
        else if(number == 0){
        	objects[0] = 0;
        }
        // Find all prime factors of input
        else for (int p = 0; p < 100; p++) {
        	
            if (number % primeNumbers[p] == 0) {
                objects[index] = primeNumbers[p];
                index++;
                number /= primeNumbers[p];
                p = p - 1;
            }
        }
        // If there are no prime factors, the input is prime
        if(index==0){
            objects[0] = number;
        }
        // If the remaining number is more than 1, not all prime factors are calculated
        if(number>1){
            System.err.println("Error: Not all prime factors are given, primeNumbers array too small.");
            objects[0] = -1;
            return false;
        }			
		for(int l = 0;l<objects.length;l++){
			if(objects[l] == value){
				return true;
			}
		}
		return false;
	}
	/**
	 * Updates the mazeL
	 * @param mazeL	The parameter which will be set to mazeL
	 */
	public static void updateMazeL(float mazeL) {
		Level.mazeL = mazeL;
	}
	/**
	 * Updates the mazeX
	 * @param mazeX	The parameter which will be set to mazeX
	 */
	public static void updateMazeX(int mazeX) {
		Level.mazeX = mazeX;
	}
	/**
	 * Set all textures at ones
	 * @param textureMaze	Texture[] which contains all needed textures
	 */
	public static void setTextureMaze(Texture[] textureMaze) {
		Level.textureMaze = textureMaze;
	}
}
