package edgeTypes;

/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * 
 * An interface for the strategies for calculating the 
 * coordinates for a cell's neighbor, since it can vary based on the edge type 
 * and grid type.
 *
 */
public interface IEdgeStrategy {
	public int calculateNewCoordinate(int coordinate, int delta, int length);
}
