package edgeTypes;

public class ToroidalEdge extends Edge {
	@Override
	public int calculateNewCoordinate(int coordinate, int delta, int length) {
		return (coordinate + delta + length) % length;
	}
}
