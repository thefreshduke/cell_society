package cellTypes;

//This entire file is part of my masterpiece.
//SCOTTY SHAW

import java.util.Map;

import edgeTypes.IEdgeStrategy;
import javafx.scene.paint.Color;

/**
 *  @author Chris Bernt, Marcus Cain, Scotty Shaw
 *  Subclass of Cell, used when implementing 
 *  WaTor World.
 *
 */
public class PredPreyCell extends Cell {

    protected double SHARK_BREED_TIME;
    protected double FISH_BREED_TIME;
    protected double SHARK_INITIAL_ENERGY;
    protected double FISH_ENERGY;
    protected double sharkEnergy;
    protected int chronons = 0;

    protected boolean imminentSharkAttack = false; //just for fish

    protected PredPreyCell(int x, int y, int state, IEdgeStrategy edgeStrategy, Map<String, Double> parameterMap, Map<Integer, Color> colorMap, int[] xDelta, int[] yDelta) {
        super(x, y, state, edgeStrategy, parameterMap, colorMap, xDelta, yDelta);

        SHARK_BREED_TIME = super.myParameterMap.get("SHARK_BREED_TIME");
        FISH_BREED_TIME = super.myParameterMap.get("FISH_BREED_TIME");
        SHARK_INITIAL_ENERGY = super.myParameterMap.get("SHARK_INITIAL_ENERGY");
        FISH_ENERGY = super.myParameterMap.get("FISH_ENERGY");

        sharkEnergy = SHARK_INITIAL_ENERGY;
        chronons = 0;
    }

    protected PredPreyCell() {
        super();
    }

    /*
     * Gets coordinates to assist in only incrementing chronons once
     */
    private String getFirstAnimalCoords() {
        for (int i = 0; i < listOfCellsInGrid.length; i++) {
            for (int j = 0; j < listOfCellsInGrid[0].length; j++) {
                if (listOfCellsInGrid[i][j].myState != 0) {
                    String s = i + " " + j;
                    return s;
                }
            }
        }
        return "";
    }

    /*
     * Increments chronons only once when the first animal is found
     * Also calls on specific rules for sharks and fish
     */
    @Override
    public void doAction() {
        String firstAnimalCoords = getFirstAnimalCoords();
        if ((myX + " " + myY).equals(firstAnimalCoords)) {
            chronons++;
        }
        PredPreyCell[] rules = new PredPreyCell[] {
                new FishRules(),
                new SharkRules()
        };
        rules[myState - 1].doAction();
    }
}