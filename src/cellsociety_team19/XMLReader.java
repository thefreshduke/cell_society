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

import simulationTypes.Cell;
import simulationTypes.LifeCell;
import simulationTypes.PredPreyCell;
import simulationTypes.SegCell;
import simulationTypes.TreeCell;

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

	public int numRows;
	public int numCols;

	private Cell[][] gridArrayOfCells;

	private Cell choice;

	private Map<String, Cell> simulationMap;

	public Map<String, Double> parameterMap;

	private String gameType;

	/***
	 * One Constructor: Initialize parameterMap, SimulaitonMap, and DOMParser
	 */
	public XMLReader(File xml){
		
		/* point to xmlFile that the user opened up */
		xmlFile = xml;
		/* Setup Document and DOMParser */
		setupDOMParser();
		
		/* iniltialize parameterMap */
		parameterMap = parameterSetup();
		
		/* initialize simulationMap */
		simulationMap = new HashMap<String, Cell>();
		simulationMap.put("Seg", new SegCell());
		simulationMap.put("Fish", new PredPreyCell());
		simulationMap.put("Tree", new TreeCell());
		simulationMap.put("Life", new LifeCell());
		
		
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
			gameType = eElement.getAttribute("gametype");
			choice = simulationMap.get(gameType);
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
				gridArrayOfCells[i][j] = choice.makeNewCell(i, j, Integer.parseInt(colStates[j]), parameterMap); //also pass in paramMap
			}
		
		}
		
		return gridArrayOfCells;
	
	}
	
	
}
