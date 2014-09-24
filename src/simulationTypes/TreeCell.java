package simulationTypes;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class TreeCell extends Cell {

	protected static double PROBABILITY_OF_CATCHING_FIRE;
	protected static double EDGE_TYPE;
	protected int nextState = 0;

	
	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	public TreeCell(int x, int y, int state, Map<String,Double> paramMap) {
		super(x, y, state, paramMap);

		myNumStates = 3;
		
		colorMap.put(0, Color.LIGHTGRAY);
		colorMap.put(1, Color.GREEN);
		colorMap.put(2, Color.RED);

		PROBABILITY_OF_CATCHING_FIRE = super.parameterMap.get("PROBABILITY_OF_CATCHING_FIRE");
		EDGE_TYPE = super.parameterMap.get("EDGE_TYPE");
	}

	public TreeCell() {
		super();
	}

	@Override
	/**
	 * this method does something awesome. 
	 */
	public void doAction() {

		//0 - dead tree or no tree
		//1 - tree
		//2 - burning tree

		if (myState != 1) {
			nextState = 0;
			return;
		}

		Cell[] neighbors = calculateNeighbors();
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] != null) {
				if (neighbors[i].myState == 2 && Math.random() < PROBABILITY_OF_CATCHING_FIRE) {
					nextState = 2;
					break;
				}
				nextState = myState;
			}
		}
	}

	@Override
	public Cell[] calculateNeighbors() {
		int[] xDelta = {-1, 1, 0, 0};
		int[] yDelta = { 0, 0, 1,-1};

		Cell[] returnListOfNeighbors = new TreeCell[4];

		/* this for loop is repeated in all subclasses (subcells) - think about refactoring this */
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

	@Override
	public TreeCell makeNewCell(int cellX, int cellY, int cellState, Map<String,Double> paramMap) {
		return new TreeCell(cellX, cellY, cellState, paramMap);
	}

	@Override
	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
	}

	@Override
	public void updateCell() {
		myState = nextState;
	}

	@Override
	public String toString() {
		return "Tree Fire Simulation";
	}

	@Override
	public Color getCorrespondingColor() {
		return colorMap.get(myState);
	}

	@Override
	public String getDesc() {
		return myX + " " + myY + " " + myState;
	}

	@Override 
	public Cell[][] updateGrid() {
		return listOfCellsInGrid;
	}
}
