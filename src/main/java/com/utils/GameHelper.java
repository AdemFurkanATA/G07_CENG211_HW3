package com.utils;

import java.util.Random;

/**
 * Utility class providing helper methods for game operations.
 * Contains random number generation and other utility functions.
 */
public class GameHelper {

    private static final Random random = new Random();

    /**
     * Generates a random integer between min (inclusive) and max (inclusive).
     *
     * @param min The minimum value
     * @param max The maximum value
     * @return A random integer in the range [min, max]
     */
    public static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Returns a random boolean with the specified probability of being true.
     *
     * @param probability The probability (0.0 to 1.0) of returning true
     * @return true with the given probability, false otherwise
     */
    public static boolean randomChance(double probability) {
        return random.nextDouble() < probability;
    }

    /**
     * Generates a random Position on the edge of a grid.
     * Edge positions are those where row or column is 0 or gridSize-1.
     *
     * @param gridSize The size of the grid
     * @return A random edge Position
     */
    public static Position randomEdgePosition(int gridSize) {
        int side = randomInt(0, 3);  // 0=top, 1=bottom, 2=left, 3=right

        switch (side) {
            case 0:  // Top edge
                return new Position(0, randomInt(0, gridSize - 1));
            case 1:  // Bottom edge
                return new Position(gridSize - 1, randomInt(0, gridSize - 1));
            case 2:  // Left edge
                return new Position(randomInt(0, gridSize - 1), 0);
            case 3:  // Right edge
                return new Position(randomInt(0, gridSize - 1), gridSize - 1);
            default:
                return new Position(0, 0);
        }
    }

    /**
     * Generates a random Position anywhere on the grid.
     *
     * @param gridSize The size of the grid
     * @return A random Position
     */
    public static Position randomPosition(int gridSize) {
        return new Position(randomInt(0, gridSize - 1), randomInt(0, gridSize - 1));
    }
}