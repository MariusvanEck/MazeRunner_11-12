package trap;

public abstract class Trap {
	private double x,y,z;
	private double activationX,activationY,activationZ; // the location that the trap activates
	
	public Trap(double x,double y,double z,double activationX,double activationY,double activationZ){
		this.x = x;
		this.y = y;
		this.z = z;
		this.activationX = activationX;
		this.activationY = activationY;
		this.activationZ = activationZ;
	}
	
	
}