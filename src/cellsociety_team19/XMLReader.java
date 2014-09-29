// This entire file is part of my masterpiece.
// Marcus Cain


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
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;

    public int numRows;
    public int numCols;

    private Cell[][] gridArrayOfCells;
    private Map<String, Double> parameterMapForCells;
    private Map<Integer, Color> colorMapForCells;
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
        /* Setup Document and DOMParser */
        setupDOMParser();

        /* iniltialize parameterMap */
        parameterMapForCells = parameterSetup();

        /* initialize colorMap */
        colorMapForCells = colorMapSetup();
    }

    /***
     * @return Map<Integer, Color> for subcells to use as their mapping b/t state and color
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
            colorMap.put(Integer.parseInt(eElement.getAttribute("state")),
                    Color.valueOf(eElement.getAttribute("color")));
        }
        return colorMap;
    }

    /***
     * creates Document and DocumentBuilder to parse the actual XMLFIle: 
     * Setup to allow programmer to search for tags/attributes
     */
    private void setupDOMParser() {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to parse: Fix XML file");
            System.exit(0);
        }
    }

    /***
     * @return Map<String,Double> of parameterList from XML File.
     */
    private Map<String, Double> parameterSetup() {
        Map<String, Double> parameterMap = new HashMap<String, Double>();

        /*Get the list of <paramter> tags */
        NodeList parameterList = doc.getElementsByTagName("parameter");

        /*Loop through the <paramater> tags and populate paramMap */
        for (int i = 0; i < parameterList.getLength(); i++) {
            Node nNode = parameterList.item(i);
            Element eElement = (Element) nNode;

            parameterMap.put(eElement.getAttribute("name"),
                    Double.parseDouble(eElement.getAttribute("value")));
        }
        return parameterMap;
    }

    /***
     * 
     * @return the grid of Cell objects (used in simulationLoop for back-end tracking).
     */
    public Cell[][] parseFile() { //changeName to setupGridArrayOfCellTypes

        /* get the simulation type --> Loop through <simulation>tags and get the attribute 'gameType' */
        gameType = (String) loopThroughTagsAndGetAttribute(gameType,"simulation","gameType");
       /* get the edge type --> Loop through <edge> tags and get the attribute "edgeType" */
        edgeType = (String) loopThroughTagsAndGetAttribute(edgeType,"edge","edgeType");

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
                gridArrayOfCells[i][j] = myFactory.createCell(i, j,
                        Integer.parseInt(colStates[j]), gameType, edgeType,
                        parameterMapForCells, colorMapForCells);
            }
        }
        return gridArrayOfCells;
    }
    
    private Object loopThroughTagsAndGetAttribute(Object obj, String tag, String attribute){
        NodeList objTypeList =doc.getElementsByTagName(tag);

        for (int i = 0; i < objTypeList.getLength(); i++) {
            Node nNode = objTypeList.item(i);
            Element eElement = (Element) nNode;
            /* set edgeType from edgetype */
            return obj = eElement.getAttribute(attribute);

        }
        return null;
        
    }
}