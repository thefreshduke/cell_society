package cellsociety_team19;

public class PredPreyCell extends Cell{
	public PredPreyCell(int x, int y, int state) {
		super(x, y, state);
		// TODO Auto-generated constructor stub
	}
	
	public PredPreyCell(){
		super();
	}
	
	@Override
	public String toString(){
		return "Predator/Prey Simulation";
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public PredPreyCell makeNew(int X, int Y, int State){
		return new PredPreyCell(X, Y, State);
	}

	@Override
	public Cell[] calculateNeighbors() {
		// TODO Auto-generated method stub
		int[] rDelta = {-1, 1, 0, 0};
		int[] cDelta = { 0, 0, 1,-1};
		
		Cell[] returnListOfNeighbors = new Cell [4];
		
		
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
	public void setGrid(Cell[][] listOfCells) {
		// TODO Auto-generated method stub
		
	}
}
