package cellsociety_team19;

import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimulationLoop {
	
	private int framesPerSecond = 5;
	
	/**
	 * Create the game's frame
	 */
	public KeyFrame start () {
		return new KeyFrame(Duration.millis(1000/framesPerSecond), oneFrame);
	}
	
	
	private EventHandler<ActionEvent> oneFrame = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evt) {
			System.out.println("test");
		}
	};
	
	public Scene init (Stage s, int width, int height) {
		
		GridPane grid = new GridPane();
		
		
		Scene scene = new Scene(grid, width, height, Color.WHITE);

		return scene;
	}
	
	
}
