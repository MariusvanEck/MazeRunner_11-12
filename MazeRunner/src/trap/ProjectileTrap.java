package trap;

import javax.media.opengl.GL;
import mazerunner.Maze;
import mazerunner.Player;

public class ProjectileTrap extends Trap {
	private Projectile projectile = null;
	private boolean triggered;
	private Maze maze;
	
	private static String modelFileLocation = "models/box.obj";
	private static String textureFileLocation = null;
	
	
	public ProjectileTrap(GL gl,Player player,Maze maze,double x,double y,double z,double activationX,double activationY,double activationZ,char direction,double speed){
		super(gl,player,x,y,z,activationX,activationY,activationZ,modelFileLocation,textureFileLocation);
		this.maze = maze;
		projectile = new Projectile(gl, x,y,z,direction,speed);
		this.triggered = false;
	}
	
	public void update(int deltaTime){
		projectile.update(deltaTime);
		
		if(this.near(player, Maze.SQUARE_SIZE/2))
		
		if(triggered && maze.isWall(projectile.getX(),projectile.getZ(),0) || maze.isStair(projectile.getX(), projectile.getZ(),0))
				projectile.setLocation(this.locationX, this.locationY, this.locationZ);
		
	}
	@Override
	public void display(GL gl) {
		model.render(gl, 0, locationX, locationY, locationZ);
		
		projectile.display(gl);
	}
	
	
}
