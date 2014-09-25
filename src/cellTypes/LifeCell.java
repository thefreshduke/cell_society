package cellTypes;

import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;

public class LifeCell extends Cell {

	protected int[] myXDelta = {1,-1, 0, 0, 1,-1, 1,-1};
	protected int[] myYDelta = {0, 0,-1, 1, 1,-1,-1, 1};

	public LifeCell(int x, int y, int state, int nextState, Map<String, Double> parameterMap, Map<Integer, String> colorMap) {
		super(x, y, state, nextState, parameterMap, colorMap);

//		colorMap.put(0, Color.WHITE);

		myNumPatchTypes = 2;
//		colorMap.put(1, Color.GREEN);
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
	public Cell createCell(int cellX, int cellY, int cellState, int cellNextState, Map<String, Double> cellParameterMap, Map<Integer, String> cellColorMap) {
		return new LifeCell(cellX, cellY, cellState, cellNextState, cellParameterMap, cellColorMap);
	}
}
