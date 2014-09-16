package cellsociety_team19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class LifeCell extends Cell {

	protected int nextState = 0;

	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	public LifeCell(int x, int y, int state) {
		super(x, y, state);

		colorMap.put(0, Color.WHITE);
		colorMap.put(1, Color.GREEN);
	}

	//Q1: does this do anything...?
	public LifeCell() {
		super();
	}

	@Override
	public Cell[] calculateNeighbors() {
		int[] xDelta = {1,-1, 0, 0,-1,-1, 1, 1};
		int[] yDelta = {0, 0,-1, 1, 1,-1,-1, 1};

		Cell[] returnListOfNeighbors = new LifeCell[8];

//		if (myState == 1) {
//		System.out.println("myX: " + myX + ", myY: " + myY);
		
		for (int i = 0; i < returnListOfNeighbors.length; i++) {
			try {
//				System.out.println("X: " + (myX + xDelta[i]) + ", Y: " + (myY + yDelta[i]));
//				System.out.println(xDelta[i]);
//				System.out.println(yDelta[i]);
//				System.out.println(myX + xDelta[i]);
//				System.out.println(myY + yDelta[i]);
//				System.out.println("myState: " + listOfCellsInGrid[myX + xDelta[i]] [myY + yDelta[i]].myState);
				returnListOfNeighbors[i] = listOfCellsInGrid[myX + xDelta[i]] [myY + yDelta[i]];
			}
			catch (Exception e) {
				returnListOfNeighbors[i] = null;
			}
		}
//		System.out.println("============");
//		}
		return returnListOfNeighbors;
	}

	public int countNumberOfLiveNeighbors() {
		int counter = 0;
//		Cell[] listOfNeighbors = calculateNeighbors();
		ArrayList<Cell> listOfNeighbors = removeNullValuesFromListOfNeighbors();
//		System.out.println(listOfNeighbors.toString());
		for (int i = 0; i < listOfNeighbors.size(); i++) {
//			System.out.println(listOfNeighbors[i].myState);
//			System.out.println(i);
//			if (listOfNeighbors[i] != null) {
//				System.out.println("wut1");
				if (listOfNeighbors.get(i).myState == 1) {
					counter++;
//					System.out.println("wut2");
				}
//			}
		}
//		if (myState == 1) {
//		System.out.println(myX + ", " + myY);
//		for (int i = 0; i < listOfNeighbors.size(); i++) {
//			System.out.println(listOfNeighbors.get(i).myX + ", " + listOfNeighbors.get(i).myY + ": " + listOfNeighbors.get(i).myState);
//		}
//		System.out.println("counter: " + counter);
//		System.out.println("=============");
//		System.out.println("wut");
//		}
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

//		if (myState == 1) {
//			System.out.println("Live: " + numLiveNeighbors);
//		}
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
		return "Game of Life Simulation";
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
