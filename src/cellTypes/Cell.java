package cellTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edgeTypes.Edge;
import edgeTypes.FiniteEdge;
import edgeTypes.ToroidalEdge;
import javafx.scene.paint.Color;

public abstract class Cell {

	//manually setting cells is still a turn behind for some reason...
	//maybe recalculate for specified cell and neighbors when clicked???

	protected int myX;
	protected int myY;
	protected int myState;
	protected int myNextState;

	protected int[] myXDelta = {1,-1, 0, 0};
	protected int[] myYDelta = {0, 0,-1, 1};
	protected Edge myEdgeType;

	public int myNumPatchTypes;
	protected Map<String, Double> myParameterMap;
	protected Map<Integer, Color> myColorMap;
	
	Edge[] edges = {new FiniteEdge(), new ToroidalEdge()};

	//protected int myPatch; ?

	protected Cell[][] listOfCellsInGrid;

	//superclass constructor

	public Cell(int x, int y, int state, Edge edgeType, Map<String, Double> parameterMap, Map<Integer, Color> colorMap) {
		myX = x;
		myY = y;
		myState = state;
		myParameterMap = parameterMap;
		myEdgeType = edgeType;
		myColorMap = colorMap;
	}

	//Creates a null Cell, allows us to make a parameterless cell before we know what its states are
	public Cell() {

	}

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
			
			newX = myEdgeType.calculateNewCoordinate(myX, xDelta[i], xLength);
			newY = myEdgeType.calculateNewCoordinate(myY, yDelta[i], yLength);
			
//			if (myEdgeType == 0) {
//				newX = calculateFiniteNewCoordinate(myX, xDelta[i], xLength);
//				newY = calculateFiniteNewCoordinate(myY, yDelta[i], yLength);
//			}
//
//			if (myEdgeType == 1) {
//				newX = calculateToroidalNewCoordinate(myX, xDelta[i], xLength);
//				newY = calculateToroidalNewCoordinate(myY, yDelta[i], yLength);
//			}

			if (newX != -1 && newY != -1) {
				listOfNeighbors.add(listOfCells[newX] [newY]);
			}
		}
		return listOfNeighbors;
	}

//	public int calculateFiniteNewCoordinate(int coordinate, int delta, int length) {
//		if (coordinate + delta >= 0 && coordinate + delta < length) {
//			System.out.println("finite");
//			return -1;
//		}
//		return (coordinate + delta);
//	}
//	
//	public int calculateToroidalNewCoordinate(int coordinate, int delta, int length) {
//		return (coordinate + delta + length) % length;
//	}

	public abstract void doAction();

	public abstract Cell makeNewCell(int cellX, int cellY, int cellState, Edge edgeType, Map<String, Double> cellParameterMap, Map<Integer, Color> cellColorMap);

	public void updateCell() {
		myState = myNextState;
	}

	//getters and setters

	public Color getCorrespondingColor() {
		return myColorMap.get(myState);
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
