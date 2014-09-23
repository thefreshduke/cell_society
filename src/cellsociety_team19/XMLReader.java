package cellsociety_team19;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import edgeTypes.Edge;
import edgeTypes.FiniteEdge;
import edgeTypes.ToroidalEdge;
import edgeTypes.Edge.*;
import simulationTypes.*;

/**
 * MARCUS'S XML READER, KEEP ALL
 * @author Marcus Cain
 *
 */

public class XMLReader {
	static File xmlFile;
	static DocumentBuilderFactory dbFactory;
	static DocumentBuilder dBuilder;
	private Document doc;

//	public double edgeType; //temporary hardcode for refactoring purposes; this will need to be refactored soon
	public int numRows;
	public int numCols;

	private Cell[][] gridArrayOfCells;

	private Cell gameChoice;
	
	private Edge edgeChoice;

	private Map<String, Object> simulationMap;
	
//	private Map<String, Edge> edgeMap;

	public Map<String, Double> parameterMap;

	private String gameType;
	
	private String edgeType;

	public XMLReader() {
		/*paraments for all seg types*/
		parameterMap = new HashMap<String,Double>();
		parameterMap.put("THRESHOLD_OF_HAPPINESS", 0.);
		parameterMap.put("PROBABILITY_OF_CATCHING_FIRE", 0.);
		parameterMap.put("SHARK_BREED_TIME", 0.);
		parameterMap.put("FISH_BREED_TIME", 0.);
		parameterMap.put("SHARK_INITIAL_ENERGY", 0.);
		parameterMap.put("FISH_ENERGY", 0.);
//		parameterMap.put("EDGE_TYPE", 0.);
		
		
//		edgeMap.put("finite", new FiniteEdge(gridArrayOfCells));
//		edgeMap.put("toroidal", new ToroidalEdge(gridArrayOfCells));
		//edgeMap.put("infinite", new InfiniteEdge(gridArrayOfCells));

		setupDOMParser();

		parseCell();
	}

	public XMLReader(File xml) {
		xmlFile = xml;

		simulationMap = new HashMap<String, Object>();
		simulationMap.put("Seg", new SegCell());
		simulationMap.put("Fish", new PredPreyCell());
		simulationMap.put("Tree", new TreeCell());
		simulationMap.put("Life", new LifeCell());
		simulationMap.put("Finite", new FiniteEdge());
		simulationMap.put("Toroidal", new ToroidalEdge());

		setupDOMParser();
	}

	public Cell[][] parseFile() {
		
//		parseCell();
		
		/* get the simulation type --> root node(tag) */
		// list of size 1
		NodeList gameTypeList = doc.getElementsByTagName("simulation");

		for (int i = 0; i < gameTypeList.getLength(); i++) {
			Node nNode = gameTypeList.item(i);
			Element eElement = (Element) nNode;
			/* set CellType from gametype */
			gameType = eElement.getAttribute("gameType");
			gameChoice = (Cell) simulationMap.get(gameType);
			//???
			edgeType = eElement.getAttribute("edgeType");
			edgeChoice = (Edge) simulationMap.get(edgeType);
			//edgeType = parameterMap.get("EDGE_TYPE"); //temporary hardcode for refactoring purposes; this will need to be refactored soon
		}

		/* parse through the XML to set up the Grid */
		// list of size 1
		NodeList gridList = doc.getElementsByTagName("grid");

		for (int x = 0; x < gridList.getLength(); x++) { // list of size 1
			//outerloop can get delete, only for testing purposes now
			NodeList rowList = doc.getElementsByTagName("row");

			numRows = rowList.getLength();
			numCols = numRows;
			gridArrayOfCells = new Cell[numRows][numCols];

			for (int i = 0; i < rowList.getLength(); i++) {
				// searching through the row tags
				Node nNode = rowList.item(i);

				Element eElement = (Element) nNode;

				/*
				 * set numRows and numColums --> assuming numRows = numCols
				 * square grid
				 */

				// for (int row = 0; row < numRows; row++) {

				// columns will be parsed from the <row> states attribute
				String[] colStates = eElement.getAttribute("states").split(",");

				int col = colStates.length;
				/* Make 2d grid array that tracks the cells in the grid */
				for (x = 0; x < col; x++) {
//					Edge curEdge = edgeMap.get(parameterMap.get("EDGE_TYPE"));
					gridArrayOfCells[i][x] = gameChoice.makeNewCell(i, x, Integer.parseInt(colStates[x]), edgeChoice);
					//gridArrayOfCells[i][x] = new SegCell(i, x, 0);
				}
			}
		}
		return gridArrayOfCells;
	}

	public void parseCell() {

		/*Get through the list of <paramter> tags */
		NodeList parameterList = doc.getElementsByTagName("parameter");

		/*Loop through the paramters and only get the ones with specified gameType */
		for (int i = 0; i < parameterList.getLength(); i++) {
			Node nNode = parameterList.item(i);
			Element eElement = (Element) nNode;

			//Dont need this if statement --> refactoring to setup all parameters for all gametypes; indivual game cell will choose from the map the correct one
			//if(eElement.getAttribute("game").equals(gameType)){
			/*parameterMap will contain values for varaibles according to game type; all other will be null of 0 */
			//	parameterMap.put(eElement.getAttribute("name"), Integer.parseInt(eElement.getAttribute("value")));
			//}

			parameterMap.put(eElement.getAttribute("name"), Double.parseDouble(eElement.getAttribute("value")));
		}
	}

	private void setupDOMParser() {
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to parse: Fix XML file");
			System.exit(0);
		}
	}

	public Map<String,Double> getParameterMap() {
		return parameterMap;
	}
}
