package cellTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * 
 * Subclass of Cell, used when implementing the
 * Segregation Simulation.
 *
 */
public class SegCell extends Cell {
    /***
     * Constant Parameter used to define the behavior of the game.
     * The percentage of a cell's non-empty neighbors have to be
     * the same state as itself to be happy. 
     */
    protected static double THRESHOLD_OF_HAPPINESS;

    protected static final int EMPTY = 0;

    /***
     * 
     * @param x - xLocation to describe location of cell on grid. Parsed and passed in my XMLReader.java
     * @param y - xLocation to describe location of cell on grid. Parsed and passed in my XMLReader.java
     * @param state - Represents the state of the of cell; A cell has different actions according to what state it is in
     * @param edgeType  - represents the strategy to consider if edges wrap around the grip, or if edges are finite
     * @param parametermap - Map<String,Double>: parameterMap that holds the values of the constants/parameters: Parsed and passed in by XMLReader.java
     * @param colorMap - Map<Integer,Color>: ColorMap to represent the color of the corresponding state: Defined by the xmlFile; parsed and passed in by XMLReader.java
     * @param xdel - xLocation deltas to define the neighbors of this cell
     * @param ydel - yLocation deltas to define the neighbors of this cell
     */
    protected SegCell(int x, int y, int state, IEdgeStrategy edgeStrategy,
            Map<String, Double> parameterMap, Map<Integer, Color> colorMap,
            int[] xDelta, int[] yDelta) {
        super(x, y, state, edgeStrategy, parameterMap, colorMap, xDelta, yDelta);
        THRESHOLD_OF_HAPPINESS = super.myParameterMap.get("THRESHOLD_OF_HAPPINESS");
    }

    protected SegCell() {
        super();
    }

    /***
     * If a cell is unhappy, it moves to random empty location on the grid
     * providing that there are empty spaces. 
     */
    @Override
    public void doAction() {

        if (myState == EMPTY) {
            return;
        }

        /* calculate neighbors */
        List<Cell> myNeighbors = super.calculateNeighbors(listOfCellsInGrid,
                myXDelta, myYDelta);

        /*determine if neighbor is satisfied*/
        if (isSatisfied(myNeighbors)) {
            myNextState = myState;
        }
        else {
            List<Cell> openCells = findEmptyGridCells();

            if (openCells.size() > 0) {
                int randChoice = r.nextInt(openCells.size());
                Cell newCell = openCells.get(randChoice);
                SegCell moveCell = new SegCell(newCell.myX, newCell.myY, 0,
                        super.myEdgeType, super.myParameterMap,
                        super.myColorMap, super.myXDelta, super.myYDelta);
                moveCell.myNextState = myState;

                listOfCellsInGrid[newCell.myX] [newCell.myY] = moveCell;
                myNextState = EMPTY;
            }
            else {
                myNextState = myState;
            }
        }
    }

    /**
     * Finds all of the empty cells in the grid.
     * This encompasses not having a cell there now
     * and no cell planning on occupying it next turn.
     * 
     * @return List of empty cells in the grid.
     */
    private List<Cell> findEmptyGridCells() {
        //assuming 0 is nobody there
        List<Cell> returnListOfAvailableCells = new ArrayList<Cell>();

        for (int i = 0; i < listOfCellsInGrid.length; i++) {
            for (int j = 0; j < listOfCellsInGrid[i].length; j++) {
                Cell cellToCheck = listOfCellsInGrid[i][j];
                if (cellToCheck.myState == EMPTY && cellToCheck.myNextState == EMPTY) {
                    returnListOfAvailableCells.add(cellToCheck);
                }
            }
        }
        return returnListOfAvailableCells;
    }

    /***
     * Checks to see whether the cell
     * is satisfied.
     * 
     * @param The list of neighbors of a cell.
     * @return Whether the cell is satisfied.
     */
    private boolean isSatisfied(List<Cell> neighbors) {
        int counter = 0;
        int numNeighborsWithSameState = 0;
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i) != null && neighbors.get(i).myState != EMPTY) {
                counter++;
            }
            if (neighbors.get(i) != null && neighbors.get(i).myState == myState) {
                numNeighborsWithSameState++;
            }
        }
        return (numNeighborsWithSameState >= (THRESHOLD_OF_HAPPINESS * counter));
    }
}
