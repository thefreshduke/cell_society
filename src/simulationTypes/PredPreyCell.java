package simulationTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;

public class PredPreyCell extends Cell {
	Cell[][] listOfCellsInGrid;

	private Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	private final static int SHARK_BREED_TIME = 10;
	private final static int FISH_BREED_TIME = 3;
	private final static int SHARK_INITIAL_ENERGY = 7;
	private final static int FISH_ENERGY = 1;
	private int sharkEnergy;
	private static int chronons = 0;

	private boolean imminentSharkAttack = false; //just for fish

	public PredPreyCell(int x, int y, int state) {
		super(x, y, state);

		colorMap.put(0, Color.BLUE);
		colorMap.put(1, Color.SALMON);
		colorMap.put(2, Color.GRAY);
		sharkEnergy = SHARK_INITIAL_ENERGY;
		chronons = 0;
	}

	public PredPreyCell() {

	}

	@Override
	public String toString() {
		return "Pred/Prey Simulation";
	}

	public String getFirstAnimalCoords() {
		for (int i = 0; i < listOfCellsInGrid.length; i++) {
			for (int j = 0; j < listOfCellsInGrid[0].length; j++) {
				if (listOfCellsInGrid[i][j].myState != 0) {
					String s = i + " " + j;
					return s;
				}
			}
		}
		return "";
	}

	@Override
	public void doAction() {

		String firstAnimalCoords = getFirstAnimalCoords();

		if ((myX + " " + myY).equals(firstAnimalCoords)) {
			chronons++;
		}

		/*if(myState == 0){
			myNextState = 0;
		}*/

		if (myState == 1) {
			doFishAction();
		}
		if (myState == 2) {
			doSharkAction();
		}
	}

	private void doFishAction() {

		if (imminentSharkAttack) {
			imminentSharkAttack = false;
			return;
		}

		ArrayList<Cell> myNeighbors = removeNullValuesFromListOfNeighbors();
		// Cell[] myNeighbors = calculateNeighbors();

		if (hasAdjacentEmptySpaces()) {
			Random rand = new Random();
			int randChoice = rand.nextInt(myNeighbors.size());
			Cell newCell = myNeighbors.get(randChoice);

			PredPreyCell destinationCell = (PredPreyCell) listOfCellsInGrid[newCell.myX] [newCell.myY];
			destinationCell.myNextState = myState;

			// System.out.println(chronons);
			if (chronons % FISH_BREED_TIME == 0) {
				myNextState = myState;
			}
			else {
				myNextState = 0;
			}
		}
		else {
			myNextState = myState;
		}
	}

	private void doSharkAction() {

		//works except for when a shark is surrounded by 4 other sharks

		if (sharkEnergy == 0) {
			myNextState = 0;
			return;
		}

		//System.out.println(sharkEnergy);
		sharkEnergy--;

		ArrayList<Cell> neighbors = calculateSharkNeighbors();
		if (neighbors.size() > 0) {
			Random r = new Random();
			int choice = r.nextInt(neighbors.size());
			Cell newCell = neighbors.get(choice);
			PredPreyCell destinationCell = (PredPreyCell) listOfCellsInGrid[newCell.myX] [newCell.myY];
			if (newCell.myState == 2 || newCell.myNextState == 2) {
				return;
			}
			if (newCell.myState == 1) { //fish are food, not friends
				sharkEnergy += FISH_ENERGY;			
				destinationCell.imminentSharkAttack = true;
			} 
			destinationCell.myNextState = myState;
			destinationCell.sharkEnergy = sharkEnergy;

			if (chronons % SHARK_BREED_TIME == 0) {
				myNextState = myState;
				sharkEnergy = SHARK_INITIAL_ENERGY;
			}
			else {
				myNextState = 0;
			}
		}
		else {
			myNextState = myState;
		}
	}

