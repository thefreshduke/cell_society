package cellTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

/**
 *  @author Chris Bernt, Marcus Cain, Scotty Shaw
 *  Cell superclass that implements rules for all cells
 *
 */
public abstract class Cell implements Rules {

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

    public Cell(int x, int y, int state, IEdgeStrategy edgeStrategy, Map<String, Double> parameterMap, Map<Integer, Color> colorMap, int[] xDelta, int[] yDelta) {
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

    //Creates a null Cell, allows us to make a parameterless cell before we know what its states are
    public Cell() {

    }

    /*
     * Implementation of calculateNeighbors from Rules.java
     */
    @Override
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

    public abstract void doAction();

    public void updateCell() {
        myState = myNextState;
    }

    //getters and setters

    public Color getCorrespondingColor() {
        return myColorMap.get(myState);
    }

    public void setGrid(Cell[][] listOfCells) {
        listOfCellsInGrid = listOfCells;
    }

    public void setState(int s) {
        myState = s;
    }

    public int getState() {
        return myState;
    }

    public int getMyNumPatchTypes() {
        return myNumberOfPatchTypes;
    }

    protected void setMyNumberOfPatchTypes(int numberOfPatchTypes) {
        myNumberOfPatchTypes = numberOfPatchTypes;
    }
}