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
    public static void solve(Configuration initialConfig){
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
                display(totalConfigs, uniqueConfigs, predecessor, current);
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
            display(totalConfigs, uniqueConfigs, null, null);
        }
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
}
