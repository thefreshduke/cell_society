package simulationTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;

public abstract class Cell {

	protected int myX;
	protected int myY;
	protected int[] myXDelta = {1,-1, 0, 0};
	protected int[] myYDelta = {0, 0,-1, 1};
	protected int myState;
	protected int myNextState;
	protected double myEdgeType = 0;
	protected String edgeType;

	public int myNumStates;
	protected Map<String, Double> parameterMap;

	//protected int myPatch; ?

	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	protected Cell[][] listOfCellsInGrid;

	//superclass constructor

	public Cell(int x, int y, int state, Map<String, Double> map) {
		myX = x;
		myY = y;
		myState = state;
		parameterMap = map;
	}

	//Creates a null Cell, allows us to make a parameterless cell before we know what its states are
	public Cell() {
		
	}

	//superclass abstract methods

	public List<Cell> calculateNeighbors(Cell[][] listOfCells, int[] xDelta, int[] yDelta) {
		List<Cell> listOfNeighbors = new ArrayList<Cell>();

		for (int i = 0; i < myXDelta.length; i++) {
			int newX = -1;
			int newY = -1;

			int xLength = listOfCells[0].length;
			int yLength = listOfCells.length;

			//we can eliminate this following if statement by using finite edges as the default
			//toroidal edges (wrap-around) is edge type 1
			//infinite edges is edge type 2

			if (myEdgeType == 0) {
				if (myX + myXDelta[i] >= 0 && myX + myXDelta[i] < xLength) {
					newX = myX + myXDelta[i];
				}
				if (myY + myYDelta[i] >= 0 && myY + myYDelta[i] < yLength) {
					newY = myY + myYDelta[i];
				}

			}

			if (myEdgeType == 1) {
				newX = (myX + myXDelta[i] + xLength) % xLength;
				newY = (myY + myYDelta[i] + yLength) % yLength;
			}

			// newX = calculateNewCoordinate(myXDelta[i], xLength, myX);
			// newY = calculateNewCoordinate(myYDelta[i], yLength, myY);

			// if (newX != -1 && newY != -1) {
			if (newX != -1 && newY != -1) {
				listOfNeighbors.add(listOfCells[newX] [newY]);
			}
		}
		return listOfNeighbors;
	}

	//	public abstract int calculateNewCoordinate(int delta, int length, int coordinate);

	public abstract void doAction();

	public abstract Cell makeNewCell(int cellX, int cellY, int cellState, Map<String, Double> map);

	public void updateCell() {
		myState = myNextState;
	}

	//getters and setters

	public Color getCorrespondingColor() {
		return colorMap.get(myState);
	}

	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
	}

	public void setState(int s) {
		myState = s;
	}

	public void setNextState(int s) {
		myNextState = s;
	}

	public int getState() {
		return myState;
	}
}
