package puzzles.lunarlanding.ptui;

import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Coordinates;
import util.Grid;
import util.Observer;

import java.util.Scanner;

/**
 * The PITUI view of the Lunar Landing puzzle.
 * @author Dara Prak
 * November 2021
 */
public class LunarLandingPTUI implements Observer<LunarLandingModel, Object> {

    private LunarLandingModel model;

    /**
     * Creates a LunarLandingModel using a file, sets itself as an observer, displays the board and updates the PTUI
     * @param filename
     */
    public LunarLandingPTUI(String filename)
    {
        this.model = new LunarLandingModel(filename);
        this.model.addObserver(this);
        displayBoard();
        update(this.model, "File loaded");
    }

    /**
     * Runs the PTUI with user input controlling the model
     */
    public void run()
    {
        Scanner in = new Scanner( System.in );
        boolean quit = false;
        while(!quit)
        {
            String line = in.nextLine();
            System.out.println(line);
            String[] command = line.split("\\s+");
            if(command.length > 0)
            {
                switch(command[0])
                {
                    case "load":
                        if(command.length != 2)
                        {
                            System.out.println("Illegal command");
                            help();
                        }else{
                            this.model.load(command[1]);
                            displayBoard();
                        }
                        break;
                    case "reload":
                        this.model.reload();
                        displayBoard();
                        break;
                    case "choose":
                        if(command.length != 3)
                        {
                            System.out.println("Illegal command");
                            help();
                        }else{
                            Coordinates cords = new Coordinates(command[1], command[2]);
                            this.model.choose(cords);
                        }
                        break;
                    case "go":
                        if(command.length != 2)
                        {
                            System.out.println("Illegal command");
                            help();
                        }else{
                            LunarLandingConfig oldConfig = this.model.getCurrentConfig();
                            this.model.go(command[1]);
                            LunarLandingConfig newConfig = this.model.getCurrentConfig();
                            if(!oldConfig.equals(newConfig))
                            {
                                displayBoard();
                            }
                        }
                        break;
                    case "hint":
                        LunarLandingConfig oldConfig = this.model.getCurrentConfig();
                        this.model.hint();
                        LunarLandingConfig newConfig = this.model.getCurrentConfig();
                        if(!oldConfig.equals(newConfig))
                        {
                            displayBoard();
                        }
                        break;
                    case "show":
                        displayBoard();
                        break;
                    case "help":
                        help();
                        break;
                    case "quit":
                        quit = true;
                        break;
                    default:
                        System.out.println("Illegal command");
                        help();
                }
            }
        }
    }

    /**
     * Prints the legal commands the user can type.
     */
    public void help()
    {
        System.out.println("Legal commands are...");
        System.out.println("\t> help : Show all commands.");
        System.out.println("\t> reload filename: Load the most recent file again.");
        System.out.println("\t> load filename: Load a new game board file. (1 argument)");
        System.out.println("\t> hint : Make the next move for me.");
        System.out.println("\t> show : Display the board.");
        System.out.println("\t> go {north|south|east|west}: Tell chosen character where to go. (1 argument)");
        System.out.println("\t> choose row column: Choose which character moves next. (2 arguments)");
        System.out.println("\t> quit");
    }

    /**
     * Prints the plain text representation of the grid.
     */
    public void displayBoard()
    {
        System.out.println(this.model.getCurrentConfig());
    }

    /**
     * Updates the PTUI with the model's new information and message.
     * @param lunarLandingModel this LunarLandingModel
     * @param announcement the message being sent for the PTUI to display
     */
    public void update(LunarLandingModel lunarLandingModel, Object announcement){
        if(!announcement.toString().equals(""))
        {
            System.out.println(announcement);
        }
    }

    /**
     * The main method to start the PTUI by loading a file.
     *
     * @param args the file name
     */
    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            System.err.println("Usage: java LunarLandingPTUI file debug");
        }else{
            LunarLandingPTUI ptui = new LunarLandingPTUI(args[0]);
            ptui.run();
        }
    }
}
