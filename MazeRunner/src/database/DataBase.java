package database;

import java.sql.*;

public class DataBase {
	private Connection connection = null;
	
	public DataBase(){
		try {
			setup();
		} catch (ClassNotFoundException e) {
			System.err.println("Somthing went wrong, couldn't initialize DataBase correctly: " + e);
		}
	}
	
	
	protected void setup() throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		
		try{
			// Create DataBase connection
			connection = DriverManager.getConnection("jdbs:sqlite:mazerunner.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // timeout to 30 sec
			
			
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
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
				System.err.println("Closing the connection failed! " + e);
			}
	}
	
	protected void finalize() throws Throwable{
		try{
			this.cleanUp();
		}finally{
			super.finalize();
		}
	}
}
