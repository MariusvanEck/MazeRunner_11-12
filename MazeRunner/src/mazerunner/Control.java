package mazerunner;
/**
 * The Control class is an abstract class containing only basic functionality such
 * as getters for all possible commands.
 * <p>
 * An update method is also included, demanding all subclasses to implement it. 
 * This method will be called just before any getters are called. The reason for this
 * is to allow the subclass to set all fields to the most recent input.
 * <p>
 * For the purposes of the assignment, it might seem unnecessary to have the 
 * actual user input in a separate class from this class. Indeed there is no other
 * subclass to Control other than UserInput, but this might change once some sort of
 * AI controlling of GameObjects is desired. Any AI that needs to control an object
 * in the game can use the same methods any human player would use, which makes it a 
 * lot more intuitive to program an AI.
 * 
 * @author Mattijs Driel
 * 
 */
public abstract class Control
{
	protected boolean forward = false;
	protected boolean back = false;
	protected boolean left = false;
	protected boolean right = false;
	protected boolean jump = false;
	protected Integer moveDirection;
	
	protected int dX = 0;
	protected int dY = 0;
	
	/**
	 * Updates the fields of the Control class to represent the
	 * most up-to-date values. 
	 */
	public abstract void update();
}
