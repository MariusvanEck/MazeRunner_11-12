package loot;

import java.util.ArrayList;
import java.util.Iterator;

import mazerunner.Maze;
import mazerunner.Player;
import mazerunner.VisibleObject;

public class LootController implements VisibleObject {
	
	private ArrayList<Loot> lootList;
	
	
	/**
	 * Create a new lootcontroller with an associated player
	 * @param player
	 */
	public LootController(Player player){
		lootList = new ArrayList<Loot>();
		lootList.add(new Food(	2 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 
								Maze.SQUARE_SIZE / 2,4 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 
								50, null));
		lootList.add(new Food(	8 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 
								Maze.SQUARE_SIZE / 2,4 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 
								50, null));
	}
	
	/**
	 * The loot display function
	 */
	public void display(){
		for(Iterator<Loot> it = lootList.iterator(); it.hasNext();)
			it.next().display();
	}
	
	/**
	 * get the loot list
	 */
	public ArrayList<Loot> getList(){
		return lootList;
	}
}
