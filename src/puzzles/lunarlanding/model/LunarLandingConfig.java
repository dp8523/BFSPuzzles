package puzzles.lunarlanding.model;

import solver.Configuration;
import util.Coordinates;
import util.Grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Configuration that holds the current state of the game
 * @author Dara Prak
 * November 2021
 */

public class LunarLandingConfig implements Configuration {
    private Grid<String> currentGrid;
    private static Coordinates landerCords;
    private Coordinates explorerCords;
    // Map of the robots' Coordinates, and the string that represents them
    private Map<Coordinates, String> robotCordsMap;

    /**
     * Constructs the initial configuration from an input file
     * @param filename filename the name of the file to read from
     * @throws FileNotFoundException if the file is not found
     */
    public LunarLandingConfig(String filename) throws FileNotFoundException
    {
        try (Scanner in = new Scanner(new File(filename))) {
            int numRows = in.nextInt();
            int numCols = in.nextInt();
            this.currentGrid = new Grid<>("_", numRows, numCols);

            int landerRow = in.nextInt();
            int landerCol = in.nextInt();
            landerCords = new Coordinates(landerRow, landerCol);

            this.robotCordsMap = new HashMap<>();
            in.nextLine();
            String line = in.nextLine();
            while(!line.equals(""))
            {
                String[] fields = line.split("\\s+");
                String figure = fields[0];
                Coordinates figureCords = new Coordinates(fields[1], fields[2]);
                if(figure.equals("E"))
                {
                    this.explorerCords = figureCords;
                }else{
                    this.robotCordsMap.put(figureCords, figure);
                }
                line = in.nextLine();
            }
        } // try-with-resources, the file is closed automatically
        if(this.isSolution())
        {
            this.currentGrid.set("!E", landerCords);
            for(Map.Entry<Coordinates, String> entry: this.robotCordsMap.entrySet())
            {
                this.currentGrid.set(entry.getValue(), entry.getKey());
            }
        }else{
            this.currentGrid.set("E", this.explorerCords);
            for(Map.Entry<Coordinates, String> entry: this.robotCordsMap.entrySet())
            {
                if(entry.getKey().equals(landerCords))
                {
                    this.currentGrid.set("!"+entry.getValue(), entry.getKey());
                }else{
                    this.currentGrid.set(entry.getValue(), entry.getKey());
                }
            }
            if(!this.robotCordsMap.containsKey(landerCords))
            {
                this.currentGrid.set("!", landerCords);
            }
        }
    }

    /**
     * The copy constructor takes a config, other, and makes a deep copy of its instance data
     *
     * @param other the config to copy
     */
    public LunarLandingConfig(LunarLandingConfig other)
    {
        this.currentGrid = new Grid<>(other.currentGrid);
        int rowCopy = other.explorerCords.row();
        int colCopy = other.explorerCords.col();
        this.explorerCords = new Coordinates(rowCopy, colCopy);
        Map<Coordinates, String> mapCopy = new HashMap<>();
        mapCopy.putAll(other.robotCordsMap);
        this.robotCordsMap = mapCopy;
    }

    /**
     * Gets the Coordinates of a figure after moving as far north as possible before it collides with another figure.
     * If it goes off the grid, null is returned.
     * @param currentGrid the current arrangement of the grid
     * @param currentCords the Coordinates of the figure being moved
     * @param otherCords the map of Coordinates and string representations of the figures it can collide with
     * @return the new Coordinates of the figure
     */
    protected Coordinates getMaxNorth(Grid<String> currentGrid, Coordinates currentCords, Map<Coordinates, String> otherCords)
    {
        Coordinates newCords = currentCords.sum(Coordinates.Direction.NORTH.coords);
        while(currentGrid.legalCoords(newCords))
        {
            if(otherCords.containsKey(newCords))
            {
                return newCords.sum(Coordinates.Direction.SOUTH.coords);
            }
            newCords = newCords.sum(Coordinates.Direction.NORTH.coords);
        }
        return null;
    }

