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
	
	public Map<String,Double> parameterMap;
	
	private String gameType;
	
	public XMLReader(){
		/*paraments for all seg types*/
		parameterMap = new HashMap<String,Double>();
		parameterMap.put("THRESHOLD_OF_HAPPINESS", 0.);
		parameterMap.put("PROBABILITY_OF_CATCHING_FIRE", 0.);
		parameterMap.put("SHARK_BREED_TIME", 0.);
		parameterMap.put("FISH_BREED_TIME", 0.);
		parameterMap.put("SHARK_INITIAL_ENERGY", 0.);
		parameterMap.put("FISH_ENERGY", 0.);
		
		setupDOMParser();
		
		parseCell();
	}
	public XMLReader(File xml) {
		xmlFile = xml;
		
		simulationMap = new HashMap<String, Cell>();
		simulationMap.put("Seg", new SegCell());
		simulationMap.put("Fish", new PredPreyCell());
		simulationMap.put("Tree", new TreeCell());
		simulationMap.put("Life", new LifeCell());
		
		
		setupDOMParser();
		
	}

	public Cell[][] parseFile() {
		
			

			/* get the simulation type --> root node(tag) */
			// list of size 1
			NodeList gameTypeList = doc.getElementsByTagName("simulation");

			for (int i = 0; i < gameTypeList.getLength(); i++) {
				Node nNode = gameTypeList.item(i);
				Element eElement = (Element) nNode;
				/* set CellType from gametype */
				gameType =eElement.getAttribute("gametype");
				choice = simulationMap.get(gameType);
			}

			/* parse through the XMl to set up the Grid */
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
						gridArrayOfCells[i][x] = choice.makeNewCell(i, x, Integer.parseInt(colStates[x]));
						//gridArrayOfCells[i][x] = new SegCell(i, x, 0);
					}
				}
			}
			return gridArrayOfCells;
		

		
	}
	
	
	public void parseCell(){
		
		/*Get through the list of <paramter> tags */
		NodeList parameterList = doc.getElementsByTagName("parameter");
		
		/*Loop through the paramters and only get the ones with specified gameType */
		for(int i=0;i<parameterList.getLength();i++){
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
	
	private void setupDOMParser(){
		try{
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
	
	public Map<String,Double> getParameterMap(){
		return parameterMap;
	}
}
