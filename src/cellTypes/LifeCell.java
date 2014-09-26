package cellTypes;

import java.util.List;
import java.util.Map;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

public class LifeCell extends Cell {

	protected int[] myXDelta = {1,-1, 0, 0, 1,-1, 1,-1};
	protected int[] myYDelta = {0, 0,-1, 1, 1,-1,-1, 1};

	public LifeCell(int x, int y, int state, IEdgeStrategy edgeStrategy, Map<String, Double> parameterMap, Map<Integer, Color> colorMap) {
		super(x, y, state, edgeStrategy, parameterMap,colorMap);

		setMyNumberOfPatchTypes(2);
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
	public Cell makeNewCell(int cellX, int cellY, int cellState, IEdgeStrategy cellEdgeStrategy, Map<String, Double> cellParameterMap, Map<Integer, Color> cellColorMap) {
		return new LifeCell(cellX, cellY, cellState, cellEdgeStrategy, cellParameterMap, cellColorMap);
	}
}
