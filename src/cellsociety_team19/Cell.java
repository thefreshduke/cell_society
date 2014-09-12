package cellsociety_team19;

import java.util.HashMap;

import javafx.scene.paint.Color;

public abstract class Cell {
	protected int myX;
	protected int myY;
	
	protected int myState;
	protected int myNextState;
	
	protected HashMap<Color,Integer> stateAssociation = new HashMap<Color,Integer>();
	
	protected Cell[][] listOfCellsInGrid;
	
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
