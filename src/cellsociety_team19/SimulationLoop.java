package cellsociety_team19;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import cellTypes.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * 
 * The class that actually runs the loop and 
 * logic based on the doAction of each Cell. Also creates
 * and updates the GUI each time step, as well as
 * keeping track of what that value is.
 * 
 * Given more time, a large change to this class
 * would have been to extract the GUI creation and 
 * updating into its own class. Since so many GUI
 * features interact with the loop, however, it would 
 * have required a significant passing of data,
 * which we did not know how to handle. 
 *
 */
public class SimulationLoop {

    private int framesPerSecond = 1;
    private int numRows;
    private int numCols;
    private int GRID_CELL_SIZE;
    private GridPane gridNew;
    private static final int GUI_HEIGHT = 400;
    private static final int GUI_WIDTH = 400;
    private static final int minHeight = 250;
    private Cell[][] gridArrayOfCells;
    private boolean shouldRun = false;
    private int genNum = 0;
    private Text generationNumber;
    private XMLReader xmlReader; 
    private Slider fpsSlider;
    private KeyFrame frame;
    private Timeline animation;
    private int BUTTON_WIDTH = 70;
    private int xShift = 10;
    private int Vgap = 4;
    private int Hgap = 10;
    private int InsetNumber = 5;
    private int layoutX = 120;
    private int layoutY = 120;

    private Map<Integer, Integer> populationCounts;

    private LineChart<Integer,Integer> lineChart;
    private Map<Integer, XYChart.Series<Integer, Integer>> myLines;

    /**
     * Creates the Frame and Timeline
     * and runs the animation.
     */
    public void start() {
        frame = makeFrame();
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }


    /**
     * Resets the population map used
     * for updating the population graph.
     */
    public void initializePopulationMap() {
        populationCounts = new HashMap<Integer, Integer>();
        for (int i = 0; i < gridArrayOfCells[0][0].getMyNumPatchTypes(); i++) {
            populationCounts.put(i, 0);
        }
    }

    /**
     * Create the game's frame
     */
    public KeyFrame makeFrame() {
        return new KeyFrame(Duration.millis(1000 / framesPerSecond), oneFrame);
    }

