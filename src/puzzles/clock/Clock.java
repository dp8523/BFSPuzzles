package puzzles.clock;

import solver.ClockConfiguration;
import solver.Solver;

import static java.lang.Integer.parseInt;

/**
 * Main class for the "clock" puzzle.
 *
 * @author Eli Lurie
 */
public class Clock {

    /**
     * Run an instance of the clock puzzle.
     * @param args [0]: number of hours on the clock;
     *             [1]: starting time on the clock;
     *             [2]: goal time to which the clock should be set.
     */
    public static void main( String[] args ) {
        if ( args.length != 3 ) {
            System.out.println( "Usage: java Clock hours start end" );
        }
        else {
            System.out.println("Hours: " + args[0] + ", Start: " + args[1] + ", End: " + args[2]);
            ClockConfiguration initialConfig = new ClockConfiguration(parseInt(args[0]),
                    parseInt(args[2]), parseInt(args[1]));
            Solver.solve(initialConfig);
        }
    }
}
