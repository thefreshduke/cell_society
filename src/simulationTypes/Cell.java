package simulationTypes;

import java.util.HashMap;
import java.util.Map;

import cellsociety_team19.XMLReader;
import javafx.scene.paint.Color;

public abstract class Cell {

	protected int myX;
	protected int myY;
	protected int myState;
	protected int myNextState;

	public int myNumStates;
	protected Map<String,Double> parameterMap;

	//protected int myPatch; ?

	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	protected Cell[][] listOfCellsInGrid;

	//superclass constructor

	public Cell(int x, int y, int state, Map<String,Double> paramMap) {
		myX = x;
		myY = y;
		myState = state;
		parameterMap = paramMap;
	}

	//Creates a null Cell, allows us to make a parameterless cell before we know what its states are
	public Cell() {
		myState = 0;
		myX = 0;
		myY = 0;
	}

	//superclass abstract methods

	public abstract Cell[] calculateNeighbors();

	public abstract void doAction();

	public abstract Cell makeNewCell(int cellX, int cellY, int cellState, Map<String,Double> paramMap);

	public abstract String toString();

	public abstract void updateCell();

	public abstract Cell[][] updateGrid();

	//getters and setters

	public abstract String getDesc();

	public abstract Color retrieveCorrespondingColorFromMap();

	public abstract void setGrid(Cell[][] listOfCells);
	
	public void setState(int s){
		myState = s;
	}
	public void setNextState(int s){
		myNextState = s;
	}
	public int getState(){
		return myState;
	}
	
	
}
