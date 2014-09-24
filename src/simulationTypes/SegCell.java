package simulationTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;

public class SegCell extends Cell {
	Cell[][] listOfCellsInGrid;

	protected static double THRESHOLD_OF_HAPPINESS;

	private static double EDGE_TYPE;

	protected HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>();

	public SegCell(int x, int y, int state, Map<String,Double> paramMap) {
		super(x, y, state, paramMap);
		colorMap.put(0, Color.WHITE);
		colorMap.put(1, Color.RED);
		colorMap.put(2, Color.BLUE);
		colorMap.put(3, Color.GREEN);
		colorMap.put(4, Color.YELLOW);

		 
		myNumPatchTypes = 5;
		colorMap.put(5, Color.PURPLE);
		colorMap.put(6, Color.ORANGE);

		THRESHOLD_OF_HAPPINESS = super.parameterMap.get("THRESHOLD_OF_HAPPINESS");
		EDGE_TYPE = super.parameterMap.get("EDGE_TYPE");
	}

	public SegCell() {
		super();
	}

	@Override
	public void doAction() {

		if (myState == 0) {
			return;
		}
		//state 0 is no one there
		//states 1-limit is type of agent

		/*for(int i = 0; i < listOfCellsInGrid.length; i++){
			for(int j = 0; j < listOfCellsInGrid[i].length; j++){
				System.out.println(listOfCellsInGrid[i][j].getDesc());
			}
		}
		System.out.println("==========");
		 */

		/* calculate neighbors */
		List<Cell> myNeighbors = calculateNeighbors(listOfCellsInGrid, myXDelta, myYDelta);

		/*determine if neighbor is satisfied*/
		if (isSatisfied(myNeighbors)) {
			//System.out.println("This cell was satisfied " + this.getDesc());
			myNextState = myState;
		}
		else {
			List<Cell> openCells = emptyCellsAvailable();

			if (openCells.size() > 0) {
				Random rand = new Random();
				int randChoice = rand.nextInt(openCells.size());
				Cell newCell = openCells.get(randChoice);
				if (myState != 0) {
					SegCell moveCell = new SegCell(newCell.myX, newCell.myY, 0, super.parameterMap);
					moveCell.myNextState = myState;

					listOfCellsInGrid[newCell.myX] [newCell.myY] = moveCell;
					myNextState = 0;
				}
			}
			else {
				myNextState = myState;
			}
		}
	}

	/*method used to determine what cells are currently empty*/
	private ArrayList<Cell> emptyCellsAvailable() {
		//assuming 0 is nobody there
		ArrayList<Cell> returnListOfAvailableCells = new ArrayList<Cell>();

		for (int i = 0; i < listOfCellsInGrid.length; i++) {
			for (int j = 0; j < listOfCellsInGrid[i].length; j++) {
				Cell cellToCheck = listOfCellsInGrid[i][j];
				if (cellToCheck.myState == 0 && cellToCheck.myNextState == 0) {
					returnListOfAvailableCells.add(cellToCheck);
				}
			}
		}
		return returnListOfAvailableCells;
	}

	private boolean isSatisfied(List<Cell> neighbors) {
		int counter = 0;
		for (int i = 0; i < neighbors.size(); i++) {
			if (neighbors.get(i) != null && neighbors.get(i).myState != 0) {
				counter++;
			}
		}

		/* loop through neighbors and determine is current cell is satisfied */
		double numNeighborsWithSameState = 0;
		for (int i = 0; i < neighbors.size(); i++) {
			if (neighbors.get(i) != null && neighbors.get(i).myState == myState) {
				numNeighborsWithSameState++;
			}
		}
		return (numNeighborsWithSameState >= (THRESHOLD_OF_HAPPINESS * counter));
	}


	@Override
	public SegCell makeNewCell(int cellX, int cellY, int cellState, Map<String,Double> paramMap) {
		return new SegCell(cellX, cellY, cellState,paramMap);
	}

	@Override
	public String toString() {
		return "Seg Cell Simulation";
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
		return null;
	}
}
