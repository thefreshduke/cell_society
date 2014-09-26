package cellTypes;

import java.util.List;

/**
 *  @author Chris Bernt, Marcus Cain, Scotty Shaw
 *  Superinterface Rules for the Rules hierarchy allows future Rules sets to be implemented
 *  Each cell calls upon these rules to evaluate and act on its environment.
 */
public interface Rules {
    /*
     * Specify calculateNeighbors() and doAction() for all Cells 
     * This method will allow 
     */
    public List<Cell> calculateNeighbors(Cell[][] listOfCells, int[] xDelta, int[] yDelta);
    public abstract void doAction();
}