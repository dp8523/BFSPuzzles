package puzzles.tipover.model;

import solver.Solver;
import util.Coordinates;
import util.Grid;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * A model to be used by the GUI and PTUI.
 * @author Eli Lurie
 * November 2021
 */
public class TipOverModel {

    private TipOverConfig currentConfig;
    private List<Observer<TipOverModel, Object>> observerList;
    private String filename;

    /**
     * Creates a new model and loads the inputted file.
     * @param filename file to load
     */
    public TipOverModel(String filename){
        observerList = new LinkedList<>();
        loadFile(filename);
    }

    /**
     * Moves the player in the inputted direction on the board.
     * @param direction direction to move
     */
    public void move(String direction){
        if(currentConfig == null){
            announce("Load a file first");
        }
        else if(currentConfig.isSolution()){
            announce("You Win!");
        }
        else if(!Coordinates.Direction.isDirection(direction)) {
            announce("Invalid Direction");
        }
        else{
            Coordinates.Direction newDir = Coordinates.Direction.valueOf(direction);
            Coordinates cords = currentConfig.getCords();
            Grid<Integer> board = currentConfig.getBoard();
            Coordinates newCords = cords.sum(newDir.coords);
            if(!board.legalCoords(newCords)){
                announce("Invalid Move");
            }
            else{
                if(board.get(newCords) != 0){
                    currentConfig.setCords(newCords);
                    announce(null);
                }
                else if(board.get(cords) != 1){
                    int value = board.get(cords);
                    boolean canFlip = true;
                    for(int i=1; i<=value; i++){
                        Coordinates toEval = cords.sum(newDir.coords.multiply(i));
                        if(!board.legalCoords(toEval) ||
                                board.get(toEval) != 0){
                            canFlip = false;
                        }
                    }
                    if(!canFlip){
                        announce("Invalid Move");
                    }
                    else{
                        currentConfig.setBoard(cords, 0);
                        for(int i=1; i<= value; i++){
                            Coordinates toSet = cords.sum(newDir.coords.multiply(i));
                            currentConfig.setBoard(toSet, 1);
                        }
                        currentConfig.setCords(newCords);
                        announce(null);
                    }
                }
                else{
                    announce("Invalid Move");
                }
            }
        }
    }

    /**
     * Returns the current board.
     * @return board
     */
    public Grid<Integer> getBoard(){
        return currentConfig == null ? null : currentConfig.getBoard();
    }

    /**
     * Returns the current coordinates.
     * @return coordinates
     */
    public Coordinates getCords(){
        return currentConfig == null ? null : currentConfig.getCords();
    }

    /**
     * Returns the goal coordinates.
     * @return goal
     */
    public Coordinates getGoal(){
        return currentConfig == null ? null : currentConfig.getGoal();
    }

    /**
     * Determines whether the current configuration is the solution.
     * @return true if solution, false if not
     */
    public boolean isSolution(){
        return currentConfig != null && currentConfig.isSolution();
    }

    /**
     * Reloads the current file.
     */
    public void reloadFile(){
        loadFile(this.filename);
    }

    /**
     * Loads a new file.
     * @param filename file name
     */
    public void loadFile(String filename){
        try{
            Scanner scanner = new Scanner(new File(filename));
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
            currentConfig = new TipOverConfig(board, position, goal);
            this.filename = filename;
            announce(null);
        }
        catch(FileNotFoundException ex){
            currentConfig = null;
            announce("File not found");
        }
    }

    /**
     * Uses the solver to find the next move that will lead to the shortest solution.
     */
    public void getHint(){
        if(currentConfig == null){
            announce("Load a file first");
        }
        else if(currentConfig.isSolution()){
            announce("You Win!");
        }
        else {
            TipOverConfig newConfig = (TipOverConfig) Solver.getHint(currentConfig);
            if (newConfig == null) {
                announce("No solutions");
            } else {
                currentConfig = newConfig;
                announce(null);
            }
        }
    }

    /**
     * Returns a string representation of the configuration.
     * @return string
     */
    @Override
    public String toString(){
        return currentConfig == null ? "" : currentConfig.toString();
    }

    /**
     * Adds a new observer to the observer list.
     * @param observer observer object
     */
    public void addObserver(Observer<TipOverModel, Object> observer){
        observerList.add(observer);
    }

    /**
     * Tells the observers that the model has been updated.
     * @param arg message to display
     */
    public void announce(String arg){
        for(var observer : observerList){
            observer.update(this, arg);
        }
    }
}