	private ArrayList<Cell> calculateSharkNeighbors() {
		int[] xDelta = {-1, 1, 0, 0};
		int[] yDelta = { 0, 0, 1,-1};

		Cell[] returnListOfNeighbors = new PredPreyCell[4];

		for (int i = 0; i < returnListOfNeighbors.length; i++) {
			try {
				Cell destinationCell = listOfCellsInGrid[myX + xDelta[i]] [myY + yDelta[i]];
				if (destinationCell.myState != 2 && destinationCell.myNextState != 2) {
					returnListOfNeighbors[i] = destinationCell;
				}
			}
			catch (Exception e) {
				returnListOfNeighbors[i] = null;
			}
//			int xLength = listOfCellsInGrid[0].length;
//			int yLength = listOfCellsInGrid.length;
//
//			int newX = (myX + xDelta[i] + xLength) % xLength;
//			int newY = (myY + yDelta[i] + yLength) % yLength;
//
//			if (listOfCellsInGrid[newX] [newY].myNextState != 2 && listOfCellsInGrid[newX] [newY].myState != 2) {
//				returnListOfNeighbors[i] = listOfCellsInGrid[newX] [newY];
//			}
		}

		//System.out.println(Arrays.toString(neighbors));

		//create generic neighbors method that creates an arraylist of neighbors, modify it for otherneighbors (specific cases)
		ArrayList<Cell> fishNeighbors = new ArrayList<Cell>();
		for (Cell c : returnListOfNeighbors) {
			if (c != null && c.myState == 1) {
				fishNeighbors.add(c);
			}
		}

		if (fishNeighbors.size() > 0) {
			return fishNeighbors;
		}

		ArrayList<Cell> otherNeighbors = new ArrayList<Cell>();
		for (Cell c : returnListOfNeighbors) {
			if (c != null && c.myState == 0 && c.myNextState == 0) { // Assuming sharks can
				// only move where no
				// other animal is
				// planning to move to
				otherNeighbors.add(c);
			}
		}
		return otherNeighbors;
	}

	private boolean hasAdjacentEmptySpaces() {
		ArrayList<Cell> adjacentNeighbors = removeNullValuesFromListOfNeighbors();
		int space = 0;
		for (int i = 0; i < adjacentNeighbors.size(); i++) {
			if (adjacentNeighbors.get(i).myState == 0 && adjacentNeighbors.get(i).myNextState == 0) {
				space++;
			}
		}
		return (space > 0);
	}

	@Override
	public PredPreyCell makeNewCell(int cellX, int cellY, int cellState) {
		return new PredPreyCell(cellX, cellY, cellState);
	}

	@Override
	public Cell[] calculateFishNeighbors() {
		int[] xDelta = {-1, 1, 0, 0};
		int[] yDelta = { 0, 0, 1,-1};

		Cell[] returnListOfNeighbors = new PredPreyCell[4];

		/* this for loop is repeated in all subclasses (subcells) - think about refactoring this */
		for (int i = 0; i < returnListOfNeighbors.length; i++) {
			try {
				Cell destinationCell = listOfCellsInGrid[myX + xDelta[i]] [myY + yDelta[i]];
				if (destinationCell.myState == 0 && destinationCell.myNextState == 0) {
					returnListOfNeighbors[i] = destinationCell;
				}
			}
			catch (Exception e) {
				returnListOfNeighbors[i] = null;
			}
		}
		return returnListOfNeighbors;
	}

	public ArrayList<Cell> removeNullValuesFromListOfNeighbors() {
		Cell[] neighbors = calculateFishNeighbors();
		ArrayList<Cell> goodNeighbors = new ArrayList<Cell>();
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] != null) {
				goodNeighbors.add(neighbors[i]);
			}
		}
		return goodNeighbors; // like State Farm
	}

	@Override
	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
	}

	@Override
	public void updateCell() {
		myState = myNextState;
	}

	@Override
	public String getDesc() {
		return myX + " " + myY + " " + myState + " " + myNextState;
	}

	@Override
	public Color retrieveCorrespondingColorFromMap() {
		return colorMap.get(myState);
	}

	@Override
	public Cell[][] updateGrid() {
		return listOfCellsInGrid;
	}
}
