package edgeTypes;

public class ToroidalEdgeStrategy implements IEdgeStrategy {
	@Override
	public int calculateNewCoordinate(int coordinate, int delta, int length) {
		return (coordinate + delta + length) % length;
	}
}
