package cellsociety_team19;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class TreeCell extends Cell{
	
	protected final static double PROBABILITY_OF_CATCHING_FIRE = 0.6;
	protected int nextState = 0;
	
	protected Map<Integer, Color> colorMap = new HashMap<Integer, Color>();
	public TreeCell(int x, int y, int state) {
		super(x, y, state);
		colorMap.put(0, Color.LIGHTGRAY);
		colorMap.put(1, Color.GREEN);
		colorMap.put(2, Color.RED);
	}
	
	public TreeCell(){
		super();
	}
	
	@Override
	public String toString(){
		return "Forest Fire Simulation";
	}
	
	@Override
	public Color getStateColor(){
		return colorMap.get(myState);
	}

	@Override
	public void doAction() {
		
		//0 - dead tree or no tree
		//1 - tree
		//2 - burning tree
		
		
		if (myState == 0) {
			nextState = 0;
			return;
		}
		
		if(myState == 2){
			nextState = 0;
			return;
		}
		
		
		Cell[] neighbors = calculateNeighbors();
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] != null && neighbors[i].myState == 2) {
				if(Math.random() < PROBABILITY_OF_CATCHING_FIRE){
					nextState = 2;
					break;
				}
				else{
					nextState = 1;
				}
			}
			else{
				nextState = myState;
			}
		}
	}
	
	@Override
	public void updateCell(){
		myState = nextState;
	}
	
	@Override
	public Cell[] calculateNeighbors() {
		int[] rDelta = {-1, 1, 0, 0};
		int[] cDelta = { 0, 0, 1,-1};
		
		Cell[] returnListOfNeighbors = new TreeCell [4];
		
		/* this for loop is repeated in all subclasses (subcells) - think about refactoring this */
		for(int i = 0;i<returnListOfNeighbors.length;i++){
			try{
				returnListOfNeighbors[i] = listOfCellsInGrid[myX + rDelta[i]][ myY + cDelta[i]];
			}
			catch(Exception e){
				returnListOfNeighbors[i] = null;
			}
		}
		return returnListOfNeighbors;
	}
	
	@Override
	public TreeCell makeNew(int X, int Y, int State){
		return new TreeCell(X, Y, State);
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
	public String getDesc(){
		return "" + myX + " " + myY + " " + myState;
	}
}
