package com.enums;

/**
 * Enum representing the four possible movement directions in the game.
 * Used by penguins and sliding hazards to determine their movement on the icy terrain.
 */
public enum Direction {
    UP,      // Move upward (decrease row index)
    DOWN,    // Move downward (increase row index)
    LEFT,    // Move left (decrease column index)
    RIGHT;   // Move right (increase column index)

    /**
     * Returns the opposite direction of the current direction.
     * Used when penguins bounce off SeaLions.
     *
     * @return The opposite Direction
     */
    public Direction getOpposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return null;
        }
    }

    /**
     * Converts user input string to Direction enum.
     * Case-insensitive input handling.
     *
     * @param input The user input string (U, D, L, R)
     * @return Corresponding Direction or null if invalid
     */
    public static Direction fromString(String input) {
        if (input == null) {
            return null;
        }

        switch (input.trim().toUpperCase()) {
            case "U":
                return UP;
            case "D":
                return DOWN;
            case "L":
                return LEFT;
            case "R":
                return RIGHT;
            default:
                return null;
        }
    }

    /**
     * Returns a string representation for display purposes.
     *
     * @return The direction name in uppercase
     */
    @Override
    public String toString() {
        return this.name();
    }
}