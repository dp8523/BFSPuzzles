package puzzles.lunarlanding;

import puzzles.lunarlanding.model.LunarLandingConfig;
import solver.Solver;
import util.Coordinates;

import java.io.FileNotFoundException;

/**
 * Runs the self-solving LunarLanding puzzle
 * @author Dara Prak
 * November 2021
 */
public class LunarLanding {
    /**
     * Creates the initial LunarLandingConfig from a file and runs it through the Solver
     * @param args the file name
     * @throws FileNotFoundException if the file is not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        if(args.length != 1)
        {
            System.err.println("Usage: java LunarLanding file debug");
        }else{
            LunarLandingConfig initialConfig = new LunarLandingConfig(args[0]);
            Solver.solve(initialConfig);
        }
    }
}
