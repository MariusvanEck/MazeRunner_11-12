package trap;

public class Projectile {
	private double x,y,z;
	private char direction;
	private double speed;
	
	
	public Projectile(double x,double y,double z,char direction,double speed){
		this.x = x;
		this.y = y;
		this.z = z;
		if(direction == 'N' || direction == 'O' || direction == 'S' || direction == 'W')
			this.direction = direction;
		else
			this.direction = 0;
		this.speed = speed;
	}
	
	public void update(int deltaTime){
		switch(direction){
			case 'N':
				z -= speed*deltaTime;
				break;
			case 'O':
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
	
	
}
