package edgeTypes;

import cellTypes.Cell;

public class FiniteEdge extends Edge {
	public FiniteEdge(Cell[][] cells) {
		super(cells);
		// TODO Auto-generated constructor stub
	}

	public FiniteEdge() {
	}

	@Override
	public int calculateNewCoordinate(int delta, int length, int coordinate) {
		if ((coordinate + delta) >= 0 && (coordinate + delta) < length) {
			return (coordinate + delta);
		}
		return -1;
	}
}