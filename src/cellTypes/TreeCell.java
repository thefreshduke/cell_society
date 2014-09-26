package cellTypes;

import java.util.List;
import java.util.Map;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

public class TreeCell extends Cell {

	protected double PROBABILITY_OF_CATCHING_FIRE;

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
	 * @param PROBABILITY_OF_CATCHING_FIRE - Each Sub Cell Type gets their parameters from the Super Class Cell (XMLReader.java parses these parameters and passes them in)
	 */
	public TreeCell(int x, int y, int state, IEdgeStrategy edgeType, Map<String, Double> parameterMap, Map<Integer, Color> colorMap) {
		super(x, y, state, edgeType, parameterMap, colorMap);

		setMyNumberOfPatchTypes(3);

		PROBABILITY_OF_CATCHING_FIRE = super.myParameterMap.get("PROBABILITY_OF_CATCHING_FIRE");
	}

	public TreeCell() {
		super();
	}

	/***
	 * doAction() - TreeCell's action based on its current state: SimulationLoop.java calls doAction() method
	 * for the Collection of cells in the grid. This method is called by SimulationLoop.java every time step
	 */
	@Override
	public void doAction() {

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
	public TreeCell makeNewCell(int cellX, int cellY, int cellState, IEdgeStrategy cellEdgeType, Map<String, Double> cellparamterMap, Map<Integer, Color> cellColorMap) {
		return new TreeCell(cellX, cellY, cellState, cellEdgeType, cellparamterMap, cellColorMap);
	}
}
