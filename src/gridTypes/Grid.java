package gridTypes;

import javafx.scene.shape.Shape;

/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * 
 * An interface that contains the structure
 * for dealing with the back end of different
 * grid types. While we did not end up having the time
 * to support the new grid types, mostly to front end
 * issues, this is architecture we were planning to use.
 *
 */
public interface Grid {	
    /**
     * @param x
     * @param y
     * @return the xDelta array for finding a cell's neighbors
     */
    public abstract int[] calculateXDelta(int x, int y);

    /**
     * @param x
     * @param y
     * @return the yDelta array for finding a cell's neighbors
     */
    public abstract int[] calculateYDelta(int x, int y);

    /**
     * @return the Shape object representing a grid tile.
     */
    public abstract Shape calculateShape();
}
