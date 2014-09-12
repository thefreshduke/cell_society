package cellsociety_team19;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public abstract class Cell {
	
	protected int myX;
	protected int myY;
	protected int myState;
	protected int myNextState;
	
	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();
	
	protected Cell[][] listOfCellsInGrid;
	
	public Cell(int x, int y, int state) {
		myX = x;
		myY = y;
		
		myState = state;
	}
	
	public Cell() {
		
	}

	public abstract String getDesc();
	
	public abstract Cell[] calculateNeighbors();
	
	public abstract void doAction();
	
	public abstract Cell makeNew(int X, int Y, int theState);
	
	public abstract String toString();
	
	public abstract int getState();
	
	public abstract Color getStateColor();
	
	public abstract void setGrid(Cell[][] listOfCells);

	public abstract void updateCell();
	
}
