package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import cast.Cast;
import cast.InvalidByteArraySize;

public class DataBase {
	private Connection connection = null;
	private Statement statement = null;
		
	public DataBase(){
		try {
			setup();
		} catch (ClassNotFoundException e) {
			System.err.println("DataBase: Somthing went wrong, couldn't initialize DataBase correctly: " + e);
		}
	}
	
	
	protected void setup() throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		
		try{
			// Create DataBase connection
			connection = DriverManager.getConnection("jdbc:sqlite:mazerunner.db");
			statement = connection.createStatement();
			statement.setQueryTimeout(30); // timeout to 30 sec
			
			if(!doesTableExists("Map",connection)){
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS Map(ID INTEGER PRIMARY KEY AUTOINCREMENT,Name TINYTEXT," + // create table with an ID, Name (max 255 byte)
											"Data BLOB,lvl0 LONGBLOB,lvl1 LONGBLOB,lvl2 LONGBLOB,lvl3 LONGBLOB, lvl4 LONGBLOB, lvl5 LONGBLOB);"); //  and Data (max 4GB per lvl)
				statement.executeUpdate("CREATE INDEX IF NOT EXISTS ID ON Map(ID);"); // create index for table Map for faster search
				
				
				// TODO:adds the default lvl's to the database if the database was empty
				
			}
		}
		catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
		}
	}
    
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
	
	@Override
	protected void finalize() throws Throwable{
		try{
			this.cleanUp();
		}finally{
			super.finalize();
		}
	}
	
	
	public boolean addMap(String name,byte[][] lvlData){
		try{
			if(lvlData.length-1 > 6 || lvlData.length == 0){ // the length of the first array
				System.err.println("DataBase: Invalid array size is: " + lvlData.length + " max size is 6");
				return false;
			}
			PreparedStatement prep = connection.prepareStatement("INSERT INTO Map(Name,Data,lvl0,lvl1,lvl2,lvl3,lvl4,lvl5)" +
																"VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
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

			prep.execute();
			return true;
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
			return false;
		}
	}
	
	
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
	
	public int[][] getMap(String name,int lvl){
		if(lvl > 6){
			System.err.println("DataBase: wrong lvl has to be less then 5");
			return null;
		}
		try{
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
						i+=4;
					}
				}
				
				
				
				return res;
			}
			else{
				System.err.println("DataBase: rs is not open!\n\tSomething wrong with SQL statement?");
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
	
	public byte[] getMap(int ID){
		
		try{
			PreparedStatement prep = connection.prepareStatement("SELECT Data " +
																"FROM Map " +
																"WHERE Map.ID = ?");
			prep.setInt(1, ID);
			
			ResultSet rs = prep.executeQuery();
			
			
			if(rs.next()){
				return rs.getBytes("Data");
			}
			else{
				System.err.println("DataBase: rs is not open!\n\tSomething wrong with SQL statement?");
				return null;
			}
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
			return null;
		}
	}
	
	private boolean doesTableExists(String tableName,Connection conn) throws SQLException{
		DatabaseMetaData dbmd = conn.getMetaData(); 
		ResultSet rs = dbmd.getTables(null, null, tableName, null);
		
		if(rs.next())
			return rs.getRow() == 1;
		
		return false;
	}
	private boolean doesMapNameExists(String name) throws SQLException{
		ResultSet temp = statement.executeQuery("SELECT * FROM Map WHERE Name = '" + name + "';");
		if(temp.next())
			return true;
		return false;
	}
	
	public int getNumLevels(String name){
		try {
			ResultSet temp = statement.executeQuery("SELECT Data FROM Map WHERE Name = '" + name + "';");
			if(temp.next()){
				byte[] b = temp.getBytes("Data");
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
	public int getMazeSize(String name){
		try {
			ResultSet temp = statement.executeQuery("SELECT Data FROM Map WHERE Name = '" + name + "';");
			if(temp.next()){
				byte[] b = temp.getBytes("Data");
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
}
