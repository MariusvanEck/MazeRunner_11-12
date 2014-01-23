package mazerunner;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import loot.Food;
import loot.Gold;
import loot.Loot;
import loot.LootController;

import org.junit.Test;

import trap.TrapController;
import database.DataBase;

public class MazeTest {
	
	DataBase database = new DataBase();
	// the first level of the test maze as printed by the editor
	int[][] UnitTestLevelOneArray = {	{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
										{1, 97, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 0, 0, 7, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 5, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 38, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 2, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 23, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 19, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 53, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 47, 43, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 0, 0, 0, 37, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
										{1, 0, 0, 43, 0, 0, 0, 0, 0, 0, 0, 11, 13, 0, 0, 0, 0, 0, 59, 1} ,
										{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

	
	

	@Test
	public void testLoadMazeFromDataBase() {
		Maze maze = new Maze(null, database, "UnitTest", null);
		assertNotNull(maze);
		assertTrue(maze.getMazeSize() == 20);
		assertTrue(maze.getLevelSize() == 6);
		int[][] getArray = maze.getLevel(maze.getCurrentLevel());
		for(int i=0; i<maze.getMazeSize(); i++) {
			for(int j=0; j<maze.getMazeSize(); j++) {
				assertTrue(getArray[i][j] == UnitTestLevelOneArray[j][i]);}}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadCurrentLevelObjectsEmptyLevel() {
		Maze maze = new Maze(null, database, "UnitTest", null);
		maze.changeLevel(5, null);
		
		ArrayList<Object> objects = maze.getAllMazeObjects();
		assertTrue(((ArrayList<Stair>)objects.get(0)).size() == 0);
		assertTrue(((ArrayList<Floor>)objects.get(1)).size() == 324);
		assertTrue(((ArrayList<Roof>)objects.get(2)).size() == 324);
		assertTrue(((ArrayList<Wall>)objects.get(3)).size() == 76);
		assertTrue(((HashMap<Point,SlidingWall>)objects.get(4)).size() == 0);
		assertNull(((End)objects.get(5)));
	};
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadCurrentLevelObjectsLevelOne() {
		Maze maze = new Maze(null, database, "UnitTest", null);
		maze.changeLevel(0, null);
		
		ArrayList<Object> objects = maze.getAllMazeObjects();
		assertTrue(((ArrayList<Stair>)objects.get(0)).size() == 1);
		assertTrue(((ArrayList<Floor>)objects.get(1)).size() == 310);
		assertTrue(((ArrayList<Roof>)objects.get(2)).size() == 310);
		assertTrue(((ArrayList<Wall>)objects.get(3)).size() == 90);
		assertTrue(((HashMap<Point,SlidingWall>)objects.get(4)).size() == 1);
		assertNotNull(((End)objects.get(5)));
		
		Enemy enemy = EnemyAI.getEnemies().get(0);
		assertTrue(maze.convertToGridX(enemy.getLocationX()) == 5);
		assertTrue(maze.convertToGridZ(enemy.getLocationZ()) == 12);
		
		ArrayList<Loot> lootList = LootController.getLootList();
		assertTrue(lootList.size() == 8);
		int numFood = 0, numGold = 0;
		for (int i=0; i<lootList.size(); i++) {
			if (lootList.get(i) instanceof Food) numFood++;
			if (lootList.get(i) instanceof Gold) numGold++;}
		assertTrue(numFood == 2);
		assertTrue(numGold == 6);
		
		assertTrue(TrapController.getTraps().size() == 4);
	};
	
	@Test
	public void testIsWallIntInt() {
		Maze maze = new Maze(null, database, "UnitTest", null);
		maze.changeLevel(0, null);
		assertTrue(maze.isWall(0,0));
		assertFalse(maze.isWall(1, 1));
		assertTrue(maze.isWall(6, 17));
	}
	
	@Test
	public void testIsWallDoubleDouble() {
		Maze maze = new Maze(null, database, "UnitTest", null);
		maze.changeLevel(0, null);
		assertTrue(maze.isWall((0+.5)*Maze.SQUARE_SIZE, (0+.8)*Maze.SQUARE_SIZE, 0));
		assertFalse(maze.isWall((1+.5)*Maze.SQUARE_SIZE, (1+.5)*Maze.SQUARE_SIZE, .3));
		assertTrue(maze.isWall((6)*Maze.SQUARE_SIZE, (17+.001)*Maze.SQUARE_SIZE, 0));
		assertTrue(maze.isWall((1+.5)*Maze.SQUARE_SIZE, (1+.5)*Maze.SQUARE_SIZE, 1.1));
	}
	
	@Test
	public void testIsStairDoubleDouble() {
		Maze maze = new Maze(null, database, "UnitTest", null);
		maze.changeLevel(0, null);
		assertTrue(maze.isStair((11+.5)*Maze.SQUARE_SIZE, (18+.8)*Maze.SQUARE_SIZE, 0));
		assertFalse(maze.isStair((12+.5)*Maze.SQUARE_SIZE, (18+.5)*Maze.SQUARE_SIZE, .3));
		assertTrue(maze.isStair((12)*Maze.SQUARE_SIZE, (18+.001)*Maze.SQUARE_SIZE, .5));
	}
	
	@Test
	public void testCurrentGridPoint() {
		Maze maze = new Maze(null, database, "UnitTest", null);
		Enemy enemy = new Enemy(null, 3, 5, 0);
		assertTrue(maze.currentGridPoint(enemy).equals(new Point(3, 5)));
	}
	
	@Test
	public void TestAtEnd() {
		Maze maze = new Maze(null, database, "UnitTest", null);
		maze.changeLevel(0, null);
		Player player1 = new Player(null, 18.5*Maze.SQUARE_SIZE, .5*Maze.SQUARE_SIZE, 18.5*Maze.SQUARE_SIZE, 0, 0, 0, null);
		Player player2 = new Player(null, 1.5*Maze.SQUARE_SIZE, .5*Maze.SQUARE_SIZE, 1.5*Maze.SQUARE_SIZE, 0, 0, 0, null);
		
		assertTrue(maze.atEnd(player1));
		assertFalse(maze.atEnd(player2));
	}

	@Test
	public void testIsVisionBlocked() {
		Maze maze = new Maze(null, database, "UnitTest", null);
		maze.changeLevel(0, null);
		Enemy enemy1 = new Enemy(null, 10, 1, 0);
		Enemy enemy2 = new Enemy(null, 9, 4, 0);
		Player player = new Player(null, 1.5*Maze.SQUARE_SIZE, .5*Maze.SQUARE_SIZE, 1.5*Maze.SQUARE_SIZE, 0, 0, 0, null);
		
		assertFalse(maze.isVisionBlocked(enemy1, player));
		assertTrue(maze.isVisionBlocked(enemy2, player));
	}
}
