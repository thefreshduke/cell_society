package cellsociety_team19;

import java.io.File;

import simulationTypes.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
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

public class SimulationLoop {

	/*2d arraylist of cell(gametype) to keep track of grid*/

	private int framesPerSecond = 1;
	private int numRows;
	private int numCols;
	private int GRID_CELL_SIZE;
	private GridPane gridNew;
	private static final int GUI_HEIGHT = 400;
	private static final int GUI_WIDTH = 400;
	private Cell[][] gridArrayOfCells;
	private boolean shouldRun = false;
	private int genNum = 0;
	private Text generationNumber;
	private XMLReader xmlReader; //keep
	private Slider fpsSlider;
	private KeyFrame frame;
	private Timeline animation;
	private int BUTTON_WIDTH = 70;

	public void start() {	
		frame = makeFrame();
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
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
				updateCells();			
				updateFPS();
			}
		}
	};

	private void updateFPS() {
		framesPerSecond = (int) Math.round(fpsSlider.getValue());		
		animation.stop();
		animation.getKeyFrames().remove(frame);
		frame = makeFrame();
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	public void updateCells() {
		updateGenerationNumber();
		doCellsAction();
		updateGraphicalInterface();
	}

	private void updateGraphicalInterface() {
		for (int i = 0; i < gridArrayOfCells.length; i++) {
			for (int j = 0; j < gridArrayOfCells[i].length; j++) {

				//System.out.println(gridArrayOfCells[i][j].getDesc());

				Cell curCell = gridArrayOfCells[i][j];

				Rectangle rec = new Rectangle(0, 0, GRID_CELL_SIZE, GRID_CELL_SIZE); 
				rec.setFill(curCell.retrieveCorrespondingColorFromMap());

				gridNew.add(rec, j, i); //GridPane uses reversed coordinates
				curCell.updateCell();
			}
		}
	}

	private void doCellsAction() {
		for (int i = 0; i < gridArrayOfCells.length; i++) {
			for (int j = 0; j < gridArrayOfCells[i].length; j++) {
				gridArrayOfCells[i][j].setGrid(gridArrayOfCells);
				Cell curCell = gridArrayOfCells[i][j];
				curCell.doAction();
			}
		}
	}

	private void updateGenerationNumber() {
		genNum++;
		gridNew.getChildren().remove(generationNumber);
		generationNumber = new Text("Generation number: " + genNum);
		generationNumber.setFill(Color.WHITE);
		gridNew.add(generationNumber, 0, numCols + 1);
	}

	public Scene init (Stage s, int width, int height) {
		return askUserForInput(s);
	}

	private File chooseFile(final Stage stage) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML File", "*.xml"));
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") +"/src/xmlFiles"));
		File file = fileChooser.showOpenDialog(stage);
		return file;
	}

	private Scene askUserForInput(final Stage stage) {
		Scene scene = new Scene(new Group(), GUI_WIDTH, GUI_HEIGHT);

		GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		Group root = (Group) scene.getRoot();

		root.getChildren().add(grid);

		Button submit = new Button("Submit");
		grid.add(submit, 11, 60);

		final Button openXMLButton = new Button("Open XML File");
		grid.add(openXMLButton, 7, 60);

		Text welcome = new Text("Welcome to Cell Society!");
		welcome.setLayoutX(120);
		welcome.setLayoutY(120);
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
				try {
					gridArrayOfCells = xmlReader.parseFile();
					createGrid(stage);
					shouldRun = true;
				}
				catch(Exception e) {
					System.out.println("Need to enter an XML File");
				}
			}
		});
		return scene;
	}

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
				shouldRun = true;
			}
		});

		fpsSlider = new Slider(1, 5, 1);
		fpsSlider.setValue(framesPerSecond);
		fpsSlider.setMajorTickUnit(1);
		fpsSlider.setSnapToTicks(true);
		fpsSlider.setMinWidth(BUTTON_WIDTH);
		fpsSlider.setMaxWidth(BUTTON_WIDTH);
		fpsSlider.setMajorTickUnit(1.0);
		fpsSlider.setShowTickMarks(true);
		fpsSlider.setSnapToTicks(true);

		int rightSide = numRows - (BUTTON_WIDTH / GRID_CELL_SIZE) - 1;
		int leftSide = 0;

		gridNew.add(generationNumber, leftSide, numCols + 1);
		gridNew.add(pause, leftSide, numCols+2);
		gridNew.add(resume, leftSide, numCols + 3);
		gridNew.add(step, leftSide, numCols + 4);

		gridNew.add(fpsSlider, rightSide, numCols+1);		
		gridNew.add(loadNew, rightSide, numCols + 2);		
		gridNew.add(reset, rightSide, numCols + 3);		
		gridNew.add(quit, rightSide, numCols + 4);

		Scene s = new Scene(gridNew);

		stage.setScene(s);
	}
}
