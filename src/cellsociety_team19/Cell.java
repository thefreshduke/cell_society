package cellsociety_team19;

public abstract class Cell {
	protected int myX;
	protected int myY;
	
	protected int myState;
	protected int myNextState;
	
	public Cell(int x, int y, int state){
		myX = x;
		myY = y;
		
		myState = state;
	}
	
	public Cell(){
		
	}
	
	public abstract void doAction();
	
	public abstract String toString();
	
	public abstract Cell[] calculateNeighbors();
	
	
	
	
	public int getState(){
		return myState;
	}
	
	public abstract Cell makeNew(int X, int Y, int theState);
	
	public abstract void setGrid(Cell[][] listOfCells);
	
}
