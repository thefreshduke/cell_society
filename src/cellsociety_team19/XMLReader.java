package cellsociety_team19;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import cellTypes.Cell;
import cellTypes.LifeCell;
import cellTypes.PredPreyCell;
import cellTypes.SegCell;
import cellTypes.TreeCell;
import edgeTypes.IEdgeStrategy;
import edgeTypes.FiniteEdgeStrategy;
import edgeTypes.ToroidalEdgeStrategy;

/**
 * MARCUS'S XML READER, KEEP ALL
 * @author Marcus Cain
 *
 */

public class XMLReader {
	private File xmlFile;
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;

	public int numRows;
	public int numCols;

	private Cell[][] gridArrayOfCells;

	private Cell gameChoice;
	private IEdgeStrategy edgeChoice;

	private Map<String, Cell> simulationMap;
	private Map<String, IEdgeStrategy> edgeMap;

	public Map<String, Double> parameterMapForCells;
	private Map<Integer, Color> colorMapForCells;
	private String gameType;
	private String edgeType;


	/***
	 * One Constructor: Initialize parameterMap, SimulaitonMap, and DOMParser
	 */
	public XMLReader(File xml) {

		/* point to xmlFile that the user opened up */
		xmlFile = xml;
		/* Setup Document and DOMParser */
		setupDOMParser();

		/* iniltialize parameterMap */
		parameterMapForCells = parameterSetup();

		/* initialize colorMap */
		colorMapForCells = colorMapSetup();

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

	/***
	 * 
	 * @return Map<Integer,Color> for subcells to use as their mapping b/t state and color
	 */
	private Map<Integer, Color> colorMapSetup() {
		/*map to return */
		Map<Integer,Color> colourMap = new HashMap<Integer,Color>();

		/*Get the list of <color> tags */
		NodeList colorList = doc.getElementsByTagName("color");

		/*Loop through the <color> tags and populate colourMap */
		for (int i = 0; i < colorList.getLength(); i++) {
			Node nNode = colorList.item(i);
			Element eElement = (Element) nNode;

			/* get the state value and corresponding Color --> populate map */
			colourMap.put(Integer.parseInt(eElement.getAttribute("state")), Color.valueOf(eElement.getAttribute("color")));
		}
		return colourMap;
	}

	/***
	 * creates Document and DocumentBuilder to parse the actual XMLFIle
	 */
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

	/***
	 * 
	 * @return Map<String,Double> of parameterList from XML File
	 */
	private Map<String, Double> parameterSetup() {
		Map<String,Double> paramMap = new HashMap<String,Double>();

		/*Get the list of <paramter> tags */
		NodeList parameterList = doc.getElementsByTagName("parameter");

		/*Loop through the <paramater> tags and populate paramMap */
		for (int i = 0; i < parameterList.getLength(); i++) {
			Node nNode = parameterList.item(i);
			Element eElement = (Element) nNode;

			paramMap.put(eElement.getAttribute("name"), Double.parseDouble(eElement.getAttribute("value")));
		}
		return paramMap;
	}

	/***
	 * 
	 * @return the two-dimensional array of GridCells (used in simulationLoop)
	 */
	public Cell[][] parseFile() { //changeName to setupGridArrayOfCellTypes

		/* get the simulation type --> Loop through <simulation>tags and get the attrittrube 'gametype' */
		NodeList gameTypeList = doc.getElementsByTagName("simulation");

		for (int i = 0; i < gameTypeList.getLength(); i++) {
			Node nNode = gameTypeList.item(i);
			Element eElement = (Element) nNode;
			/* set CellType from gametype */
			gameType = eElement.getAttribute("gameType");
			gameChoice = simulationMap.get(gameType);
		}

		NodeList edgeTypeList = doc.getElementsByTagName("edge");

		for (int i = 0; i < edgeTypeList.getLength(); i++) {
			Node nNode = edgeTypeList.item(i);
			Element eElement = (Element) nNode;
			/* set edgeType from edgetype */
			edgeType = eElement.getAttribute("edgeType");
			edgeChoice = edgeMap.get(edgeType);
		}

		/* get list of <row> tags and set numRows & numCols */
		NodeList rowList = doc.getElementsByTagName("row");
		numRows = rowList.getLength();
		numCols = numRows;
		gridArrayOfCells = new Cell[numRows][numCols];

		/*loop through <row> tags and loop through states of each <row> tag and create 2d array */
		for (int i = 0; i < rowList.getLength(); i++) {
			Node nNode = rowList.item(i);
			Element eElement = (Element) nNode;
			// columns will be parsed from the <row> states attribute
			String[] colStates = eElement.getAttribute("states").split(",");

			/* Make 2d grid array that tracks the cells in the grid */
			for (int j = 0; j < colStates.length; j++) {
				gridArrayOfCells[i][j] = gameChoice.makeNewCell(i, j, Integer.parseInt(colStates[j]), edgeChoice, parameterMapForCells, colorMapForCells); //also pass in paramMap
				//factory class
			}
		}
		return gridArrayOfCells;
	}
}