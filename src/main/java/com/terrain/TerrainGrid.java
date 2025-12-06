package com.terrain;

import com.interfaces.ITerrainObject;
import com.utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the terrain grid for the Sliding Penguins game.
 * Manages a 2D grid structure using Lists and ArrayLists.
 * Demonstrates the use of Lists and ArrayLists as required in the homework.
 *
 * SECURITY ENHANCED VERSION:
 * - All position parameters are defensively copied
 * - Grid access is controlled and never exposes internal structure
 * - All list returns are deep copied and unmodifiable
 * - Comprehensive null safety checks
 * - Input validation on all methods
 * - No direct references to internal objects are ever returned
 */
public class TerrainGrid {

    private final List<List<ITerrainObject>> grid;
    private final int size;

    /**
     * Constructor for TerrainGrid.
     * Uses final fields to prevent reassignment and ensure immutability of references.
     *
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
     * Safe to return primitive value.
     *
     * @return The grid size
     */
    public int getSize() {
        return size;
    }

    /**
     * Places an object on the grid at the specified position.
     * SECURITY: Position parameter is defensively copied to prevent external modification.
     *
     * @param object The ITerrainObject to place (can be null to clear a cell)
     * @param position The position to place it at (must not be null)
     * @return true if placement was successful, false if position is invalid
     * @throws IllegalArgumentException if position is null
     */
    public boolean placeObject(ITerrainObject object, Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        // Defensive copy - use the copied position for validation
        Position safeCopy = new Position(position);

        if (!isValidPosition(safeCopy)) {
            return false;
        }

        // Place object using the validated copy
        grid.get(safeCopy.getRow()).set(safeCopy.getColumn(), object);
        return true;
    }

    /**
     * Removes an object from the grid at the specified position.
     * SECURITY: Position parameter is defensively copied.
     *
     * @param position The position to clear (must not be null)
     * @return The removed object, or null if position was empty or invalid
     * @throws IllegalArgumentException if position is null
     */
    public ITerrainObject removeObject(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        // Defensive copy
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
     * SECURITY: Position parameter is defensively copied.
     * NOTE: Returns the actual object reference, but position is protected.
     *
     * @param position The position to check (must not be null)
     * @return The ITerrainObject at that position, or null if empty or invalid
     * @throws IllegalArgumentException if position is null
     */
    public ITerrainObject getObjectAt(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        // Defensive copy
        Position safeCopy = new Position(position);

        if (!isValidPosition(safeCopy)) {
            return null;
        }

        return grid.get(safeCopy.getRow()).get(safeCopy.getColumn());
    }

    /**
     * Checks if a position is valid (within grid bounds).
     * SECURITY: Position parameter is defensively copied.
     *
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
     * SECURITY: Position parameter is defensively copied.
     *
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
     * SECURITY: Position parameter is defensively copied.
     *
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
     * Safe operation as it only modifies internal state.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid.get(i).set(j, null);
            }
        }
    }

    /**
     * SECURITY CRITICAL: Gets a DEEP COPY of the grid structure.
     * This is the most important security method in this class.
     *
     * WARNING: Even with this deep copy, the ITerrainObjects themselves are still
     * the same references. This is intentional as we want to observe the game state,
     * not create parallel universes. However, the List structure itself is completely
     * isolated from external manipulation.
     *
     * DO NOT change this to return the internal grid directly!
     *
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
                // Objects themselves are same references (intentional for game state observation)
                // but the List structure is completely new
                gridCopy.add(new ArrayList<>(row));
            }
        }

        // Return as unmodifiable to prevent any modifications
        // Even though it's a copy, we want to enforce read-only access
        return Collections.unmodifiableList(gridCopy);
    }

    /**
     * Displays the grid in a formatted manner.
     * Shows shorthand notation for each object.
     * Safe to call - only reads internal state without exposing it.
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
     * Safe operation - only reads internal state.
     *
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
     * SECURITY: Returns a new list with new Position objects (deep copy).
     *
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
     * SECURITY: Returns a new list with new Position objects (deep copy).
     *
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
     * SECURITY: Gets a defensive copy of all objects currently on the grid.
     * Useful for iterating over all game objects without exposing positions.
     *
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
     * SECURITY: Validates grid integrity.
     * Useful for debugging and ensuring the grid is in a valid state.
     *
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
     * Safe method - only reads state without exposing internal structure.
     *
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