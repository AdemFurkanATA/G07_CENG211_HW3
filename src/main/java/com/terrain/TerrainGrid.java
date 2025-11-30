package com.terrain;

import com.interfaces.ITerrainObject;
import com.utils.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the terrain grid for the Sliding Penguins game.
 * Manages a 2D grid structure using Lists and ArrayLists.
 * Demonstrates the use of Lists and ArrayLists as required in the homework.
 */
public class TerrainGrid {

    private List<List<ITerrainObject>> grid;
    private int size;

    /**
     * Constructor for TerrainGrid.
     *
     * @param size The size of the grid (e.g., 10 for a 10x10 grid)
     */
    public TerrainGrid(int size) {
        this.size = size;
        this.grid = new ArrayList<>();
        initializeGrid();
    }

    /**
     * Initializes the grid with empty cells.
     * Each cell is initialized to null, representing an empty space.
     */
    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            List<ITerrainObject> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                row.add(null);  // Empty cell
            }
            grid.add(row);
        }
    }

    /**
     * Gets the size of the grid.
     *
     * @return The grid size
     */
    public int getSize() {
        return size;
    }

    /**
     * Places an object on the grid at the specified position.
     *
     * @param object The ITerrainObject to place
     * @param position The position to place it at
     * @return true if placement was successful, false if position is invalid
     */
    public boolean placeObject(ITerrainObject object, Position position) {
        if (!isValidPosition(position)) {
            return false;
        }

        grid.get(position.getRow()).set(position.getColumn(), object);
        return true;
    }

    /**
     * Removes an object from the grid at the specified position.
     *
     * @param position The position to clear
     * @return The removed object, or null if position was empty
     */
    public ITerrainObject removeObject(Position position) {
        if (!isValidPosition(position)) {
            return null;
        }

        ITerrainObject removed = grid.get(position.getRow()).get(position.getColumn());
        grid.get(position.getRow()).set(position.getColumn(), null);
        return removed;
    }

    /**
     * Gets the object at the specified position.
     *
     * @param position The position to check
     * @return The ITerrainObject at that position, or null if empty or invalid
     */
    public ITerrainObject getObjectAt(Position position) {
        if (!isValidPosition(position)) {
            return null;
        }

        return grid.get(position.getRow()).get(position.getColumn());
    }

    /**
     * Checks if a position is valid (within grid bounds).
     *
     * @param position The position to check
     * @return true if position is valid, false otherwise
     */
    public boolean isValidPosition(Position position) {
        return position.isValid(size);
    }

    /**
     * Checks if a position is empty (contains no object).
     *
     * @param position The position to check
     * @return true if position is empty, false if occupied or invalid
     */
    public boolean isEmpty(Position position) {
        if (!isValidPosition(position)) {
            return false;
        }

        return getObjectAt(position) == null;
    }

    /**
     * Checks if a position is on the edge of the grid.
     *
     * @param position The position to check
     * @return true if position is on an edge, false otherwise
     */
    public boolean isEdge(Position position) {
        if (!isValidPosition(position)) {
            return false;
        }

        return position.isEdge(size);
    }

    /**
     * Clears the entire grid, removing all objects.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid.get(i).set(j, null);
            }
        }
    }

    /**
     * Gets a copy of the grid structure.
     *
     * @return A copy of the 2D grid
     */
    public List<List<ITerrainObject>> getGrid() {
        return grid;
    }

    /**
     * Displays the grid in a formatted manner.
     * Shows shorthand notation for each object.
     */
    public void display() {
        String separator = "-------------------------------------------------------------";
        System.out.println(separator);

        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                ITerrainObject obj = grid.get(i).get(j);
                String cell = (obj == null) ? "    " : " " + obj.getShorthand() + " ";
                System.out.print(cell + "|");
            }
            System.out.println();
            System.out.println(separator);
        }
    }

    /**
     * Counts the number of objects currently on the grid.
     *
     * @return The total number of objects
     */
    public int countObjects() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid.get(i).get(j) != null) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Finds all empty positions on the grid.
     *
     * @return A list of empty Positions
     */
    public List<Position> getEmptyPositions() {
        List<Position> emptyPositions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid.get(i).get(j) == null) {
                    emptyPositions.add(new Position(i, j));
                }
            }
        }

        return emptyPositions;
    }

    /**
     * Finds all empty edge positions on the grid.
     *
     * @return A list of empty edge Positions
     */
    public List<Position> getEmptyEdgePositions() {
        List<Position> emptyEdgePositions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Position pos = new Position(i, j);
                if (grid.get(i).get(j) == null && pos.isEdge(size)) {
                    emptyEdgePositions.add(pos);
                }
            }
        }

        return emptyEdgePositions;
    }
}