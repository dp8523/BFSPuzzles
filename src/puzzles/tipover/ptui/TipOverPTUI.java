package puzzles.tipover.ptui;

import puzzles.tipover.model.TipOverModel;
import util.Observer;

import java.util.Locale;
import java.util.Scanner;

/**
 * The PTUI to display the Tip Over game.
 * @author Eli Lurie
 * November 2021
 */
public class TipOverPTUI implements Observer<TipOverModel, Object> {
    private TipOverModel model;

    /**
     * Creates a new model and sets itself as an observer.
     * @param filename file name
     */
    public TipOverPTUI(String filename){
        model = new TipOverModel(filename);
        model.addObserver(this);
        update(model, null);
    }

    /**
     * Displays the current board.
     */
    public void displayBoard(){
        System.out.println(model.toString());
    }

    /**
     * Runs the PTUI, letting the user make different commands.
     */
    public void run(){
        Scanner in = new Scanner(System.in);
        boolean go = true;
        while(go){
            String line = in.nextLine();
            String[] words = line.split("\\s+");
            if(words.length > 0){
                switch (words[0]) {
                    case "move" -> model.move(words[1].toUpperCase(Locale.ROOT));
                    case "reload" -> model.reloadFile();
                    case "load" -> model.loadFile(words[1]);
                    case "hint" -> model.getHint();
                    case "show" -> displayBoard();
                    case "quit" -> go = false;
                    case "help" -> help();
                    default -> System.out.println("Invalid command (enter 'help' for a list of commands)");
                }
            }
        }
    }

    /**
     * Displays all valid user commands.
     */
    public void help(){
        System.out.println("Valid command are: ");
        System.out.println("\t> help : show all commands");
        System.out.println("\t> move {north|south|east|west} : move in given direction");
        System.out.println("\t> reload : load the most recent file again");
        System.out.println("\t> load {filename} : load a new game board file");
        System.out.println("\t> hint : make the next move for me");
        System.out.println("\t> show : show the game board");
        System.out.println("\t> quit : quit the game");
    }

    /**
     * Initiates a new PTUI.
     * @param args filename
     */
    public static void main( String[] args ) {
        if(args.length == 1) {
            TipOverPTUI ptui = new TipOverPTUI(args[0]);
            ptui.run();
        }
        else{
            System.out.println("Invalid arguments");
        }
    }

    /**
     * Updates the PTUI.
     * @param thisModel the model
     * @param announce message to display
     */
    public void update(TipOverModel thisModel, Object announce){
        if(announce != null){
            System.out.println(announce);
        }
        displayBoard();
    }
}