    /**
     * Gets the Coordinates of a figure after moving as far south as possible before it collides with another figure.
     * If it goes off the grid, null is returned.
     * @param currentGrid the current arrangement of the grid
     * @param currentCords the Coordinates of the figure being moved
     * @param otherCords the map of Coordinates and string representations of the figures it can collide with
     * @return the new Coordinates of the figure
     */
    protected Coordinates getMaxSouth(Grid<String> currentGrid, Coordinates currentCords, Map<Coordinates, String> otherCords)
    {
        Coordinates newCords = currentCords.sum(Coordinates.Direction.SOUTH.coords);
        while(currentGrid.legalCoords(newCords))
        {
            if(otherCords.containsKey(newCords))
            {
                return newCords.sum(Coordinates.Direction.NORTH.coords);
            }
            newCords = newCords.sum(Coordinates.Direction.SOUTH.coords);
        }
        return null;
    }

    /**
     * Gets the Coordinates of a figure after moving as far east as possible before it collides with another figure.
     * If it goes off the grid, null is returned.
     * @param currentGrid the current arrangement of the grid
     * @param currentCords the Coordinates of the figure being moved
     * @param otherCords the map of Coordinates and string representations of the figures it can collide with
     * @return the new Coordinates of the figure
     */
    protected Coordinates getMaxEast(Grid<String> currentGrid, Coordinates currentCords, Map<Coordinates, String> otherCords)
    {
        Coordinates newCords = currentCords.sum(Coordinates.Direction.EAST.coords);
        while(currentGrid.legalCoords(newCords))
        {
            if(otherCords.containsKey(newCords))
            {
                return newCords.sum(Coordinates.Direction.WEST.coords);
            }
            newCords = newCords.sum(Coordinates.Direction.EAST.coords);
        }
        return null;
    }

    /**
     * Gets the Coordinates of a figure after moving as far west as possible before it collides with another figure.
     * If it goes off the grid, null is returned.
     * @param currentGrid the current arrangement of the grid
     * @param currentCords the Coordinates of the figure being moved
     * @param otherCords the map of Coordinates and string representations of the figures it can collide with
     * @return the new Coordinates of the figure
     */
    protected Coordinates getMaxWest(Grid<String> currentGrid, Coordinates currentCords, Map<Coordinates, String> otherCords)
    {
        Coordinates newCords = currentCords.sum(Coordinates.Direction.WEST.coords);
        while(currentGrid.legalCoords(newCords))
        {
            if(otherCords.containsKey(newCords))
            {
                return newCords.sum(Coordinates.Direction.EAST.coords);
            }
            newCords = newCords.sum(Coordinates.Direction.WEST.coords);
        }
        return null;
    }

