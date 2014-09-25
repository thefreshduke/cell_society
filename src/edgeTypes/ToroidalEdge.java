package edgeTypes;

import cellTypes.Cell;

public class ToroidalEdge extends Edge {
	public ToroidalEdge(Cell[][] cells) {
		super(cells);
		// TODO Auto-generated constructor stub
	}

	public ToroidalEdge() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int calculateNewCoordinate(int delta, int length, int coordinate) {
		return ((coordinate + delta + length) % length);
	}
}