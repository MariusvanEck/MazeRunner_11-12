package mazerunner;

public class InvalidSpawnLocationException extends Exception{
	public InvalidSpawnLocationException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;
}
