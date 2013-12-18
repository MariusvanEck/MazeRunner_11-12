package database;

import java.sql.SQLException;

import cast.InvalidByteArraySize;

public class DataBaseTest
{
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InvalidByteArraySize
	{
		DataBase dataBase = new DataBase();
		dataBase.getMap("test");
	}
}