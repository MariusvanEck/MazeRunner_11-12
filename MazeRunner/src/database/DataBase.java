package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

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
				System.err.println("DataBase: Closing the connection failed! " + e);
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
	
	public void importMap(String name,String fileLocation){
		try{
			FileInputStream in = new FileInputStream(fileLocation);
			String data = "";
			int bufferSize = in.available(); // estimation of the available bytes to read
			byte[] buffer = new byte[bufferSize];
			int readed = 0,totalReaded = 0;
			while(readed != -1 && in.available() > 0){
				readed = in.read(buffer, totalReaded, bufferSize);
				
				if(readed != -1){
					data += new String(buffer);
					totalReaded = readed;
				}
			}
			in.close();
		
			statement.executeUpdate("INSERT INTO Map(Name,Data) " +
								"VALUES('" + name + "','" + data + "');");
		}catch(IOException e){
			System.err.println("DataBase: " + e.getMessage());
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
		}
	}
	
	
	public int[][] getMap(String name,int lvl){
		if(lvl > 6){
			System.err.println("DataBase: wrong lvl has to be less then 5");
			return null;
		}
		try{
			ResultSet temp = statement.executeQuery("SELECT * FROM Map");
			
			if(temp.next()){
				System.out.println(temp.getInt("ID") + " " + temp.getString("Name"));
			}
			
			
			
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
}
