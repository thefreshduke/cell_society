package simulationTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class LifeCell extends Cell {

	protected int nextState = 0;

	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();
	
	private static double EDGE_TYPE;

	public LifeCell(int x, int y, int state) {
		super(x, y, state);

		colorMap.put(0, Color.WHITE);
		colorMap.put(1, Color.GREEN);
		
		EDGE_TYPE = xmlReader.getParameterMap().get("EDGE_TYPE");
	}

	public LifeCell() {

	}

	@Override
	public Cell[] calculateNeighbors() {
		int[] xDelta = {1,-1, 0, 0,-1,-1, 1, 1};
		int[] yDelta = {0, 0,-1, 1, 1,-1,-1, 1};

		Cell[] returnListOfNeighbors = new LifeCell[8];
		
		for (int i = 0; i < returnListOfNeighbors.length; i++) {
			int newX = 0;
			int newY = 0;
			
			int xLength = listOfCellsInGrid[0].length;
			int yLength = listOfCellsInGrid.length;

			//we can eliminate this following if statement by using finite edges as the default
			//toroidal edges (wrap-around) is edge type 1
			//infiniate edges is edge type 2
			if (EDGE_TYPE == 0) {
				newX = myX + xDelta[i];
				newY = myY + yDelta[i];
			}

			if (EDGE_TYPE == 1) {
				newX = (myX + xDelta[i] + xLength) % xLength;
				newY = (myY + yDelta[i] + yLength) % yLength;
			}
			
			try {
				returnListOfNeighbors[i] = listOfCellsInGrid[newX] [newY];
			}
			catch (Exception e) {
				returnListOfNeighbors[i] = null;
			}
		}
		return returnListOfNeighbors;
	}

	public int countNumberOfLiveNeighbors() {
		int counter = 0;
		ArrayList<Cell> listOfNeighbors = removeNullValuesFromListOfNeighbors();
		for (int i = 0; i < listOfNeighbors.size(); i++) {
			if (listOfNeighbors.get(i).myState == 1) {
				counter++;
			}
		}
		return counter;
	}

	public ArrayList<Cell> removeNullValuesFromListOfNeighbors() {
		Cell[] neighbors = calculateNeighbors();
		ArrayList<Cell> goodNeighbors = new ArrayList<Cell>();
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] != null) {
				goodNeighbors.add(neighbors[i]);
			}
		}
		return goodNeighbors; // like State Farm
	}

	@Override
	public void doAction() {
		int numLiveNeighbors = countNumberOfLiveNeighbors();
		
		if (numLiveNeighbors == 3) {
			myNextState = 1;
		}
		else if (numLiveNeighbors == 2) {
			myNextState = myState;
		}
		else {
			myNextState = 0;
		}
	}

	@Override
	public Cell makeNewCell(int cellX, int cellY, int cellState) {
		return new LifeCell(cellX, cellY, cellState);
	}

	@Override
	public String toString() {
		return "Life Cell Simulation";
	}

	@Override
	public void updateCell() {
		myState = myNextState;
	}

	@Override
	public Cell[][] updateGrid() {
		return listOfCellsInGrid;
	}

	@Override
	public String getDesc() {
		return myX + " " + myY + " " + myState;
	}

	@Override
	public Color retrieveCorrespondingColorFromMap() {
		return colorMap.get(myState);
	}

	@Override
	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
	}
}
