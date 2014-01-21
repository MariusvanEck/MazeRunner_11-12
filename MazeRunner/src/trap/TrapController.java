package trap;

import java.util.ArrayList;

import javax.media.opengl.GL;

import mazerunner.Enemy;
import mazerunner.Player;
import mazerunner.VisibleObject;

/**
 * Class for controlling all ingame traps
 */
public class TrapController implements VisibleObject {
	
	private static ArrayList<Trap> list = new ArrayList<Trap>();
	private Player player;
	private ArrayList<Enemy> enemies;
	
	public TrapController(Player player, ArrayList<Enemy> enemies){
		this.player = player;
		this.enemies = enemies;
	}

	/**
	 * Display function
	 */
	@Override
	public void display(GL gl) {
		for(Trap t: list)
			t.display(gl);
	}
	
	/**
	 * update function
	 */
	public void update(int deltaTime){
		for(Trap t:list)
			t.update(deltaTime, player, enemies);
	}
	
	/**
	 * Add a trap to the controller
	 * @param trap
	 */
	public void addTrap(Trap trap){
		list.add(trap);
	}
	
	/**
	 * Return an arraylist with all the traps associated with the controller
	 */
	public static ArrayList<Trap> getTraps(){
		return list;
	}
	
	/**
	 * Set the traps associated with the controller
	 */
	public static void setTraps(ArrayList<Trap> traps) {
		list = traps;
	}
}
