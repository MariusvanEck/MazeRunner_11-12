package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import cast.Cast;
import cast.InvalidByteArraySize;

public class DataBase {
	private Connection connection = null;
	
	/**
	 * Constructs a connection to the dataBase.
	 * If the there is no DataBase it will be created.
	 */
	public DataBase(){
		try {
			setup();
		} catch (ClassNotFoundException e) {
			System.err.println("DataBase: Somthing went wrong, couldn't initialize DataBase correctly: " + e);
		}
	}
	
	/**
	 * Setup the connection if possible.
	 * @throws ClassNotFoundException
	 */
	protected void setup() throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		
		try{
			// Create DataBase connection
			refresh();
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // timeout to 30 sec
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Map(ID INTEGER PRIMARY KEY AUTOINCREMENT,Name TINYTEXT," + // create table with an ID, Name (max 255 byte)
										"Data BLOB,lvl0 LONGBLOB,lvl1 LONGBLOB,lvl2 LONGBLOB,lvl3 LONGBLOB, lvl4 LONGBLOB, lvl5 LONGBLOB," + //  and Data (max 4GB per lvl)
										"HighScore BLOB);"); // HighScore Data per map
			statement.executeUpdate("CREATE INDEX IF NOT EXISTS ID ON Map(ID);"); // create index for table Map for faster search
			statement.close();
		}
		catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
		}
	}
	/**
	 * Refresh the connection with the dataBase
	 * @throws SQLException
	 */
	protected void refresh() throws SQLException{
		if(connection != null)
			connection.close();
		connection = DriverManager.getConnection("jdbc:sqlite:mazerunner.db");
		
	}
    
	/**
	 * Cleanup method, This will close the connection with the dataBase.
	 */
	protected void cleanUp(){
		try
		{
			if(connection != null)
				connection.close();
		}
			catch(SQLException e)
			{
				System.err.println("DataBase: Closing the connection failed!\n\t" + e);
			}
	}
	/**
	 * Make sure that the connection is closed when the object is destroyed
	 * @throws Throwable
	 */
	@Override
	protected void finalize() throws Throwable{
		try{
			this.cleanUp();
		}finally{
			super.finalize();
		}
	}
	
	/**
	 * Add a Map to the dataBase
	 * @param name		The mapName
	 * @param lvlData	The MapData
	 * @return			Returns true if map is added successful. False otherwise
	 */
	public boolean addMap(String name,byte[][] lvlData){
		try{
			refresh();
			if(lvlData.length-1 > 6 || lvlData.length == 0){ // the length of the first array
				System.err.println("DataBase: Invalid array size is: " + lvlData.length + " max size is 6");
				return false;
			}
			if(doesMapNameExists(name)){
				System.err.println("DataBase: Map name already in use");
				return false;
			}
			PreparedStatement prep = connection.prepareStatement("INSERT INTO Map(Name,Data,lvl0,lvl1,lvl2,lvl3,lvl4,lvl5,HighScore)" +
																"VALUES(?, ?, ?, ?, ?, ?, ?, ?,?);");
			prep.setString(1, name);
			
			prep.setBytes(2,lvlData[6]);
			
			switch(lvlData.length-2){
				case 5:
					prep.setBytes(8, lvlData[5]);
				case 4:
					prep.setBytes(7, lvlData[4]);
				case 3:
					prep.setBytes(6, lvlData[3]);
				case 2:
					prep.setBytes(5, lvlData[2]);
				case 1:
					prep.setBytes(4, lvlData[1]);
				case 0:
					prep.setBytes(3, lvlData[0]);
					break;
				default:
					System.err.println("DataBase: Something went wrong!");
					return false;
			}
			prep.setBytes(9, null);

			prep.execute();
			return true;
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Import Map from file
	 */
	public void importMap(){
		JFileChooser chooser = new JFileChooser();
		File file = new File("mazes\\test.maze");
		chooser.setCurrentDirectory(file);
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal != JFileChooser.APPROVE_OPTION) {
				System.err.println("DataBase: Can't find file!");
				return;
		}
		file = chooser.getSelectedFile();
		String name = file.getAbsolutePath().split("\\\\")[file.getAbsolutePath().split("\\\\").length-1].split("\\.")[0];
		
		int numLevels = 0,mazeSize = 0;
		int[][][] levels = null;
		try{	
			if(doesMapNameExists(name)){
				System.err.println("DataBase: Map name already in use");
				return;
			}
			
			
			FileInputStream fmaze = new FileInputStream(file);
			ObjectInputStream omaze = new ObjectInputStream(fmaze);
			
			numLevels = (Integer) omaze.readObject();
			mazeSize = (Integer) omaze.readObject();
			
			
			int[][] firstLevel = (int[][]) omaze.readObject();
			int x = firstLevel.length;
			
			mazeSize = x;
	
			levels = new int[numLevels][][];
			
			int[][] nextLevel;
			levels[0] = firstLevel;
			for ( int n=1; n<numLevels; n++){
				 nextLevel = (int[][]) omaze.readObject();
				 if (nextLevel == null){
					System.err.println("DataBase: Corrupted File!");
					omaze.close();
					return;
				 }
				levels[n] = nextLevel;
			}
			omaze.close();
		}
				
		catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
		}
		catch(IOException e){
			System.err.println("DataBase: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("DataBase: " + e.getMessage());
		}
		
		//mirror the levels
		for(int n = 0; n < numLevels; n++){
    		int[][] templevel = levels[n];
    		levels[n] = new int[mazeSize][mazeSize]; 
    		for(int x = 0; x<mazeSize; x++){
    			for(int y = mazeSize-1; y>=0; y--){
    				int oy = mazeSize-y-1;
    				levels[n][x][oy] = templevel[x][y];
    			}
    		}
    	}
		
		byte[][] res = new byte[7][];
		res[6] = new byte[8];
		byte[] temp = Cast.intToByteArray(numLevels); 
		res[6][0] = temp[0];
		res[6][1] = temp[1];
		res[6][2] = temp[2];
		res[6][3] = temp[3];
		
		temp = Cast.intToByteArray(mazeSize);
		res[6][4] = temp[0];
		res[6][5] = temp[1];
		res[6][6] = temp[2];
		res[6][7] = temp[3];
		
		
		for(int y = 0; y < numLevels;y++){
			ArrayList<Byte> list = new ArrayList<Byte>();
			int[][] lvl = levels[y];
			for(int z = 0; z < mazeSize; z++){
				for(int x = 0; x < mazeSize; x++){
					temp = Cast.intToByteArray(lvl[x][z]);
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
		this.addMap(name, res);
	}
	
	/**
	 * Gives back the level matrix from the Map
	 * @param name	The map name
	 * @param lvl	The level that will be extracted
	 * @return		Returns the level matrix if successful, null otherwise.
	 */
	public int[][] getMap(String name,int lvl){;
		if(lvl > 6){
			System.err.println("DataBase: wrong lvl has to be less then 5");
			return null;
		}
		try{
			refresh();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * " +
												"FROM Map " +
												"WHERE Map.Name = '" + name + "';");
			if(rs.next()){
				byte[] b = rs.getBytes("Data"); 
				int mazeSize = Cast.byteArrayToInt(new byte[] {b[4],b[5],b[6],b[7]});
				
				byte[] b_lvl = rs.getBytes("lvl"+lvl);
				
				int[][] res = new int[mazeSize][mazeSize];
				int i = 0;
				for(int z = 0; z < mazeSize; z++){
					for(int x = 0; x < mazeSize;x++){
						res[x][z] = Cast.byteArrayToInt(new byte[] {b_lvl[i],b_lvl[i+1],b_lvl[i+2],b_lvl[i+3]});
						System.out.print(res[x][z]);
						System.out.print(' ');
						i+=4;
					}
					System.out.print('\n');
				}
				
				
				statement.close();
				return res;
			}
			else{
				System.err.println("DataBase: rs is not open!\n\tSomething wrong with SQL statement?");
				statement.close();
				return null;
			}
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
			return null;
		} catch (InvalidByteArraySize e) {
			System.err.println("DataBase: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Check if map name exists in the dataBase. 
	 * @param name				The Map Name
	 * @return					True if exists, false otherwise
	 * @throws SQLException
	 */
	private boolean doesMapNameExists(String name) throws SQLException{
		refresh();
		Statement statement = connection.createStatement();
		ResultSet temp = statement.executeQuery("SELECT * FROM Map WHERE Name = '" + name + "';");
		if(temp.next()){
			statement.close();
			return true;
		}
		statement.close();
		return false;
	}
	/**
	 * Gives back the number of levels in given Map
	 * @param name	Map name
	 * @return		amount of numbers in Map if successful, 0 otherwise
	 */
	public int getNumLevels(String name){
		try {
			refresh();
			Statement statement = connection.createStatement();
			ResultSet temp = statement.executeQuery("SELECT Data FROM Map WHERE Name = '" + name + "';");
			if(temp.next()){
				byte[] b = temp.getBytes("Data");
				statement.close();
				return Cast.byteArrayToInt(new byte[] {b[0],b[1],b[2],b[3]});
			}
			else{
				System.err.println("DataBase: rs is not open!\n\tSomething wrong with SQL statement?");
				return 0;
			}
		} catch (SQLException e) {
			System.err.println("DataBase: " + e.getMessage());
			return 0;
		} catch (InvalidByteArraySize e) {
			System.err.println("DataBase: " + e.getMessage());
			return 0;
		}
	}
	/**
	 * Gives back the size of the level matrix
	 * @param name	Map name
	 * @return		Size of the level matrix if successful, 0 otherwise
	 */
	public int getMazeSize(String name){
		try {
			refresh();
			Statement statement = connection.createStatement();
			ResultSet temp = statement.executeQuery("SELECT Data FROM Map WHERE Name = '" + name + "';");
			if(temp.next()){
				byte[] b = temp.getBytes("Data");
				statement.close();
				return Cast.byteArrayToInt(new byte[] {b[4],b[5],b[6],b[7]});
			}
			else{
				System.err.println("DataBase: rs is not open!\n\tSomething wrong with SQL statement?");
				return 0;
			}
		} catch (SQLException e) {
			System.err.println("DataBase: " + e.getMessage());
			return 0;
		} catch (InvalidByteArraySize e) {
			System.err.println("DataBase: " + e.getMessage());
			return 0;
		}
	}
	/**
	 * Add a score to the HighScore of a Map
	 * @param mapName		Map name
	 * @param playerName	The Player name that should be added
	 * @param score			The score that should be added
	 */
	public void addScore(String mapName,String playerName,int score){
		try{
			refresh();
			boolean update = false;
			Scores scores = getScores(mapName);
			if(scores == null){
				scores = new Scores();
			}
			
			ArrayList<String> nameList = new ArrayList<String>();
			ArrayList<Integer> scoreList = new ArrayList<Integer>();
			
			for(int i = 0;  (i < scores.size()) && (nameList.size() < 10);i++){
				if(score > scores.scores.get(i) && !update){
					nameList.add(playerName);
					scoreList.add(score);
					update = true;
					i--; // because scores at i are not used and need still be added
				}else{
					nameList.add(scores.names.get(i));
					scoreList.add(scores.scores.get(i));
				}
				
			}
			if(nameList.size() < 10 && !update){
				nameList.add(playerName);
				scoreList.add(score);
				update = true;
			}
			
			
			if(update){
				int nameSize = 0;
				
				for(String name:nameList){
					nameSize += (name.length() + 1);
				}
				
				byte[] data = new byte[nameSize + scoreList.size()*4];

				int k = 0;
				for(int i = 0; i < nameList.size();i++){
					for(int j = 0; j < nameList.get(i).length();j++){
						data[k++] = (byte)nameList.get(i).charAt(j);
					}
					data[k++] = ' ';
					byte[] temp = Cast.intToByteArray(scoreList.get(i));
					data[k++] = temp[0];
					data[k++] = temp[1];
					data[k++] = temp[2];
					data[k++] = temp[3];
				}	
				
				PreparedStatement prep = connection.prepareStatement("UPDATE Map SET HighScore = ? WHERE Name = ?;");
				prep.setBytes(1, data);
				prep.setString(2, mapName);
				
				prep.execute();
			}
			
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
		}
		
	}
	/**
	 * Gives back the score pressent in the DataBase
	 * @param mapName	Name of the map
	 * @return			A Scores-object will be returned if successful, null otherwise
	 */
	public Scores getScores(String mapName){
		try{
			refresh();
			PreparedStatement prep = connection.prepareStatement("SELECT HighScore FROM Map WHERE Name == ?");
			prep.setString(1, mapName);
			ResultSet temp = prep.executeQuery();
			Scores res = new Scores();
			if(temp.next()){
				byte[] data = temp.getBytes("HighScore");
				if(data == null)
					data = new byte[0];
				
				
				String name = "";
				int score = 0;
				for(int i = 0; i < data.length;i++){
					if(data[i] != ' ')
						name +=(char) data[i];
					else{
						i++; // skip the ' '
						score = Cast.byteArrayToInt(new byte[] {data[i++],data[i++],data[i++],data[i]});
						res.names.add(name);
						res.scores.add(score);
						name = "";
						score = 0;
					}
				}
			}else{
				System.err.println("No HighScore found!");
				return null;
			}
			
			return res;
			
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
			return null;
		} catch (InvalidByteArraySize e) {
			System.err.println("DataBase: " + e.getMessage());
			return null;
		}
	}
	/**
	 * Gives the map names present in the database
	 * @return	String[] containing the names if successful, null otherwise
	 */
	public String[] getMapNames(){
		
		try{
			refresh();
			Statement statement = connection.createStatement();
			ResultSet temp = statement.executeQuery("SELECT Name FROM Map");
			ArrayList<String> resList = new ArrayList<String>();
			while(temp.next()){
				resList.add(temp.getString("Name"));
			}
			String res[] = new String[resList.size()];
			
			for(int i = 0; i < resList.size();i++){
				res[i] = resList.get(i);
			}
			statement.close();
			return res;
			
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
			return null;
		}
	}
	
}
