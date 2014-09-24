package simulationTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;

public class LifeCell extends Cell {

	protected int nextState = 0;

	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();
	
	protected int[] myXDelta = {1,-1, 0, 0,1,-1,1,-1};
	protected int[] myYDelta = {0, 0,-1, 1,1,-1,-1,1};
	
	private static double EDGE_TYPE;

	public LifeCell(int x, int y, int state, Map<String,Double> paramMap) {
		super(x, y, state, paramMap);

		colorMap.put(0, Color.WHITE);
		
		myNumPatchTypes = 2;
		colorMap.put(1, Color.GREEN);

		EDGE_TYPE = super.parameterMap.get("EDGE_TYPE");
	}

	public LifeCell() {
		super();
	}

	public int countNumberOfLiveNeighbors() {
		int counter = 0;
		List<Cell> listOfNeighbors = super.calculateNeighbors(listOfCellsInGrid, myXDelta, myYDelta);
		for (int i = 0; i < listOfNeighbors.size(); i++) {
			if (listOfNeighbors.get(i).myState == 1) {
				counter++;
			}
		}
		return counter;
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
	public Cell makeNewCell(int cellX, int cellY, int cellState, Map<String,Double> paramMap) {
		return new LifeCell(cellX, cellY, cellState, paramMap);
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
	public Color getCorrespondingColor() {
		return colorMap.get(myState);
	}

	@Override
	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
	}
}
