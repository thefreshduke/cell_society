package cellsociety_team19;

import javafx.scene.paint.Color;

public class SegCell extends Cell{
	Cell[][] listOfCellsInGrid;
	
	private final double PROB_HAPPY = .50;
	
	public SegCell(int x, int y, int state){
		super(x, y, state);
		
		
	}
	
	public SegCell(){
		
	}
	
	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
		/* calculate neighbors */
		Cell[] myNeighbors = calculateNeighbors();
		
		/*determine if neighbor is satified*/
		if(isSatisfied(myNeighbors)){
			//do something here
		}
	}
	
	private boolean isSatisfied(Cell[] neighbors){
		/* loop through neighbors and determine is current cell is satified */
		double numNeighborsWithSameState = 0;
		for(int i = 0; i <neighbors.length;i++){
			if(neighbors[i] != null && neighbors[i].getState() == myState){
				numNeighborsWithSameState ++;
			}
		}
		
		return numNeighborsWithSameState > (PROB_HAPPY*neighbors.length);
	}

	@Override
	public Cell[] calculateNeighbors() {
		// TODO Auto-generated method stub
		int[] rDelta = {-1, 1, 0, 0,-1, 1,-1,-1};
		int[] cDelta = { 0, 0, 1,-1, 1, 1,-1, 1};
		
		Cell[] returnListOfNeighbors = new SegCell [8];
		
		
		for(int i =0;i<returnListOfNeighbors.length;i++){
			try{
				returnListOfNeighbors[i] = listOfCellsInGrid[myX + rDelta[i]][ myY + cDelta[i]];
			}
			catch(Exception e ){
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
		// TODO Auto-generated method stub
		listOfCellsInGrid = listOfCells;
	}
	
	@Override
	public int getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateCell() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getStateColor() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
