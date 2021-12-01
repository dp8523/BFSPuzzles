package puzzles.tipover.model;

import solver.Configuration;
import util.Coordinates;
import util.Grid;

import java.util.ArrayList;
import java.util.List;

/**
 * A configuration of the tip over board.
 * @author Eli Lurie
 * November 2021
 */
public class TipOverConfig implements Configuration {
    private Grid<Integer> board;
    private Coordinates cords;
    private final Coordinates goal;

    /**
     * Creates a new configuration.
     * @param board the game board
     * @param cords current coordinates
     * @param goal coordinates of the goal
     */
    public TipOverConfig(Grid<Integer> board, Coordinates cords, Coordinates goal){
        this.board = board;
        this.cords = cords;
        this.goal = goal;
    }

    /**
     * Returns a list of all the possible neighbors of the current configuration.
     * @return list of neighbors
     */
    public List<Configuration> getNeighbors(){
        List<Configuration> toReturn = new ArrayList<>();
        for(Coordinates.Direction d : Coordinates.CARDINAL_NEIGHBORS){
            Coordinates newCords = cords.sum(d.coords);
            if(board.legalCoords(newCords) && board.get(newCords) != 0){
                TipOverConfig newConfig = new TipOverConfig(board, newCords, goal);
                toReturn.add(newConfig);
            }
        }

        int value = board.get(cords);
        if(value != 0 && value != 1){
            for(Coordinates.Direction d : Coordinates.CARDINAL_NEIGHBORS){
                boolean canFlip = true;
                for(int i=1; i<=value; i++){
                    Coordinates newCords = cords.sum(d.coords.multiply(i));
                    if(!board.legalCoords(newCords) || board.get(newCords) != 0){
                        canFlip = false;
                    }
                }
                if(canFlip) {
                    Grid<Integer> newBoard = new Grid<>(board);
                    Coordinates newCords = cords.sum(d.coords);
                    newBoard.set(0, cords);
                    for(int i=1; i<= value; i++){
                        Coordinates toSet = cords.sum(d.coords.multiply(i));
                        newBoard.set(1, toSet);
                    }
                    TipOverConfig newConfig = new TipOverConfig(newBoard, newCords, goal);
                    toReturn.add(newConfig);
                }
            }
        }

        return toReturn;
    }

    /**
     * Returns whether the current coordinates are the solution.
     * @return true if solution, false otherwise
     */
    public boolean isSolution(){
        return cords.equals(goal);
    }

    /**
     * Returns the hashcode of the configuration
     * @return hash code
     */
    @Override
    public int hashCode(){
        return board.hashCode() + cords.hashCode();
    }

    /**
     * Determines whether the current configuration is equal to the inputted configuration.
     * @param other object to compare to
     * @return true is equal, false if not
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof TipOverConfig newOther){
            return this.board.equals(newOther.board) && this.cords.equals(newOther.cords);
        }
        return false;
    }

    /**
     * Returns a string representation of the configuration.
     * @return string
     */
    @Override
    public String toString(){
        String toReturn = "\n     ";
        for(int i=0; i<board.getNCols(); i++){
            toReturn += i + "  ";
        }
        toReturn += "\n   ";
        for(int i=0; i<board.getNCols(); i++){
            toReturn += "___";
        }
        toReturn += "\n";
        for(int i=0; i<board.getNRows(); i++){
            toReturn += i + " | ";
            for(int j=0; j<board.getNCols(); j++){
                Coordinates thisCords = new Coordinates(i, j);
                int value = board.get(thisCords);
                if(thisCords.equals(cords)){
                    toReturn += "*";
                }
                else if(thisCords.equals(goal)){
                    toReturn += "!";
                }
                else{
                    toReturn += " ";
                }

                if(value == 0){
                    toReturn += "_ ";
                }
                else{
                    toReturn += value + " ";
                }
            }
            toReturn += "\n";
        }
        return toReturn;
    }

    /**
     * Returns the current coordinates.
     * @return coordinates
     */
    public Coordinates getCords() {
        return cords;
    }

    /**
     * Returns the goal coordinates.
     * @return coordinates of goal
     */
    public Coordinates getGoal() {
        return goal;
    }

    /**
     * Returns the current board.
     * @return grid
     */
    public Grid<Integer> getBoard(){
        return board;
    }

    /**
     * Sets a specific spot of the board to a new number.
     * @param newCords coordinates to change
     * @param num number to set
     */
    public void setBoard(Coordinates newCords, int num) {
        board.set(num, newCords);
    }

    /**
     * Sets the current coordinates to new coordinates.
     * @param cords coordinates
     */
    public void setCords(Coordinates cords){
        this.cords = cords;
    }
}
