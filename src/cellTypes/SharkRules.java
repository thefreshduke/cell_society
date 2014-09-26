package cellTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  @author Chris Bernt, Marcus Cain, Scotty Shaw
 *  Extension of PredPreyCell and implementation of Rules for WaTor World shark
 *
 */
public class SharkRules extends PredPreyCell implements Rules {

    /*
     * Calculate neighbors for each Shark
     */
    @Override
    public List<Cell> calculateNeighbors(Cell[][] listOfCells, int[] xDelta, int[] yDelta) {
        List<Cell> superNeighbors = super.calculateNeighbors(listOfCells, xDelta, yDelta);
        List<Cell> returnListOfNeighbors = new ArrayList<Cell>();
        for(Cell c : superNeighbors){
            if (c.myState != 2 && c.myNextState != 2) {
                returnListOfNeighbors.add(c);
            }
        }
        //create generic neighbors method that creates an arraylist of neighbors, modify it for otherNeighbors (specific cases)
        ArrayList<Cell> fishNeighbors = new ArrayList<Cell>();
        for (Cell c : returnListOfNeighbors) {
            if (c != null && c.myState == 1) {
                fishNeighbors.add(c);
            }
        }

        if (fishNeighbors.size() > 0) {
            return fishNeighbors;
        }

        ArrayList<Cell> otherNeighbors = new ArrayList<Cell>();
        for (Cell c : returnListOfNeighbors) {
            if (c != null && c.myState == 0 && c.myNextState == 0) {
                otherNeighbors.add(c);
            }
        }
        return otherNeighbors;
    }

    /*
     * Move to a valid cell
     */
    @Override
    public void doAction() {
        if (sharkEnergy == 0) {
            myNextState = 0;
            return;
        }
        sharkEnergy--;

        List<Cell> neighbors = calculateNeighbors(super.listOfCellsInGrid, super.myXDelta, super.myYDelta);
        if (neighbors.size() > 0) {
            Random r = new Random();
            int choice = r.nextInt(neighbors.size());
            Cell newCell = neighbors.get(choice);
            PredPreyCell destinationCell = (PredPreyCell) listOfCellsInGrid[newCell.myX] [newCell.myY];
            if (newCell.myState == 2 || newCell.myNextState == 2) {
                return;
            }

            // fish are food, not friends
            if (newCell.myState == 1) {
                super.sharkEnergy += super.FISH_ENERGY;         
                destinationCell.imminentSharkAttack = true;
            } 
            destinationCell.myNextState = myState;
            destinationCell.sharkEnergy = sharkEnergy;

            if (chronons % super.SHARK_BREED_TIME == 0) {
                myNextState = myState;
                sharkEnergy = super.SHARK_INITIAL_ENERGY;
            }
            else {
                myNextState = 0;
            }
        }
        else {
            myNextState = myState;
        }
    }
}