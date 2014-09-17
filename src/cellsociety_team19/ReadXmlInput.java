package cellsociety_team19;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.layout.GridPane;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * MARCUS'S XML READER, KEEP ALL
 * @author Marcus Cain
 *
 */


public class ReadXmlInput {
	static File xmlFile;

	public int numRows;
	public int numCols;

	private Cell[][] gridArrayOfCells;

	Cell choice;

	Map<String, Cell> simulationMap;

	public ReadXmlInput(File xml) {
		xmlFile = xml;

		simulationMap = new HashMap<String, Cell>();
		simulationMap.put("Segregation", new SegCell());
		simulationMap.put("PredPrey", new PredPreyCell());
		simulationMap.put("Tree", new TreeCell());
		simulationMap.put("GameOfLife", new LifeCell());

	}

	public Cell[][] parseFile() {

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			/* get the simulation type --> root node(tag) */
			NodeList gameTypeList = doc.getElementsByTagName("simulation"); // list
																			// of
																			// size
																			// 1
			for (int i = 0; i < gameTypeList.getLength(); i++) {

				Node nNode = gameTypeList.item(i);

				Element eElement = (Element) nNode;

				/* set CellType from gametype */
				choice = simulationMap.get(eElement
						.getAttribute("gametype"));

				System.out.println("Simulation Type: "
						+choice); // print out
																// gameType

			}

			/* parse through the XMl to set up the Grid */
			NodeList gridList = doc.getElementsByTagName("grid"); // list of
																	// size 1

			for (int x = 0; x < gridList.getLength(); x++) { // list of size 1;
																// outerloop can
																// get delete,
																// only for
																// testing
																// purposes now
				System.out
						.println("Parsing through the <grid> to get the elements <row>");

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
					System.out.println("Colstate: " + Arrays.toString(colStates));
					int col = colStates.length;
					/* Make 2d grid array that tracks the cells in the grid */
					for (x = 0; x < col; x++) {
						
						System.out.println(choice.getClass());
						
						gridArrayOfCells[i][x] = choice.makeNewCell(i, x, Integer.parseInt(colStates[x]));
						//gridArrayOfCells[i][x] = new SegCell(i, x, 0);
					}

					// }

				}

			}

			for (int i = 0; i < gridArrayOfCells.length; i++) {
				for (int j = 0; j < gridArrayOfCells[i].length; j++) {
					System.out.print(gridArrayOfCells[i][j]);
				}
				System.out.println();
			}

			return gridArrayOfCells;
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Couldnt Parse ! Fix the XML");
			System.exit(0);
			return null;
		}

	}

}
