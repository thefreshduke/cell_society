package cellsociety_team19;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The class that runs the animation. It sets up the stage and initializes
 * the first scene and animation/frame details. It is very similar to Professor
 * Duvall's starter code form GitHub.
 *
 * @author Chris Bernt
 */

public class Main extends Application
{
	private SimulationLoop mySimulation;
	private int guiSize = 300;

	/**
	 * Set things up at the beginning.
	 */
	@Override
	public void start (Stage s) {
		
		s.setTitle("Cell Society");
		
		//create new SimulationLoop that runs most everything
		mySimulation = new SimulationLoop();
		
		Scene scene = mySimulation.init(s, guiSize, guiSize);
		s.setScene(scene);
		s.show();
		
		// sets up the loop
		
		
		
		KeyFrame frame = mySimulation.start();
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	/**
	 * The method that begins the Game. Called from Main.java in its main method.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
