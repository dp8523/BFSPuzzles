package solver;

import java.util.List;

/**
 * Configuration abstraction for the solver algorithm
 *
 * @author Eli Lurie
 */
public interface Configuration {
    /**
     * Get the neighbors of the configuration
     * @return list of configurations
     */
    List<Configuration> getNeighbors();

    /**
     * Determines if the configuration is the solution
     * @return true if is solution, else false
     */
    boolean isSolution();

    /**
     * Overrides the hash code method
     * @return hashCode
     */
    int hashCode();

    /**
     * Overrides the equals method
     * @param other object to compare to
     * @return true if equal, false if not
     */
    boolean equals(Object other);

    /**
     * Overrides the toString method
     * @return String
     */
    String toString();
}
