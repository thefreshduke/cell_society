package cellTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

public class SegCell extends Cell {

	private int[] myXDelta = {1,-1, 0, 0, 1,-1, 1,-1};
	protected int[] myYDelta = {0, 0,-1, 1, 1,-1,-1, 1};

	protected static double THRESHOLD_OF_HAPPINESS;

	public SegCell(int x, int y, int state, IEdgeStrategy edgeType, Map<String, Double> parameterMap, Map<Integer, Color> colorMap) {
		super(x, y, state, edgeType, parameterMap, colorMap);
		setMyNumberOfPatchTypes(5);
		THRESHOLD_OF_HAPPINESS = super.myParameterMap.get("THRESHOLD_OF_HAPPINESS");
	}

	public SegCell() {
		super();
	}

	@Override
	public void doAction() {

		if (myState == 0) {
			return;
		}
		
		/* calculate neighbors */
		List<Cell> myNeighbors = super.calculateNeighbors(listOfCellsInGrid, myXDelta, myYDelta);
		//		System.out.println(myNeighbors.size());

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
					SegCell moveCell = new SegCell(newCell.myX, newCell.myY, 0, super.myEdgeType, super.myParameterMap, super.myColorMap);
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
	public SegCell makeNewCell(int cellX, int cellY, int cellState, IEdgeStrategy cellEdgeType, Map<String, Double> cellParameterMap, Map<Integer, Color> cellColorMap) {
		return new SegCell(cellX, cellY, cellState, cellEdgeType, cellParameterMap, cellColorMap);
	}
}
