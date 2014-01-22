package mazerunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PlayerTest {

	double x = 1.5*Maze.SQUARE_SIZE, y=2.25*Maze.SQUARE_SIZE, z = 3.375*Maze.SQUARE_SIZE;
	double h = 0, v = 0;
	int hp = 150;
	
	@Test
	public void testPlayer() {
		Player player = new Player(null, x, y, z, h, v, hp, null);
		
		assertNotNull(player);
		assertTrue(player.getLocationX() == x);
		assertTrue(player.getLocationY() == y);
		assertTrue(player.getLocationZ() == z);
		assertTrue(player.getHorAngle() == h);
		assertTrue(player.getVerAngle() == v);
		assertTrue(player.getHitpoints() == hp);
		assertTrue(player.getMaxHP() == 200);
	}

	@Test
	public void testAddScorePos() {
		Player player = new Player(null, x, y, z, h, v, hp, null);
		
		assertTrue(player.getScore() == 0);
		player.addScore(25);
		assertTrue(player.getScore() == 25);
		player.addScore(35);
		assertTrue(player.getScore() == 60);
	}
	
	@Test
	public void testAddScoreNeg() {
		Player player = new Player(null, x, y, z, h, v, hp, null);
		
		assertTrue(player.getScore() == 0);
		player.addScore(-25);
		assertTrue(player.getScore() == 0);
		player.addScore(35);
		assertTrue(player.getScore() == 35);
	}

	@Test
	public void testAddHP() {
		Player player = new Player(null, x, y, z, h, v, hp, null);
		
		assertTrue(player.getHitpoints() == hp);
		player.addHP(50);
		assertTrue(player.getHitpoints() == hp + 50);
		player.addHP(100);
		assertTrue(player.getHitpoints() == player.getMaxHP());
	}

	@Test
	public void testRemoveHP() {
		Player player = new Player(null, x, y, z, h, v, hp, null);
		
		assertTrue(player.getHitpoints() == hp);
		player.removeHP(50);
		assertTrue(player.getHitpoints() == hp - 50);
		player.addHP(300);
		player.removeHP(300);
		assertTrue(player.getHitpoints() == 0);
	}

	@Test
	public void testDistanceToGameObject() {
		Player player1 = new Player(null, x, y, z, h, v, hp, null);
		Player player2 = new Player(null, x, y, z, h, v, hp, null);
		
		assertTrue(player1.distanceTo(player2) == 0);
		player2.setLocationX(player2.getLocationX() + 3);
		player2.setLocationZ(player2.getLocationZ() + 4);
		assertTrue(player1.distanceTo(player2) == 5);
	}

	@Test
	public void testDistanceToDoubleDouble() {
		Player player1 = new Player(null, x, y, z, h, v, hp, null);
		
		assertTrue(player1.distanceTo(x, z) == 0);
		assertTrue(player1.distanceTo(x + .2, z + .5) == Math.sqrt(Math.pow(.2,2) + Math.pow(.5,2)));
	}
	
	@Test
	public void testNear() {
		Player player1 = new Player(null, x, y, z, h, v, hp, null);
		Player player2 = new Player(null, x, y, z, h, v, hp, null);
		
		assertTrue(player1.near(player2, 0));
		assertTrue(player2.near(player1, 0));
		player2.setLocationX(player2.getLocationX() + Maze.SQUARE_SIZE);
		player2.setLocationY(Double.MAX_VALUE);
		player2.setLocationZ(player2.getLocationZ() + Maze.SQUARE_SIZE);
		assertTrue(player1.near(player2, 2));
		assertFalse(player1.near(player2, 1));
		assertTrue(player2.near(player2, 2));
	}

	@Test
	public void testNormaliseAngle() {
		assertTrue(GameObject.normaliseAngle(720) == 0);
		assertTrue(GameObject.normaliseAngle(-180) == 180);
		assertTrue(GameObject.normaliseAngle(-95.2) == -95.2);
		assertTrue(GameObject.normaliseAngle(1439.5) == -.5);
	}
	
	@Test
	public void testGetLocation() {
		Player player1 = new Player(null, x, y, z, h, v, hp, null);
		Location location1 = new Location(x, z);
		
		assertNotNull(player1.getLocation());
		assertTrue(player1.distanceTo(location1) == 0);
	}

	@Test
	public void testPLayerUpdate() {
		Player player = new Player(null, x, y, z, h, v, hp, null);
		class TestControl extends Control {
			@Override
			public void update() {}
		}
		TestControl control = new TestControl();
		player.setControl(control);
		
		int deltaTime = 10;
		control.dX = 2; control.dY = 3;
		player.update(deltaTime);
		assertTrue(player.getHorAngle() == h - control.dX);
		assertTrue(player.getVerAngle() == v - control.dY);
		
		player.setHorAngle(h);
		control.dX = 0; control.dY = 0;
		control.moveDirection = 0;
		player.update(deltaTime);
		assertTrue(player.getLocationX() == x);
		assertTrue(player.getLocationZ() == z - player.getSpeed()*deltaTime);
		
		
	}


}
