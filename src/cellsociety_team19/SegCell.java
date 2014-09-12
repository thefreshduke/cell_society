package cellsociety_team19;

public class SegCell extends Cell{

	public SegCell(int x, int y, int state){
		super(x, y, state);
	}
	
	public SegCell(){
		
	}
	
	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Cell[] calculateNeighbors() {
		// TODO Auto-generated method stub
		return null;
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
		
	}
	
}
