package cellTypes;

import java.util.List;

public interface Rules {
	public List<Cell> calculateNeighbors(Cell[][] listOfCells, int[] xDelta, int[] yDelta);
	public abstract void doAction();
}
