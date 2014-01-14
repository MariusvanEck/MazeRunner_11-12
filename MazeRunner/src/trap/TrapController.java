package trap;

import java.util.ArrayList;

import javax.media.opengl.GL;

import mazerunner.VisibleObject;

public class TrapController implements VisibleObject {
	private ArrayList<Trap> list;
	
	public TrapController(){
		this.list = new ArrayList<Trap>();
	}
	
	public void addTrap(Trap trap){
		list.add(trap);
	}

	public ArrayList<Trap> getTraps(){return list;}


	@Override
	public void display(GL gl) {
		for(Trap t: list)
			t.display(gl);
	}
	
	public void update(int deltaTime){
		for(Trap t:list)
			t.update(deltaTime);
	}
}
