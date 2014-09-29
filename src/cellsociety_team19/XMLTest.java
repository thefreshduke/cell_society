package cellsociety_team19;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

import org.junit.Test;



public class XMLTest {
    /*
    public void testOpenFile(){
        String xmlFile = "TreeCell.xml";
        File file = new File(System.getProperty("user.dir") +"/src/xmlFiles/" + xmlFile);
        XMLReader xmlReader  = new XMLReader(file);
        xmlReader.parseFile();
        assertEquals(xmlReader.gameType,"Tree");
    }
    */
    @Test
    public void testColorMapParameters(){
        String xmlFile = "TreeCell.xml";
        File file = new File(System.getProperty("user.dir") +"/src/xmlFiles/" + xmlFile);
        XMLReader xmlReader  = new XMLReader(file);
        Map<Integer, Color> colorMap = new HashMap<Integer, Color>();
        //test against known color states  for the xmlFile -- instert known colors
        colorMap.put(0, Color.GRAY);
        colorMap.put(1, Color.GREEN);
        colorMap.put(2, Color.RED);
        
        assertEquals(xmlReader.colorMapForCells,colorMap);
    }
    
}
 