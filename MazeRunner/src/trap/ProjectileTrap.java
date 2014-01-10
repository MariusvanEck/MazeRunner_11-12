package trap;

import javax.media.opengl.GL;
import mazerunner.Maze;
import mazerunner.Player;

public class ProjectileTrap extends Trap {
	private Projectile projectile = null;
	private boolean triggered;
	private Maze maze;
	
	private static String modelFileLocation = "models/trap/trapbase.obj";
	private static String textureFileLocation = "models/textures/stone2.jpg";
	
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
	public ProjectileTrap(GL gl,Player player,Maze maze,double x,double y,double z,double activationX,double activationY,double activationZ,char direction,double speed){
		super(gl,player,x,y,z,activationX,activationY,activationZ,modelFileLocation,textureFileLocation);
		this.maze = maze;
		projectile = new Projectile(gl, x,y,z,direction,speed*player.getSpeed());
		this.triggered = false;
	}
	
	public void update(int deltaTime){
		double x = projectile.getX();
		double z = projectile.getZ();
		projectile.update(deltaTime);
		
		if(this.near(player, Maze.SQUARE_SIZE/2))
			triggered = true;
		if(triggered && maze.isWall(projectile.getX(),projectile.getZ(),Math.abs(x-projectile.getX())) || maze.isStair(projectile.getX(), projectile.getZ(),Math.abs(z-projectile.getZ())))
				projectile.setLocation(this.locationX, this.locationY, this.locationZ);
		
	}
	@Override
	public void display(GL gl) {
		model.render(gl, 90, locationX, locationY, locationZ);
		
		projectile.display(gl);
	}
	
	
}
