package database;

import java.util.ArrayList;

public class Scores {
	public ArrayList<String> names;
	public ArrayList<Integer> scores;
	
	public Scores(){
		names = new ArrayList<String>();
		scores = new ArrayList<Integer>();
	}
	
	public int size(){return scores.size();}
}
