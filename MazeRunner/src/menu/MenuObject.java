package menu;

public abstract class MenuObject implements MenuInterface{
	protected int minX,minY;
	protected int maxX,maxY;
	protected boolean selected;
	
	public MenuObject(int minX,int maxX,int minY,int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.selected = true;
	}
	
	public int getMinX(){return minX;}
	public int getMaxX(){return maxX;}
	public int getMinY(){return minY;}
	public int getMaxY(){return maxY;}
	
	public void setSelected(boolean select){selected = select;}
	public boolean isSelected(){return selected;}

	public void setMinX(int minX) {this.minX = minX;}
	public void setMinY(int minY) {this.minY = minY;}
	public void setMaxX(int maxX) {this.maxX = maxX;}
	public void setMaxY(int maxY) {this.maxY = maxY;}
}