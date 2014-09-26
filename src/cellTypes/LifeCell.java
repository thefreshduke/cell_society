package cellTypes;

import java.util.List;
import java.util.Map;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

/**
 *  @author Chris Bernt, Marcus Cain, Scotty Shaw
 *  Subclass of Cell, used when implementing 
 *  Conway's Game Of Life.
 *
 */
public class LifeCell extends Cell {

	protected final static int DEAD = 0;
	protected final static int ALIVE = 1;
	
	/***
	 * 
	 * @param x - xLocation to describe location of cell on grid. Parsed and passed in my XMLReader.java
	 * @param y - xLocation to describe location of cell on grid. Parsed and passed in my XMLReader.java
	 * @param state - Represents the state of the of cell; A cell has different actions according to what state it is in
	 * @param edgeType  - represents the strategy to consider if edges wrap around the grip, or if edges are finite
	 * @param parameterMap - Map<String,Double>: parameterMap that holds the values of the constants/parameters: Parsed and passed in by XMLReader.java
	 * @param colorMap - Map<Integer,Color>: ColorMap to represent the color of the corresponding state: Defined by the xmlFile; parsed and passed in by XMLReader.java
	 * @param xdel - xLocation deltas to define the neighbors of this cell
	 * @param ydel - yLocation deltas to define the neighbors of this cell
	 */
	public LifeCell(int x, int y, int state, IEdgeStrategy edgeStrategy, Map<String, Double> parameterMap, Map<Integer, Color> colorMap, int[] xDelta, int[] yDelta) {
		super(x, y, state, edgeStrategy, parameterMap,colorMap, xDelta, yDelta);
		setMyNumberOfPatchTypes(2);
	}

	public LifeCell() {
		super();
	}

	/***
	 * 
	 * @return Number of neighbors that are alive in a cell's neighborhood.
	 */
	public int countNumberOfLiveNeighbors() {
		int counter = 0;
		List<Cell> listOfNeighbors = super.calculateNeighbors(listOfCellsInGrid, myXDelta, myYDelta);
		for (int i = 0; i < listOfNeighbors.size(); i++) {
			if (listOfNeighbors.get(i).myState == ALIVE) {
				counter++;
			}
		}
		return counter;
	}

	
	/***
	 * Overrides Cell's doAction. If a cell has 3 live neighbors, it comes to life.
	 * If a cell has 2 live neighbors, it stays alive. If it has 1 or fewer,
	 * or more than 3, it dies.
	 */
	@Override
	public void doAction() {
		int numLiveNeighbors = countNumberOfLiveNeighbors();

		if (numLiveNeighbors == 3) {
			myNextState = ALIVE;
		}
		else if (numLiveNeighbors == 2) {
			myNextState = myState;
		}
		else {
			myNextState = DEAD;
		}
	}
}
