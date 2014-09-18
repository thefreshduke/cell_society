package simulationTypes;

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

	//Creates a null Cell
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

	public abstract Color retrieveCorrespondingColorFromMap();

	public abstract void setGrid(Cell[][] listOfCells);
}
