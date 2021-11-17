package puzzles.tipover;

import puzzles.tipover.model.TipOverConfig;
import solver.Solver;
import util.Coordinates;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author Eli Lurie
 * November 2021
 */
public class TipOver {

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
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
            int[][] board = new int[height][width];
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    board[i][j] = scanner.nextInt();
                }
            }
            TipOverConfig initialConfig = new TipOverConfig(board, position, goal);
            Solver.solve(initialConfig);
        }
    }
}