    /**
     * Gets a list of all possible neighbors of the configuration. It should include all legal possibilities of each
     * figure moving in each cardinal direction.
     * @return the configurations for the next possible step in the puzzle
     */
    public List<Configuration> getNeighbors()
    {
        List<Configuration> neighbors = new ArrayList<>();

        // does not move explorer north if one spot north has a figure or is out of bounds
        if(!this.robotCordsMap.containsKey(this.explorerCords.sum(Coordinates.Direction.NORTH.coords)) &&
                this.currentGrid.legalCoords(this.explorerCords.sum(Coordinates.Direction.NORTH.coords))) {
            LunarLandingConfig moveENorth = new LunarLandingConfig(this);
            moveENorth.currentGrid.set("_", moveENorth.explorerCords);
            moveENorth.explorerCords = getMaxNorth(moveENorth.currentGrid, moveENorth.explorerCords, moveENorth.robotCordsMap);
            // null means continuously moving north brought it out of bounds
            if (moveENorth.explorerCords != null) {
                if (moveENorth.isSolution()) {
                    moveENorth.currentGrid.set("!E", moveENorth.explorerCords);
                } else {
                    moveENorth.currentGrid.set("E", moveENorth.explorerCords);
                }
                neighbors.add(moveENorth);
            }
        }

        if(!this.robotCordsMap.containsKey(this.explorerCords.sum(Coordinates.Direction.SOUTH.coords)) &&
                this.currentGrid.legalCoords(this.explorerCords.sum(Coordinates.Direction.SOUTH.coords))) {
            LunarLandingConfig moveESouth = new LunarLandingConfig(this);
            moveESouth.currentGrid.set("_", moveESouth.explorerCords);
            moveESouth.explorerCords = getMaxSouth(moveESouth.currentGrid, moveESouth.explorerCords, moveESouth.robotCordsMap);
            if (moveESouth.explorerCords != null) {
                if (moveESouth.isSolution()) {
                    moveESouth.currentGrid.set("!E", moveESouth.explorerCords);
                } else {
                    moveESouth.currentGrid.set("E", moveESouth.explorerCords);
                }
                neighbors.add(moveESouth);
            }
        }

        if(!this.robotCordsMap.containsKey(this.explorerCords.sum(Coordinates.Direction.EAST.coords)) &&
                this.currentGrid.legalCoords(this.explorerCords.sum(Coordinates.Direction.EAST.coords))) {
            LunarLandingConfig moveEEast = new LunarLandingConfig(this);
            moveEEast.currentGrid.set("_", moveEEast.explorerCords);
            moveEEast.explorerCords = getMaxEast(moveEEast.currentGrid, moveEEast.explorerCords, moveEEast.robotCordsMap);
            if (moveEEast.explorerCords != null) {
                if (moveEEast.isSolution()) {
                    moveEEast.currentGrid.set("!E", moveEEast.explorerCords);
                } else {
                    moveEEast.currentGrid.set("E", moveEEast.explorerCords);
                }
                neighbors.add(moveEEast);
            }
        }

        if(!this.robotCordsMap.containsKey(this.explorerCords.sum(Coordinates.Direction.WEST.coords)) &&
                this.currentGrid.legalCoords(this.explorerCords.sum(Coordinates.Direction.WEST.coords))) {
            LunarLandingConfig moveEWest = new LunarLandingConfig(this);
            moveEWest.currentGrid.set("_", moveEWest.explorerCords);
            moveEWest.explorerCords = getMaxWest(moveEWest.currentGrid, moveEWest.explorerCords, moveEWest.robotCordsMap);
            if (moveEWest.explorerCords != null) {
                if (moveEWest.isSolution()) {
                    moveEWest.currentGrid.set("!E", moveEWest.explorerCords);
                } else {
                    moveEWest.currentGrid.set("E", moveEWest.explorerCords);
                }
                neighbors.add(moveEWest);
            }
        }

        for(Map.Entry<Coordinates, String> entry: this.robotCordsMap.entrySet())
        {
            // Creates and fills otherCords with all the figures except this robot
            Map<Coordinates, String> otherCords = new HashMap<>();
            otherCords.putAll(this.robotCordsMap);
            otherCords.remove(entry.getKey());
            otherCords.put(this.explorerCords, "E");

            if(!otherCords.containsKey(entry.getKey().sum(Coordinates.Direction.NORTH.coords)) &&
                    this.currentGrid.legalCoords(entry.getKey().sum(Coordinates.Direction.NORTH.coords))) {
                LunarLandingConfig moveRNorth = new LunarLandingConfig(this);
                if (entry.getKey().equals(landerCords)) {
                    moveRNorth.currentGrid.set("!", entry.getKey());
                } else {
                    moveRNorth.currentGrid.set("_", entry.getKey());
                }
                Coordinates newNorthCords = getMaxNorth(moveRNorth.currentGrid, entry.getKey(), otherCords);
                moveRNorth.robotCordsMap.put(newNorthCords, entry.getValue());
                moveRNorth.robotCordsMap.remove(entry.getKey());
                if (newNorthCords != null) {
                    if (newNorthCords.equals(landerCords)) {
                        moveRNorth.currentGrid.set("!" + entry.getValue(), newNorthCords);
                    } else {
                        moveRNorth.currentGrid.set(entry.getValue(), newNorthCords);
                    }
                    neighbors.add(moveRNorth);
                }
            }

            if(!otherCords.containsKey(entry.getKey().sum(Coordinates.Direction.SOUTH.coords)) &&
                    this.currentGrid.legalCoords(entry.getKey().sum(Coordinates.Direction.SOUTH.coords))) {
                LunarLandingConfig moveRSouth = new LunarLandingConfig(this);
                if (entry.getKey().equals(landerCords)) {
                    moveRSouth.currentGrid.set("!", entry.getKey());
                } else {
                    moveRSouth.currentGrid.set("_", entry.getKey());
                }
                Coordinates newSouthCords = getMaxSouth(moveRSouth.currentGrid, entry.getKey(), otherCords);
                moveRSouth.robotCordsMap.put(newSouthCords, entry.getValue());
                moveRSouth.robotCordsMap.remove(entry.getKey());
                if (newSouthCords != null) {
                    if (newSouthCords.equals(landerCords)) {
                        moveRSouth.currentGrid.set("!" + entry.getValue(), newSouthCords);
                    } else {
                        moveRSouth.currentGrid.set(entry.getValue(), newSouthCords);
                    }
                    neighbors.add(moveRSouth);
                }
            }

            if(!otherCords.containsKey(entry.getKey().sum(Coordinates.Direction.EAST.coords)) &&
                    this.currentGrid.legalCoords(entry.getKey().sum(Coordinates.Direction.EAST.coords))) {
                LunarLandingConfig moveREast = new LunarLandingConfig(this);
                if (entry.getKey().equals(landerCords)) {
                    moveREast.currentGrid.set("!", entry.getKey());
                } else {
                    moveREast.currentGrid.set("_", entry.getKey());
                }
                Coordinates newEastCords = getMaxEast(moveREast.currentGrid, entry.getKey(), otherCords);
                moveREast.robotCordsMap.put(newEastCords, entry.getValue());
                moveREast.robotCordsMap.remove(entry.getKey());
                if (newEastCords != null) {
                    if (newEastCords.equals(landerCords)) {
                        moveREast.currentGrid.set("!" + entry.getValue(), newEastCords);
                    } else {
                        moveREast.currentGrid.set(entry.getValue(), newEastCords);
                    }
                    neighbors.add(moveREast);
                }
            }

            if(!otherCords.containsKey(entry.getKey().sum(Coordinates.Direction.WEST.coords)) &&
                    this.currentGrid.legalCoords(entry.getKey().sum(Coordinates.Direction.WEST.coords))) {
                LunarLandingConfig moveRWest = new LunarLandingConfig(this);
                if (entry.getKey().equals(landerCords)) {
                    moveRWest.currentGrid.set("!", entry.getKey());
                } else {
                    moveRWest.currentGrid.set("_", entry.getKey());
                }
                Coordinates newWestCords = getMaxWest(moveRWest.currentGrid, entry.getKey(), otherCords);
                moveRWest.robotCordsMap.put(newWestCords, entry.getValue());
                moveRWest.robotCordsMap.remove(entry.getKey());
                if (newWestCords != null) {
                    if (newWestCords.equals(landerCords)) {
                        moveRWest.currentGrid.set("!" + entry.getValue(), newWestCords);
                    } else {
                        moveRWest.currentGrid.set(entry.getValue(), newWestCords);
                    }
                    neighbors.add(moveRWest);
                }
            }
        }
        return neighbors;
    }

