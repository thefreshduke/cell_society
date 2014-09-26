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
	}

	public Cell createCell(int x, int y, int state, String cellType, String edgeType, Map<String, Double> parameterMap, Map<Integer, Color> colorMap){
		return simulationMap.get(cellType).makeNewCell(x, y, state, edgeMap.get(edgeType), parameterMap, colorMap);
	}
}
