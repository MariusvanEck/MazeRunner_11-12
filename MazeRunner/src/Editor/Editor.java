package Editor;

import gamestate.GameStateManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import cast.Cast;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import database.DataBase;

/* List of used PrimeNumbers
 * 0 - Floor
 * 1 - Wall
 * 2 - Gold1
 * 3 - Gold2
 * 5 - Gold3
 * 7 - Gold4
 * 11 - Stair Low
 * 13 - Stair High
 * 19 - Food
 * 23 - Enemy
 * 29 - Coin
 * 31 - Chest
 * 37 - SlidingWall
 * 41 - TrapN
 * 43 - TrapE
 * 47 - TrapS
 * 53 - TrapW
 * 59 - The End
 * 97 - Player
 */

public class Editor extends JFrame implements GLEventListener, MouseListener, MouseMotionListener, ActionListener {

	/**
	 * This is the LevelEditor for Die Trying. With this editor mazes consisting of 1 to 6 levels can be
	 * designed.
	 */
	private static final long serialVersionUID = -1698109322093496405L;
	//Graphic related variables
	private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private int screenWidth = gd.getDisplayMode().getWidth();
	private int screenHeight =  gd.getDisplayMode().getHeight();
	private GLCanvas canvas;
	
	//Maze related variables
	private int mazeX = 20;						//number of X-squares, 10 entered for testing
	private int nlevels = 6;									//number of levels in the maze
	private int buttonRow = 10;
	public float mazeL = ((screenWidth-screenHeight)/3*2);					//Left bound of mazeDrawingWindow
	private float mazeR = screenWidth-((screenWidth-screenHeight)/3);		//Right bound of mazeDrawingWindow
	
	//creates the initial levels with walls on the borders
	protected Level level = new Level(mazeX,mazeX);
	protected Level[] levels = new Level[nlevels];

	//Button related
	private Button btn[];
	private Button btnr[];	
	
	//New Maze Menu variables
	private JTextField size = new JTextField();
	private JTextField nlev = new JTextField();
	private JTextField mapName = new JTextField();
	private JFrame frame = new JFrame("Create New Maze");
	private JFrame saveFrame = new JFrame("Save Map");
	private JFrame loadFrame = new JFrame("Load Map");
	private JButton newmaze = new JButton("Create New Maze");
	private JButton saveMap = new JButton("Save");
	private JButton loadMap = new JButton("Load");
	private JPanel paneln = new JPanel();
	private JPanel savePanel = new JPanel();
	private JPanel loadPanel = new JPanel();
	private JLabel sizel = new JLabel("Level size(3-63): ");
	private JLabel nlevl = new JLabel("Number of levels (1-6): ");
	private JLabel mapNamel = new JLabel("name");
    int returnVal;
	private Texture[] textureLeft;
	private Texture[] levelTextures;
	
	/**
	 * Constructor for the Editor
	 * Creates a frame and adds a canvas and animator to it
	 * Initializes the other parameters 
	 */
	public Editor() {
		super("Level Editor Beta v1.0");
		setSize(screenWidth, screenHeight);
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		canvas = new GLCanvas(caps);
		add(canvas);
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		Animator anim = new Animator(canvas);
		anim.start();
		setUndecorated(true);
		setResizable(false);
		setVisible(true);
	}
	
	/**
	 * Draws the level
	 * @param gl	GL for the drawing
	 */
	public void drawLevel(GL gl){
		if (mazeX > 19){
	   		level.lineWidth = 1;
	   	}	
		level.draw(gl, mazeL, 0, mazeR-mazeL, screenHeight);
	}
	
	/**
	 * Draws the buttons
	 * @param gl	GL for the drawing
	 */
	public void Buttons(GL gl){
		
		for (int i = 0; i < buttonRow*3; i++){
			btn[i].draw(gl);
		}
		
		//left row of buttons
		for (int i = 0; i < buttonRow; i++){
			btnr[i].draw(gl);
		}	
	}
	
