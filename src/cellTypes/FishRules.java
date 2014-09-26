package cellTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  @author Chris Bernt, Marcus Cain, Scotty Shaw
 *  Extension of PredPreyCell and implementation of Rules for WaTor World fish
 *
 */
public class FishRules extends PredPreyCell implements Rules {

    /*
     * Calculate neighbors for each Fish
     */
    @Override
    public List<Cell> calculateNeighbors(Cell[][] listOfCells, int[] xDelta, int[] yDelta) {
        List<Cell> superNeighbors = super.calculateNeighbors(listOfCells, xDelta, yDelta);
        List<Cell> returnListOfNeighbors = new ArrayList<Cell>();
        for(Cell c : superNeighbors){
            if (c.myState == 0 && c.myNextState == 0) {
                returnListOfNeighbors.add(c);
            }
        }
        return returnListOfNeighbors;
    }

    /*
     * Move to a valid cell
     */
    @Override
    public void doAction() {
        if (super.imminentSharkAttack) {
            super.imminentSharkAttack = false;
            return;
        }

        List<Cell> myNeighbors = calculateNeighbors(super.listOfCellsInGrid, super.myXDelta, super.myYDelta);

        if (hasAdjacentEmptySpaces()) {
            Random rand = new Random();
            int randChoice = rand.nextInt(myNeighbors.size());
            Cell newCell = myNeighbors.get(randChoice);

            PredPreyCell destinationCell = (PredPreyCell) listOfCellsInGrid[newCell.myX] [newCell.myY];
            destinationCell.myNextState = myState;

            if (super.chronons % super.FISH_BREED_TIME == 0) {
                myNextState = myState;
            }
            else {
                myNextState = 0;
            }
        }
        else {
            myNextState = myState;
        }
    }

    /*
     * Find an empty cell for the fish
     */
    private boolean hasAdjacentEmptySpaces() {
        List<Cell> adjacentNeighbors = calculateNeighbors(super.listOfCellsInGrid, super.myXDelta, super.myYDelta);
        int space = 0;
        for (int i = 0; i < adjacentNeighbors.size(); i++) {
            if (adjacentNeighbors.get(i).myState == 0 && adjacentNeighbors.get(i).myNextState == 0) {
                space++;
            }
        }
        return (space > 0);
    }
}