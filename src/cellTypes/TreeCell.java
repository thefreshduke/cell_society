package cellTypes;

import java.util.List;
import java.util.Map;

import edgeTypes.Edge;
import javafx.scene.paint.Color;

public class TreeCell extends Cell {

	protected static double PROBABILITY_OF_CATCHING_FIRE;

	public TreeCell(int x, int y, int state, Edge edgeType, Map<String, Double> parameterMap, Map<Integer, Color> colorMap) {
		super(x, y, state, edgeType, parameterMap, colorMap);

		myNumPatchTypes = 3;

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
	public TreeCell makeNewCell(int cellX, int cellY, int cellState, Edge cellEdgeType, Map<String, Double> cellparamterMap, Map<Integer, Color> cellColorMap) {
		return new TreeCell(cellX, cellY, cellState, cellEdgeType, cellparamterMap, cellColorMap);
	}
}
