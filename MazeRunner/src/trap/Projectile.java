package trap;

import javax.media.opengl.GL;

import model.Model;
import model.TexturedModel;

public class Projectile {
	private double x,y,z;
	private char direction;
	private double speed;
	private TexturedModel model;
	private double angle;
	
	private static String modelFileLocation = "models/trap/projectile.obj";
	private static String textureFileLocation = "models/textures/stone1.jpg";
	
	public Projectile(GL gl,double x,double y,double z,char direction,double speed){
		this.x = x;
		this.y = y;
		this.z = z;
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
				z -= speed*deltaTime;
				break;
			case 'E':
				x += speed*deltaTime;
				break;
			case 'S':
				z+= speed*deltaTime;
				break;
			case 'W':
				x -= speed*deltaTime;
				break;
		}
	}
	
	public void setLocation(double x,double y,double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = 0;
	}
	
	public double getX(){return x;}
	public double getY(){return y;}
	public double getZ(){return z;}
	
	
	public void display(GL gl){
		
		model.render(gl, angle+=5, x, y, z);
	}
	
}
