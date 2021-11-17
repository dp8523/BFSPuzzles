package solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for clock data
 */
public class ClockConfiguration implements Configuration{
    private final int max;
    private final int solution;
    private final int current;

    /**
     * Constructor for a new ClockConfiguration object
     * @param max max value of clock
     * @param solution number to find
     * @param current current number
     */
    public ClockConfiguration(int max, int solution, int current){
        this.max = max;
        this.solution = solution;
        this.current = current;
    }

    /**
     * Get the configurations that the current number is connected to: 1 greater and 1 less
     * @return list of configurations
     */
    public List<Configuration> getNeighbors(){
        int small = current-1;
        if(small < 1){
            small = max;
        }
        int large = current+1;
        if(large > max){
            large = 1;
        }
        ClockConfiguration newSmall = new ClockConfiguration(max, solution, small);
        ClockConfiguration newLarge = new ClockConfiguration(max, solution, large);
        List<Configuration> toReturn = new ArrayList<>();
        toReturn.add(newSmall);
        toReturn.add(newLarge);
        return toReturn;
    }

    /**
     * Determine if the current value is equal to the solution
     * @return true if equal, false otherwise
     */
    public boolean isSolution(){
        return solution == current;
    }

    /**
     * Overrides the toString method
     * @return current
     */
    @Override
    public String toString(){
        return String.valueOf(current);
    }

    /**
     * Overrides the hashCode method
     * @return value of current
     */
    @Override
    public int hashCode(){
        return current;
    }

    /**
     * Overrides the equals method
     * @param other object to compare to
     * @return true if other is equal, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof ClockConfiguration){
            return ((ClockConfiguration) other).current == this.current;
        }
        return false;
    }
}
