package edgeTypes;

public class FiniteEdgeStrategy implements IEdgeStrategy {
	@Override
	public int calculateNewCoordinate(int coordinate, int delta, int length) {
		if (coordinate + delta >= 0 && coordinate + delta < length) {
			return (coordinate + delta);
		}
		return -1;
	}
}
