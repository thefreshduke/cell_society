package cellTypes;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class TreeCell extends Cell {

	protected static double PROBABILITY_OF_CATCHING_FIRE;

	public TreeCell(int x, int y, int state, Map<String, Double> map, Map<Integer, Color> m) {
		super(x, y, state, map, m);

		myNumPatchTypes = 3;


		PROBABILITY_OF_CATCHING_FIRE = super.parameterMap.get("PROBABILITY_OF_CATCHING_FIRE");
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
	public TreeCell makeNewCell(int cellX, int cellY, int cellState, Map<String, Double> map, Map<Integer, Color> m) {
		return new TreeCell(cellX, cellY, cellState, map, m);
	}
}
