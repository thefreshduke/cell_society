package cellsociety_team19;

public class TreeCell extends Cell{
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
	public Cell[] getNeighbors() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public TreeCell makeNew(int X, int Y, int State){
		return new TreeCell(X, Y, State);
	}
}
