package cellsociety_team19;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class TreeCell extends Cell {

	protected final static double PROBABILITY_OF_CATCHING_FIRE = 0.6;
	protected int nextState = 0;

	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	public TreeCell(int x, int y, int state) {
		super(x, y, state);

		colorMap.put(0, Color.LIGHTGRAY);
		colorMap.put(1, Color.GREEN);
		colorMap.put(2, Color.RED);
	}

	public TreeCell() {

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
			try {
				returnListOfNeighbors[i] = listOfCellsInGrid[myX + xDelta[i]] [myY + yDelta[i]];
			}
			catch (Exception e) {
				returnListOfNeighbors[i] = null;
			}
		}
		return returnListOfNeighbors;
	}

	@Override
	public TreeCell makeNewCell(int cellX, int cellY, int cellState) {
		return new TreeCell(cellX, cellY, cellState);
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
		return "Forest Fire Simulation";
	}

	@Override
	public Color retrieveCorrespondingColorFromMap() {
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
