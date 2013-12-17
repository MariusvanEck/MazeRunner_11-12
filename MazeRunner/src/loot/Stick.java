package loot;

import javax.media.opengl.GL;

public class Stick extends Weapon{
	public Stick(double x,double y,double z,boolean equipped,String modelFileLocation){
		super(x, y, z, 10,equipped,modelFileLocation);
	}
	
	public void display(GL gl){}
}