    /**
     * Tells whether the current configuration is the solution, where the explorer is on the lander.
     * @return true if the explorer is on the lander, false otherwise.
     */
    public boolean isSolution()
    {
        return this.explorerCords.equals(landerCords);
    }

    /**
     * Tells whether this LunarLandingConfig equals another object. Two LunarLandingConfigs are equal if they
     * have the same grid arrangement.
     * @param other object to compare to
     * @return True if both are LunarLandingConfigs with matching grids, false otherwise.
     */
    @Override
    public boolean equals(Object other)
    {
        boolean result = false;
        if(other instanceof LunarLandingConfig)
        {
            LunarLandingConfig o = (LunarLandingConfig) other;
            result = this.currentGrid.equals(o.currentGrid);
        }
        return result;
    }

    /**
     * Calculates the hashcode of a LunarLandingConfig by using Grid's hashcode method
     * @return the integer of the LunarLandingConfig's hashcode
     */
    @Override
    public int hashCode()
    {
        return this.currentGrid.hashCode();
    }

    /**
     * Creates a string representation of the LunarLandingConfig's currentGrid
     * @return a string representing the grid
     */
    @Override
    public String toString()
    {
        String grid = "\n     ";
        for(int i=0; i<this.currentGrid.getNCols(); i++){
            grid += i + "  ";
        }
        grid += "\n   ";
        for(int i=0; i<this.currentGrid.getNCols(); i++){
            grid += "___";
        }
        grid += "\n";
        String[] gridRows = this.currentGrid.toString().split("\\|");
        for(int i=0; i<this.currentGrid.getNRows(); i++){
            grid += i + " |";
            // there is an extra empty leading element due to the format of currentGrid.toString
            String row = gridRows[i+1];
            String[] rowElements = row.split("\\s");
            // there is an extra empty leading element due to how rowElements split
            for (int j = 1; j < rowElements.length; j++) {
                if(rowElements[j].length() == 2)
                {
                    grid += " "+rowElements[j];
                }else{
                    grid += "  "+rowElements[j];
                }
            }
            grid += "\n";
        }
        return grid;
    }

