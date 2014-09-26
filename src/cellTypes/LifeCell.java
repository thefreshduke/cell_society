package cellTypes;

import java.util.List;
import java.util.Map;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

public class LifeCell extends Cell {

	protected int[] myXDelta = {1,-1, 0, 0, 1,-1, 1,-1};
	protected int[] myYDelta = {0, 0,-1, 1, 1,-1,-1, 1};

	/***
	 * 
	 * @param x - xLocation to describe location of cell on grid. Parsed and passed in my XMLReader.java
	 * @param y - xLocation to describe location of cell on grid. Parsed and passed in my XMLReader.java
	 * @param state - Represents the state of the of cell: 0 = dead/no tree, 1 = tree, 2 = burning tree; A cell has different actions according to what state it is in
	 * @param edgeType  - represents the strategy to consider if edges wrap around the grip, or if edges are finite
	 * @param parameterMap - Map<String,Double>: parameterMap that holds the values of the constants/parameters: Parsed and passed in by XMLReader.java
	 * @param colorMap - Map<Integer,Color>: ColorMap to represent the color of the corresponding state: Defined by the xmlFile; parsed and passed in by XMLReader.java
	 * @param xdel - xLocation deltas to define the neighbors of this cell
	 * @param ydel - yLocation deltas to define the neighbors of this cell
	 */
	public LifeCell(int x, int y, int state, IEdgeStrategy edgeType, Map<String, Double> parameterMap, Map<Integer, Color> colorMap) {
		super(x, y, state, edgeType, parameterMap,colorMap);

		setMyNumberOfPatchTypes(2);
	}

	public LifeCell() {
		super();
	}
	
	/***
	 * 
	 * @return number of neighbors that are alive
	 */
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
	
	/***
	 * doAction() - LifeCell's action based on its current state: SimulationLoop.java calls doAction() method
	 * for the Collection of cells in the grid. This method is called by SimulationLoop.java every time step
	 */
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
	public Cell makeNewCell(int cellX, int cellY, int cellState, IEdgeStrategy cellEdgeType, Map<String, Double> cellParameterMap, Map<Integer, Color> cellColorMap) {
		return new LifeCell(cellX, cellY, cellState, cellEdgeType, cellParameterMap, cellColorMap);
	}
}
