package trap;

import javax.media.opengl.GL;

import mazerunner.GameObject;
import model.Model;
import model.TexturedModel;

public class Projectile extends GameObject{

	private double speed;			// the speed
	private final int damage = 10;	// damage for the projectile
	
	// The projectiles direction
	private char direction;
	private double angle;
	
	// the model
	private TexturedModel model;	
	private static String modelFileLocation = "models/trap/projectile.obj";
	private static String textureFileLocation = "models/trap/projectile.png";
	
	/**
	 * Constructor creates a new projectile
	 */
	public Projectile(GL gl,double x,double y,double z,char direction,double speed){
		super(x,y,z);
		if(direction == 'N' || direction == 'E' || direction == 'S' || direction == 'W')
			this.direction = direction;
		else
			this.direction = 0;
		this.speed = speed;
		this.model = new TexturedModel(gl,new Model(modelFileLocation,0.025f),textureFileLocation);
		this.angle = 0;
	}
	
	/**
	 * Update the projectile
	 */
	public void update(int deltaTime){
		angle -= deltaTime;
		
		switch(direction){
			case 'N':
				locationZ -= speed*deltaTime;
				break;
			case 'E':
				locationX += speed*deltaTime;
				break;
			case 'S':
				locationZ+= speed*deltaTime;
				break;
			case 'W':
				locationX -= speed*deltaTime;
				break;
		}
	}
	
	/**
	 * Change the projectile location
	 */
	public void setLocation(double x,double y,double z){
		this.locationX = x;
		this.locationY = y;
		this.locationZ = z;
		this.angle = 0;
	}
	
	/**
	 * get the projectile damage
	 */
	public int getDamage(){return damage;}
	
	/**
	 * Display the projectile
	 * @param gl
	 */
	public void display(GL gl){
		model.render(gl, angle, locationX, locationY, locationZ);
	}
	
}
