package cellTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * 
 * The superclass for the Objects that will populate
 * the simulation grid. We chose to make Cell an abstract
 * superclass and not a rules handler since we figured
 * the cells are themselves doing their own actions,
 * as if they were people. Each has its own rules associated
 * with its variety of states. 
 * 
 * This class contains variables for all of the subclass cells,
 * including myX, myY, number of possibles patches, myState, myNextState,
 * etc. 
 *
 */
public abstract class Cell {

    //manually setting cells is still a turn behind for some reason...
    //maybe recalculate for specified cell and neighbors when clicked???

    protected int myX;
    protected int myY;
    protected int myNumberOfPatchTypes;
    protected int myState;
    protected int myNextState;

    protected int[] myXDelta;
    protected int[] myYDelta;
    protected IEdgeStrategy myEdgeType;

    protected Map<String, Double> myParameterMap;
    protected Map<Integer, Color> myColorMap;

    protected Random r;

    //protected int myPatch; ?

    protected Cell[][] listOfCellsInGrid;

    //superclass constructor

    public Cell(int x, int y, int state, IEdgeStrategy edgeStrategy,
            Map<String, Double> parameterMap,
            Map<Integer, Color> colorMap, int[] xDelta, int[] yDelta) {
        myX = x;
        myY = y;
        myNumberOfPatchTypes = colorMap.size();
        myState = state;
        myParameterMap = parameterMap;
        myEdgeType = edgeStrategy;
        myColorMap = colorMap;
        myXDelta = xDelta;
        myYDelta = yDelta;
        r = new Random();
    }

    /**
     * This constructor simply creates an empty version of a Cell. 
     * This was necessary for us since in the CellFactory class
     * we populate a Map with new, empty instances of all
     * subclasses, so we can later use those classes to
     * create objects of the same type with specified parameters.
     * 
     */
    public Cell() {

    }


    /**
     * @param listOfCells The Cell grid.
     * @param xDelta List of X coordinate changes regarding where a cell's neighbors are.
     * @param yDelta List of Y coordinate changes regarding where a cell's neighbors are.
     * @return List of Cells representing a Cell's neighborhood.
     * 
     * Every subclass in some manner needs to consider its neighbors
     * in order to make a decision about what it should do. We therefore
     * put this general method in the superclass since every subclass uses it
     * in exactly the same way.
     */
    public List<Cell> calculateNeighbors(Cell[][] listOfCells, int[] xDelta, int[] yDelta) {
        List<Cell> listOfNeighbors = new ArrayList<Cell>();

        for (int i = 0; i < xDelta.length; i++) {
            int xLength = listOfCells[0].length;
            int yLength = listOfCells.length;

            int newX = myEdgeType.calculateNewCoordinate(myX, xDelta[i], xLength);
            int newY = myEdgeType.calculateNewCoordinate(myY, yDelta[i], yLength);

            if (newX != -1 && newY != -1) {
                listOfNeighbors.add(listOfCells[newX] [newY]);
            }
        }
        return listOfNeighbors;
    }


    /**
     * The key to the whole simulation process. Every subclass
     * must do an action every time step in some way, but in 
     * different ways, hence why this method is abstract.
     */
    public abstract void doAction();


    /**
     * A cell updates its current state
     * to be what its next state used to be.
     * Same for all subclasses.
     */
    public final void updateCell() {
        myState = myNextState;
    }

    /**
     * Get a cell's color
     */
    public final Color getCorrespondingColor() {
        return myColorMap.get(myState);
    }

    public final void setGrid(Cell[][] listOfCells) {
        listOfCellsInGrid = listOfCells;
    }

    public final void setState(int s) {
        myState = s;
    }

    public final int getState() {
        return myState;
    }

    public final int getMyNumPatchTypes() {
        return myNumberOfPatchTypes;
    }

    protected final void setMyNumberOfPatchTypes(int numberOfPatchTypes) {
        myNumberOfPatchTypes = numberOfPatchTypes;
    }
}
