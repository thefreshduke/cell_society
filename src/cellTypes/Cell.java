package cellTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;

public abstract class Cell {

	//manually setting cells is still a turn behind for some reason...
	//maybe recalculate for specified cell and neighbors when clicked???

	protected int myX;
	protected int myY;
	protected int myState;
	protected int myNextState;
	protected double myEdgeType;
	protected Color myColor;

	protected int[] myXDelta = {1,-1, 0, 0};
	protected int[] myYDelta = {0, 0,-1, 1};

	public int myNumPatchTypes;
	protected Map<String, Double> myParameterMap;
	protected Map<Integer, Color> myColorMap;

	//protected int myPatch; ?

	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	protected Cell[][] listOfCellsInGrid;

	//superclass constructor
	public Cell(int x, int y, int state, int nextState, Map<String,Double> parameterMap, Map<Integer, Color> colorMap) {
		myX = x;
		myY = y;
		myState = state;
		myNextState = nextState;
		myParameterMap = parameterMap;
		myEdgeType = myParameterMap.get("EDGE_TYPE");
		myColorMap = colorMap;
//		myColor = myColorMap.get(state);
	}

	//Creates a null Cell, allows us to make a parameterless cell before we know what its states are
	public Cell() {

	}

	//superclass abstract methods

	public List<Cell> calculateNeighbors(Cell[][] listOfCells, int[] xDelta, int[] yDelta) {
		List<Cell> listOfNeighbors = new ArrayList<Cell>();

		for (int i = 0; i < xDelta.length; i++) {
			int newX = -1;
			int newY = -1;

			int xLength = listOfCells[0].length;
			int yLength = listOfCells.length;

			//we can eliminate this following if statement by using finite edges as the default
			//toroidal edges (wrap-around) is edge type 1
			//infinite edges is edge type 2

			if (myEdgeType == 0) {
				if (myX + xDelta[i] >= 0 && myX + xDelta[i] < xLength) {
					newX = myX + xDelta[i];
				}
				if (myY + yDelta[i] >= 0 && myY + yDelta[i] < yLength) {
					newY = myY + yDelta[i];
				}
			}

			if (myEdgeType == 1) {
				newX = (myX + xDelta[i] + xLength) % xLength;
				newY = (myY + yDelta[i] + yLength) % yLength;
			}

			//			newX = calculateNewCoordinate(myXDelta[i], xLength, myX);
			//			newY = calculateNewCoordinate(myYDelta[i], yLength, myY);

			if (newX != -1 && newY != -1) {
				listOfNeighbors.add(listOfCells[newX] [newY]);
			}
		}
		return listOfNeighbors;
	}

	//	public abstract int calculateNewCoordinate(int delta, int length, int coordinate);//myXDelta[i], xLength, myX);

	public abstract void doAction();

	public abstract Cell createCell(int cellX, int cellY, int cellState, int cellNextState, Map<String, Double> cellParameterMap, Map<Integer, Color> cellColorMap);

	public void updateCell() {
		myState = myNextState;
	}

	//getters and setters

	public abstract Color getCorrespondingColor();
//	public Color getCorrespondingColor() {
//		return colorMap.get(myState);
//	}

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
