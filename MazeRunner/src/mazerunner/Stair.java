package mazerunner;

import javax.media.opengl.GL;

public class Stair implements VisibleObject{
	private double lowerX,lowerY,lowerZ;
	private double upperX,upperZ;
	
	
	public Stair(double lowerX,double lowerY,double lowerZ,double upperX,double upperZ){
		this.lowerX = lowerX;
		this.lowerY = lowerY;
		this.lowerZ = lowerZ;
		
		this.upperX = upperX;
		this.upperZ = upperZ;
	}
	
	
	
	public double getLowerX(){return lowerX;}
	public double getLowerY(){return lowerY;}
	public double getLowerZ(){return lowerZ;}
	
	
	public double getUpperX(){return upperX;}
	public double getUpperY(){return lowerY+1;}
	public double getUpperZ(){return upperZ;}
	
	
	
	
	public void display(GL gl){
		
	}
}
