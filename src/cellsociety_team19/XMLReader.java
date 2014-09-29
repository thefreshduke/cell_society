package cellsociety_team19;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import cellTypes.Cell;
import cellTypes.CellFactory;

/**
 * XMLReader: Class purpose is to take in an XML file and DOM parser through it. This class parses through the xml file 
 * looking for specific tags and attributes.
 * This class has two purposes 
 * 1) Parse Parameter Tags, Colors Tags, and determine the x/y deltas a specific cell type.
 * 2) Parse Grid Tags and return to SimulationLoop.java a Collection of cell types with corresponding parameters from (1)
 * @author Marcus Cain, Chris Bernt, Scotty Shaw
 *
 */

public class XMLReader {
    private CellFactory myFactory;

    private File xmlFile;
    private DocumentBuilderFactory docBuilderFactory;
    private DocumentBuilder docBuilder;
    private Document doc;

    int numRows;
    int numCols;

    private Cell[][] gridArrayOfCells;
    private Map<String, Double> parameterMapForCells;
    private Map<String, Double> colorMapForCells;
    private String gameType;
    private String edgeType;

    /***
     * One Constructor: Get corresponding x/y deltas,
     * Initialize parameterMap, colorMap, SimulationMap, and DOMParser
     */
    public XMLReader(File xml) {

        myFactory = new CellFactory();

        /* point to xmlFile that the user opened up */
        xmlFile = xml;

        setupDocumentObjectModelParser();
        parameterMapForCells = createMapForCells("parameter");
        colorMapForCells = createMapForCells("color");
    }

    /***
     * creates Document and DocumentBuilder to parse the actual XMLFIle: 
     * Setup to allow programmer to search for tags/attributes
     */
    private void setupDocumentObjectModelParser() {
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.out.println("ParserConfigurationException detected");
        }
        catch (SAXException e) {
            e.printStackTrace();
            System.out.println("SAXException detected");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException detected");
        }
    }

    private Map<String, Double> createMapForCells(String s) {
        /*map to return */
        Map<String, Double> map = new HashMap<String, Double>();

        /*Get the list of <color> tags */
        NodeList listOfNodes = doc.getElementsByTagName(s);

        /*Loop through the <color> tags and populate colorMap */
        for (int i = 0; i < listOfNodes.getLength(); i++) {
            Node nNode = listOfNodes.item(i);
            Element eElement = (Element) nNode;

            /* get the state value and corresponding Color --> populate map */
            map.put(eElement.getAttribute("state").toString(), Double.valueOf(eElement.getAttribute(s)));
        }
        return map;
    }

    /***
     * 
     * @return the grid of Cell objects (used in simulationLoop for back-end tracking).
     */
    public Cell[][] setupGridArrayOfCellTypes() {

        extractSimulationInfoFromXMLFile("game");
        extractSimulationInfoFromXMLFile("edge");

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
                gridArrayOfCells[i][j] = myFactory.createCell(i, j, Integer.parseInt(colStates[j]),
                        gameType, edgeType, parameterMapForCells, colorMapForCells);
            }
        }
        return gridArrayOfCells;
    }

    private void extractSimulationInfoFromXMLFile(String s) {
        NodeList typeList = doc.getElementsByTagName(s);

        for (int i = 0; i < typeList.getLength(); i++) {
            Node nNode = typeList.item(i);
            Element eElement = (Element) nNode;
            gameType = eElement.getAttribute(s + "Type");
        }
    }
}