    private EventHandler<ActionEvent> oneFrame = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent evt) {
            if (shouldRun) {
                updateAll();
            }
        }

        /**
         * Calls all necessary methods to run the simulation.
         */
        private void updateAll() {
            updateCells();
            updateFPS();

            updateDataPoints();
            updatePopulationGraph();
            initializePopulationMap();
        }
    };

    /**
     * Initializes the GUI graph for population.
     * Requires the suppression of warning since the
     * LineChart wants type parameterization, but adding
     * them is not defined for LineChart somehow.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void initializePopulationGraph() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setTickLabelsVisible(false);
        xAxis.setLabel("Generation Number");

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(numRows * numCols);
        yAxis.setTickLabelsVisible(false);

        xAxis.setLabel("Generation Number");
        //creating the chart

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("Population Monitoring");
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lineChart.setMinHeight(minHeight);
        lineChart.setLegendVisible(false);
        myLines = new HashMap<Integer, XYChart.Series<Integer, Integer>>();

        addLineChart();
    }


    /**
     * Updates the population graph by clearing
     * old data, adding new data to a Series,
     * then adding it to the chart and displaying it.
     */
    public void updatePopulationGraph() {
        if (gridNew.getChildren().contains(lineChart)) {
            gridNew.getChildren().remove(lineChart);
        }

        lineChart.getData().clear();

        for (Integer i: populationCounts.keySet()) {
            if (i != 0) {
                lineChart.getData().add(myLines.get(i));
            }
        }
        addLineChart();
    }

    /**
     * Adds the LineChart to the GUI.
     */
    private void addLineChart() {
        gridNew.add(lineChart, numRows + xShift, 3);
    }


    /**
     * Method to check the value of the 
     * fps slider and update the animation speed.
     */
    private void updateFPS() {
        framesPerSecond = (int) Math.round(fpsSlider.getValue());
        animation.stop();
        animation.getKeyFrames().remove(frame);
        frame = makeFrame();
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    /**
     * Calls the appropriate methods
     * to update the cells.
     */
    public void updateCells() {
        updateGenerationNumber();
        doCellsAction();
        updateGraphicalInterface();
    }

    /**
     * Updates the data points for the LineChart.
     */
    public void updateDataPoints() {
        for (Integer i : populationCounts.keySet()) {
            if (!myLines.containsKey(i)) {
                myLines.put(i, new XYChart.Series<Integer, Integer>());
            }
            XYChart.Series<Integer, Integer> curPoints = myLines.get(i);
            curPoints.getData().add(new XYChart.Data<Integer, Integer>(genNum, populationCounts.get(i)));
            //System.out.println(curPoints);

            myLines.put(i, curPoints);
        }
    }

    /**
     * Updates the color of the cells in the GUI.
     * Also allows the user to click on tiles to 
     * change their state. 
     */
    private void updateGraphicalInterface() {
        for (int i = 0; i < gridArrayOfCells.length; i++) {
            for (int j = 0; j < gridArrayOfCells[i].length; j++) {

                //System.out.println(gridArrayOfCells[i][j].getDesc());

                final Cell curCell = gridArrayOfCells[i][j];
                int cellState = curCell.getState();

                populationCounts.put(cellState, populationCounts.get(cellState) + 1);

                Rectangle existingTile = (Rectangle) getNodeByRowColumnIndex(i, j, gridNew);

                gridNew.getChildren().remove(existingTile);

                final Rectangle tile = new Rectangle(0, 0, GRID_CELL_SIZE, GRID_CELL_SIZE); 

                tile.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        int newState = (curCell.getState() + 1) % curCell.getMyNumPatchTypes();
                        curCell.setState(newState);
                        tile.setFill(curCell.getCorrespondingColor()); //move this before calculateNeighbors() happens
                    }
                });


                tile.setFill(curCell.getCorrespondingColor());

                gridNew.add(tile, j, i); //GridPane uses reversed coordinates
                curCell.updateCell();
            }
        }
    }

    /**
     * @param row
     * @param column
     * @param gridPane
     * @return Node at the row/column index of the GridPane
     * 
     * Grabs the Node at a specified row and column in a grid pane.
     */
    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }


    /**
     * Method that calls doAction on all 
     * cells in the grid.
     */
    private void doCellsAction() {
        for (int i = 0; i < gridArrayOfCells.length; i++) {
            for (int j = 0; j < gridArrayOfCells[i].length; j++) {
                gridArrayOfCells[i][j].setGrid(gridArrayOfCells);
                Cell curCell = gridArrayOfCells[i][j];
                curCell.doAction();
            }
        }
    }

    /**
     * Updates the numerical value of the Generation
     * Number and adds it to the GUI.
     */
    private void updateGenerationNumber() {
        genNum++;
        gridNew.getChildren().remove(generationNumber);
        generationNumber = new Text("Generation number: " + genNum);
        generationNumber.setFill(Color.WHITE);
        addGenerationNumber();
    }

    /**
     * Adds generation number to GUI.
     */
    private void addGenerationNumber() {
        gridNew.add(generationNumber, 0, numCols + 1);
    }

    /**
     * @param s
     * @param width
     * @param height
     * @return Scene representing the visible GUI.
     * 
     * One of the first methods called, it asks the user
     * for an XML file input and creates the subsequent GUI
     * based on that XML.
     */
    public Scene init (Stage s, int width, int height) {
        return askUserForInput(s);
    }

    /**
     * @param stage
     * @return chosen File
     * 
     * Allows the user to choose a .XML file, with
     * the default directory as the xmlFiles folder.
     */
    private File chooseFile(final Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML File", "*.xml"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") +"/src/xmlFiles"));
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }


    /**
     * @param stage
     * @return Scene representing new GUI view.
     * 
     * Sets up the first GUI that requests an XML file from the user.
     * Creates the new Grid based on that information.
     */
    private Scene askUserForInput(final Stage stage) {
        Scene scene = new Scene(new Group(), GUI_WIDTH, GUI_HEIGHT);

        GridPane grid = new GridPane();
        grid.setVgap(Vgap);
        grid.setHgap(Hgap);
        grid.setPadding(new Insets(InsetNumber, InsetNumber, InsetNumber, InsetNumber));
        Group root = (Group) scene.getRoot();

        root.getChildren().add(grid);

        Button submit = new Button("Submit");
        grid.add(submit, 11, 60);

        final Button openXMLButton = new Button("Open XML File");
        grid.add(openXMLButton, 7, 60);

        Text welcome = new Text("Welcome to Cell Society!");
        welcome.setLayoutX(layoutX);
        welcome.setLayoutY(layoutY);
        Text hello = new Text("Please select an XML File for your simulation.");
        Text info = new Text("Examples are provided for the proper format of the file.");
        Text names = new Text("Created by Chris Bernt, Marcus Cain, and Scotty Shaw.");
        root.getChildren().add(welcome);
        grid.add(hello, 5, 30, 20, 1);
        grid.add(info, 2, 31, 20, 1);
        grid.add(names, 2, 32, 20, 1);

        openXMLButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {

                        //keep everything in here

                        File file = chooseFile(stage);

                        if (file != null) {
                            xmlReader = new XMLReader(file);
                        }
                    }
                });

        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                try{
                    gridArrayOfCells = xmlReader.parseFile();					
                }
                catch(Exception e) {
                    System.out.println("Need to enter an XML File");
                }

                initializePopulationMap();
                createGrid(stage);
                shouldRun = true;
            }
        });
        return scene;
    }


    /**
     * @param stage
     * Creates the grid to represent the cells as well as the 
     * buttons as their functionality on the GUI.
     */
    private void createGrid(final Stage stage) {


        gridNew = new GridPane(); 

        gridNew.setMinSize(GUI_WIDTH, GUI_HEIGHT);
        numRows = xmlReader.numRows;
        numCols = xmlReader.numCols;
        GRID_CELL_SIZE = GUI_WIDTH / xmlReader.numCols;

        //
        //
        //^^^^^
        /**
         * ASSUMES N X N GRID!!!!!!!!
         */
        //
        //
        //
        //

        for (int i = 0; i < numCols; i++) {
            gridNew.getColumnConstraints().add(new ColumnConstraints(GRID_CELL_SIZE));
        }
        for (int i = 0; i < numRows; i++) {
            gridNew.getRowConstraints().add(new RowConstraints(GRID_CELL_SIZE));
        }

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                Rectangle r = new Rectangle(0, 0, GRID_CELL_SIZE, GRID_CELL_SIZE);
                //				Circle r = new Circle(GRID_CELL_SIZE / 2);
                r.setFill(Color.WHITE);
                gridNew.add(r, j, i);
            }
        }

        gridNew.setHgap(1);
        gridNew.setVgap(1);
        gridNew.setStyle("-fx-background-color: black");

        generationNumber = new Text("Generation number: " + genNum);
        generationNumber.setFill(Color.WHITE);

        Button pause = new Button("Pause");
        pause.setMinWidth(BUTTON_WIDTH);

        pause.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                shouldRun = false;
            }
        });

        Button resume = new Button("Resume");
        resume.setMinWidth(BUTTON_WIDTH);

        resume.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                shouldRun = true;
            }
        });

        Button reset = new Button("Reset");
        reset.setMinWidth(BUTTON_WIDTH);

        reset.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                shouldRun = true; //keep
                gridArrayOfCells = xmlReader.parseFile(); //keep
                genNum = 0;
                initializePopulationMap();
                initializePopulationGraph();
            }
        });

        Button quit = new Button("Quit");
        quit.setMinWidth(BUTTON_WIDTH);

        quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });

        Button step = new Button("Step");
        step.setMinWidth(BUTTON_WIDTH);
        step.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                shouldRun = false;
                updateCells();
            }
        });

        Button loadNew = new Button("New");
        loadNew.setMinWidth(BUTTON_WIDTH);
        loadNew.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                shouldRun = false;

                File file = chooseFile(stage);

                if (file != null) {
                    xmlReader = new XMLReader(file);
                    gridArrayOfCells = xmlReader.parseFile();
                    genNum = 0;
                }
                createGrid(stage);
                initializePopulationMap();
                initializePopulationGraph();
                shouldRun = true;
            }
        });

        fpsSlider = new Slider(1, 10, 1);
        fpsSlider.setValue(framesPerSecond);
        fpsSlider.setMajorTickUnit(1);
        fpsSlider.setSnapToTicks(true);
        fpsSlider.setMinWidth(BUTTON_WIDTH);
        fpsSlider.setMaxWidth(BUTTON_WIDTH);
        fpsSlider.setMajorTickUnit(1.0);
        fpsSlider.setShowTickMarks(false);
        fpsSlider.setSnapToTicks(true);

        int rightSide = numRows - (BUTTON_WIDTH / GRID_CELL_SIZE) - 1;
        int leftSide = 0;

        addGenerationNumber();
        gridNew.add(pause, leftSide, numCols + 2);
        gridNew.add(resume, leftSide, numCols + 3);
        gridNew.add(step, leftSide, numCols + 4);
        
        gridNew.add(fpsSlider, rightSide, numCols + 1);		
        gridNew.add(loadNew, rightSide, numCols + 2);		
        gridNew.add(reset, rightSide, numCols + 3);		
        gridNew.add(quit, rightSide, numCols + 4);

        initializePopulationGraph();

        Scene s = new Scene(gridNew);

        stage.setScene(s);
    }
}
