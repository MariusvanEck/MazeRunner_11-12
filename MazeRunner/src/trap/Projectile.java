package trap;

import javax.media.opengl.GL;

import mazerunner.GameObject;
import model.Model;
import model.TexturedModel;

public class Projectile extends GameObject{
	private char direction;
	private double speed;
	private TexturedModel model;
	private double angle;
	private final int damage = 10;
	
	private static String modelFileLocation = "models/trap/projectile.obj";
	private static String textureFileLocation = "models/trap/projectile.png";
	
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
	
	public void update(int deltaTime){
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
	
	public void setLocation(double x,double y,double z){
		this.locationX = x;
		this.locationY = y;
		this.locationZ = z;
		this.angle = 0;
	}
	
	public double getX(){return locationX;}
	public double getY(){return locationY;}
	public double getZ(){return locationZ;}
	public int getDamage(){return damage;}
	
	
	public void display(GL gl){
		model.render(gl, angle-=5, locationX, locationY, locationZ);
	}
	
}
