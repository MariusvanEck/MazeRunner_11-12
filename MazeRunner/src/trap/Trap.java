package trap;

import java.util.ArrayList;

import javax.media.opengl.GL;

import mazerunner.Enemy;
import mazerunner.Player;

public interface Trap {
	public void update(int deltaTime, Player player, ArrayList<Enemy> enemies);
	public void display(GL gl);
}
