package puzzles.lunarlanding.model;

import solver.Solver;
import util.Coordinates;
import util.Observer;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * A model of the rules of the LunarLanding puzzle that UIs can use.
 * @author Dara Prak
 * November 2021
 */
public class LunarLandingModel {

    private List<Observer< LunarLandingModel, Object >> observers;

    private LunarLandingConfig currentConfig;

    private String lastSuccessful;

    private Coordinates currentFigure;

    /**
     * Creates the model by loading the configuration and creating the list of observers
     * @param filename the name of the file to be loaded
     */
    public LunarLandingModel(String filename)
    {
        this.observers = new LinkedList<>();
        load(filename);
    }

    /**
     * Loads an inputted file to make a LunarLandingConfig and records the file name if successful.
     * @param filename the file name
     */
    public void load(String filename)
    {
        try{
            this.currentConfig = new LunarLandingConfig(filename);
            this.lastSuccessful = filename;
            announce("File loaded");
        }catch (FileNotFoundException e){
            this.currentConfig = null;
            announce("File could not be opened");
        }
    }

    /**
     * Reloads the last successful file.
     */
    public void reload()
    {
        load(this.lastSuccessful);
    }

    /**
     * The current figure being moved is selected by the given Coordinates
     * @param cords the Coordinates of the figure being selected
     */
    public void choose(Coordinates cords)
    {
        if(this.currentConfig == null)
        {
            announce("Load a file first");
        }else{
            if(this.currentConfig.getFigureCords().containsKey(cords))
            {
                this.currentFigure = cords;
                announce("");
            }else{
                this.currentFigure = null;
                announce("No figure at that position");
            }
        }
    }

    /**
     * Moves the current figure in the specified direction if appropriate in the game's current state
     * @param direction a String representing the cardinal direction to move
     */
    public void go(String direction)
    {
        if(this.currentConfig == null)
        {
            announce("Load a file first");
        }
        else if(this.currentFigure == null)
        {
            announce("Choose a figure first");
        }
        else if(!(direction.equalsIgnoreCase("north") || direction.equalsIgnoreCase("south") ||
                direction.equalsIgnoreCase("east") || direction.equalsIgnoreCase("west")))
        {
            announce("Legal directions are\n[north, south, east, west]");
        }else{
            Coordinates newCords = null;
            LunarLandingConfig newConfig = new LunarLandingConfig(this.currentConfig);
            Map<Coordinates, String> otherCords = new HashMap<>();
            otherCords.putAll(newConfig.getFigureCords());
            otherCords.remove(this.currentFigure);
            switch (direction.toLowerCase(Locale.ROOT))
            {
                case "north":
                    newCords = newConfig.getMaxNorth(newConfig.getCurrentGrid(), this.currentFigure, otherCords);
                    break;
                case "south":
                    newCords = newConfig.getMaxSouth(newConfig.getCurrentGrid(), this.currentFigure, otherCords);
                    break;
                case "east":
                    newCords = newConfig.getMaxEast(newConfig.getCurrentGrid(), this.currentFigure, otherCords);
                    break;
                case "west":
                    newCords = newConfig.getMaxWest(newConfig.getCurrentGrid(), this.currentFigure, otherCords);
            }
            if(newCords != null && !newCords.equals(this.currentFigure))
            {
                if(this.currentFigure.equals(newConfig.getLanderCords()))
                {
                    newConfig.setCurrentGrid("!", this.currentFigure);
                }else{
                    newConfig.setCurrentGrid("_", this.currentFigure);
                }
                if(newCords.equals(newConfig.getLanderCords()))
                {
                    newConfig.setCurrentGrid("!"+newConfig.getFigureCords().get(this.currentFigure), newCords);
                }else{
                    newConfig.setCurrentGrid(newConfig.getFigureCords().get(this.currentFigure), newCords);
                }
                if(newConfig.getExplorerCords().equals(this.currentFigure))
                {
                    newConfig.setExplorerCords(newCords);
                }else{
                    String figureString = newConfig.getFigureCords().get(this.currentFigure);
                    newConfig.removeRobotCordsMap(this.currentFigure);
                    newConfig.putRobotCordsMap(newCords, figureString);
                }
                this.currentConfig = newConfig;
                if(newConfig.isSolution())
                {
                    announce("YOU WON!");
                }else{
                    announce("");
                }
            }else{
                announce("Illegal move");
            }
            this.currentFigure = null;
        }
    }

    /**
     * Finds the next move leading to the shortest solution using Solver.
     */
    public void hint()
    {
        if(this.currentConfig == null)
        {
            announce("Load a file first");
        }
        else if(this.currentConfig.isSolution())
        {
            announce("Current board is already solved");
        }else{
            LunarLandingConfig newConfig = (LunarLandingConfig) Solver.getHint(this.currentConfig);
            if(newConfig == null)
            {
                announce("Unsolvable board");
            }else{
                this.currentConfig = newConfig;
                if(this.currentConfig.isSolution())
                {
                    announce("I WON!");
                }else{
                    announce("");
                }
            }
        }
    }

    /**
     * Gets the current configuration
     * @return the configuration
     */
    public LunarLandingConfig getCurrentConfig()
    {
        return this.currentConfig;
    }

    /**
     * Add a new observer to the list for this model
     * @param obs an object that wants an
     *            {@link Observer#update(Object, Object)}
     *            when something changes here
     */
    public void addObserver(Observer<LunarLandingModel, Object> obs)
    {
        this.observers.add(obs);
    }

    /**
     * Tells the observers the model was updated, and a message indicating the state of the game.
     * @param arg the message being sent
     */
    private void announce(String arg)
    {
        for(var obs : this.observers)
        {
            obs.update( this, arg);
        }
    }
}
