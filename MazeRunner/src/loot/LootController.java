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
	public static int primeNumbers[] = new int[100];
    // private int aantalobjecten = 100;
	
    /**
	 * Create a new lootcontroller with an associated player
	 * @param player
	 */
	public LootController(GL gl,Player player,Maze maze){
		this.player = player;
		
		lootList = new ArrayList<Loot>();
		double ms = Maze.SQUARE_SIZE;
		for (int i=0; i < maze.getLevelSize(); i++) {
			for (int z = 0; z < maze.getMazeSize(); z++){
				for(int x = 0; x < maze.getMazeSize(); x++){
					lootList.add(new Food(gl,5, 5, 5, 50, null,null));
					if(check(i,x,z,2,maze)){
						lootList.add(new Food(gl,x * ms + ms / 2, ms / 2, z * ms + ms / 2, 50, null,null));
					}
					if(check(i,x,z,3,maze)){
						lootList.add(new Food(gl,x * ms + ms / 2, 3 * ms / 4, z * ms + ms / 2, 50, null,null));
					}
					if(check(i,x,z,5,maze)){
						lootList.add(new Coin(gl,x * ms + ms / 2, 3 * ms / 4, z * ms + ms / 2, null,null));
					}
				}
			}
		}
	}
	
	public static void primes(){
		// Initialize array of the first 100 prime numbers
        int index = 0;
        //int product = 1;
        //index<VALUE change value for larger thingy
        while(index<100){
            for (int i = 2; i < 100*10; i++){
                boolean primeNum = true;
                for(int j=2; j<i; j++){
                    if (i%j==0){
                        primeNum = false;
                    }
                }
                if (primeNum){
                    primeNumbers[index] = i;
                    //System.out.println(i);
                    index++;
                    if(index==100){
                        break;
                    }
                }
            }
        }
	}
	
	public boolean check(int i, int x, int z, int value, Maze maze){
	/*	int index = 0;
		int number = maze.getMaze(i)[x][z];
		int objects[] = new int[10];
        // Input is larger than the maximum value, so the value given will miss factors and thus objects
		if(number == value){
			return true;
		}
		else if(number<=1){
			return false;
		}
		else if(value>=Integer.MAX_VALUE){
            System.out.println("LootController.check error: value is larger than Integer.MAX_VALUE.");
            objects[0] = -1;
            System.exit(0);
        }
        // Find all prime factors of input
        // p<VALUE VALUE is primeNumber array size, also change in primes()
        else for (int p = 0; p < 100; p++) {
            //System.out.println(primeNumbers[i]);
            if (number % primeNumbers[p] == 0) {
                if(primeNumbers[p] == value){
                	return true;
                }
                objects[index] = primeNumbers[p];
                index++;
                //System.out.println(primeNumbers[i]);
                number /= primeNumbers[p];
                p = p - 1;
            }
        }
        // If the remaining number is more than 1, not all prime factors are calculated
        if(number>1){
            System.out.println("Error: Not all prime factors are given, primeNumbers array too small.");
            objects[0] = -1;
            System.exit(0);
        }*/		
		return false;
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
				if(currentLoot.near(player, .2)){
					player.addHP(50);
					lootIterator.remove();}}}
	}
}
