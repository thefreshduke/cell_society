package edgeTypes;

import java.util.ArrayList;
import java.util.List;

import cellsociety_team19.XMLReader;
import simulationTypes.Cell;

public abstract class Edge {

	protected int myX;
	protected int myY;
	protected int[] myXDelta = {1,-1, 0, 0};
	protected int[] myYDelta = {0, 0,-1, 1};
	protected Cell[][] listOfCellsInGrid;
	protected XMLReader xmlReader;

	public Edge(int x, int y) {
		myX = x;
		myY = y;
//		xmlReader = new XMLReader();
	}
	
	public Edge() {
		
	}
	
	public Edge(Cell[][] cells) {
		listOfCellsInGrid = cells;
	}
	
	public void setXDelta(int[] xDelta){
		myXDelta = xDelta;
	}
	
	public void setYDelta(int[] yDelta){
		myYDelta = yDelta;
	}

//	public Cell[] calculateNeighbors() {
	public List<Cell> calculateNeighbors(int myX, int myY, Cell[][] listOfCells) {
		//use List<Cell> instead of Cell[] //maybe Collection or Iterable down the line???
		//higher up interfaces have fewer functions allowed, which protect the structure a little more
		//by disallowing certain things that "lower" interfaces allow
//		int[] xDelta = {1,-1, 0, 0,-1,-1, 1, 1};
//		int[] yDelta = {0, 0,-1, 1, 1,-1,-1, 1};

//		Cell[] returnListOfNeighbors = new LifeCell[xDelta.length];
		List<Cell> listOfNeighbors = new ArrayList<>();

		for (int i = 0; i < myXDelta.length; i++) {
			int newX = 0;
			int newY = 0;

			int xLength = listOfCells[0].length;
			int yLength = listOfCells.length;

			//we can eliminate this following if statement by using finite edges as the default
			//toroidal edges (wrap-around) is edge type 1
			//infinite edges is edge type 2
			
			newX = calculateNewCoordinate(myXDelta[i], xLength, myX);
			newY = calculateNewCoordinate(myYDelta[i], yLength, myY);
			
//			if (listOfCellsInGrid[newX] [newY] != null) {
			if (newX != -1 && newY != -1) {
				listOfNeighbors.add(listOfCellsInGrid[newX] [newY]);
			}
		}
		return listOfNeighbors;
	}
	
	public abstract int calculateNewCoordinate(int delta, int length, int coordinate);
	
//	public Cell[][] updateGrid() {
//		return listOfCellsInGrid;
//	}
//	
//	public void setGrid(Cell[][] listOfCells) {
//		listOfCellsInGrid = listOfCells;
//	}

}
