package gamestate;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

import mazerunner.InvalidSpawnLocationException;
import mazerunner.MazeRunner;
import menu.MainMenu;
import menu.MenuState;
import Editor.Editor;

import com.sun.opengl.util.Animator;

/**
 * De GameStateManager holds the main function of the game and the main display method. Also it provides
 * the switching functionality between game states
 * 
 * gameStates
 * 
 * MENU:		the game is in the menu
 * INGAME: 		the game is running
 * PAUSE: 		the game is paused (INGAME displaying is frozen)
 * HIGHSCORE:	the game is finished, highscore is displayed
 * EDITOR:		the game is in the editor
 * 
 */
public class GameStateManager extends Frame implements GLEventListener{
	private static final long serialVersionUID = 4002514451736607431L;
	
	
	/*
	 * **********************************************
	 * *		attributes and constructor			*
	 * **********************************************
	 */

	public static int screenWidth = 800, screenHeight = 500;		// screenSize
	private GLCanvas canvas;										// canvas for drawing
	private GameState gameState;									// current GameState
	
	private static MazeRunner mazeRunner;							// INGAME functionality
	private MainMenu menu;											// MENU functionality
	private static HighScore highScore = null;						// HighScore display
	private UserInput input;										// Mouse and Keyboard input functionality
	
	// Cursor for ingame state
	private Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");										
	
	/**
	 * Initialises the complete game.
	 * <p>
	 * GSM extends Java AWT Frame, to function as the window. It creats a canvas on 
	 * itself where JOGL will be able to paint the OpenGL graphics. It then initializes all 
	 * game components and initializes JOGL, giving it the proper settings to accurately 
	 * display MazeRunner. Finally, it adds itself as the OpenGL event listener, to be able 
	 * to function as the view controller.
	 */
	public GameStateManager() {
		// Make a new window
		super("MazeRunner");
		
		// Set the window settings
		setSize(screenWidth, screenHeight);
		setBackground( Color.white );
		setLocationRelativeTo(null);
		
		// Set window closing action
		this.addWindowListener( new WindowAdapter()
		{
			public void windowClosing( WindowEvent e )
			{
				System.exit(0);
			}
		});
			
		// Initialize JOGL
		initJOGL();
		
		// set visible
		setVisible(true);
		
		// Initialize and set a UserInput Object
		input = new UserInput(canvas, getSize());
	}
	
	
	/*
	 * **********************************************
	 * *		Initialization methods				*
	 * **********************************************
	 */
	
	/**
	 * initJOGL() sets up JOGL to work properly.
	 * <p>
	 * It sets the capabilities we want for MazeRunner, and uses these to create the GLCanvas upon which 
	 * MazeRunner will actually display our screen. To indicate to OpenGL that is has to enter a 
	 * continuous loop, it uses an Animator, which is part of the JOGL api.
	 */
	private void initJOGL()	{
		// First, we set up JOGL. We start with the default settings.
		GLCapabilities caps = new GLCapabilities();
		
		// Then we make sure that JOGL is hardware accelerated and uses double buffering.
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);

		// Now we add the canvas, where OpenGL will actually draw for us. We'll use settings we've just defined. 
		canvas = new GLCanvas(caps);
		add(canvas);
		