	/**
	 * Get the button index
	 * @param x		The button x-location
	 * @param y		The button y-location
	 * @return		returns the button index if there is button, -1 otherwise.
	 */
	public int getButton(int x,int y){
		int i = -1;
		for(int it = 0;it<buttonRow*3;it++){
			if(btn[it].getX() < x && x < btn[it].getSizex()+btn[it].getX() && btn[it].getY() < y &&
					y < btn[it].getSizey()+btn[it].getY()){
				i = it;
				break;
			}
		}
		return i;
	}
	
	/**
	 * Get the buttonR index
	 * @param x		The button x-location
	 * @param y		The button y-location
	 * @return		returns the button index if there is button, -1 otherwise.
	 */
	public int getButtonR(int x,int y){
		int i = -1;
		for(int it = 0; it<buttonRow;it++){
			if(btnr[it].getX() < x && x < btnr[it].getSizex()+btnr[it].getX() && btnr[it].getY() < y &&
					y < btnr[it].getSizey()+btnr[it].getY()){
				i = it;
				break;
			}
		}
		return i;
	}
	
	/**
	 * openGL display handling
	 */
	@Override
	public void display(GLAutoDrawable drawable) {

		GL gl = drawable.getGL();

		// Set the clear color and clear the screen.
		gl.glClearColor(255/255f, 238/255f, 131/255f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		// Draw the buttons.
		Buttons(gl);
		
		// Draw the level
		drawLevel(gl);

		// Flush the OpenGL buffer, outputting the result to the screen.
		gl.glFlush();
	}

	/**
	 * openGL init
	 */
	@Override
	public void init(GLAutoDrawable drawable) {

		//Retrieve the OpenGL handle, this allows us to use OpenGL calls.
		GL gl = drawable.getGL();

		// Set the matrix mode to GL_PROJECTION, allowing us to manipulate the
		// projection matrix
		gl.glMatrixMode(GL.GL_PROJECTION);

		// Always reset the matrix before performing transformations, otherwise
		// those transformations will stack with previous transformations!
		gl.glLoadIdentity();

		/*
		* glOrtho performs an "orthogonal projection" transformation on the
		* active matrix. In this case, a simple 2D projection is performed,
		* matching the viewing frustum to the screen size.
		*/
		
		gl.glOrtho(0, screenWidth, 0, screenHeight, -1, 1);

		// Set the matrix mode to GL_MODELVIEW, allowing us to manipulate the
		// model-view matrix.
		gl.glMatrixMode(GL.GL_MODELVIEW);

		// We leave the model view matrix as the identity matrix. As a result,
		// we view the world 'looking forward' from the origin.
		gl.glLoadIdentity();

		// We have a simple 2D application, so we do not need to check for depth
		// when rendering.
		gl.glDisable(GL.GL_DEPTH_TEST);	
		
		level.primes();
		
		//Creating the left buttonmenu
		//Vierkante knoppen?
		float buttonsizex = mazeL/4;
    	float buttonsizey = screenHeight/(buttonRow+1);
    	float spacingy = buttonsizey/(buttonRow);
    	float spacingx = buttonsizex/4;
    	
		btn = new Button[buttonRow*3];
		btnr = new Button[buttonRow];
		textureLeft = new Texture[3];
		// textureRight = new Texture[buttonRow];
		
		
		//Loading all the textures
		try {
			textureLeft[1] = TextureIO.newTexture(new File("textures/Editor/StairsL.png"), false);
			textureLeft[2] = TextureIO.newTexture(new File("textures/Editor/StairsH.png"), false);
			
			levelTextures = new Texture[100];
			levelTextures[1] = TextureIO.newTexture(new File("textures/Editor/Wall.png"), false);
			levelTextures[37] = TextureIO.newTexture(new File("textures/Editor/SlidingWall.png"), false);
			
			levelTextures[2] = TextureIO.newTexture(new File("textures/Editor/Gold1.png"), false);
			levelTextures[3] = TextureIO.newTexture(new File("textures/Editor/Gold2.png"), false);
			levelTextures[5] = TextureIO.newTexture(new File("textures/Editor/Gold3.png"), false);
			levelTextures[7] = TextureIO.newTexture(new File("textures/Editor/Gold4.png"), false);
			
			levelTextures[11] = TextureIO.newTexture(new File("textures/Editor/StairsL.png"), false);
			levelTextures[13] = TextureIO.newTexture(new File("textures/Editor/StairsH.png"), false);
			
			levelTextures[19] = TextureIO.newTexture(new File("textures/Editor/Food.png"), false);
			
			levelTextures[23] = TextureIO.newTexture(new File("textures/Editor/Enemy.png"), false);
			levelTextures[97] = TextureIO.newTexture(new File("textures/Editor/Player.png"), false);
			
	   		levelTextures[41] = TextureIO.newTexture(new File("textures/Editor/TrapN.png"), false);
	    	levelTextures[43] = TextureIO.newTexture(new File("textures/Editor/TrapE.png"), false);
	    	levelTextures[47] = TextureIO.newTexture(new File("textures/Editor/TrapS.png"), false);
	    	levelTextures[53] = TextureIO.newTexture(new File("textures/Editor/TrapW.png"), false);
	    	levelTextures[59] = TextureIO.newTexture(new File("textures/Editor/the_end.png"), false);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//Colors for the buttons on the left
		float colors[][] = new float[4][buttonRow*3];
		
		//color of the other buttons (temporary)
		for (int i = 0; i < 3; i++){
			for (int j = 2; j < 30; j++){
				colors[i][j] = 0.5f;
				colors[3][j] = 1.0f;
			}
		}
		
		//color of the walldraw button0
		colors[0][0] = 87/255f; colors[1][0] = 84/255f; colors[2][0] = 83/255f; colors[3][0] = 1.0f;
		
		//background color of the stairsL button1
		colors[0][1] = 255/255f; colors[1][1] = 255/255f; colors[2][1] = 255/255f; colors[3][1] = 1.0f;
		
		//background color of the stairsH button2
		colors[0][2] = 255/255f; colors[1][2] = 255/255f; colors[2][2] = 255/255f; colors[3][2] = 1.0f;
		
		//color of the void button27
		colors[0][27] = 0/255f; colors[1][27] = 0/255f; colors[2][27] = 0/255f; colors[3][27] = 1.0f;
		
		//Texts for the buttons on the left
		String text[] = new String[30];
		for (int i = 0; i < 30; i++){
			text[i] = "TEMPORARY";
			//empty text for these buttons
			if (i == 1 || i == 2){
				text[i] = "";
			}
		}
		text[0] = "Wall"; text[3] = "Gold1"; text[6] = "Gold2"; text[9] = "Gold3"; text[12] = "Gold4";
		text[4] ="TrapN"; text[7] ="TrapE";text[10] ="TrapS";text[13] ="TrapW";
		text[8] = "Food"; text[11] = "Enemy"; text[5] = "SlidingWall"; text[17] = "The End"; text[14] = "Player";
		text[27] = "LtoString"; text[28] = "Clear"; text[29] = "ClearAll";
		
		//Create the buttons on the left
	   	int index = 0;
	   	for(int row = 0;row<buttonRow;row++){
	   		for(int col = 0;col<3;col++){
	   			btn[index] = new Button(gl, spacingx+col*spacingx+col*buttonsizex, screenHeight - (row+1)*(spacingy+buttonsizey), 
	   					buttonsizex, buttonsizey, colors[0][index], colors[1][index], colors[2][index], colors[3][index], text[index]);
	   			index++;
	   		 }
	   	 }
	   	
	   	//If present, set textures on the buttons on the left
	   	btn[1].setTexture(textureLeft[1]);
	   	btn[2].setTexture(textureLeft[2]);
	   	
	   	//Texts for the buttons on the right
	   	String textr[] = new String[10];
	   	for (int i = 3; i < 9; i++){
	   		textr[i] = "Level " + (i-2);
	   	}
	   	textr[0] = "New Maze"; textr[1] = "Save Maze"; textr[2] = "Load Maze"; textr[9] = "Exit";
	   	
	   	//Create the buttons on the right
	   	int indexr = 0;
	   	for (int i = 0; i < buttonRow; i++){
	   		btnr[indexr] = new Button(gl, mazeR+spacingx, screenHeight - (spacingy+spacingy*(i)+(i+1)*buttonsizey), 
	   				(screenWidth-mazeR)-2*spacingx, buttonsizey, 0.5f, 0.5f, 0.5f, 1.0f, textr[indexr]);
	   		indexr++;
	   	}
	   	
	   	for (int k = 0; k < nlevels; k++){
	   		levels[k] = new Level(mazeX,mazeX);
	   	}
	   	levels[0].set(1, 1, 97); 						// default spawn location of player
	   	levels[nlevels-1].set(mazeX-2, mazeX-2, 59); 	// default end location
	   	level = levels[0];
	   	//set level button default to level 1
	   	btnr[3].setSelected(true);
	   	//set default to walldraw
	   	btn[0].setSelected(true);
	   	
	   	Level.setTextureMaze(levelTextures);
	}
	
	/**
	 * Save the current map to the DataBase
	 * @param name			The map name
	 * @param dataBase		The DataBase that should be used
	 * @return				True if save was successful, false otherwise.
	 */
	public boolean saveToDataBase(String name,DataBase dataBase){
		byte[][] res = new byte[7][];
		res[6] = new byte[8];
		byte[] temp = Cast.intToByteArray(nlevels); 
		res[6][0] = temp[0];
		res[6][1] = temp[1];
		res[6][2] = temp[2];
		res[6][3] = temp[3];
		
		temp = Cast.intToByteArray(mazeX);
		res[6][4] = temp[0];
		res[6][5] = temp[1];
		res[6][6] = temp[2];
		res[6][7] = temp[3];
		
		
		for(int y = 0; y < nlevels;y++){
			ArrayList<Byte> list = new ArrayList<Byte>();
			Level lvl = levels[y];
			for(int z = 0; z < mazeX; z++){
				for(int x = 0; x < mazeX; x++){
					temp = Cast.intToByteArray(lvl.level[x][z]);
					list.add(temp[0]);
					list.add(temp[1]);
					list.add(temp[2]);
					list.add(temp[3]);
				}
			}
			
			res[y] = new byte[list.size()];
				for(int i = 0; i  < list.size(); i++)
					res[y][i] = list.get(i);
		}
		return dataBase.addMap(name, res);
	}
	
	/**
	 * load a map from the DataBase
	 * @param name			The map name
	 * @param dataBase		The DataBase that should be used
	 */
	public void loadFromDataBase(String name,DataBase dataBase){
		nlevels = dataBase.getNumLevels(name);
		mazeX = dataBase.getMazeSize(name);
		level.setX(mazeX);
		level.setY(mazeX);
		levels = new Level[nlevels];
		for(int i = 0; i < nlevels;i++){
			levels[i] = new Level(mazeX,mazeX);
			levels[i].setLevel(dataBase.getMap(name, i));
		}
		level = levels[0];
	}
	
	/**
	 * The mouseEvent handler for selecting the function buttons
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		/*
		 * The 30 buttons on the left side are defined here below
		 * First we determine which button is selected
		 */
		int i;
		i = getButton(me.getX(),screenHeight-me.getY());
		//set selected button to true
		if(i>=0){
			btn[i].setSelected(true);
			//set selected for other buttons to false
			for(int j = 0; j < buttonRow*3; j++){
				if (j != i){
					btn[j].setSelected(false);
				}
			}
		}
		
		/*
		 * Now we will define the functionality of the individual buttons if they use the 
		 * mouseReleased function
		 */	
		
        //print level to console
        if (i == 27){
            System.out.println(level.toString());
            btn[25].setSelected(false);
        }
        
		
		//Clear level button
		if (i == 28){
			for(int j = 3; j < buttonRow-1; j++){
				if (btnr[j].selected){
					levels[j-3] = new Level(mazeX,mazeX);
					break;
				}
			}
			//select the walldraw as default and de-select the current button
			btn[28].setSelected(false);
			btn[0].setSelected(true);
		}
		
		//Clear all button
		if (i == 29){
			for(int j = 3; j < buttonRow-1; j++){
				levels[j-3] = new Level(mazeX,mazeX);
			}
			//select the walldraw as default and de-select the current button
			btn[29].setSelected(false);
			btn[0].setSelected(true);
		}
	
		
		/*
		 * The 10 buttons on the right side are defined here below
		 * First we determine which button is selected
		 */
		int k;
		k = getButtonR(me.getX(),screenHeight-me.getY());
		//set 1 selected button to true for the level buttons
		if(k >= 3 && k < nlevels+3){
			btnr[k].setSelected(true);
			//set selected for other buttons to false
			for(int j = 3; j < buttonRow-1; j++){
				if (j != k){
					btnr[j].setSelected(false);
				}
			}
		}
		
		//set 1 selected button to true for the new, load and save buttons
		if(k >= 0 && k < 3){
	   		btnr[k].setSelected(true);
	   		for(int j = 0; j<3; j++){
	   			if (j!=k){
	   				btnr[j].setSelected(false);
	   			}
	   		}
	   	}
		
		// new maze
		if (k == 0){
			toFront();
			setSize(1,1);
			frame.setAlwaysOnTop(true);
		    frame.setSize(250, 120);
		    frame.setVisible(true);
		    frame.setResizable(false);
		    frame.toFront();
		    
		    newmaze.addActionListener(this);
		    
		    size.setPreferredSize(new Dimension(30,20));
		    nlev.setPreferredSize(new Dimension(30,20));
		    
		    paneln.setBackground(Color.WHITE);
		    paneln.add(sizel);
		    paneln.add(size);
		    paneln.add(nlevl);
		    paneln.add(nlev);
		    paneln.add(newmaze);
		    //add the panel to the frame
		    frame.add(paneln);
		    
			btnr[0].setSelected(false);
			frame.toFront();
		}
		
		//Saving a file
		else if(k == 1){
			toFront();
			setSize(1,1);
			saveFrame.setAlwaysOnTop(true);
		    saveFrame.setSize(250, 120);
		    saveFrame.setVisible(true);
		    saveFrame.setResizable(false);
		    saveFrame.toFront();
		    
		    saveMap.addActionListener(new saveActionListener(this));
		    
		    mapName.setPreferredSize(new Dimension(150,20));
		    
		    savePanel.setBackground(Color.WHITE);
		    savePanel.add(mapNamel);
		    savePanel.add(mapName);
		    savePanel.add(saveMap);
		    //add the panel to the frame
		    saveFrame.add(savePanel);
		    
			btnr[1].setSelected(false);
			saveFrame.toFront();
			
			
		}
        //Loading a file
		else if(k==2){
			toFront();
			setSize(1,1);
			loadFrame.setAlwaysOnTop(true);
		    loadFrame.setSize(250, 120);
		    loadFrame.setVisible(true);
		    loadFrame.setResizable(false);
		    loadFrame.toFront();
		    
		    loadMap.addActionListener(new loadActionListener(this));
		    
		    mapName.setPreferredSize(new Dimension(150,20));
		    
		    loadPanel.setBackground(Color.WHITE);
		    loadPanel.add(mapNamel);
		    loadPanel.add(mapName);
		    loadPanel.add(loadMap);
		    //add the panel to the frame
		    loadFrame.add(loadPanel);
		    
		    btnr[2].setSelected(false);
		    loadFrame.toFront();
		}
		//The Exit button on the bottom-right
		if(k == 9){
			new GameStateManager();
			Level.updateMazeX(20);
			dispose();
		}
		
		//Check which level-button is selected
		for (k = 3; k < nlevels+3; k++){
			if (btnr[k].selected){
				level = levels[k-3];
			}
		}
	}

	/**
	 * The mouseEvent handler for selecting the draw buttons
	 */	
	@Override
	public void mousePressed(MouseEvent me) {
		double squareX = Math.floor( ( (me.getX() - (mazeL)) / level.buttonSize));
		double squareY = Math.floor(((me.getY())/level.buttonSize));
		int X = (int) squareX;
		int Y = mazeX - (int) squareY;
		
		//The wall draw button
		if (btn[0].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			level.level[X][Y-1] = 1;
		}
		
		// TrapN draw button
		else if(btn[4].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			level.level[X][Y-1] = 41;
		}
		//TrapE draw button
		else if(btn[7].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			level.level[X][Y-1] = 43;
		}
		//TrapS draw button
		else if(btn[10].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			level.level[X][Y-1] = 47;
		}
		//TrapW draw button
		else if(btn[13].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			level.level[X][Y-1] = 53;
		}
		
		//The slidingwall draw button
		else if (btn[5].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			level.level[X][Y-1] = 37;
		}
		
		//Stair Low Part draw button
		else if (btn[1].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			level.level[X][Y-1] = 11;
		}
		
		//Stair High Part draw button
		else if (btn[2].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			level.level[X][Y-1] = 13;
		}
		
		//Gold1 draw button
		else if (btn[3].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 37 && level.level[X][Y-1] != 97 && level.check(X,Y-1,2) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 2;
			}
			else{
			    level.level[X][Y-1] *= 2;
			}
		}
		
		//Gold2 draw button
		else if (btn[6].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 37 && level.level[X][Y-1] != 97 && level.check(X,Y-1,3) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 3;
			}
			else{
			    level.level[X][Y-1] *= 3;
			}
		}
		
