package puzzles.water;

import solver.Solver;
import solver.WaterConfiguration;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Eli Lurie
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main( String[] args ) {
        if ( args.length < 2 ) {
            System.out.println(
                    ( "Usage: java Water amount bucket1 bucket2 ..." )
            );
        }
        else {
            int amount = parseInt(args[0]);
            List<Integer> buckets = new ArrayList<>();
            List<Integer> current = new ArrayList<>();
            for(int i=1; i<args.length; i++){
                buckets.add(parseInt(args[i]));
                current.add(0);
            }
            System.out.println("Amount: " + amount + ", Buckets: " + buckets);
            WaterConfiguration initialConfig = new WaterConfiguration(buckets, current, amount);
            Solver.solve(initialConfig);
        }
    }
}