		/* We need to add a GLEventListener to interpret OpenGL events for us. Since MazeRunner implements
		 * GLEventListener, this means that we add the necesary init(), display(), displayChanged() and reshape()
		 * methods to this class.
		 * These will be called when we are ready to perform the OpenGL phases of MazeRunner. 
		 */
		canvas.addGLEventListener(this);
		
		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		Animator anim = new Animator(canvas);
		anim.start();
	}
	
	
	/*
	 * **********************************************
	 * *		OpenGL event handlers				*
	 * **********************************************
	 */

	/**
	 * init(GLAutodrawable) is called to initialize the OpenGL context
	 * <p>
	 * Implemented through GLEventListener. 
	 * <p> 
	 * It is <b>very important</b> to realize that there should be no drawing at all in this method.
	 */
	public void init(GLAutoDrawable drawable) {
		drawable.setGL( new DebugGL(drawable.getGL() )); 			// We set the OpenGL pipeline to Debugging mode.
	}
	
	/**
	 * display(GLAutoDrawable) is called upon whenever OpenGL is ready to draw a new frame and handles 
	 * all of the drawing.
	 * <p>
	 * Implemented through GLEventListener. 
	 * 
	 * The display function should be used to pick the right sub display function depending on the gameState
	 */
	public void display(GLAutoDrawable drawable) {
		try{
			GL gl = drawable.getGL();
			
			
			// update the game status
			updateGameState(gl);
			
			// set the right volume
			Sound.setVolume();
			
			// clear the color buffer
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
	
			// update and display using the current gamestate
			switch (gameState) {
			case INGAME:
				updateScreenCenter();
				mazeRunner.update(gl);							// update mazerunner game
				mazeRunner.display(gl);							// display mazerunner game
				break;
			case MENU:
				switchTo2D(gl);									// switch to 2D
				menu.update();									// update menu
				menu.display(gl);								// display menu
				switchTo3D(gl);									// switch to 3D
				break;
			case HIGHSCORE:
				if(highScore != null){
					switchTo2D(gl);								// switch to 2D
					highScore.display(gl);						// display HighScore
					switchTo3D(gl);
				}else{
					gameState = GameState.MENU;
				}
				break;
			case PAUSE:
				mazeRunner.display(gl);							// display frozen mazerunner game
				switchTo2D(gl);									// switch to 2D
				Pause.display(gl, screenWidth, screenHeight);	// display pause
				switchTo3D(gl);									// switch to 3D
				break;
			case EDITOR: 
				break;}
			
	        // Load identity
	        gl.glLoadIdentity();
	        // Flush the OpenGL buffer.
	        gl.glFlush();
		}catch(GLException e){
			
		}
	}
	
	/**
	 * displayChanged(GLAutoDrawable, boolean, boolean) is called upon whenever the display mode changes.
	 * <p>
	 * Implemented through GLEventListener. 
	 * Seeing as this does not happen very often, we leave this unimplemented.
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
	
	/**
	 * reshape(GLAutoDrawable, int, int, int, int, int) is called upon whenever the viewport changes shape, 
	 * to update the viewport setting accordingly.
	 * <p>
	 * Implemented through GLEventListener. 
	 * This mainly happens when the window changes size, thus changin the canvas (and the viewport 
	 * that OpenGL associates with it). It adjust the projection matrix to accomodate the new shape.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		
		// Update the screen/canvas size and adjusting the viewport.
		screenWidth = width;
		screenHeight = height;
		input.setScreenSize(getSize());
		input.setCanvasSize(canvas.getSize());
		gl.glViewport(x, y, screenWidth, screenHeight);
	
		// Set the new projection matrix.
		gl.glMatrixMode( GL.GL_PROJECTION );
		gl.glLoadIdentity();
		glu.gluPerspective( 60, (double)width/(double)height, .1, 200 );
		gl.glMatrixMode( GL.GL_MODELVIEW );
		if(menu != null){
			menu.reshape(screenWidth/2-(screenWidth/8), screenWidth/2+(screenWidth/8), screenHeight/2-(screenHeight/8),
					screenHeight/2+(screenHeight/8));
		}
		if(highScore != null)
			highScore.update(0, screenWidth, 0, screenHeight);
		
	}

	
	/*
	 * **********************************************
	 * *			   Switch projection			*
	 * **********************************************
	 */	
	
	/**
	 * Switches the projection to 2D by pushing a projection and modelview matrix
	 * Also disables 3D projection options
	 */
	public static void switchTo2D(GL gl) {
		GLU glu = new GLU();
		
		// push matrices and set projection to '2D' orthogonal
		gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        glu.gluOrtho2D(0,screenWidth,0,screenHeight);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        
        // disable z-buffer back face culling and lighting
        gl.glDisable(GL.GL_CULL_FACE);  
        gl.glDisable(GL.GL_DEPTH_TEST);  
        gl.glDisable(GL.GL_LIGHTING);
        
        // clear the color buffer bit
        gl.glClearColor(0,0,0,0);
	}

	/**
	 * Switches the projection back to 3D by popping a projection and a modelview matrix
	 * re-enables 3D projection options
	 */
	public static void switchTo3D(GL gl) {
		// pop matrices
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPopMatrix();
        
        // re-enable z-buffer and back face culling and lighting
        gl.glEnable(GL.GL_CULL_FACE);  
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_LIGHTING);
	}
	
	
	/*
	 * **********************************************
	 * *				  updates					*
	 * **********************************************
	 */
	
	/**	
	 * updateGameState(GL gl) updates the game status. If the gameState is null (start of the program)
	 * it initialises gameState.
	 */
	@SuppressWarnings("deprecation")
	private void updateGameState(GL gl) {
		// gameState initialisation 
		if (gameState == null) {
			menu = new MainMenu(input,0,0,0,0); 
			MazeRunner.init(gl, screenWidth, screenHeight);
			input.setGameState(GameState.MENU);}
		
		// setup new game if required
		if (input.isNewGame()) {
			String mazeName = menu.getMapName();
			
			if (mazeName != null) {
				try {
					mazeRunner = new MazeRunner(gl, input);
					mazeRunner.initObjects(gl, mazeName);}
				catch(InvalidSpawnLocationException isle) {
					System.err.println(isle.getMessage());
					input.setGameState(GameState.MENU);}}
			else {
				input.setGameState(GameState.MENU);
				MainMenu.menuState = MenuState.PLAY;}
			
			input.setNewGame(false);}
		
		// check if the gameState and is changed and update
		if (gameState != input.getGameState()) {
			if (gameState == GameState.MENU) menu.theme.stop();
			gameState = input.getGameState();
			
			switch(gameState) {
			case INGAME:
				mazeRunner.setPreviousTime();
				input.centerMouse();
				input.resetMouseLocation();
				setCursor(blankCursor);
				break;
			case PAUSE:
				setCursor(blankCursor);
				break;
			case MENU:
				menu.theme.loop();
				setCursor(Cursor.DEFAULT_CURSOR);
				break;
			case HIGHSCORE:
				break;
			case EDITOR:
				new Editor();
				dispose();
				break;}}
	}
	
	/**
	 * Sets the center of the screen correctly
	 */
	private void updateScreenCenter() {
		Point screenCenter = canvas.getLocationOnScreen(); 
		screenCenter.translate(getSize().width/2, getSize().height/2);
		input.setScreenCenter(screenCenter);
	}
	
	/**
	 * Set the highscore
	 */
	public static void setHighScore(HighScore score){
		highScore = score;
	}
	
	/**
	 * Check if a mazeRunner game is started
	 */
	public static boolean isMazeRunnerStarted() {
		return mazeRunner != null;
	}
	
	/*
	 * **********************************************
	 * *				  Main						*
	 * **********************************************
	 */
	
	/**
	 * Program entry point
	 */
	public static void main(String[] args) {
		// Create and run MazeRunner.
		new GameStateManager();
	}
}
