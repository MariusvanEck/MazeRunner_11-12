package database;

import java.sql.*;

public class Sample
{
  
	private static boolean doesTableExists(String tableName,Connection conn) throws SQLException{
		DatabaseMetaData dbmd = conn.getMetaData(); 
		ResultSet rs = dbmd.getTables(null, null, tableName, null);
		
		if(rs.next())
			return rs.getRow() == 1;
		
		return false;
	}
	
	
	
public static void main(String[] args) throws ClassNotFoundException
  {
    String createTable = "CREATE TABLE IF NOT EXISTS person" +
    					"(id integer, name string)";
	// load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC");

    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:mazerunner.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      
      if(!doesTableExists("person",connection)){
    	  System.out.println("Creating table");
    	  statement.executeUpdate(createTable);
      }
      System.out.println(doesTableExists("person",connection));
      //statement.executeUpdate("insert into person values(1, 'leo')");
      //statement.executeUpdate("insert into person values(2, 'yui')");

      
      
      ResultSet rs = statement.executeQuery("select * from person");
      while(rs.next())
        System.out.println("id = " + rs.getInt("id") + "\tname = " + rs.getString("name"));
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory", 
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }
}