    /**
     * Gets a map of all this LunarLandingConfig's figure Coordinates and matching string representations.
     * @return
     */
    public Map<Coordinates, String> getFigureCords()
    {
        Map<Coordinates, String> figureCords = new HashMap<>();
        figureCords.putAll(this.robotCordsMap);
        figureCords.put(this.explorerCords, "E");
        return figureCords;
    }

    /**
     * Gets the current grid of this LunarLandingConfig
     * @return the grid
     */
    public Grid<String> getCurrentGrid()
    {
        return this.currentGrid;
    }

    /**
     * Sets a specified position in this grid to the specified String value
     * @param value the String being set in the grid
     * @param cords the Coordinates where an element is being changed
     */
    public void setCurrentGrid(String value, Coordinates cords)
    {
        this.currentGrid.set(value, cords);
    }

    /**
     * Gets the Coordinates of the lander
     * @return the lander's Coordinates
     */
    public Coordinates getLanderCords()
    {
        return landerCords;
    }

    /**
     * Gets the current Coordinates of the explorer
     * @return the explorer's Coordinates
     */
    public Coordinates getExplorerCords()
    {
        return this.explorerCords;
    }

    /**
     * Sets the position of the explorer to the specified Coordinates
     * @param cords where the explorer is moved
     */
    public void setExplorerCords(Coordinates cords)
    {
        this.explorerCords = cords;
    }

    /**
     * Removes a pair from the map using the specified key
     * @param key Coordinates being removed with its String
     */
    public void removeRobotCordsMap(Coordinates key)
    {
        this.robotCordsMap.remove(key);
    }

    /**
     * Puts a new Coordinates and String pair into this robotCordsMap
     * @param key the Coordinates being added
     * @param value the corresponding String being added
     */
    public void putRobotCordsMap(Coordinates key, String value)
    {
        this.robotCordsMap.put(key, value);
    }
}
