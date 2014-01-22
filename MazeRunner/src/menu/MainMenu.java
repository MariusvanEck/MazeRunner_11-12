package menu;

import gamestate.GameState;
import gamestate.GameStateManager;
import gamestate.Sound;
import gamestate.UserInput;

import java.io.File;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class MainMenu extends MenuObject implements MenuInterface{
	
	private UserInput input;			
	
	private Button buttons[];						// menu buttons
	private Texture[] textures;						// button textures
	public static MenuState menuState;				// the menu state
	public Sound theme = new Sound("theme.wav");	// the menu theme
	int x,y;										// the mouse location
	private String mapName = null;					// the current map name
	
	// sub menus
	private PlayMenu playMenu;
	private OptionsMenu optionsMenu;
	private QuitMenu quitMenu;
	
	// menu options
	public static final byte PLAY = 0;
	public static final byte OPTIONS = 1;
	public static final byte EDITOR = 2;
	public static final byte QUIT = 3;
	
	/**
	 * Constructor creates the menu objects
	 */
	public MainMenu(UserInput input, int minX, int maxX, int minY, int maxY){
		super(minX, maxX, minY, maxY);
		
		// set the menu state to MAIN
		menuState = MenuState.MAIN;
		
		// create sub menus
		playMenu = new PlayMenu(minX, maxX, minY, maxY);
		optionsMenu = new OptionsMenu(minX, maxX, minY, maxY);
		quitMenu = new QuitMenu(minX, maxX, minY, maxY);
		
		// set input object
		this.input = input;
		
		// load the menu textures
		textures = new Texture[5];
		loadTextures();
		
		// create menu buttons
		buttons = new Button[4];
		buttons[0] = new Button(minX,maxX,minY+(maxY-minY)*3/4,maxY, textures[1]);						//Play
		buttons[1] = new Button(minX,maxX,minY+(maxY-minY)*2/4,minY+(maxY-minY)*3/4, textures[2]);		//Options
		buttons[2] = new Button(minX,maxX,minY+(maxY-minY)*1/4,minY+(maxY-minY)*2/4, textures[3]);		//Editor
		buttons[3] = new Button(minX,maxX,minY,minY+(maxY-minY)*1/4, textures[4]);						//Quit
	}
	
	/**
	 * Load the textures
	 */
	public void loadTextures(){
		try {
			textures[0] = TextureIO.newTexture(new File("textures/menu/Background.png"), false);
			textures[1] = TextureIO.newTexture(new File("textures/menu/Play.png"), false);
			textures[2] = TextureIO.newTexture(new File("textures/menu/options.png"), false);
			textures[3] = TextureIO.newTexture(new File("textures/menu/editor.png"), false);
			textures[4] = TextureIO.newTexture(new File("textures/menu/quit.png"), false);
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Returns the value of the button hovered over
	 */
	public int getButton(int x,int y){
		if(buttons[0].minX < x && x < buttons[0].maxX && buttons[0].minY < y && y < buttons[0].maxY)
			return PLAY;
		if(buttons[1].minX < x && x < buttons[1].maxX && buttons[1].minY < y && y < buttons[1].maxY)
			return OPTIONS;
		if(buttons[2].minX < x && x < buttons[2].maxX && buttons[2].minY < y && y < buttons[2].maxY)
			return EDITOR;
		if(buttons[3].minX < x && x < buttons[3].maxX && buttons[3].minY < y && y < buttons[3].maxY)
			return QUIT;
		return -1;
	}
	
	
	/*
	 * **********************************************
	 * *			   drawing methods				*
	 * **********************************************
	 */
	
	/**
	 * Select the current menu display method
	 */
	public void display(GL gl){
		
		//Drawing the background
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		textures[0].enable();
		textures[0].bind();
		//White background color for normal texture view
		gl.glColor3f(255/255f, 255/255f, 255/255f);
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0,0);
			gl.glVertex2f(0, GameStateManager.screenHeight);
			gl.glTexCoord2f(1,0);
			gl.glVertex2f(GameStateManager.screenWidth, GameStateManager.screenHeight);
			gl.glTexCoord2f(1,1);
			gl.glVertex2f(GameStateManager.screenWidth, 0);
			gl.glTexCoord2f(0,1);
			gl.glVertex2f(0, 0);
		gl.glEnd();
		textures[0].disable();
		
		switch(menuState) {
		case MAIN:
			displayMain(gl);
			break;
		case OPTIONS:
			optionsMenu.reshape(GameStateManager.screenWidth/2-(GameStateManager.screenWidth/8),
					GameStateManager.screenWidth/2+(GameStateManager.screenWidth/8),
					GameStateManager.screenHeight/2-(GameStateManager.screenHeight/8),
					GameStateManager.screenHeight/2+(GameStateManager.screenHeight/8));
			optionsMenu.display(gl);
			break;
		case PLAY:
			playMenu.reshape(GameStateManager.screenWidth/2-(GameStateManager.screenWidth/8),
					GameStateManager.screenWidth/2+(GameStateManager.screenWidth/8),
					GameStateManager.screenHeight/2-(GameStateManager.screenHeight/8),
					GameStateManager.screenHeight/2+(GameStateManager.screenHeight/8));
			playMenu.display(gl);
			break;
		case NEW:
			playMenu.getLevelSelector().reshape(GameStateManager.screenWidth/2-(GameStateManager.screenWidth/8),
					GameStateManager.screenWidth/2+(GameStateManager.screenWidth/8),
					GameStateManager.screenHeight/2-(GameStateManager.screenHeight/8),
					GameStateManager.screenHeight/2+(GameStateManager.screenHeight/8));
			playMenu.getLevelSelector().display(gl);
			break;
		case QUIT:
			quitMenu.reshape(GameStateManager.screenWidth/2-(GameStateManager.screenWidth/16),
					GameStateManager.screenWidth/2+(GameStateManager.screenWidth/16),
					GameStateManager.screenHeight/2-(GameStateManager.screenHeight/8),
					GameStateManager.screenHeight/2+(GameStateManager.screenHeight/8));
			quitMenu.display(gl);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Draw the mainmenu
	 */
	private void displayMain(GL gl) {
		for (Button b : buttons) {
			b.display(gl);}
	}
	
	
	/*
	 * **********************************************
	 * *			   update methods				*
	 * **********************************************
	 */
	
	/**
	 * Get the update variables and select the current update method
	 */
	public void update(){
		x = input.mouseLocation.x;
		y = input.mouseLocation.y;
		
		switch(menuState) {
		case MAIN:		updateMain(x,y); break;
		case OPTIONS:	optionsMenu.update(x,y); break;
		case PLAY:		playMenu.update(x,y); break;
		case NEW:		playMenu.getLevelSelector().update(x,y); break;
		case QUIT:		quitMenu.update(x,y); break;
		default:
			break;}
		
		if(UserInput.wasMousePressed()) {
			UserInput.resetMousePressed();
			buttonPressed(x,y);
		}
	}
	
	/**
	 * Update the menu
	 */
	private void updateMain(int x, int y) {
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		// set selected button to true
		switch(getButton(x,y)){
		case PLAY: 		buttons[PLAY].setSelected(true); break;
		case OPTIONS: 	buttons[OPTIONS].setSelected(true);	break;
		case EDITOR:	buttons[EDITOR].setSelected(true); break;
		case QUIT:		buttons[QUIT].setSelected(true); break;
		}
	}

	/**
	 * Reshape 
	 */
	public void reshape(int minX, int maxX,int minY,int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		
		buttons[0].update(minX,maxX,minY+(maxY-minY)*3/4,maxY);
		buttons[1].update(minX,maxX,minY+(maxY-minY)*2/4,minY+(maxY-minY)*3/4);
		buttons[2].update(minX,maxX,minY+(maxY-minY)/4,minY+(maxY-minY)*2/4);
		buttons[3].update(minX,maxX,minY,minY+(maxY-minY)/4);
	}
	

	
	/*
	 * **********************************************
	 * *			   buttonPressed				*
	 * **********************************************
	 */
	
	/**
	* This method performs the correct action when a button is pressed
	**/
	public void buttonPressed(int x, int y){

		switch(menuState) {
		case MAIN:
			switch(getButton(x,y)) {
			case PLAY: 		menuState = MenuState.PLAY; break;
			case OPTIONS: 	menuState = MenuState.OPTIONS; break;
			case EDITOR: 	
				input.setGameState(GameState.EDITOR); 
				menuState = MenuState.MAIN; 
				break;
			case QUIT: 		menuState = MenuState.QUIT; break;}
			break;
			
		case OPTIONS:
			switch(optionsMenu.getButton(x, y)) {
			case OptionsMenu.BACK:	menuState = MenuState.MAIN; break;
			case OptionsMenu.VOLUME: optionsMenu.setVolume(x); break;
			case OptionsMenu.DIFFICULTY: optionsMenu.setDifficulty(x); break;}
			break;
			
		case PLAY:
			switch(playMenu.getButton(x, y)) {
				case PlayMenu.BACK: 	menuState = MenuState.MAIN; break;
				case PlayMenu.CONTINUE: 
					if (GameStateManager.isMazeRunnerStarted()) {
						input.setGameState(GameState.INGAME); break;
					}
				case PlayMenu.NEW:		
					menuState = MenuState.NEW;
					break;
			}
			break;
		case NEW:
			boolean notNull = playMenu.getLevelSelector().getName(playMenu.getLevelSelector().getButton(x, y)) != null;
			if(notNull && playMenu.getLevelSelector().getName(playMenu.getLevelSelector().getButton(x, y)).equals("up")){
				playMenu.getLevelSelector().goUp();
			}
			else if(notNull && playMenu.getLevelSelector().getName(playMenu.getLevelSelector().getButton(x, y)).equals("down")){
				playMenu.getLevelSelector().goDown();
			}
			else if(notNull && playMenu.getLevelSelector().getName(playMenu.getLevelSelector().getButton(x, y)).equals("back"))
				menuState = MenuState.PLAY;
			else if(notNull){
				mapName = playMenu.getLevelSelector().getName(playMenu.getLevelSelector().getButton(x, y));
				input.setGameState(GameState.INGAME);
				input.setNewGame(true);
				menuState = MenuState.PLAY;
			}
			break;
		case QUIT:
			switch(quitMenu.getButton(x, y)) {
			case QuitMenu.YES: 		System.exit(0);	break;			
			case QuitMenu.NO: 		menuState = MenuState.MAIN; break;}
			break;
		default:
			break;

		}
	}
	
	/**
	 * return the current map name
	 */
	public String getMapName(){
		return mapName;
	}
}
