package cellsociety_team19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;

public class SegCell extends Cell{
	Cell[][] listOfCellsInGrid;
	
	private final static double THRESHOLD_OF_HAPPINESS = .4;
	
	private HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>();

	
	public SegCell(int x, int y, int state){
		super(x, y, state);
		colorMap.put(0,  Color.WHITE);
		colorMap.put(1, Color.RED);
		colorMap.put(2, Color.BLUE);
		colorMap.put(3, Color.GREEN);
		colorMap.put(4, Color.YELLOW);
		colorMap.put(5, Color.PURPLE);
		colorMap.put(6, Color.ORANGE);
	}
	
	public SegCell() {
		
	}
	
	@Override
	public void doAction() {
		
		//state 0 is no one there
		//states 1-limit is type of agent
		
		
		
		/* calculate neighbors */
		Cell[] myNeighbors = calculateNeighbors();
		
		/*determine if neighbor is satisfied*/
		if(isSatisfied(myNeighbors)){
			//do something here
			myNextState = myState;

		}
		else{
			List<Cell> openCells = emptyCellsAvailable();
			
			
			if(openCells.size() == 0) {
				return;
			}
			
			for(Cell c: openCells){
				System.out.println(c.getDesc());
			}
			Random rand = new Random();
			int randChoice = rand.nextInt(openCells.size());
			Cell newCell = openCells.get(randChoice);
			if(myState != 0){
				listOfCellsInGrid[newCell.myX][newCell.myY].myNextState = myState;
							
				myNextState = 0;
			}
			
		}
	}
	
	/*method used to determine what cells are currently empty*/
	private ArrayList<Cell> emptyCellsAvailable(){
		//assuming 0 is nobody there
		ArrayList<Cell> returnListOfAvailableCells = new ArrayList<Cell>();
		
		for(int i = 0; i <listOfCellsInGrid.length;i++){
			for(int j=0;j<listOfCellsInGrid[i].length;j++){
				if(listOfCellsInGrid[i][j].getState() == 0 && listOfCellsInGrid[i][j].myNextState == 0){
					returnListOfAvailableCells.add(listOfCellsInGrid[i][j]);
				}
			}
		}
		return returnListOfAvailableCells;
	}
	
	private boolean isSatisfied(Cell[] neighbors){
		if(myState == 0){
			return true;
		}
		/* loop through neighbors and determine is current cell is satisfied */
		double numNeighborsWithSameState = 0;
		for(int i = 0; i <neighbors.length;i++){
			if(neighbors[i] != null && neighbors[i].getState() == myState){
				numNeighborsWithSameState ++;
			}
		}
		
		return numNeighborsWithSameState >= (THRESHOLD_OF_HAPPINESS*neighbors.length);
	}

	@Override
	public Cell[] calculateNeighbors() {
		int[] rDelta = {-1, 1, 0, 0,-1, 1,-1,-1};
		int[] cDelta = { 0, 0, 1,-1, 1, 1,-1, 1};
		
		Cell[] returnListOfNeighbors = new SegCell [8];
		
		
		for(int i = 0; i < returnListOfNeighbors.length; i++) {
			try {
				returnListOfNeighbors[i] = listOfCellsInGrid[myX + rDelta[i]][myY + cDelta[i]];
			}
			catch(Exception e) {
				returnListOfNeighbors[i] = null;
			}
		}
		
		return returnListOfNeighbors;
	}

	@Override
	public SegCell makeNew(int X, int Y, int theState) {
		return new SegCell(X, Y, theState);
	}
	
	@Override
	public String toString(){
		return "Segregation Simulation";
	}

	@Override
	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
	}
	
	@Override
	public int getState() {
		return myState;
	}

	@Override
	public void updateCell() {
		myState = myNextState;
	}

	@Override
	public String getDesc() {
		return "" + myX + " " + myY + " " + myState;
	}

	@Override
	public Color getStateColor() {
		return colorMap.get(myState);
	}
	
	@Override 
	public Cell[][] updateGrid(){
		return listOfCellsInGrid;
	}
}
