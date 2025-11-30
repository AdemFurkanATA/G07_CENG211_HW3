package com.utils;

import com.enums.Direction;

/**
 * Represents a coordinate (row, column) on the terrain grid.
 * Enforces non-negative coordinates to ensure grid validity.
 * Provides utility methods for movement simulation and boundary checks.
 */
public class Position {
    private int row;
    private int column;

    /**
     * Constructor for Position.
     * Validates that coordinates are non-negative.
     *
     * @param row The row index (must be >= 0)
     * @param column The column index (must be >= 0)
     * @throws IllegalArgumentException if row or column is negative
     */
    public Position(int row, int column) {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Coordinates cannot be negative: (" + row + ", " + column + ")");
        }
        this.row = row;
        this.column = column;
    }

    /**
     * Copy constructor for Position.
     * Creates a new instance with the same coordinates.
     *
     * @param other The Position object to copy
     */
    public Position(Position other) {
        this(other.row, other.column);
    }

    /**
     * Gets the row index.
     *
     * @return The row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index.
     *
     * @return The column index
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the row index.
     *
     * @param row The new row index (must be >= 0)
     * @throws IllegalArgumentException if row is negative
     */
    public void setRow(int row) {
        if (row < 0) {
            throw new IllegalArgumentException("Row cannot be negative: " + row);
        }
        this.row = row;
    }

    /**
     * Sets the column index.
     *
     * @param column The new column index (must be >= 0)
     * @throws IllegalArgumentException if column is negative
     */
    public void setColumn(int column) {
        if (column < 0) {
            throw new IllegalArgumentException("Column cannot be negative: " + column);
        }
        this.column = column;
    }

    /**
     * Checks if this position is on the edge of a grid with given size.
     *
     * @param gridSize The size of the grid
     * @return true if the position is on any edge, false otherwise
     */
    public boolean isEdge(int gridSize) {
        return row == 0 || row == gridSize - 1 || column == 0 || column == gridSize - 1;
    }

    /**
     * Checks if this position is within the valid bounds of the grid.
     *
     * @param gridSize The size of the grid
     * @return true if position is within bounds [0, gridSize-1], false otherwise
     */
    public boolean isValid(int gridSize) {
        return row >= 0 && row < gridSize && column >= 0 && column < gridSize;
    }

    /**
     * Returns the hypothetical next position in a given direction without modifying the current instance.
     *
     * @param direction The direction to project movement
     * @return A new Position object representing the target location
     */
    public Position getNextPosition(Direction direction) {
        switch (direction) {
            case UP:
                return new Position(row - 1, column);
            case DOWN:
                return new Position(row + 1, column);
            case LEFT:
                return new Position(row, column - 1);
            case RIGHT:
                return new Position(row, column + 1);
            default:
                return new Position(this);
        }
    }

    /**
     * Modifies the current position by moving one step in the specified direction.
     * Note: This method may result in negative coordinates if unchecked,
     * but usually validated by isValid() before use in game logic.
     *
     * @param direction The direction to move
     */
    public void move(Direction direction) {
        switch (direction) {
            case UP:
                row--;
                break;
            case DOWN:
                row++;
                break;
            case LEFT:
                column--;
                break;
            case RIGHT:
                column++;
                break;
        }
    }

    /**
     * Checks if this position equals another object based on coordinates.
     *
     * @param obj The object to compare
     * @return true if positions are functionally identical
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && column == position.column;
    }

    /**
     * Returns a hash code for this position.
     *
     * @return Hash code based on row and column values
     */
    @Override
    public int hashCode() {
        return 31 * row + column;
    }

    /**
     * Returns a string representation of the position coordinates.
     *
     * @return String in format "(row, column)"
     */
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}