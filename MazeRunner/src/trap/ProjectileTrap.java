package trap;

public class ProjectileTrap extends Trap {
	private Projectile projectile = null;
	
	
	public ProjectileTrap(double x,double y,double z,double activationX,double activationY,double activationZ,char direction,double speed){
		super(x,y,z,activationX,activationY,activationZ);
		projectile = new Projectile(x,y,z,direction,speed);
	}
	
	public void update(int deltaTime){
		projectile.update(deltaTime);
		//TODO: hier moet gechecked worden of het projectiel een muur raakt
		// zo ja dan projectile reset naar begin positie
	}
	
	
}
