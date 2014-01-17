package trap;

import java.util.ArrayList;

import javax.media.opengl.GL;

import mazerunner.Enemy;
import mazerunner.Player;
import mazerunner.VisibleObject;

public class TrapController implements VisibleObject {
	private static ArrayList<Trap> list = new ArrayList<Trap>();
	private Player player;
	private ArrayList<Enemy> enemies;
	
	
	public TrapController(Player player, ArrayList<Enemy> enemies){
		this.player = player;
		this.enemies = enemies;
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
