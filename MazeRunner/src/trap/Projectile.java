package trap;

import javax.media.opengl.GL;

import model.Model;
import model.TexturedModel;

public class Projectile {
	private double x,y,z;
	private char direction;
	private double speed;
	private TexturedModel model;
	
	private static String modelFileLocation = "models/box.obj";
	private static String textureFileLocation = null;
	
	public Projectile(GL gl,double x,double y,double z,char direction,double speed){
		this.x = x;
		this.y = y;
		this.z = z;
		if(direction == 'N' || direction == 'E' || direction == 'S' || direction == 'W')
			this.direction = direction;
		else
			this.direction = 0;
		this.speed = speed;
		this.model = new TexturedModel(gl,new Model(modelFileLocation,0.3f),textureFileLocation);
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
	}
	
	public double getX(){return x;}
	public double getY(){return y;}
	public double getZ(){return z;}
	
	
	public void display(GL gl){
		model.render(gl, 0, x, y, z);
	}
	
}
