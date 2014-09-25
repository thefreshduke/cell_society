package cellTypes;

import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;

public class TreeCell extends Cell {

	protected static double PROBABILITY_OF_CATCHING_FIRE;

	public TreeCell(int x, int y, int state, int nextState, Map<String, Double> parameterMap, Map<Integer, String> colorMap) {
		super(x, y, state, nextState, parameterMap, colorMap);

		myNumPatchTypes = 3;

//		colorMap.put(0, Color.LIGHTGRAY);
//		colorMap.put(1, Color.GREEN);
//		colorMap.put(2, Color.RED);

		PROBABILITY_OF_CATCHING_FIRE = super.myParameterMap.get("PROBABILITY_OF_CATCHING_FIRE");
	}

	public TreeCell() {
		super();
	}

	@Override
	public void doAction() {

		//0 - dead tree or no tree
		//1 - tree
		//2 - burning tree

		if (myState != 1) {
			myNextState = 0;
			return;
		}

		List<Cell> neighbors = super.calculateNeighbors(listOfCellsInGrid, myXDelta, myYDelta);
		for (int i = 0; i < neighbors.size(); i++) {
			if (neighbors.get(i) != null) {
				if (neighbors.get(i).myState == 2 && Math.random() < PROBABILITY_OF_CATCHING_FIRE) {
					myNextState = 2;
					break;
				}
				myNextState = myState;
			}
		}
	}

	@Override
	public Cell createCell(int cellX, int cellY, int cellState, int cellNextState, Map<String, Double> cellParameterMap, Map<Integer, String> cellColorMap) {
		return new TreeCell(cellX, cellY, cellState, cellNextState, cellParameterMap, cellColorMap);
	}
}
