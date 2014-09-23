package simulationTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edgeTypes.Edge;
import javafx.scene.paint.Color;

public class TreeCell extends Cell {

	protected static double PROBABILITY_OF_CATCHING_FIRE;
	protected static double EDGE_TYPE;
	protected int nextState = 0;
	protected Edge myEdgeType;


	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

	//<<<<<<< HEAD
	//	public TreeCell(int x, int y, int state, Edge edgeType) {
	//		super(x, y, state, edgeType);
	//=======
	public TreeCell(int x, int y, int state, Map<String,Double> paramMap) {
		super(x, y, state, paramMap);
		//>>>>>>> 84878a9f45fdd55dccd5f6ac0d4178f067eb1d8c

		myNumStates = 3;

		colorMap.put(0, Color.LIGHTGRAY);
		colorMap.put(1, Color.GREEN);
		colorMap.put(2, Color.RED);

		//<<<<<<< HEAD
		//		PROBABILITY_OF_CATCHING_FIRE = xmlReader.getParameterMap().get("PROBABILITY_OF_CATCHING_FIRE");
		//		myEdgeType = edgeType; 
		//=======
		PROBABILITY_OF_CATCHING_FIRE = super.parameterMap.get("PROBABILITY_OF_CATCHING_FIRE");
		EDGE_TYPE = super.parameterMap.get("EDGE_TYPE");
		//>>>>>>> 84878a9f45fdd55dccd5f6ac0d4178f067eb1d8c
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



		List<Cell> neighbors = myEdgeType.calculateNeighbors(myX, myY, listOfCellsInGrid);
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
		public Cell[] calculateNeighbors() {
			int[] xDelta = {-1, 1, 0, 0};
			int[] yDelta = { 0, 0, 1,-1};
	
			Cell[] returnListOfNeighbors = new TreeCell[xDelta.length];
	
			/* this for loop is repeated in all subclasses (subcells) - think about refactoring this */
			for (int i = 0; i < returnListOfNeighbors.length; i++) {
				int newX = 0;
				int newY = 0;
	
				int xLength = listOfCellsInGrid[0].length;
				int yLength = listOfCellsInGrid.length;
	
				//we can eliminate this following if statement by using finite edges as the default
				//toroidal edges (wrap-around) is edge type 1
				//infiniate edges is edge type 2
				if (EDGE_TYPE == 0) {
					newX = myX + xDelta[i];
					newY = myY + yDelta[i];
				}
	
				if (EDGE_TYPE == 1) {
					newX = (myX + xDelta[i] + xLength) % xLength;
					newY = (myY + yDelta[i] + yLength) % yLength;
				}
	
				try {
					returnListOfNeighbors[i] = listOfCellsInGrid[newX] [newY];
				}
				catch (Exception e) {
					returnListOfNeighbors[i] = null;
				}
			}
			return returnListOfNeighbors;
		}

	@Override
	//<<<<<<< HEAD
	//	public TreeCell makeNewCell(int cellX, int cellY, int cellState, Edge edgeType) {
	//		return new TreeCell(cellX, cellY, cellState, edgeType);
	//=======
	public TreeCell makeNewCell(int cellX, int cellY, int cellState, Map<String,Double> paramMap) {
		return new TreeCell(cellX, cellY, cellState, paramMap);
	}

	@Override
	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
		//>>>>>>> 84878a9f45fdd55dccd5f6ac0d4178f067eb1d8c
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

//	@Override
//	public Cell[] calculateNeighbors() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Cell[][] updateGrid() {
		// TODO Auto-generated method stub
		return null;
	}

	//	@Override 
	//	public Cell[][] updateGrid() {
	//		return listOfCellsInGrid;
	//	}
}
