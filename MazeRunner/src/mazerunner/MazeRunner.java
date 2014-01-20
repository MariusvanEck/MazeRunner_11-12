package mazerunner;
import gamestate.GameState;
import gamestate.GameStateManager;
import gamestate.UserInput;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import loot.LootController;
import loot.Sword;
import loot.Weapon;
import trap.TrapController;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import database.DataBase;
import database.Scores;

/** 
 * MazeRunner is the class representing the INGAME GameState
 * 
 * @author Bruno Scheele, revised by Mattijs Driel
 */
public class MazeRunner {
	static final long serialVersionUID = 7526471155622776147L;

	
	/*
	 * **********************************************
	 * *		attributes and constructor			*
	 * **********************************************
	 */
	
	private static ArrayList<VisibleObject> visibleObjects;					// A list of objects that will be displayed on screen.
	
	private Player player;													// the player
	private EnemyAI enemyAI;												// the enemyAI with the enemies
	private Weapon weapon;
	private LootController lootController;									// the loot
	private TrapController trapController;									// the traps
	private Camera camera;													// the camera
	private Maze maze; 														// the maze
	private String mapName;
	private DataBase dataBase;
	
	private HeadsUpDisplay headsUpDisplay;									// the headsUpDisplay overlay
	private UserInput input;												// user input object controls the game.
	private long previousTime = Calendar.getInstance().getTimeInMillis(); 	// Used to calculate elapsed time.
	
	private int spawnLocationX, spawnLocationZ;								// store the spawn location of the player
	
	private static HashMap<String, Texture> textures;

/*
 * **********************************************
 * *		Initialisation methods				*
 * **********************************************
 */
	
	/**
	 * Initialises the the INGAME part of the game.
	 */
	public MazeRunner(GL gl, UserInput input) {
		// set input
		this.input = input;
		
		// load textures
		loadTextures();
	}
	
	/**
	 * initializeObjects() creates all the objects needed for the game to start normally.
	 * <p>
	 * This includes the following:
	 * <ul>
	 * <li> the default Maze
	 * <li> the Player
	 * <li> the Camera
	 * <li> the User input
	 * </ul>
	 * <p>
	 * Remember that every object that should be visible on the screen, should be added to the
	 * visualObjects list of MazeRunner through the add method, so it will be displayed 
	 * automagically. 
	 */
	public void initObjects(GL gl, String mapName)	throws InvalidSpawnLocationException{
		// We define an ArrayList of VisibleObjects to store all the objects that need to be
		// displayed by MazeRunner.
		visibleObjects = new ArrayList<VisibleObject>();
		
		// Add the maze that we will be using.
		this.mapName = mapName;
		dataBase = new DataBase();
		maze = new Maze(gl, dataBase, mapName, textures);
		if(!setPlayerSpawn(gl)) throw new InvalidSpawnLocationException("No Spawn Found");
		visibleObjects.add(maze);
		
		// Initialise the player.
		player = new Player(gl, 
							spawnLocationX * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 	// x coordinate
							Maze.SQUARE_SIZE / 2,										// y coordinate
							spawnLocationZ * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2, 	// z coordinate
							90, 0, 100, weapon);
		
		// Initialise the loot
		lootController = new LootController(gl, player);
		visibleObjects.add(lootController);
		
		
		// initialise enemies and add
		enemyAI = new EnemyAI(gl, player, maze);
		visibleObjects.add(enemyAI);
		
		// Initialise the Traps
				trapController = new TrapController(player, enemyAI.getEnemies());
				visibleObjects.add(trapController);
		
		// set up a camera
		camera = new Camera( player.getLocationX(), player.getLocationY(), player.getLocationZ(), 
				             player.getHorAngle(), player.getVerAngle() );
		
		weapon = new Sword(gl, "models/Killer_Frost_Ice_Sword/Killer_Frost_Ice_Sword.obj",
				"models/Killer_Frost_Ice_Sword/Killer_Frost_Ice_Sword_D.tga");
		player.setWeapon(weapon);
		weapon.setCreature(player);
		visibleObjects.add(weapon);
		
		// set player control
		player.setControl(input);
		
		// setup headsUpDisplay
		headsUpDisplay = new HeadsUpDisplay(player);
	}
	
