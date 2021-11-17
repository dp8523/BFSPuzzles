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
    private final Grid<Num> board;
    private record Num(int val){}
    private final Coordinates cords;
    private final Coordinates goal;


    public TipOverConfig(int[][] board, Coordinates cords, Coordinates goal){
        Num initialNum = new Num(0);
        this.board = new Grid<>(initialNum, board.length, board[0].length);
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                Num toSet = new Num(board[i][j]);
                this.board.set(toSet, i, j);
            }
        }
        this.cords = cords;
        this.goal = goal;
    }

    public TipOverConfig(Grid<Num> board, Coordinates cords, Coordinates goal){
        this.board = board;
        this.cords = cords;
        this.goal = goal;
    }

    public List<Configuration> getNeighbors(){
        List<Configuration> toReturn = new ArrayList<>();
        for(Coordinates.Direction d : Coordinates.CARDINAL_NEIGHBORS){
            Coordinates newCords = cords.sum(d.coords);
            if(board.legalCoords(newCords) && board.get(newCords).val != 0){
                TipOverConfig newConfig = new TipOverConfig(board, newCords, goal);
                toReturn.add(newConfig);
            }
        }

        return toReturn;
    }

    public boolean isSolution(){
        return cords.equals(goal);
    }

    public int hashCode(){
        return board.hashCode() + cords.hashCode();
    }

    public boolean equals(Object other){
        if(other instanceof TipOverConfig newOther){
            return this.board.equals(newOther.board) && this.cords.equals(newOther.cords);
        }
        return false;
    }

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
                int value = board.get(thisCords).val;
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
}
