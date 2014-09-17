package cellsociety_team19;

import java.io.File;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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

	private static final int GUI_HEIGHT = 400;
	private static final int GUI_WIDTH = 400;

	private GridPane grid;

	private Cell[][] gridArrayOfCells;

	private boolean shouldRun = false;

	private int genNum = 0;
	private Text generationNumber;
	
	Slider fpsSlider;
	
	KeyFrame frame;
	Timeline animation;
	
	/**
	 * Create the game's frame
	 */
	public KeyFrame start() {
		return new KeyFrame(Duration.millis(1000 / framesPerSecond), oneFrame);
	}

	private EventHandler<ActionEvent> oneFrame = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evt) {
			if (shouldRun) {
				updateCells();
				framesPerSecond = (int) Math.round(fpsSlider.getValue());
			}
		}
	};
	
	public void updateCells() {
		updateGenerationNumber();
		setUpGridForCells();
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

				grid.add(rec, j, i); //GridPane uses reversed coordinates
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

	private void setUpGridForCells() {
		for (int i = 0; i < gridArrayOfCells.length; i++) {
			for (int j = 0; j < gridArrayOfCells[i].length; j++) {
				gridArrayOfCells[i][j].setGrid(gridArrayOfCells);
			}
		}
	}

	private void updateGenerationNumber() {
		genNum++;
		grid.getChildren().remove(generationNumber);
		generationNumber = new Text("Generation number: " + genNum);
		generationNumber.setFill(Color.WHITE);
		grid.add(generationNumber, 1, numCols + 1);
	}

	public Scene init (Stage s, int width, int height) {
		/* instantiate arraylist of simulation game types */

		//stage = s;

		/*run method to obtain user input, initalize gridsize/xml file */
		//Scene sceneForUserInput = askUserForInput();

		//GridPane grid = new GridPane();


		//Scene scene = new Scene(grid, width, height, Color.WHITE);

		return askUserForInput(s);
	}

	private Scene askUserForInput(final Stage stage) {
		Scene scene = new Scene(new Group(), 400, 400);
		final Cell[] simulations = new Cell[] {
				new TreeCell(),
				new PredPreyCell(),
				new SegCell(),
				new LifeCell()
				//add here	
		};

		final HashMap<Cell, Integer> map = new HashMap<Cell, Integer>();
		for (int i = 0; i < simulations.length; i++) {
			map.put(simulations[i], i);
		}

		final ComboBox<Cell> simulationBox = new ComboBox<Cell>();
		simulationBox.getItems().addAll(simulations);

		final Label labelForGridSizeRow = new Label("How many rows?");
		final Label labelForGridSizeCol = new Label("How many columns?");

		final TextField textForRow = new TextField();
		final TextField textForCol = new TextField();
		Button submit = new Button("Submit");

		GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.add(new Label("Choose a simulation: "), 0, 0);
		grid.add(simulationBox, 1, 0);
		grid.add(labelForGridSizeRow, 0, 3);
		grid.add(labelForGridSizeCol, 0, 4);
		grid.add(textForRow, 1, 3);
		grid.add(textForCol, 1, 4);
		Group root = (Group) scene.getRoot();
		root.getChildren().add(grid);
		//grid.setGridLinesVisible(true);
		grid.add(submit, 1, 30);

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open XML File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML File", "*.xml*"));

		final Button openXMLButton = new Button("Open XML File");
		grid.add(openXMLButton, 0, 30);

		openXMLButton.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						File file = fileChooser.showOpenDialog(stage);
						if (file != null) {
							System.out.println(file + " has been opened");
						}
					}
				});

		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//set the grid instance variable to what the user typed in
				numRows = Integer.parseInt(textForRow.getText()); // surround in try/catch for errors?
				numCols = Integer.parseInt(textForCol.getText());
				//System.out.println("row: " + numRows + ", col: " + numCols);

				Cell choice = (Cell) simulationBox.getValue();
				int choiceIndex = map.get(choice);

				/*instantiate 2d array of game type to keep track of grid*/
				gridArrayOfCells = new Cell[numRows] [numCols];

				for (int x = 0; x < numRows; x++) {
					for (int y = 0; y < numCols; y++) {
						gridArrayOfCells[x][y] = (simulations[choiceIndex].makeNewCell(x, y, 1));
					}
				}

				//startTreeSimDebugVersion();
				//startSegSimDebugVersion();
				//startPredPreySimDebugVersion();
				startGameOfLifeSimDebugVersion();

				//				int c = 0;
				//				for (int i = 0; i < numRows; i++) {
				//					for (int j = 0; j < numCols; j++) {
				//						gridArrayOfCells[i][j] = new SegCell(i, j, (c % 3));
				//						c++;
				//					}
				//				}

				//gridArrayOfCells[numRows / 2][numCols / 2] = new TreeCell(numRows / 2, numCols / 2, 2);

				/*for (int i = 0; i < gridArrayOfCells.length; i++) {
					for (int j = 0; j < gridArrayOfCells[i].length; j++) {
						System.out.println(gridArrayOfCells[i][j].toString());
					}
				}*/

				/* exit the scene */
				//stage.close();

				createGrid(stage);
				shouldRun = true;
			}
		});
		return scene;
	}

	private void startTreeSimDebugVersion() {
		for (int x = 0; x < gridArrayOfCells.length; x++) {
			for (int y = 0; y < gridArrayOfCells[x].length; y++) {
				gridArrayOfCells[x] [y] = new TreeCell(x, y, 1);
			}
		}
		gridArrayOfCells[numRows / 2] [numCols / 2] = new TreeCell(numRows / 2, numCols / 2, 2);
		gridArrayOfCells[numRows / 2] [numCols / 2 + 1] = new TreeCell(numRows / 2, numCols / 2 + 1, 2);
		gridArrayOfCells[numRows / 2 + 1] [numCols / 2] = new TreeCell(numRows / 2 + 1, numCols / 2, 2);
	}

	private void startSegSimDebugVersion() {
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				int state = 0;

				double r = Math.random();
				if (r < 0.2) {
					state = 0;
				}
				else if (r < 0.4) {
					state = 1;
				}
				else if (r < 0.6) {
					state = 2;
				}
				else if (r < 0.8) {
					state = 3;
				}
				else {
					state = 4;
				}
				gridArrayOfCells[x][y] = new SegCell(x, y, state);
			}
		}
	}

	private void startPredPreySimDebugVersion() {
		for (int x = 0; x < gridArrayOfCells.length; x++) {
			for (int y = 0; y < gridArrayOfCells[x].length; y++) {
				gridArrayOfCells[x][y] = new PredPreyCell(x, y, 1);
			}
		}
//		gridArrayOfCells[numRows / 2] [numCols / 2] = new PredPreyCell(numRows / 2, numCols / 2, 1);
//		gridArrayOfCells[3] [2] = new PredPreyCell(3, 2, 1);
//		gridArrayOfCells[0] [0] = new PredPreyCell(0, 0, 1);

		gridArrayOfCells[2] [1] = new PredPreyCell(2, 1, 2);
		gridArrayOfCells[1] [2] = new PredPreyCell(1, 2, 2);
		gridArrayOfCells[2] [2] = new PredPreyCell(2, 2, 2);
		gridArrayOfCells[2] [3] = new PredPreyCell(2, 3, 2);
		gridArrayOfCells[3] [2] = new PredPreyCell(3, 2, 2);
	}

	private void startGameOfLifeSimDebugVersion() {
		for (int x = 0; x < gridArrayOfCells.length; x++) {
			for (int y = 0; y < gridArrayOfCells[x].length; y++) {
				gridArrayOfCells[x][y] = new LifeCell(x, y, 0);
			}
		}
		gridArrayOfCells[0] [0] = new LifeCell(0, 0, 1);
		gridArrayOfCells[1] [0] = new LifeCell(1, 0, 1);
		gridArrayOfCells[0] [1] = new LifeCell(0, 1, 1);
		gridArrayOfCells[1] [1] = new LifeCell(1, 1, 1);
		gridArrayOfCells[2] [0] = new LifeCell(2, 0, 1);
		gridArrayOfCells[0] [2] = new LifeCell(0, 2, 1);
		gridArrayOfCells[numRows / 2] [numCols / 2] = new LifeCell(numRows / 2, numCols / 2, 1);
		gridArrayOfCells[numRows / 2 + 1] [numCols / 2] = new LifeCell(numRows / 2 + 1, numCols / 2, 1);
		gridArrayOfCells[numRows / 2 - 1] [numCols / 2] = new LifeCell(numRows / 2 - 1, numCols / 2, 1);
		gridArrayOfCells[numRows - 1] [numCols - 1] = new LifeCell(numRows - 1, numCols - 1, 1);
		gridArrayOfCells[3] [3] = new LifeCell(3, 3, 1);
		gridArrayOfCells[4] [4] = new LifeCell(4, 4, 1);
		gridArrayOfCells[3] [4] = new LifeCell(3, 4, 1);
	}

	private void createGrid(Stage stage) {
		grid = new GridPane();

		grid.setMinSize(GUI_WIDTH, GUI_HEIGHT);
		GRID_CELL_SIZE = GUI_WIDTH / numCols;

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
			grid.getColumnConstraints().add(new ColumnConstraints(GRID_CELL_SIZE));
		}
		for (int i = 0; i < numRows; i++) {
			grid.getRowConstraints().add(new RowConstraints(GRID_CELL_SIZE));
		}
		
		grid.setHgap(1);
		grid.setVgap(1);
		grid.setStyle("-fx-background-color: black");

		generationNumber = new Text("Generation number: " + genNum);
		generationNumber.setFill(Color.WHITE);

		Button pause = new Button("Pause");
		pause.setMinWidth(70);
		pause.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				shouldRun = false;
			}
		});

		Button resume = new Button("Resume");
		resume.setMinWidth(70);
		resume.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				shouldRun = true;
			}
		});

		Button reset = new Button("Reset");
		reset.setMinWidth(70);
		reset.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//startTreeSimDebugVersion();
				//startSegSimDebugVersion();
				startPredPreySimDebugVersion();
				//startGameOfLifeSimDebugVersion();
				genNum = 0;
			}
		});

		Button quit = new Button("Quit");
		quit.setMinWidth(70);
		quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.exit(0);
			}
		});
		
		Button step = new Button("Step");
		step.setMinWidth(70);
		step.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				shouldRun = false;
				updateCells();
			}
		});
		
		Button loadNew = new Button("New");
		loadNew.setMinWidth(70);
		loadNew.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				//run xml loader	
			}
		});
			
		fpsSlider = new Slider(1, 5, 1);
		fpsSlider.setValue(framesPerSecond);
		fpsSlider.setMajorTickUnit(1);
		fpsSlider.setSnapToTicks(true);
		fpsSlider.setMinWidth(70);
		
		int rightSide = numRows - (70 / GRID_CELL_SIZE) - 2;
		
		grid.add(generationNumber, 1, numCols + 1);
		grid.add(fpsSlider, rightSide, numCols+1);
		grid.add(pause, 1, numCols+2);
		grid.add(loadNew, rightSide, numCols + 2);
		grid.add(resume, 1, numCols + 3);
		grid.add(reset, rightSide, numCols + 3);
		grid.add(step, 1, numCols + 4);
		grid.add(quit, rightSide, numCols + 4);
		
		Scene s = new Scene(grid);
		stage.setScene(s);
	}
}
