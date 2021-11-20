package solver;

import java.util.*;

/**
 * This class contains a universal algorithm to find a path from a starting
 * configuration to a solution, if one exists
 *
 * @author Eli Lurie
 */
public class Solver {
    /**
     * Performs a breadth first search starting with the inputted configuration.
     * @param initialConfig starting configuration
     */
    public static Configuration solver(Configuration initialConfig, boolean toDisplay){
        Queue<Configuration> queue = new LinkedList<>();
        queue.offer(initialConfig);

        Map<Configuration, Configuration> predecessor = new HashMap<>();
        predecessor.put(initialConfig, null);

        int totalConfigs = 0;
        int uniqueConfigs = 0;

        boolean foundSolution = false;

        while(!queue.isEmpty()){
            Configuration current = queue.remove();
            if(current.isSolution()){
                if(toDisplay) {
                    display(totalConfigs, uniqueConfigs, predecessor, current);
                }
                else{
                    return nextMove(predecessor, current);
                }
                foundSolution = true;
                break;
            }
            else {
                for (Configuration neighbor : current.getNeighbors()) {
                    totalConfigs++;
                    if (!predecessor.containsKey(neighbor)) {
                        uniqueConfigs++;
                        predecessor.put(neighbor, current);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        if(!foundSolution) {
            if(toDisplay) {
                display(totalConfigs, uniqueConfigs, null, null);
            }
            else{
                return null;
            }
        }
        return null;
    }

    /**
     * Finds and displays the path to the final configuration using the inputted map.
     * @param totalConfigs total number of configurations computed
     * @param uniqueConfigs number of unique configurations found
     * @param configMap map containing configurations as keys, and the configurations they were found from as values
     * @param lastConfig the solution configuration
     */
    public static void display(int totalConfigs, int uniqueConfigs, Map<Configuration, Configuration> configMap, Configuration lastConfig){
        System.out.println("Total configs: " + totalConfigs);
        System.out.println("Unique configs: " + uniqueConfigs);

        if(lastConfig != null){
            List<Configuration> path = new LinkedList<>();
            path.add(0, lastConfig);
            Configuration current = configMap.get(lastConfig);
            while(current != null){
                path.add(0, current);
                current = configMap.get(current);
            }

            for(int i=0; i<path.size(); i++){
                System.out.println("Step " + i + ": " + path.get(i));
            }
        }
        else{
            System.out.println("No solution");
        }
    }

    /**
     * Calls the solver function to display the resulting path.
     * @param initialConfig starting configuration
     */
    public static void solve(Configuration initialConfig){
        solver(initialConfig, true);
    }

    /**
     * Calls the solver function to return the first configuration in the path.
     * @param initialConfig starting configuration
     * @return next move to solve the puzzle
     */
    public static Configuration getHint(Configuration initialConfig){
        return solver(initialConfig, false);
    }

    /**
     * Returns the first move that will result in the shortest solution.
     * @param configMap contains the path of configurations
     * @param lastConfig the solution configuration
     * @return configuration for the next move
     */
    public static Configuration nextMove(Map<Configuration, Configuration> configMap, Configuration lastConfig){
        if(lastConfig == null){
            return null;
        }
        else{
            List<Configuration> path = new LinkedList<>();
            path.add(0, lastConfig);
            Configuration current = configMap.get(lastConfig);
            while(current != null){
                path.add(0, current);
                current = configMap.get(current);
            }
            return path.get(1);
        }
    }
}
