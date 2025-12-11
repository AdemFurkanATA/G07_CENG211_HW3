package com.utils;

import com.enums.Direction;

/**
 * Represents a position (coordinates) on the icy terrain grid.
 * Uses row and column indices to identify locations.
 */
public class Position {
    private int row;
    private int column;

    /**
     * Constructor for Position.
     * @param row The row index (should be >= 0)
     * @param column The column index (should be >= 0)
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Copy constructor for Position.
     * @param other The Position to copy (must not be null)
     * @throws IllegalArgumentException if other is null
     */
    public Position(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot copy null position");
        }
        this.row = other.row;
        this.column = other.column;
    }

    /**
     * Gets the row index.
     * @return The row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index.
     * @return The column index
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the row index.
     * @param row The new row index
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the column index.
     * @param column The new column index
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * @param row The new row index
     * @param column The new column index
     */
    public void setCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Checks if this position is on the edge of a grid with given size.
     * @param gridSize The size of the grid (e.g., 10 for 10x10) (must be > 0)
     * @return true if the position is on an edge, false otherwise
     * @throws IllegalArgumentException if gridSize <= 0
     */
    public boolean isEdge(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        try {
            return row == 0 || row == gridSize - 1 ||
                    column == 0 || column == gridSize - 1;
        } catch (Exception e) {
            System.err.println("ERROR in isEdge: " + e.getMessage());
            return false; // Safe fallback
        }
    }

    /**
     * Checks if this position is valid within a grid of given size.
     * @param gridSize The size of the grid (must be > 0)
     * @return true if position is within bounds, false otherwise
     * @throws IllegalArgumentException if gridSize <= 0
     */
    public boolean isValid(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        try {
            return row >= 0 && row < gridSize &&
                    column >= 0 && column < gridSize;
        } catch (Exception e) {
            System.err.println("ERROR in isValid: " + e.getMessage());
            return false; // Safe fallback
        }
    }

    /**
     * Returns the next position in a given direction.
     * @param direction The direction to move (can be null)
     * @return A new Position representing the next location (never null)
     */
    public Position getNextPosition(Direction direction) {
        // SECURITY: Handle null direction
        if (direction == null) {
            System.err.println("WARNING: getNextPosition called with null direction");
            return new Position(this); // Return copy of current position
        }

        try {
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
                    // SECURITY: Unexpected direction value
                    System.err.println("WARNING: Unexpected direction in getNextPosition: " + direction);
                    return new Position(this);
            }
        } catch (Exception e) {
            System.err.println("ERROR in getNextPosition: " + e.getMessage());
            return new Position(this); // Safe fallback (current position)
        }
    }

    /**
     * Moves this position one step in the given direction.
     * @param direction The direction to move (can be null)
     */
    public void move(Direction direction) {
        if (direction == null) {
            System.err.println("WARNING: move called with null direction");
            return; // Don't move if direction is null
        }

        try {
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
                default:
                    System.err.println("WARNING: Unexpected direction in move: " + direction);
                    break;
            }
        } catch (Exception e) {
            System.err.println("ERROR in move: " + e.getMessage());
            // Position remains unchanged on error
        }
    }

    /**
     * @return Array of 4 positions [UP, DOWN, LEFT, RIGHT] (never null, no null elements)
     */
    public Position[] getAllAdjacentPositions() {
        try {
            return new Position[] {
                    new Position(row - 1, column),  // UP
                    new Position(row + 1, column),  // DOWN
                    new Position(row, column - 1),  // LEFT
                    new Position(row, column + 1)   // RIGHT
            };
        } catch (Exception e) {
            System.err.println("ERROR in getAllAdjacentPositions: " + e.getMessage());
            // Return array with copies of current position as safe fallback
            Position current = new Position(this);
            return new Position[] {current, current, current, current};
        }
    }

    /**
     * @param gridSize The size of the grid (must be > 0)
     * @return Array of valid adjacent positions (never null, may be empty)
     * @throws IllegalArgumentException if gridSize <= 0
     */
    public Position[] getValidAdjacentPositions(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        try {
            Position[] allAdjacent = getAllAdjacentPositions();
            int validCount = 0;

            // Count valid positions
            for (Position pos : allAdjacent) {
                if (pos != null && pos.isValid(gridSize)) {
                    validCount++;
                }
            }

            // Create array with only valid positions
            Position[] valid = new Position[validCount];
            int index = 0;
            for (Position pos : allAdjacent) {
                if (pos != null && pos.isValid(gridSize)) {
                    valid[index++] = pos;
                }
            }

            return valid;
        } catch (Exception e) {
            System.err.println("ERROR in getValidAdjacentPositions: " + e.getMessage());
            return new Position[0]; // Return empty array on error
        }
    }

    /**
     * @param other The other position (must not be null)
     * @return The Manhattan distance (always non-negative)
     * @throws IllegalArgumentException if other is null
     */
    public int distanceTo(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot calculate distance to null position");
        }

        try {
            int rowDiff = Math.abs(this.row - other.row);
            int colDiff = Math.abs(this.column - other.column);
            return rowDiff + colDiff;
        } catch (Exception e) {
            System.err.println("ERROR in distanceTo: " + e.getMessage());
            return Integer.MAX_VALUE; // Safe fallback (maximum distance)
        }
    }

    /**
     * @param other The other position (must not be null)
     * @return true if positions are adjacent, false otherwise
     * @throws IllegalArgumentException if other is null
     */
    public boolean isAdjacentTo(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot check adjacency with null position");
        }

        try {
            return distanceTo(other) == 1;
        } catch (Exception e) {
            System.err.println("ERROR in isAdjacentTo: " + e.getMessage());
            return false;
        }
    }

    /**
     * @param other The other position (must not be null)
     * @return true if positions are diagonally adjacent, false otherwise
     * @throws IllegalArgumentException if other is null
     */
    public boolean isDiagonallyAdjacentTo(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot check diagonal adjacency with null position");
        }

        try {
            int rowDiff = Math.abs(this.row - other.row);
            int colDiff = Math.abs(this.column - other.column);
            // Diagonally adjacent means both differences are exactly 1
            return rowDiff == 1 && colDiff == 1;
        } catch (Exception e) {
            System.err.println("ERROR in isDiagonallyAdjacentTo: " + e.getMessage());
            return false; // Safe fallback
        }
    }

    /**
     * @param other The other position (must not be null)
     * @return true if same row, false otherwise
     * @throws IllegalArgumentException if other is null
     */
    public boolean isSameRow(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare with null position");
        }
        return this.row == other.row;
    }

    /**
     * @param other The other position (must not be null)
     * @return true if same column, false otherwise
     * @throws IllegalArgumentException if other is null
     */
    public boolean isSameColumn(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare with null position");
        }
        return this.column == other.column;
    }

    /**
     * @param other The target position (must not be null)
     * @return The Direction to other, or null if not in same row/column
     * @throws IllegalArgumentException if other is null
     */
    public Direction getDirectionTo(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot get direction to null position");
        }

        try {
            if (this.equals(other)) {
                return null;
            }

            if (this.row == other.row) {
                return (other.column > this.column) ? Direction.RIGHT : Direction.LEFT;
            }

            if (this.column == other.column) {
                return (other.row > this.row) ? Direction.DOWN : Direction.UP;
            }

            return null;
        } catch (Exception e) {
            System.err.println("ERROR in getDirectionTo: " + e.getMessage());
            return null; // Safe fallback
        }
    }

    /**
     * @param rowOffset The row offset (can be negative)
     * @param colOffset The column offset (can be negative)
     * @return A new Position with applied offsets (never null)
     */
    public Position offset(int rowOffset, int colOffset) {
        try {
            return new Position(this.row + rowOffset, this.column + colOffset);
        } catch (Exception e) {
            System.err.println("ERROR in offset: " + e.getMessage());
            return new Position(this); // Safe fallback (copy of current)
        }
    }

    /**
     * @param gridSize The grid size (must be > 0)
     * @return A new Position clamped to grid bounds (never null)
     * @throws IllegalArgumentException if gridSize <= 0
     */
    public Position clampToGrid(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        try {
            int clampedRow = Math.max(0, Math.min(row, gridSize - 1));
            int clampedCol = Math.max(0, Math.min(column, gridSize - 1));
            return new Position(clampedRow, clampedCol);
        } catch (Exception e) {
            System.err.println("ERROR in clampToGrid: " + e.getMessage());
            return new Position(0, 0); // Safe fallback (top-left corner)
        }
    }

    /**
     * @param gridSize The grid size (must be > 0)
     * @return true if position is a corner, false otherwise
     * @throws IllegalArgumentException if gridSize <= 0
     */
    public boolean isCorner(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        try {
            boolean isTopOrBottom = (row == 0 || row == gridSize - 1);
            boolean isLeftOrRight = (column == 0 || column == gridSize - 1);
            return isTopOrBottom && isLeftOrRight;
        } catch (Exception e) {
            System.err.println("ERROR in isCorner: " + e.getMessage());
            return false; // Safe fallback
        }
    }

    /**
     * @param gridSize The grid size (must be > 0)
     * @return String describing corner ("TOP_LEFT", "TOP_RIGHT", etc.) or null if not a corner
     * @throws IllegalArgumentException if gridSize <= 0
     */
    public String getCornerType(int gridSize) {
        if (!isCorner(gridSize)) {
            return null;
        }

        try {
            if (row == 0 && column == 0) return "TOP_LEFT";
            if (row == 0 && column == gridSize - 1) return "TOP_RIGHT";
            if (row == gridSize - 1 && column == 0) return "BOTTOM_LEFT";
            if (row == gridSize - 1 && column == gridSize - 1) return "BOTTOM_RIGHT";
            return null;
        } catch (Exception e) {
            System.err.println("ERROR in getCornerType: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if this position equals another position.
     * @param obj The object to compare
     * @return true if positions are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        try {
            Position position = (Position) obj;
            return row == position.row && column == position.column;
        } catch (Exception e) {
            System.err.println("ERROR in equals: " + e.getMessage());
            return false; // Safe fallback
        }
    }

    /**
     * Returns a hash code for this position.
     * @return Hash code based on row and column
     */
    @Override
    public int hashCode() {
        try {
            return 31 * row + column;
        } catch (Exception e) {
            System.err.println("ERROR in hashCode: " + e.getMessage());
            return 0; // Safe fallback
        }
    }

    /**
     * Returns a string representation of the position.
     * @return String in format "(row, column)"
     */
    @Override
    public String toString() {
        try {
            return "(" + row + ", " + column + ")";
        } catch (Exception e) {
            System.err.println("ERROR in toString: " + e.getMessage());
            return "(?, ?)"; // Safe fallback
        }
    }

    /**
     * @param gridSize The grid size to validate against (must be > 0)
     * @return Detailed string representation
     * @throws IllegalArgumentException if gridSize <= 0
     */
    public String toDetailedString(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Position[row=").append(row);
            stringBuilder.append(", col=").append(column);
            stringBuilder.append(", valid=").append(isValid(gridSize));
            stringBuilder.append(", edge=").append(isEdge(gridSize));
            stringBuilder.append(", corner=").append(isCorner(gridSize));
            stringBuilder.append("]");
            return stringBuilder.toString();
        } catch (Exception e) {
            System.err.println("ERROR in toDetailedString: " + e.getMessage());
            return toString(); // Fallback to simple toString
        }
    }

    /**
     * @return true if state is valid, false if corrupted
     */
    public boolean validateState() {
        // Position can have any integer values (even negative)
        // Validation against grid bounds happens at usage site
        // This method mainly exists for consistency with other classes
        return true;
    }

    /**
     * @return A new Position with same coordinates (never null)
     */
    public Position copy() {
        return new Position(this);
    }
}