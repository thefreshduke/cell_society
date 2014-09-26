package edgeTypes;

/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * 
 * Implements IEdgeStrategy, represents the algorithm
 * for a finite grid edge.
 *
 */
public class FiniteEdgeStrategy implements IEdgeStrategy {
	@Override
	public int calculateNewCoordinate(int coordinate, int delta, int length) {
		if (coordinate + delta >= 0 && coordinate + delta < length) {
			return (coordinate + delta);
		}
		return -1;
	}
}