		//Gold3 draw button
		else if (btn[9].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 37 && level.level[X][Y-1] != 97 && level.check(X,Y-1,5) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 5;
			}
			else{
			    level.level[X][Y-1] *= 5;
			}
		}
		
		//Gold4 draw button
		else if (btn[12].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 37 && 
				level.level[X][Y-1] != 97 && level.check(X,Y-1,7) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 7;
			}
			else{
			    level.level[X][Y-1] *= 7;
			}
		}
		
		//Food draw button
		else if (btn[8].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 37 && 
				level.level[X][Y-1] != 97 && level.check(X,Y-1,19) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 19;
			}
			else{
			    level.level[X][Y-1] *= 19;
			}
		}
		
		//Enemy draw button
		else if (btn[11].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 37 && 
				level.level[X][Y-1] != 97 && level.check(X,Y-1,23) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 23;
			}
			else{
			    level.level[X][Y-1] *= 23;
			}
		}
		
		//Coin draw button
		else if (btn[5].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 37 && 
				level.level[X][Y-1] != 97 && level.check(X,Y-1,29) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 29;
			}
			else{
			    level.level[X][Y-1] *= 29;
			}
		}
		
		//Chest draw button
		else if (btn[4].selected == true && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 37 && 
				level.level[X][Y-1] != 97 && level.check(X,Y-1,31) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 31;
			}
			else{
			    level.level[X][Y-1] *= 31;
			}
		}
		
        //Player Spawn button
		else if (SwingUtilities.isLeftMouseButton(me) && btn[14].selected == true && squareX > 0 && 
        		squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 97){
            for(int a = 0; a < mazeX; a++){
                for(int b = 0; b < mazeX; b++){
                	for (int c = 0; c < nlevels; c++){
	                    if (levels[c].level[a][b] == 97){
	                        levels[c].level[a][b] = 0;
	                    }
                	}
                }
            }
            level.level[X][Y-1] = 97;
        }
		
		//The End button
		else if (SwingUtilities.isLeftMouseButton(me) && btn[17].selected == true && squareX > 0 && 
        		squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0 && level.level[X][Y-1] != 59){
            for(int a = 0; a < mazeX; a++){
                for(int b = 0; b < mazeX; b++){
                	for (int c = 0; c < nlevels; c++){
	                    if (levels[c].level[a][b] == 59){
	                        levels[c].level[a][b] = 0;
	                    }
                	}
                }
            }
            level.level[X][Y-1] = 59;
        }
		
		//The right mouse button always draws an empty floor tile
		if(SwingUtilities.isRightMouseButton(me) && squareX > 0 && squareX < mazeX-1 && squareY < mazeX-1 && squareY > 0){
			level.level[X][Y-1] = 0;
		}
	}
	/**
	 * Resets the size of the screen
	 */
	protected void resetSize(){
		setSize(screenWidth,screenHeight);
	}
	/**
	 * Get the name of the map
	 * @return	String with the name
	 */
	protected String getMapName(){
		return mapName.getText();
	}
	/**
	 * Get the saveFrame
	 * @return	Returns the Save Frame
	 */
	protected JFrame getSaveFrame(){
		return saveFrame;
	}
	/**
	 * Get the loadFrame
	 * @return	Returns the load Frame
	 */
	protected JFrame getLoadFrame(){
		return loadFrame;
	}
	
	/**
	 * Makes drawing walls easier
	 */
	@Override
	public void mouseDragged(MouseEvent me) {
		//The wall and floor draw buttons can be dragged for easier drawing
		if(btn[0].selected || SwingUtilities.isRightMouseButton(me)){
			mousePressed(me);
		}
		
	}
	/**
	 * Action event for new Maze
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
        setSize(screenWidth,screenHeight); 
        try{
        	nlevels = Integer.parseInt(nlev.getText());
        	mazeX = Integer.parseInt(size.getText());
        	Level.updateMazeX(mazeX);
        	Level.updateMazeL(mazeL);
        }catch (NumberFormatException ex){
        	System.err.println("One or more invalid numbers were entered");
        }
        
        if (mazeX < 3 || mazeX > 63){
            mazeX = 63;
        }
        
        if (nlevels < 1 || nlevels > 6){
            nlevels = 6;
        }
        
        level = new Level(mazeX,mazeX);
        levels = new Level[nlevels];
        
           for (int k = 0; k < nlevels; k++){
               levels[k] = new Level(mazeX,mazeX);
           }
           
           level = levels[0];
           btnr[3].setSelected(true);
           for (int i = 4; i < 9; i++){
               btnr[i].setSelected(false);
           }
           
   	       levels[0].set(1, 1, 97); 						// default spawn location of player
   	   	   levels[nlevels-1].set(mazeX-2, mazeX-2, 59); 	// default end location
           frame.dispose();


	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {/* NOT USED */}
	@Override
	public void mouseMoved(MouseEvent arg0) {/*NOT USED*/}
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {/*NOT USED*/}
	@Override
	public void mouseClicked(MouseEvent me) {/*NOT USED*/}
	@Override
	public void mouseEntered(MouseEvent arg0) {/*NOT USED*/}
	@Override
	public void mouseExited(MouseEvent arg0) {/*NOT USED*/}
	
	/**
	 * Get the button
	 * @return the button
	 */
	public Button[] getBtn() {
		return btn;
	}
	
	/**
	 * Get the maze size
	 * @return the maze size
	 */
	public int getMazeX() {
		return mazeX;
	}
}
