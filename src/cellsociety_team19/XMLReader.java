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
import cellTypes.CellFactory;

/**
 * MARCUS'S XML READER, KEEP ALL
 * @author Marcus Cain
 *
 */

public class XMLReader {
	private CellFactory myFactory;

	private File xmlFile;
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;

	public int numRows;
	public int numCols;

	private Cell[][] gridArrayOfCells;
	public Map<String, Double> parameterMapForCells;
	private Map<Integer, Color> colorMapForCells;
	private String gameType;
	private String edgeType;

	/***
	 * One Constructor: Initialize parameterMap, SimulaitonMap, and DOMParser
	 */
	public XMLReader(File xml) {

		myFactory = new CellFactory();

		/* point to xmlFile that the user opened up */
		xmlFile = xml;
		/* Setup Document and DOMParser */
		setupDOMParser();

		/* iniltialize parameterMap */
		parameterMapForCells = parameterSetup();

		/* initialize colorMap */
		colorMapForCells = colorMapSetup();
	}

	/***
	 * 
	 * @return Map<Integer,Color> for subcells to use as their mapping b/t state and color
	 */
	private Map<Integer, Color> colorMapSetup() {
		/*map to return */
		Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

		/*Get the list of <color> tags */
		NodeList colorList = doc.getElementsByTagName("color");

		/*Loop through the <color> tags and populate colorMap */
		for (int i = 0; i < colorList.getLength(); i++) {
			Node nNode = colorList.item(i);
			Element eElement = (Element) nNode;

			/* get the state value and corresponding Color --> populate map */
			colorMap.put(Integer.parseInt(eElement.getAttribute("state")), Color.valueOf(eElement.getAttribute("color")));
		}
		return colorMap;
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
		Map<String, Double> parameterMap = new HashMap<String, Double>();

		/*Get the list of <paramter> tags */
		NodeList parameterList = doc.getElementsByTagName("parameter");

		/*Loop through the <paramater> tags and populate paramMap */
		for (int i = 0; i < parameterList.getLength(); i++) {
			Node nNode = parameterList.item(i);
			Element eElement = (Element) nNode;

			parameterMap.put(eElement.getAttribute("name"), Double.parseDouble(eElement.getAttribute("value")));
		}
		return parameterMap;
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
		}

		NodeList edgeTypeList = doc.getElementsByTagName("edge");

		for (int i = 0; i < edgeTypeList.getLength(); i++) {
			Node nNode = edgeTypeList.item(i);
			Element eElement = (Element) nNode;
			/* set edgeType from edgetype */
			edgeType = eElement.getAttribute("edgeType");
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
				gridArrayOfCells[i][j] = myFactory.createCell(i, j, Integer.parseInt(colStates[j]), gameType, edgeType, parameterMapForCells, colorMapForCells);
			}
		}
		return gridArrayOfCells;
	}
}