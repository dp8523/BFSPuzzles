package puzzles.tipover.model;

import solver.Configuration;
import util.Coordinates;
import util.Grid;

import java.util.ArrayList;
import java.util.List;

/**
 * DESCRIPTION
 * @author Eli Lurie
 * November 2021
 */
public class TipOverConfig implements Configuration {
    private Grid<Integer> board;
    private Coordinates cords;
    private final Coordinates goal;


    public TipOverConfig(Grid<Integer> board, Coordinates cords, Coordinates goal){
        this.board = board;
        this.cords = cords;
        this.goal = goal;
    }

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

    public boolean isSolution(){
        return cords.equals(goal);
    }

    @Override
    public int hashCode(){
        return board.hashCode() + cords.hashCode();
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof TipOverConfig newOther){
            return this.board.equals(newOther.board) && this.cords.equals(newOther.cords);
        }
        return false;
    }

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

    public Coordinates getCords() {
        return cords;
    }

    public Coordinates getGoal() {
        return goal;
    }

    public Grid<Integer> getBoard(){
        return board;
    }

    public void setBoard(Coordinates newCords, int num) {
        board.set(num, newCords);
    }

    public void setCords(Coordinates cords){
        this.cords = cords;
    }
}
