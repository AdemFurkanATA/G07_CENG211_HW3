package com.terrain;

import com.interfaces.ITerrainObject;
import com.utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the terrain grid for the Sliding Penguins game.
 */
public class TerrainGrid {

    private final List<List<ITerrainObject>> grid;
    private final int size;

    /**
     * Constructor for TerrainGrid.
     * Uses final fields to prevent reassignment and ensure immutability of references.
     * @param size The size of the grid (e.g., 10 for a 10x10 grid)
     * @throws IllegalArgumentException if size is less than 1
     */
    public TerrainGrid(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Grid size must be at least 1, got: " + size);
        }

        this.size = size;
        this.grid = new ArrayList<>();
        initializeGrid();
    }

    /**
     * Initializes the grid with empty cells.
     * Each cell is initialized to null, representing an empty space.
     * Creates a completely new 2D structure to prevent external manipulation.
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
     * @return The grid size
     */
    public int getSize() {
        return size;
    }

    /**
     * Places an object on the grid at the specified position.
     * @param object The ITerrainObject to place (can be null to clear a cell)
     * @param position The position to place it at (must not be null)
     * @return true if placement was successful, false if position is invalid
     * @throws IllegalArgumentException if position is null
     */
    public boolean placeObject(ITerrainObject object, Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        Position safeCopy = new Position(position);

        if (!isValidPosition(safeCopy)) {
            return false;
        }

        grid.get(safeCopy.getRow()).set(safeCopy.getColumn(), object);
        return true;
    }

    /**
     * Removes an object from the grid at the specified position.
     * @param position The position to clear (must not be null)
     * @return The removed object, or null if position was empty or invalid
     * @throws IllegalArgumentException if position is null
     */
    public ITerrainObject removeObject(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        Position safeCopy = new Position(position);

        if (!isValidPosition(safeCopy)) {
            return null;
        }

        ITerrainObject removed = grid.get(safeCopy.getRow()).get(safeCopy.getColumn());
        grid.get(safeCopy.getRow()).set(safeCopy.getColumn(), null);
        return removed;
    }

    /**
     * Gets the object at the specified position.
     * @param position The position to check (must not be null)
     * @return The ITerrainObject at that position, or null if empty or invalid
     * @throws IllegalArgumentException if position is null
     */
    public ITerrainObject getObjectAt(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        Position safeCopy = new Position(position);

        if (!isValidPosition(safeCopy)) {
            return null;
        }

        return grid.get(safeCopy.getRow()).get(safeCopy.getColumn());
    }

    /**
     * Checks if a position is valid (within grid bounds).
     * @param position The position to check (must not be null)
     * @return true if position is valid, false otherwise
     * @throws IllegalArgumentException if position is null
     */
    public boolean isValidPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        // Use defensive copy for validation
        Position safeCopy = new Position(position);
        return safeCopy.isValid(size);
    }

    /**
     * Checks if a position is empty (contains no object).
     * @param position The position to check (must not be null)
     * @return true if position is empty, false if occupied or invalid
     * @throws IllegalArgumentException if position is null
     */
    public boolean isEmpty(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        // Defensive copy
        Position safeCopy = new Position(position);

        if (!isValidPosition(safeCopy)) {
            return false;
        }

        return getObjectAt(safeCopy) == null;
    }

    /**
     * Checks if a position is on the edge of the grid.
     * @param position The position to check (must not be null)
     * @return true if position is on an edge, false otherwise
     * @throws IllegalArgumentException if position is null
     */
    public boolean isEdge(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        // Defensive copy
        Position safeCopy = new Position(position);

        if (!isValidPosition(safeCopy)) {
            return false;
        }

        return safeCopy.isEdge(size);
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
     * @return An unmodifiable deep copy of the 2D grid structure
     */
    public List<List<ITerrainObject>> getGrid() {
        // Create a complete deep copy of the list structure
        List<List<ITerrainObject>> gridCopy = new ArrayList<>();

        for (List<ITerrainObject> row : grid) {
            if (row == null) {
                gridCopy.add(new ArrayList<>());
            } else {
                // Create a new ArrayList with the same contents
                gridCopy.add(new ArrayList<>(row));
            }
        }

        // Return as unmodifiable to prevent any modifications
        return Collections.unmodifiableList(gridCopy);
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
                String cell;

                if (obj == null) {
                    cell = "    ";
                } else {
                    String shorthand = obj.getShorthand();
                    if (shorthand == null) {
                        cell = " ?? ";  // Fallback for null shorthand
                    } else {
                        cell = " " + shorthand + " ";
                    }
                }

                System.out.print(cell + "|");
            }
            System.out.println();
            System.out.println(separator);
        }
    }

    /**
     * Counts the number of objects currently on the grid.
     * @return The total number of non-null objects on the grid
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
     * @return An unmodifiable list of empty Positions (all positions are new objects)
     */
    public List<Position> getEmptyPositions() {
        List<Position> emptyPositions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid.get(i).get(j) == null) {
                    // Create NEW Position objects, not references to existing ones
                    emptyPositions.add(new Position(i, j));
                }
            }
        }

        // Return unmodifiable copy to prevent external modification
        return Collections.unmodifiableList(emptyPositions);
    }

    /**
     * Finds all empty edge positions on the grid.
     * @return An unmodifiable list of empty edge Positions (all positions are new objects)
     */
    public List<Position> getEmptyEdgePositions() {
        List<Position> emptyEdgePositions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Create NEW Position object for edge checking
                Position pos = new Position(i, j);

                if (grid.get(i).get(j) == null && pos.isEdge(size)) {
                    // Add a NEW Position object to the list
                    emptyEdgePositions.add(new Position(i, j));
                }
            }
        }

        // Return unmodifiable copy to prevent external modification
        return Collections.unmodifiableList(emptyEdgePositions);
    }

    /**
     * @return An unmodifiable list of all non-null ITerrainObjects on the grid
     */
    public List<ITerrainObject> getAllObjects() {
        List<ITerrainObject> objects = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ITerrainObject obj = grid.get(i).get(j);
                if (obj != null) {
                    objects.add(obj);
                }
            }
        }

        // Return unmodifiable list
        return Collections.unmodifiableList(objects);
    }

    /**
     * @return true if grid structure is valid, false if corrupted
     */
    public boolean validateGridIntegrity() {
        if (grid == null) {
            return false;
        }

        if (grid.size() != size) {
            return false;
        }

        for (List<ITerrainObject> row : grid) {
            if (row == null || row.size() != size) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a string representation of the grid for debugging.
     * @return String representation of grid dimensions and object count
     */
    @Override
    public String toString() {
        return "TerrainGrid{" +
                "size=" + size +
                ", objectCount=" + countObjects() +
                ", isValid=" + validateGridIntegrity() +
                '}';
    }
}