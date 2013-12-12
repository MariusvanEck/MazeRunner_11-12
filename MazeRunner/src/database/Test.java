package database;

import java.sql.*;

public class Test {
	// JDBC driver name and database URL
	 static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	 static final String DB_URL = "";
	
	 //  Database credentials
	 static final String USER = "username";
	 static final String PASS = "password";
	
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try{
		   //STEP 2: Register JDBC driver
		   Class.forName("com.mysql.jdbc.Driver");
	    //STEP 3: Open a connection
		   System.out.println("Connecting to database...");
		   conn = DriverManager.getConnection(DB_URL, USER, PASS);

		    //STEP 4: Execute a query
	    	System.out.println("Creating database...");
	    	stmt = conn.createStatement();
		    
		    String sql = "CREATE DATABASE STUDENTS";
		    stmt.executeUpdate(sql);
		    System.out.println("Database created successfully...");
		 }catch(SQLException se){
		    se.printStackTrace();
		 }catch(Exception e){
		    e.printStackTrace();
		 }finally{
		    try{
		       if(stmt!=null)
		          stmt.close();
		    }catch(SQLException se2){
		    }
		    try{
		       if(conn!=null)
		          conn.close();
		    }catch(SQLException se){
		       se.printStackTrace();
		    }
		 }
		 System.out.println("Goodbye!");
	}

}
