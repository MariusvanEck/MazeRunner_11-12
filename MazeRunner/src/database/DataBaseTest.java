package database;

import java.sql.SQLException;

import cast.Cast;
import cast.InvalidByteArraySize;

public class DataBaseTest
{
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InvalidByteArraySize
	{
		DataBase dataBase = new DataBase();
		int[][] lvl = dataBase.getMap("test",0);
		
		for(int z = 0; z < 20; z++){
			for(int x = 0; x < 20; x++){
				System.out.print(lvl[x][z]);
				System.out.print(' ');
			}
			System.out.print('\n');
		}
		
	}
}