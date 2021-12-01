package puzzles.tipover;

import puzzles.tipover.model.TipOverConfig;
import solver.Solver;
import util.Coordinates;
import util.Grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Used to run the Tip Over solver.
 * @author Eli Lurie
 * November 2021
 */
public class TipOver {

    /**
     * Creates a new tip over configuration and inputs it to the solver to run.
     * @param args file name
     */
    public static void main( String[] args ) throws FileNotFoundException {
        if(args.length < 1){
            System.out.println("Usage: java TipOver filename");
        }
        else{
            Scanner scanner = new Scanner(new File(args[0]));
            int height = scanner.nextInt();
            int width = scanner.nextInt();
            Coordinates position = new Coordinates(scanner.nextInt(), scanner.nextInt());
            Coordinates goal = new Coordinates(scanner.nextInt(), scanner.nextInt());
            Grid<Integer> board = new Grid<>(0, height, width);
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    board.set(scanner.nextInt(), i, j);
                }
            }
            TipOverConfig initialConfig = new TipOverConfig(board, position, goal);
            Solver.solve(initialConfig);
        }
    }
}
