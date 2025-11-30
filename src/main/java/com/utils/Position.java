package com.utils;

import com.enums.Direction;

/**
 * Represents a coordinate (row, column) on the terrain grid.
 * Provides utility methods for position manipulation and validation.
 */
public class Position {
    private int row;
    private int column;

    /**
     * Constructor for Position.
     * Initializes a position with given row and column.
     * Note: Does not enforce boundary checks during instantiation to allow
     * external logic (like IcyTerrain) to calculate potential off-grid positions.
     *
     * @param row The row index
     * @param column The column index
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Copy constructor for Position.
     *
     * @param other The Position object to copy
     */
    public Position(Position other) {
        this.row = other.row;
        this.column = other.column;
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
     * @param row The new row index
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the column index.
     *
     * @param column The new column index
     */
    public void setColumn(int column) {
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
     * This is the primary method for validating coordinates.
     *
     * @param gridSize The size of the grid
     * @return true if position is within bounds [0, gridSize-1], false otherwise
     */
    public boolean isValid(int gridSize) {
        return row >= 0 && row < gridSize && column >= 0 && column < gridSize;
    }

    /**
     * Returns the next position in a given direction without modifying the current instance.
     *
     * @param direction The direction to move
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