package cellsociety_team19;

public class TreeCell extends Cell{
	stateAssociation = new HashMap<Color,Integer>();
	public TreeCell(int x, int y, int state) {
		super(x, y, state);
		// TODO Auto-generated constructor stub
	}
	
	public TreeCell(){
		super();
	}
	
	@Override
	public String toString(){
		return "Forest Fire Simulation";
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Cell[] calculateNeighbors() {
		// TODO Auto-generated method stub
		int[] rDelta = {-1, 1, 0, 0};
		int[] cDelta = { 0, 0, 1,-1};
		
		Cell[] returnListOfNeighbors = new Cell [4];
		
		/* this for loop is repated in all subclasses (subcells) - think about refactoring this */
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
	public TreeCell makeNew(int X, int Y, int State){
		return new TreeCell(X, Y, State);
	}

	@Override
	public void setGrid(Cell[][] listOfCells) {
		listOfCellsInGrid = listOfCells;
		
	}
	
	
}
