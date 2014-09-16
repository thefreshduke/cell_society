package cellsociety_team19;

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
	private final static int SHARK_INITIAL_ENERGY = 10;
	private final static int FISH_ENERGY = 1;
	private int sharkEnergy;
	private int chronons;
	
	public PredPreyCell(int x, int y, int state) {
		super(x, y, state);
		
		colorMap.put(0, Color.BLUE);
		colorMap.put(1, Color.SALMON);
		colorMap.put(2, Color.GRAY);
		
//		chronons = 0; //instance variable or unique to each cell? shark births out of sync with fish births
		//but if instance variable, also able to say %3 => fish give birth and %10 => sharks give birth 
		sharkEnergy = SHARK_INITIAL_ENERGY;
	}
	
	//Q1: does this do anything...?
	public PredPreyCell() {
		super();
	}
	
	@Override
	public String toString() {
		return "Predator/Prey Simulation";
	}

	@Override
	public void doAction() {
		chronons++;
		if (myState == 1) {
			doFishAction();
		}
		if (myState == 2) {
			doSharkAction();
		}
	}

	private void doFishAction() {
//		ArrayList<Cell> myNeighbors = removeNullValuesFromListOfNeighbors();
		Cell[] myNeighbors = calculateNeighbors();
		
		if (hasSpace()) {
			Random rand = new Random();
			int randChoice = rand.nextInt(myNeighbors.length);
			Cell newCell = myNeighbors[randChoice];
			
			PredPreyCell destinationCell = (PredPreyCell) listOfCellsInGrid[newCell.myX][newCell.myY];
			destinationCell.myNextState = myState;
			destinationCell.chronons = this.chronons;
		
			
			if (chronons == 3) {
				myNextState = myState;
				chronons = 0;
			}
			else {
				myNextState = 0;
			}
		}
		else{
			myNextState = myState;
		}
	}
	
	private void doSharkAction() {
		// TODO Auto-generated method stub
		
	}

	private boolean hasSpace() {
		Cell[] adjacentNeighbors = calculateNeighbors();
		int neighbors = 0;
		for(int i = 0; i < adjacentNeighbors.length; i++){
			if(adjacentNeighbors[i].myState != 0){
				neighbors++;
			}
		}
		return neighbors < 4;
	}
	
	@Override
	public PredPreyCell makeNewCell(int cellX, int cellY, int cellState) {
		return new PredPreyCell(cellX, cellY, cellState);
	}

	@Override
	public Cell[] calculateNeighbors() {
		int[] xDelta = {-1, 1, 0, 0};
		int[] yDelta = { 0, 0, 1,-1};
		
		Cell[] returnListOfNeighbors = new PredPreyCell [4];
		
		for (int i = 0; i < returnListOfNeighbors.length; i++) {
			int newX = (myX + xDelta[i] + listOfCellsInGrid[0].length) % listOfCellsInGrid[0].length;
			int newY = (myY + yDelta[i] + listOfCellsInGrid.length) % listOfCellsInGrid.length;
			returnListOfNeighbors[i] = listOfCellsInGrid[newX] [newY];
		}
		return returnListOfNeighbors;
	}
	
//	public ArrayList<Cell> removeNullValuesFromListOfNeighbors() {
//		Cell[] neighbors = calculateNeighbors();
//		ArrayList<Cell> goodNeighbors = new ArrayList<Cell>();
//		for (int i = 0; i < neighbors.length; i++){
//			if(neighbors[i] != null){
//				goodNeighbors.add(neighbors[i]);
//			}
//		}
//		return goodNeighbors; //like State Farm
//	}

	@Override
	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
	}

//	@Override
//	public int getState() {
//		return 0;
//	}

	@Override
	public void updateCell() {
		myState = myNextState;
	}

	@Override
	public String getDesc() {
		return myX + " " + myY + " " + myState + " " + myNextState;
	}

	@Override
	public Color getStateColor() {
		return colorMap.get(myState);
	}
	
	@Override 
	public Cell[][] updateGrid() {
		return listOfCellsInGrid;
	}
}
