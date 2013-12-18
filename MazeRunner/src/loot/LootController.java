package loot;

import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL;

import mazerunner.Maze;
import mazerunner.Player;
import mazerunner.VisibleObject;

public class LootController implements VisibleObject {
	
	private ArrayList<Loot> lootList;
	private Player player;
	
	/**
	 * Create a new lootcontroller with an associated player
	 * @param player
	 */
	public LootController(Player player){
		this.player = player;
		
		lootList = new ArrayList<Loot>();
		lootList.add(new Food(	2 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 
								Maze.SQUARE_SIZE / 2, 
								4 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 
								50, null));
		lootList.add(new Food(	8 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 
								Maze.SQUARE_SIZE / 2, 
								4 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 
								50, null));
	}
	
	/**
	 * The loot display function
	 */
	public void display(GL gl){
		for(Iterator<Loot> it = lootList.iterator(); it.hasNext();)
			it.next().display(gl);
	}
	
	/**
	 * get the loot list
	 */
	public ArrayList<Loot> getList(){
		return lootList;
	}
	
	/**
	 * the loot update function
	 */
	public void update() {
		Loot currentLoot;
		Iterator<Loot> lootIterator = getList().listIterator();
		while(lootIterator.hasNext()){
			currentLoot = lootIterator.next();
			
			if(currentLoot instanceof Food){
				if(currentLoot.near(player, .2*Maze.SQUARE_SIZE)){
					player.addHP(50);
					lootIterator.remove();}}}
	}
}
