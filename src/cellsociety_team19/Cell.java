package cellsociety_team19;

public abstract class Cell {
	private int myX;
	private int myY;
	
	private int myState;
	
	public Cell(int x, int y, int state){
		myX = x;
		myY = y;
		
		myState = state;
	}
	
	public abstract void doAction();
	
	public abstract Cell[] getNeighbors();
	
	public int getState(){
		return myState;
	}
	
}
