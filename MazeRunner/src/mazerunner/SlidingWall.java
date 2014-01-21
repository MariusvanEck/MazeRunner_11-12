package mazerunner;

import javax.media.opengl.GL;

public class SlidingWall extends Wall {
	
	private final double locationYup = .5*Maze.SQUARE_SIZE;			// locationY when wall is completely up
	private final double locationYdown = -.5*Maze.SQUARE_SIZE;		// locationY when wall is completely down
	private final double slidingspeed = 0.001;						// wall sliding speed	
	private boolean isWall = true;									// boolean for the wall state
	private Floor floor;											// floor on top of the sliding wall
	
	/**
	 * constructor
	 */
	public SlidingWall(int x, int z) {
		super(x, z);
		floor = new Floor(x, z);
		floor.setLocationY(locationY + Maze.SQUARE_SIZE);
		
	}
	
	/**
	 * update function
	 */
	public void update(int deltaTime, Player player) {
		if (this.near(player, 1.5)) {
			if (locationY > locationYdown) {
				locationY -= slidingspeed*deltaTime*Maze.SQUARE_SIZE;
			}
		}
		else {
			if (locationY < locationYup) {
				locationY += slidingspeed*deltaTime*Maze.SQUARE_SIZE;
			}
		}
		
		floor.setLocationY(locationY + Maze.SQUARE_SIZE);
		isWall = locationY > locationYdown;
	}
	
	/**
	 * Checks if this sliding wall is in the "wall" state
	 * @return
	 */
	public boolean isWall() {
		return isWall;
	}

	/**
	 * Displays the floor on top of the sliding wall
	 * @param gl
	 */
	public void displayFloor(GL gl) {
		floor.display(gl);
	}
}
