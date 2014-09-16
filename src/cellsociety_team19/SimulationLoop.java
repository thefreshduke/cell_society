package cellsociety_team19;



import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

	private int framesPerSecond = 3;
	private int numRows;
	private int numCols;
	private final static int GRID_CELL_SIZE = 20;
	private GridPane grid;

	private Cell[][] gridArrayOfCells;
	
	private boolean shouldRun = false;
	
	private int genNum = 0;
	private Text generationNumber;

	/**
	 * Create the game's frame
	 */
	public KeyFrame start () {
		return new KeyFrame(Duration.millis(1000/framesPerSecond), oneFrame);
	}


	private EventHandler<ActionEvent> oneFrame = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evt) {
			if(shouldRun){
				updateCells();
				genNum++;
				
				
				grid.getChildren().remove(generationNumber);
				generationNumber = new Text("Generation number: " + genNum);
				generationNumber.setFill(Color.WHITE);
				grid.add(generationNumber, 1, numCols + 3);
			}
		}
	};
	
	
	public void updateCells(){
		
		
		for(int i = 0; i < gridArrayOfCells.length; i++){
			for(int j = 0; j < gridArrayOfCells[i].length; j++){
				gridArrayOfCells[i][j].setGrid(gridArrayOfCells);
			}
		}
		
		for(int i = 0; i < gridArrayOfCells.length; i++){
			for(int j = 0; j < gridArrayOfCells[i].length; j++){
				gridArrayOfCells[i][j].setGrid(gridArrayOfCells);
				Cell curCell = gridArrayOfCells[i][j];
				curCell.doAction();
				
			}
		}

		for(int i = 0; i < gridArrayOfCells.length; i++){
			for(int j = 0; j < gridArrayOfCells[i].length; j++){
				
				//System.out.println(gridArrayOfCells[i][j].getDesc());
				
				Cell curCell = gridArrayOfCells[i][j];
				
				Rectangle rec = new Rectangle(0, 0, GRID_CELL_SIZE, GRID_CELL_SIZE); 
				rec.setFill(curCell.getStateColor());
				
				grid.add(rec, j, i); //GridPane uses reversed coordinates

				
				curCell.updateCell();
			}
		}

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

		Scene scene = new Scene(new Group(), 400,400);

		final Cell[] sims = new Cell[] {
				new TreeCell(),
				new PredPreyCell(),
				new SegCell()
				//add here	
		};

		final HashMap<Cell, Integer> map = new HashMap<Cell, Integer>();
		for(int i =0; i < sims.length; i++){
			map.put(sims[i], i);
		}


		final ComboBox<Cell> simulationBox = new ComboBox<Cell>();
		simulationBox.getItems().addAll(sims);



		final Label labelForGridSizeRow = new Label("How many Rows?");
		final Label labelForGridSizeCol = new Label("How many Cols");


		final TextField textForRow = new TextField();
		final TextField textForCol = new TextField();
		Button submit = new Button("Submit");

		GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.add(new Label("What Kind of Simulation: "), 0, 0);
		grid.add(simulationBox,1,0);
		grid.add(labelForGridSizeRow, 0,3);
		grid.add(labelForGridSizeCol, 0,4);
		grid.add(textForRow,1,3);
		grid.add(textForCol,1,4);
		Group root = (Group)scene.getRoot();
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
				numRows = Integer.parseInt(textForRow.getText()); // surround in try/catch for errors
				numCols = Integer.parseInt(textForCol.getText());
				//System.out.println("row: " + numRows + ", col: " + numCols);
				
				Cell choice = (Cell) simulationBox.getValue();
				int choiceIndex = map.get(choice);

				/*instantiate 2d array of game type to keep track of grid*/
				gridArrayOfCells = new Cell[numRows][numCols];
				for(int i = 0;i<numRows;i++){
					for(int j=0;j<numCols;j++){
						gridArrayOfCells[i][j] = (sims[choiceIndex].makeNew(i, j, 1));
					}
				}
				
				
				startSegSimDebugVersion();
				
		
				//startTreeSimDebugVersion();



				createGrid(stage);
				shouldRun = true;
			}

			
		});
		return scene;
	}
	
	private void startTreeSimDebugVersion() {
		gridArrayOfCells[numRows/2][numCols/2] = new TreeCell(numRows/2,numCols/2,2);
	}
	
	private void startSegSimDebugVersion() {
		for(int i = 0;i<numRows;i++){
			for(int j=0;j<numCols;j++){
				int state = 0;
				
				double r = Math.random();
				if( r < 0.2) state = 0;
				else if(r < 0.4) state = 1;
				else if(r < 0.6) state = 2;
				else if(r < .8) state = 3;
				else{
					state = 4;
				}
				
				gridArrayOfCells[i][j] = new SegCell(i, j, state);
			}
		}
	}

	private void createGrid(Stage stage) {
		grid = new GridPane();

		for(int i =0;i<numCols;i++) {
			grid.getColumnConstraints().add(new ColumnConstraints(GRID_CELL_SIZE));
		}
		for(int i=0;i<numRows;i++) {
			grid.getRowConstraints().add(new RowConstraints(GRID_CELL_SIZE));
		}
		for(int i = 0; i < numCols; i++){
			for(int j = 0; j < numRows; j++){

				Rectangle r = new Rectangle(0, 0, GRID_CELL_SIZE, GRID_CELL_SIZE);
				r.setFill(Color.WHITE);
				grid.add(r, j, i);
				
			}
		}
		
		grid.setHgap(1);
		grid.setVgap(1);
		grid.setStyle("-fx-background-color: black");
		
		
		generationNumber = new Text("Generation number: " + genNum);
		generationNumber.setFill(Color.WHITE);
		grid.add(generationNumber, 1, numCols + 3);

		Button pause = new Button("Pause");
		pause.setMinWidth(70);
		grid.add(pause, 4, numCols + 5);
		
		pause.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				shouldRun = false;
				
			}
			
		});
		
		Button resume = new Button("Resume");
		resume.setMinWidth(70);
		grid.add(resume, 4, numCols + 7);
		
		resume.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				shouldRun = true;
				
			}
			
		});
		
		Button reset = new Button("Reset");
		reset.setMinWidth(70);
		grid.add(reset, 4, numCols + 9);
		
		reset.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				startSegSimDebugVersion();
				//startTreeSimDebugVersion();
				genNum = 0;
				
			}
			
		});
		
		Button quit = new Button("Quit");
		quit.setMinWidth(70);
		grid.add(quit, 4, numCols + 11);
		
		quit.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				System.exit(0);
				
			}
			
		});
		
		Scene s = new Scene(grid);
		stage.setScene(s);
	}
}
