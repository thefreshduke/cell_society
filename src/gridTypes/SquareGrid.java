package gridTypes;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * Square Grid class that implements Grid.
 */
public class SquareGrid implements Grid {

    @Override
    public int[] calculateXDelta(int x, int y) {
        return new int[] {1,-1, 0, 0};
    }

    @Override
    public int[] calculateYDelta(int x, int y) {
        return new int[] {0, 0, 1,-1};
    }

    @Override
    public Shape calculateShape() {
        return new Rectangle();
    }
}
