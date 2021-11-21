package puzzles.lunarlanding;

import puzzles.lunarlanding.model.LunarLandingConfig;
import solver.Solver;
import util.Coordinates;

import java.io.FileNotFoundException;

/**
 * DESCRIPTION
 * @author Dara Prak
 * November 2021
 */
public class LunarLanding {

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
     */

    public static void main( String[] args ) throws FileNotFoundException {
        if(args.length != 1)
        {
            System.err.println("Usage: java LunarLanding file debug");
        }else{
            LunarLandingConfig initialConfig = new LunarLandingConfig(args[0]);
            Solver.solve(initialConfig);
        }
    }
}
