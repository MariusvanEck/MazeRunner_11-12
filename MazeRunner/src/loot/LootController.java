package loot;

import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL;

import mazerunner.Player;
import mazerunner.VisibleObject;

public class LootController implements VisibleObject {
	
	private static ArrayList<Loot> lootList = new ArrayList<Loot>();
	private Player player;
	
    /**
	 * Create a new lootcontroller with an associated player
	 * @param player
	 */
	public LootController(GL gl, Player player){
		this.player = player;
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
	public static ArrayList<Loot> getLootList(){
		return lootList;
	}
	
	/**
	 * the loot update function
	 */
	public void update() {
		Loot currentLoot;
		Iterator<Loot> lootIterator = getLootList().listIterator();
		while(lootIterator.hasNext()){
			currentLoot = lootIterator.next();
			
			if(currentLoot instanceof Food){
				if(currentLoot.near(player, .2)){
					if(player.addHP(50)) {
						lootIterator.remove();
					}
				}
			}else if(currentLoot instanceof Gold){
				if(currentLoot.near(player, .2)){
					player.addScore(10);
					lootIterator.remove();
				}
			}
		}
	}
	
	/**
	 * Set the loot list
	 */
	public static void setLootList(ArrayList<Loot> loot) {
		lootList = loot;
	}
}
