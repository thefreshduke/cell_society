package cellTypes;

import java.util.Map;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

public class PredPreyCell extends Cell {

	protected double SHARK_BREED_TIME;
	protected double FISH_BREED_TIME;
	protected double SHARK_INITIAL_ENERGY;
	protected double FISH_ENERGY;
	protected double sharkEnergy;
	protected int chronons = 0;

	protected boolean imminentSharkAttack = false; //just for fish

	public PredPreyCell(int x, int y, int state, IEdgeStrategy edgeStrategy, Map<String, Double> parameterMap, Map<Integer, Color> colorMap, int[] xDelta, int[] yDelta) {
		super(x, y, state, edgeStrategy, parameterMap, colorMap, xDelta, yDelta);

		SHARK_BREED_TIME = super.myParameterMap.get("SHARK_BREED_TIME");
		FISH_BREED_TIME = super.myParameterMap.get("FISH_BREED_TIME");
		SHARK_INITIAL_ENERGY = super.myParameterMap.get("SHARK_INITIAL_ENERGY");
		FISH_ENERGY = super.myParameterMap.get("FISH_ENERGY");

		sharkEnergy = SHARK_INITIAL_ENERGY;
		chronons = 0;

		setMyNumberOfPatchTypes(3);
	}

	public PredPreyCell() {
		super();
	}

	public String getFirstAnimalCoords() {
		for (int i = 0; i < listOfCellsInGrid.length; i++) {
			for (int j = 0; j < listOfCellsInGrid[0].length; j++) {
				if (listOfCellsInGrid[i][j].myState != 0) {
					String s = i + " " + j;
					return s;
				}
			}
		}
		return "";
	}

	@Override
	public void doAction() {
		String firstAnimalCoords = getFirstAnimalCoords();
		if ((myX + " " + myY).equals(firstAnimalCoords)) {
			chronons++;
		}
		
		PredPreyCell[] rules = new PredPreyCell[] {new FishRules(), new SharkRules()};
		
		rules[myState-1].doAction();
	}

//	private void doFishAction() {
//		if (imminentSharkAttack) {
//			imminentSharkAttack = false;
//			return;
//		}
//
//		List<Cell> myNeighbors = calculateNeighbors();
//
//		if (hasAdjacentEmptySpaces()) {
//			Random rand = new Random();
//			int randChoice = rand.nextInt(myNeighbors.size());
//			Cell newCell = myNeighbors.get(randChoice);
//
//			PredPreyCell destinationCell = (PredPreyCell) listOfCellsInGrid[newCell.myX] [newCell.myY];
//			destinationCell.myNextState = myState;
//
//			if (chronons % FISH_BREED_TIME == 0) {
//				myNextState = myState;
//			}
//			else {
//				myNextState = 0;
//			}
//		}
//		else {
//			myNextState = myState;
//		}
//	}

//	private void doSharkAction() {
//		if (sharkEnergy == 0) {
//			myNextState = 0;
//			return;
//		}
//		sharkEnergy--;
//
//		ArrayList<Cell> neighbors = calculateSharkNeighbors();
//		if (neighbors.size() > 0) {
//			Random r = new Random();
//			int choice = r.nextInt(neighbors.size());
//			Cell newCell = neighbors.get(choice);
//			PredPreyCell destinationCell = (PredPreyCell) listOfCellsInGrid[newCell.myX] [newCell.myY];
//			if (newCell.myState == 2 || newCell.myNextState == 2) {
//				return;
//			}
//			if (newCell.myState == 1) { //fish are food, not friends
//				sharkEnergy += FISH_ENERGY;			
//				destinationCell.imminentSharkAttack = true;
//			} 
//			destinationCell.myNextState = myState;
//			destinationCell.sharkEnergy = sharkEnergy;
//
//			if (chronons % SHARK_BREED_TIME == 0) {
//				myNextState = myState;
//				sharkEnergy = SHARK_INITIAL_ENERGY;
//			}
//			else {
//				myNextState = 0;
//			}
//		}
//		else {
//			myNextState = myState;
//		}
//	}

//	private ArrayList<Cell> calculateSharkNeighbors() {
//		List<Cell> superNeighbors = super.calculateNeighbors(listOfCellsInGrid, myXDelta, myYDelta);
//		List<Cell> returnListOfNeighbors = new ArrayList<Cell>();
//		for(Cell c : superNeighbors){
//			if (c.myState != 2 && c.myNextState != 2) {
//				returnListOfNeighbors.add(c);
//			}
//		}
//		//create generic neighbors method that creates an arraylist of neighbors, modify it for otherneighbors (specific cases)
//		ArrayList<Cell> fishNeighbors = new ArrayList<Cell>();
//		for (Cell c : returnListOfNeighbors) {
//			if (c != null && c.myState == 1) {
//				fishNeighbors.add(c);
//			}
//		}
//
//		if (fishNeighbors.size() > 0) {
//			return fishNeighbors;
//		}
//
//		ArrayList<Cell> otherNeighbors = new ArrayList<Cell>();
//		for (Cell c : returnListOfNeighbors) {
//			if (c != null && c.myState == 0 && c.myNextState == 0) {
//				otherNeighbors.add(c);
//			}
//		}
//		return otherNeighbors;
//	}

//	private boolean hasAdjacentEmptySpaces() {
//		List<Cell> adjacentNeighbors = calculateNeighbors();
//		int space = 0;
//		for (int i = 0; i < adjacentNeighbors.size(); i++) {
//			if (adjacentNeighbors.get(i).myState == 0 && adjacentNeighbors.get(i).myNextState == 0) {
//				space++;
//			}
//		}
//		return (space > 0);
//	}

//	public List<Cell> calculateNeighbors() {
//		List<Cell> superNeighbors = super.calculateNeighbors(listOfCellsInGrid, myXDelta, myYDelta);
//		List<Cell> returnListOfNeighbors = new ArrayList<Cell>();
//		for(Cell c : superNeighbors){
//			if (c.myState == 0 && c.myNextState == 0) {
//				returnListOfNeighbors.add(c);
//			}
//		}
//		return returnListOfNeighbors;
//	}
}
