package cellsociety_team19;

import static org.junit.Assert.*;

import java.util.List;
import java.io.File;

import javafx.scene.paint.Color;

import org.junit.Test;

import cellTypes.Cell;
import cellTypes.TreeCell;

/**
 * @author Chris
 * Basic JUnit Tester class to test the functionality of the
 * Cell superclass, in this case a TreeCell particularly.
 * This is reliant on the XMLReader, however, as the cell's
 * vital actions like doAction() need information from the XML
 * file that cannot be provided in one class like this (like xDelta
 * and yDelta, the grid, etc.)
 * 
 */
public class Tester {
    
    XMLReader myXML = new XMLReader(new File("src/xmlFiles/TreeCell.xml"));
    Cell[][] testGrid = myXML.parseFile();
    Cell testCell = testGrid[0][0];
    
    @Test
    public void checkNotNull(){
        assertNotEquals("Check not null", testCell, null);
    }
    
    @Test
    public void checkRightClass(){
        assertEquals("Correct class", testCell.getClass(), new TreeCell().getClass());
    }
    
    @Test
    public void checkSubclass(){
        assertTrue("Subclass of Cell", testCell instanceof Cell);
    }

    @Test
    public void checkState(){
        assertEquals("Is it burning", testCell.getState(), 2);
    }
    
    @Test
    public void checkColor(){
        assertEquals("Is color correct", testCell.getCorrespondingColor(), Color.RED);
    }
    
    @Test
    public void checkNumPatches(){
        assertEquals("Number of patch states", testCell.getMyNumPatchTypes(), 3);
    }
    
    @Test
    public void checkNeighbors(){
        List<Cell> neighbors = testCell.calculateNeighbors(testGrid, new int[] {1}, new int[] {0});
        assertEquals("Neighbor is alive", neighbors.get(0).getState(), 1);
    }
    
    
    @Test 
    public void checkDead(){
        testCell.doAction();
        testCell.updateCell();
        assertEquals("The tree is gone", testCell.getState(), 0);
    }
    
    @Test
    public void checkGridUpdated(){
        testCell.doAction();
        testCell.updateCell();
        assertEquals("The tree is gone in grid", testGrid[0][0].getState(), 0);
    }
    
}
