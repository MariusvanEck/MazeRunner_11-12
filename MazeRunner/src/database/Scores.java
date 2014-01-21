package database;

import java.util.ArrayList;

public class Scores {
	public ArrayList<String> names;
	public ArrayList<Integer> scores;
	
	
	/**
	 * Initialize the attributes
	 */
	public Scores(){
		names = new ArrayList<String>();
		scores = new ArrayList<Integer>();
	}
	
	/**
	 * Gives back the size of scores
	 * Names should be the same size
	 * @return	returns the size of the ArrayList scores
	 */
	public int size(){return scores.size();}
}
