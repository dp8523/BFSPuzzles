package solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Configuration for water data
 */
public class WaterConfiguration implements Configuration{
    private final List<Integer> max;
    private final List<Integer> current;
    private final int solution;

    /**
     * Constructor for a new WaterConfiguration object
     * @param max capacity of buckets
     * @param current how full buckets are
     * @param solution amount of water to find
     */
    public WaterConfiguration(List<Integer> max, List<Integer> current, int solution){
        this.max = max;
        this.current = current;
        this.solution = solution;
    }

    /**
     * Get the configurations that can be found from the current configuration by either filling a bucket,
     * draining a bucket, or pouring a bucket into another bucket
     * @return list of configurations
     */
    public List<Configuration> getNeighbors(){
        List<Configuration> toReturn = new ArrayList<>();
        for(int i=0; i<current.size(); i++){
            //fill
            List<Integer> newCurrent = new ArrayList<>(current);
            newCurrent.set(i, max.get(i));
            WaterConfiguration newConfig = new WaterConfiguration(max, newCurrent, solution);
            toReturn.add(newConfig);
            //drain
            List<Integer> newCurrent2 = new ArrayList<>(current);
            newCurrent2.set(i, 0);
            WaterConfiguration newConfig2 = new WaterConfiguration(max, newCurrent2, solution);
            toReturn.add(newConfig2);
            //pour
            for(int j=0; j<current.size(); j++){
                List<Integer> toPour = new ArrayList<>(current);
                if(j != i){
                    toPour.set(j, current.get(j) + current.get(i));
                    if(toPour.get(j) <= max.get(j)){
                        toPour.set(i, 0);
                    }
                    else{
                        toPour.set(i, toPour.get(j) - max.get(j));
                        toPour.set(j, max.get(j));
                    }
                    WaterConfiguration newConfig3 = new WaterConfiguration(max, toPour, solution);
                    toReturn.add(newConfig3);
                }
            }
        }
        return toReturn;
    }

    /**
     * Determine if one of the buckets contains the wanted amount of water
     * @return true if solution, false if otherwise
     */
    public boolean isSolution(){
        for(int i : current){
            if(i == solution){
                return true;
            }
        }
        return false;
    }

    /**
     * Overrides the hashCode method
     * @return hashCode
     */
    @Override
    public int hashCode(){
        int total = 0;
        for(int i : current){
            total += i;
        }
        return total;
    }

    /**
     * Overrides the equals method
     * @param other object to compare to
     * @return true if other is equal, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof WaterConfiguration newOther){
            for(int i=0; i<newOther.current.size(); i++){
                if(!Objects.equals(newOther.current.get(i), this.current.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Overrides the toString method
     * @return String of the current list
     */
    @Override
    public String toString(){
        return current.toString();
    }
}
