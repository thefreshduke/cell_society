package cellsociety_team19;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public abstract class Cell {
	
	protected int myX;
	protected int myY;
	protected int myState;
	protected int myNextState;
	
	//protected int myPatch; ?
	
	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();
	
	protected Cell[][] listOfCellsInGrid;
	
	//superclass constructor
	
	public Cell(int x, int y, int state) {
		myX = x;
		myY = y;
		
		myState = state;
	}
	
	//Q1: does this do anything...?
	public Cell() {
		
	}
	
	//superclass abstract methods
	
	public abstract Cell[] calculateNeighbors();
	
	public abstract void doAction();
	
	public abstract Cell makeNewCell(int cellX, int cellY, int cellState);
	
	public abstract String toString();

	public abstract void updateCell();
	
	public abstract Cell[][] updateGrid();
	
	//getters and setters
	
	public abstract String getDesc();
	
//	public abstract int getState();
	
	public abstract Color getStateColor();
	
	public abstract void setGrid(Cell[][] listOfCells);
}
