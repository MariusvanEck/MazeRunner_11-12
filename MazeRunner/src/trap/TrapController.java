package trap;

import java.util.ArrayList;

import javax.media.opengl.GL;

import mazerunner.Player;
import mazerunner.VisibleObject;

public class TrapController implements VisibleObject {
	private static ArrayList<Trap> list = new ArrayList<Trap>();
	private Player player;
	
	
	public TrapController(Player player){
		this.player = player;
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
			t.update(deltaTime,player);
	}
	
	public static void setTraps(ArrayList<Trap> traps) {
		list = traps;
	}
}
