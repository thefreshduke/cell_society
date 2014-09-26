package cellsociety_team19;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The class that sets up the stage, initializes
 * the first scene and animation/frame details, and
 * runs the program as an Application.
 *
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 */

public class Initializer extends Application {
    private SimulationLoop mySimulation;
    private int guiSize = 400;

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

        mySimulation.start();
    }

    /**
     * @param args
     * Begins the simulation. Called in Main.java.
     */
    public void beginSimulation(String[] args) {
        launch(args);
    }
}
