package cellsociety_team19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;

public class PredPreyCell extends Cell {
	Cell[][] listOfCellsInGrid;

	private Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	private final static int SHARK_BREED_TIME = 8;
	private final static int FISH_BREED_TIME = 3;
	private final static int SHARK_INITIAL_ENERGY = 5;
	private final static int FISH_ENERGY = 2;
	private int sharkEnergy;
	private static int chronons = 0;

	private boolean imminentSharkAttack = false; //just for fish

	public PredPreyCell(int x, int y, int state) {
		super(x, y, state);

		colorMap.put(0, Color.BLUE);
		colorMap.put(1, Color.SALMON);
		colorMap.put(2, Color.GRAY);

		// chronons = 0; //instance variable or unique to each cell? shark
		// births out of sync with fish births
		// but if instance variable, also able to say %3 => fish give birth and
		// %10 => sharks give birth
		sharkEnergy = SHARK_INITIAL_ENERGY;
	}

	// Q1: does this do anything...?
	public PredPreyCell() {
		super();
	}

	@Override
	public String toString() {
		return "Predator/Prey Simulation";
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

		Cell[] neighbors = new PredPreyCell[4];

		for (int i = 0; i < neighbors.length; i++) {
			int xLength = listOfCellsInGrid[0].length;
			int yLength = listOfCellsInGrid.length;

			int newX = (myX + xDelta[i] + xLength) % xLength;
			int newY = (myY + yDelta[i] + yLength) % yLength;

			if (listOfCellsInGrid[newX] [newY].myNextState != 2) {
				neighbors[i] = listOfCellsInGrid[newX] [newY];
			}
		}

		//System.out.println(Arrays.toString(neighbors));

		ArrayList<Cell> fishNeighbors = new ArrayList<Cell>();
		for (Cell c : neighbors) {
			if (c != null && c.myState == 1) {
				fishNeighbors.add(c);
			}
		}

		if (fishNeighbors.size() > 0) {
			return fishNeighbors;
		}

		ArrayList<Cell> otherNeighbors = new ArrayList<Cell>();
		for (Cell c : neighbors) {
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
			if (adjacentNeighbors.get(i).myState == 0) {
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
	public Cell[] calculateNeighbors() {
		int[] xDelta = {-1, 1, 0, 0};
		int[] yDelta = { 0, 0, 1,-1};

		Cell[] returnListOfNeighbors = new PredPreyCell[4];

		for (int i = 0; i < returnListOfNeighbors.length; i++) {
			int xLength = listOfCellsInGrid[0].length;
			int yLength = listOfCellsInGrid.length;

			int newX = (myX + xDelta[i] + xLength) % xLength;
			int newY = (myY + yDelta[i] + yLength) % yLength;

			if (listOfCellsInGrid[newX][newY].myNextState == 0) {
				returnListOfNeighbors[i] = listOfCellsInGrid[newX] [newY];
			}
			else {
				returnListOfNeighbors[i] = null;
			}
		}
		return returnListOfNeighbors;
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
