package mazerunner;

import javax.media.opengl.GL;

public class SlidingWall extends Wall {
	
	private final double locationYup = .5*Maze.SQUARE_SIZE;
	private final double locationYdown = -.5*Maze.SQUARE_SIZE;
	private final double slidingspeed = 0.001;
	private boolean isWall = true;
	private Floor floor;
	
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
	
	public boolean isWall() {
		return isWall;
	}

	public void displayFloor(GL gl) {
		floor.display(gl);
	}
}
