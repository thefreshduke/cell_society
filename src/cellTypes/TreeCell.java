// This entire file is part of my masterpiece.
// Marcus Cain
package cellTypes;

import java.util.List;
import java.util.Map;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * 
 * Subclass of Cell, used when implementing
 * Forest Fire Simulation.
 *
 */
public class TreeCell extends Cell {

    /**
     * Defines the probability that a cell, if it
     * has a neighbor that is on fire, will catch fire
     * next turn.
     * 
     * Read in from XML file and parsed by XMLReader
     */
    protected double PROBABILITY_OF_CATCHING_FIRE;

    final protected static int NO_TREE = 0;
    final protected static int ALIVE = 1;
    final protected static int BURNING = 2;

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
    public TreeCell(int x, int y, int state, IEdgeStrategy edgeStrategy,
            Map<String, Double> parameterMap, Map<Integer, Color> colorMap,
            int[] xDelta, int[] yDelta) {
        super(x, y, state, edgeStrategy, parameterMap, colorMap, xDelta, yDelta);
        setMyNumberOfPatchTypes(3);
        PROBABILITY_OF_CATCHING_FIRE = super.myParameterMap.get("PROBABILITY_OF_CATCHING_FIRE");
    }

    public TreeCell() {
        super();
    }

    /**
     * If a cell's neighbor is burning, it has a certain
     * percent chance of burning as well next turn. If a tree
     * is burning, it goes out next turn.
     */
    @Override
    public void doAction() {

        if (myState != ALIVE) {
            myNextState = NO_TREE;
            return;
        }

        List<Cell> neighbors = super.calculateNeighbors(listOfCellsInGrid, myXDelta, myYDelta);
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i) != null) {
                if (neighbors.get(i).myState == BURNING && Math.random() < PROBABILITY_OF_CATCHING_FIRE) {
                    myNextState = BURNING;
                    break;
                }
                myNextState = myState;
            }
        }
    }
}
