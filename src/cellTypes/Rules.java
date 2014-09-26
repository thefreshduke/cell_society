package cellTypes;

import java.util.List;

public interface Rules {
    /*
     * Specify calculateNeighbors() and doAction() for all Cells 
     */
    public List<Cell> calculateNeighbors(Cell[][] listOfCells, int[] xDelta, int[] yDelta);
    public abstract void doAction();
}