package cellTypes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import edgeTypes.FiniteEdgeStrategy;
import edgeTypes.IEdgeStrategy;
import edgeTypes.ToroidalEdgeStrategy;

/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * 
 * The class that handles all the creation of 
 * new types of Cells. The most important method
 * is the createCell() method. The instance 
 * variables map string inputs from the XML file
 * to the structures those strings represent, and 
 * the Arrays of ints help set up the neighbor
 * map.
 *
 */
@SuppressWarnings("rawtypes")
public class CellFactory {

    private Map<String, Class> simulationMap;
    private Map<String, IEdgeStrategy> edgeMap;
    private Map<String, int[]> xDeltas;
    private Map<String, int[]> yDeltas;

    private static int[] cardinalXDelta = new int[] {1,-1, 0, 0};
    private static int[] cardinalYDelta = new int[] {0, 0, 1,-1};

    private static int[] surroundXDelta = new int[] {1,-1, 0, 0, 1, 1,-1,-1};
    private static int[] surroundYDelta = new int[] {0, 0, 1,-1, 1,-1, 1,-1};


    public CellFactory() {
        /* initialize simulationMap */
        simulationMap = new HashMap<String, Class>();
        simulationMap.put("Seg", new SegCell().getClass());
        simulationMap.put("Fish", new PredPreyCell().getClass());
        simulationMap.put("Tree", new TreeCell().getClass());
        simulationMap.put("Life", new LifeCell().getClass());

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

    /**
     * @param x
     * @param y
     * @param state
     * @param cellType
     * @param edgeStrategy
     * @param parameterMap
     * @param colorMap
     * @return New Cell subclass instance.
     * 

     /**
     * We accidentally stumbled upon reflection in this class.
     * We originally had each subclass of Cell
     * contain a nullary constructor which we instantiate in the Map like we did here, 
     * and an additional method that created a new instance of their class with
     * the given parameters. This resulted in some ugly repeated code, so we went online to
     * figure out how we might be able to move it all into this class. We followed the java 
     * documentation for newInstance and later realized this technique was called reflection.
     */
    @SuppressWarnings("unchecked")
    public Cell createCell(int x, int y, int state, String cellType,
            String edgeStrategy, Map<String, Double> parameterMap,
            Map<Integer, Color> colorMap) {		
        Class c = simulationMap.get(cellType);
        Constructor construct = null;
        try {
            construct = c.getConstructor(int.class, int.class, int.class,
                    IEdgeStrategy.class, Map.class, Map.class, int[].class,
                    int[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        try {
            return (Cell) construct.newInstance(x, y, state,
                    edgeMap.get(edgeStrategy), parameterMap, colorMap,
                    xDeltas.get(cellType), yDeltas.get(cellType));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param xDelta
     * @param yDelta
     * @param cellType
     *
     * Sets up the maps for xDelta and yDelta, which represent
     * the transformations to get a given cell's neighbors.
     */
    private void setUpDeltas(int[] xDelta, int[] yDelta, String cellType) {
        xDeltas.put(cellType, xDelta);
        yDeltas.put(cellType, yDelta);
    }
}
