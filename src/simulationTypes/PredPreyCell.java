package simulationTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;

public class PredPreyCell extends Cell {
	Cell[][] listOfCellsInGrid;


	private static double SHARK_BREED_TIME;
	private static double FISH_BREED_TIME;
	private static double SHARK_INITIAL_ENERGY;
	private static double FISH_ENERGY;
	private static double EDGE_TYPE;
	private double sharkEnergy;
	private static int chronons = 0;
	

	private boolean imminentSharkAttack = false; //just for fish

	public PredPreyCell(int x, int y, int state, Map<String,Double> paramMap, Map<Integer,Color> colourMap) {
		super(x, y, state, paramMap, colourMap);

		

		SHARK_BREED_TIME = super.parameterMap.get("SHARK_BREED_TIME");
		FISH_BREED_TIME = super.parameterMap.get("FISH_BREED_TIME");
		SHARK_INITIAL_ENERGY = super.parameterMap.get("SHARK_INITIAL_ENERGY");
		FISH_ENERGY = super.parameterMap.get("FISH_ENERGY");
		EDGE_TYPE = super.parameterMap.get("EDGE_TYPE");

		
		sharkEnergy = SHARK_INITIAL_ENERGY;
		chronons = 0;
		
		myNumStates = 3;
	}

	public PredPreyCell() {
		super();
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

		/*if(myState == 0) {
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
				if (listOfCellsInGrid[newX] [newY].myState != 2 && listOfCellsInGrid[newX] [newY].myNextState != 2) {
					returnListOfNeighbors[i] = listOfCellsInGrid[newX] [newY];
				}
			}
			catch (Exception e) {
				returnListOfNeighbors[i] = null;
			}
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
	public PredPreyCell makeNewCell(int cellX, int cellY, int cellState, Map<String,Double> paramMap, Map<Integer,Color> colourMap) {
		return new PredPreyCell(cellX, cellY, cellState, paramMap, colourMap);
	}

	@Override
	public Cell[] calculateNeighbors() {
		int[] xDelta = {-1, 1, 0, 0};
		int[] yDelta = { 0, 0, 1,-1};

		Cell[] returnListOfNeighbors = new PredPreyCell[4];

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
				if (listOfCellsInGrid[newX] [newY].myState == 0 && listOfCellsInGrid[newX] [newY].myNextState == 0) {
					returnListOfNeighbors[i] = listOfCellsInGrid[newX] [newY];
				}
			}
			catch (Exception e) {
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
	public Color getCorrespondingColor() {
		return colorMap.get(myState);
	}

	@Override
	public Cell[][] updateGrid() {
		return listOfCellsInGrid;
	}
}