	/**
	 * Checks for a player spawn location and sets it
	 */
	private boolean setPlayerSpawn(GL gl) {
		for (int i=0; i<maze.getLevelSize(); i++) {
			int[][] level = maze.getLevel(i);
			
			for (int j=0; j<maze.getMazeSize(); j++) {
				for (int k=0; k<maze.getMazeSize(); k++){
					if (level[j][k] == 97) {
						spawnLocationX = j;
						spawnLocationZ = k;
						maze.changeLevel(i, gl);
						return true;
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * Loads the textures used for ingame display
	 */
	public void loadTextures() {
		textures = new HashMap<String, Texture>();
		
		try {
			textures.put("wall", TextureIO.newTexture(new File("textures/Level/wall.png"), false));
			textures.put("floor", TextureIO.newTexture(new File("textures/Level/floor.png"), false));
			textures.put("roof", TextureIO.newTexture(new File("textures/Level/floor.png"), false));}

		catch (Exception e) {e.printStackTrace(); System.exit(0);}
	}
	

/*
 * **********************************************
 * *				draw methods 				*
 * **********************************************
 */

	/**
	 * init(GL) is called to setup GL for INGAME display.
	 * <p>
	 * It sets up most of the OpenGL settings for the viewing, as well as the general lighting.
	 * <p> 
	 * It is <b>very important</b> to realize that there should be no drawing at all in this method.
	 */
	public static void init(GL gl, int screenWidth, int screenHeight) {
        GLU glu = new GLU();
        
        gl.glClearColor(0, 0, 0, 0);								// Set the background color.
        
        // Now we set up our viewpoint.
        gl.glMatrixMode( GL.GL_PROJECTION );						// We'll use orthogonal projection.
        gl.glLoadIdentity();										// Reset the current matrix.
        glu.gluPerspective(60, screenWidth, screenHeight, 200);		// Set up the parameters for perspective viewing. 
        gl.glMatrixMode( GL.GL_MODELVIEW );
        
        // Enable back-face culling.
        gl.glCullFace( GL.GL_BACK );
        gl.glEnable( GL.GL_CULL_FACE );
        
        // Enable Z-buffering.
        gl.glEnable( GL.GL_DEPTH_TEST );
        
        // Enable alpha blending
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE_MINUS_SRC_ALPHA);
        
        // Set and enable the lighting.
        float lightPosition[] = { 5.0f, 10.0f, 5.0f, 1.0f }; 			// High up in the sky!
        float lightColour[] = { .6f, .6f, .6f, 1.0f };					// White light!
        gl.glLightfv( GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0 );	// Note that we're setting Light0.
        gl.glLightfv( GL.GL_LIGHT0, GL.GL_AMBIENT, lightColour, 0);
        gl.glEnable( GL.GL_LIGHTING );
        gl.glEnable( GL.GL_LIGHT0 );
        
        // Set the shading model.
        gl.glShadeModel( GL.GL_SMOOTH );
	}
	
	/**
	 * display(GL) is called upon by the GameStateManager when it is ready to draw a frame and the
	 * GameState is INGAME.
	 * <p>
	 * In order to draw everything needed, it iterates through MazeRunners' list of visibleObjects. 
	 * For each visibleObject, this method calls the object's display(GL) function, which specifies 
	 * how that object should be drawn. The object is passed a reference of the GL context, so it 
	 * knows where to draw.
	 */
	public void display(GL gl) {
		GLU glu = new GLU();
		
		// set the viewing transformation
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
        glu.gluLookAt( camera.getLocationX(), camera.getLocationY(), camera.getLocationZ(), 
 			   camera.getVrpX(), camera.getVrpY(), camera.getVrpZ(),
 			   camera.getVuvX(), camera.getVuvY(), camera.getVuvZ() );

        // Display all the visible objects of MazeRunner.
        for(Iterator<VisibleObject> it = visibleObjects.iterator(); it.hasNext();) {
        	it.next().display(gl);}
        
        if(input.getGameState() == GameState.INGAME) {
	        GameStateManager.switchTo2D(gl);
	        headsUpDisplay.display(gl);
	        GameStateManager.switchTo3D(gl);}
	}

	
/*
 * **********************************************
 * *			   update methods				*
 * **********************************************
 */

	/**
	 * update() updates the mazerunner game using the past time since the previous frame
	 */
	public void update(GL gl) {
		// if players health is 0 go to main menu and reset
		if (player.getHitpoints() == 0) {
			dataBase.addScore(mapName,player.getName(),player.getScore());
			
			Scores scores = dataBase.getScores(mapName);
			
			System.out.println("MazeRunner: " + scores.size());
			
			for(int i = 0; i < scores.size();i++){
				System.out.println(scores.names.get(i) + " " + scores.scores.get(i));
			}
			
			
			System.out.println();
			input.setGameState(GameState.MENU);
		}
		
		// Calculating time since last frame.
		Calendar now = Calendar.getInstance();		
		long currentTime = now.getTimeInMillis();
		int deltaTime = (int)(currentTime - previousTime);
		previousTime = currentTime;
		
		// Update the player
		updatePlayerMovement(gl,deltaTime);
		
		// Update the enemies
		updateEnemyMovement(deltaTime);
		
		//update traps
		trapController.update(deltaTime);
		
		// Update Loot
		lootController.update();
		
		// update slidingWalls
		maze.updateSlidingWalls(deltaTime, player);
		
		// Update headsUpDisplay
		headsUpDisplay.update(deltaTime);
		
		// Set camera according to the players position
		updateCamera();
		
		// update the weapon location
		updateWeaponLocation();
	}
	
	/**
	 * updatePlayerMovement(int) updates the player position and oriention
	 */
	private void updatePlayerMovement(GL gl,int deltaTime) {
		
		// save current coordinates
		double previousX = player.getLocationX();
		double previousZ = player.getLocationZ();
		
		// update
		player.update(deltaTime);
		
		// check if a wall was hit
		boolean hitWall = maze.isWall(player.getLocationX(), player.getLocationZ(), 0.1);
		
		// go to the next level if a stair was hit
		if (maze.isStair(player.getLocationX(), player.getLocationZ(), 0)) {
			maze.changeLevel(maze.getCurrentLevel() + 1, gl);}
		
		// set player back if a wall was hit
		if (hitWall){
			player.locationX = previousX;
			player.locationZ = previousZ;
		}
	}	
	
	/**
	 * updateEnemyMovement(int) updates the enemies' position and orientation
	 */
	private void updateEnemyMovement(int deltaTime) {
		// Update the enemyAI
		enemyAI.update(deltaTime);
		
		// get an iterator over the enemies
		Iterator<Enemy> it = enemyAI.getEnemies().iterator();
		
		Enemy enemy;
		while (it.hasNext()) {
			enemy = it.next();
			
			double previousX = enemy.getLocationX();
			double previousZ = enemy.getLocationZ();
			
			// update
			enemy.update(deltaTime);
			
			// check if a wall was hit
			enemy.setHitWall(	maze.isWall(enemy.getLocationX(), enemy.getLocationZ(), 0.2) ||
					maze.isStair(enemy.locationX, enemy.locationZ, 0));
			
			// set enemy back if a wall or stair was hit
			if (enemy.hasHitWall()){
				enemy.locationX = previousX;
				enemy.locationZ = previousZ;}}
	}
	
	/**
	 * updateCamera() updates the camera position and orientation.
	 * <p>
	 * This is done by copying the locations from the Player, since MazeRunner runs on a first person view.
	 */
	private void updateCamera() {
		camera.setLocationX( player.getLocationX() );
		camera.setLocationY( player.getLocationY() );  
		camera.setLocationZ( player.getLocationZ() );
		camera.setHorAngle( player.getHorAngle() );
		camera.setVerAngle( player.getVerAngle() );
		camera.calculateVRP();
	}
	
	/**
	 * updateWeaponLocation() updates the weapon location so that the player holds the weapon in his hands.
	 */
	private void updateWeaponLocation(){	
		double weaponAngleX = 20+player.getVerAngle();
		double weaponAngleY = player.getHorAngle();
		double weaponAngleZ = 90;

		weapon.setAngleX(weaponAngleX);
		weapon.setAngleY(weaponAngleY); // sideways, without effects
		weapon.setAngleZ(weaponAngleZ);// - 10*Math.sin(Math.toRadians(player.getHorAngle()%360))); // twists sword
		
		weapon.setWieldX(player.getLocationX() + Math.cos(Math.toRadians(player.getHorAngle())));
		weapon.setWieldY(player.getLocationY() - 0.5);
		weapon.setWieldZ(player.getLocationZ() - Math.sin(Math.toRadians(player.getHorAngle())));

		//TODO implement angles and strafe-positioning
	}
	
	
/*
 * **********************************************
 * *				  setters					*
 * **********************************************
 */

	/**
	 * sets the previousTime variable to the current time
	 */
	public void setPreviousTime() {
		this.previousTime = Calendar.getInstance().getTimeInMillis();
	}
}