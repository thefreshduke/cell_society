package cellTypes;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import edgeTypes.FiniteEdgeStrategy;
import edgeTypes.IEdgeStrategy;
import edgeTypes.ToroidalEdgeStrategy;

public class CellFactory {

	private Map<String, Cell> simulationMap;
	private Map<String, IEdgeStrategy> edgeMap;
	private Map<String, int[]> xDeltas;
	private Map<String, int[]> yDeltas;
	
	private static int[] cardinalXDelta = new int[] {1, -1, 0, 0};
	private static int[] cardinalYDelta = new int[] {0, 0, 1, -1};
	
	private static int[] surroundXDelta = new int[] {1, -1, 0, 0, 1, 1, -1, -1};
	private static int[] surroundYDelta = new int[] {0, 0, 1, -1, 1, -1, 1, -1};
	

	public CellFactory(){
		/* initialize simulationMap */
		simulationMap = new HashMap<String, Cell>();
		simulationMap.put("Seg", new SegCell());
		simulationMap.put("Fish", new PredPreyCell());
		simulationMap.put("Tree", new TreeCell());
		simulationMap.put("Life", new LifeCell());

		/* initialize edgeMap */
		edgeMap = new HashMap<String, IEdgeStrategy>();
		edgeMap.put("Finite", new FiniteEdgeStrategy());
		edgeMap.put("Toroidal", new ToroidalEdgeStrategy());
		
		xDeltas = new HashMap<String, int[]>();
		yDeltas = new HashMap<String, int[]>();
		
		setUpDeltas(cardinalXDelta, cardinalYDelta, "Tree");
		setUpDeltas(cardinalXDelta, cardinalYDelta, "Fish");
		setUpDeltas(surroundXDelta, surroundYDelta, "Life");
		setUpDeltas(surroundXDelta, surroundYDelta, "Seg");
		
	}

	public Cell createCell(int x, int y, int state, String cellType, String edgeType, Map<String, Double> parameterMap, Map<Integer, Color> colorMap){
		return simulationMap.get(cellType).makeNewCell(x, y, state, edgeMap.get(edgeType), parameterMap, colorMap, xDeltas.get(cellType), yDeltas.get(cellType));
	}
	
	private void setUpDeltas(int[] xDelta, int[] yDelta, String cellType){
		xDeltas.put(cellType, xDelta);
		yDeltas.put(cellType, yDelta);
	}
}
