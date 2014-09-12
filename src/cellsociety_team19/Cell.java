package cellsociety_team19;

public abstract class Cell {
	protected int myX;
	protected int myY;
	
	protected int myState;
	protected int myNextState;
	
	protected Cell[][] listOfCellsInGrid;
	
	public Cell(int x, int y, int state){
		myX = x;
		myY = y;
		
		myState = state;
	}
	
	public Cell(){
		
	}
	
	public abstract void doAction();
	
	public abstract Cell[] calculateNeighbors();
	
	public abstract Cell makeNew(int X, int Y, int theState);
	
	public abstract String toString();
	
	public abstract Cell[][] getGrid(Cell[][] grid);
	
	public int getState(){
		return myState;
	}
	
	public abstract void setGrid(Cell[][] listOfCells);
	
}
