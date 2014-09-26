package edgeTypes;

/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * 
 * Implements IEdgeStrategy, represents the algorithm
 * for a toroidal grid edge, one where cell's neighbors
 * wrap around the sides of the grid.
 *
 */
public class ToroidalEdgeStrategy implements IEdgeStrategy {
	@Override
	public int calculateNewCoordinate(int coordinate, int delta, int length) {
		return (coordinate + delta + length) % length;
	}
}
