package cellsociety_team19;


/**
 * @author Chris Bernt, Marcus Cain, Scotty Shaw
 * Class whose sole purpose is to start the simulation.
 *
 */
public class Main {
    
	/**
	 * @param args
	 * Runs the simulation.
	 */
	public static void main(final String[] args) {
		final Initializer init = new Initializer();
		init.beginSimulation(args);
	}
}
