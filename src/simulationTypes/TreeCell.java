package simulationTypes;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class TreeCell extends Cell {

	protected static double PROBABILITY_OF_CATCHING_FIRE;
	protected static double EDGE_TYPE;
	protected int nextState = 0;

	
	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	public TreeCell(int x, int y, int state, Map<String,Double> paramMap) {
		super(x, y, state, paramMap);

		myNumPatchTypes = 3;
		
		colorMap.put(0, Color.LIGHTGRAY);
		colorMap.put(1, Color.GREEN);
		colorMap.put(2, Color.RED);

		PROBABILITY_OF_CATCHING_FIRE = super.parameterMap.get("PROBABILITY_OF_CATCHING_FIRE");
		EDGE_TYPE = super.parameterMap.get("EDGE_TYPE");
	}

	public TreeCell() {
		super();
	}

	@Override
	/**
	 * this method does something awesome. 
	 */
	public void doAction() {

		//0 - dead tree or no tree
		//1 - tree
		//2 - burning tree

		if (myState != 1) {
			nextState = 0;
			return;
		}

		List<Cell> neighbors = super.calculateNeighbors(listOfCellsInGrid, myXDelta, myYDelta);
		for (int i = 0; i < neighbors.size(); i++) {
			if (neighbors.get(i) != null) {
				if (neighbors.get(i).myState == 2 && Math.random() < PROBABILITY_OF_CATCHING_FIRE) {
					nextState = 2;
					break;
				}
				nextState = myState;
			}
		}
	}

	@Override
	public TreeCell makeNewCell(int cellX, int cellY, int cellState, Map<String,Double> paramMap) {
		return new TreeCell(cellX, cellY, cellState, paramMap);
	}

	@Override
	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
	}

	@Override
	public void updateCell() {
		myState = nextState;
	}

	@Override
	public String toString() {
		return "Tree Fire Simulation";
	}

	@Override
	public Color getCorrespondingColor() {
		return colorMap.get(myState);
	}

	@Override
	public String getDesc() {
		return myX + " " + myY + " " + myState;
	}

	@Override 
	public Cell[][] updateGrid() {
		return listOfCellsInGrid;
	}
}
