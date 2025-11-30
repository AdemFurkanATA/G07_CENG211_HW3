package com.utils;

import java.util.Random;

/**
 * Utility class providing centralized helper methods for game operations.
 * Manages random number generation and position calculations to ensure consistency throughout the game.
 * Uses a single Random instance to maintain statistical randomness quality.
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
     * Returns a random boolean based on the specified probability.
     * Uses the shared Random instance for consistent probability distribution.
     *
     * @param probability The probability (0.0 to 1.0) of returning true
     * @return true if the random event occurs, false otherwise
     */
    public static boolean randomChance(double probability) {
        return random.nextDouble() < probability;
    }

    /**
     * Generates a random Position specifically on the edge of the grid.
     * Edge positions are required for spawning penguins at the start of the game.
     * Randomly selects one of the four sides (Top, Bottom, Left, Right) and a coordinate on that side.
     *
     * @param gridSize The size of the grid to determine boundaries
     * @return A valid Position located on the grid's edge
     */
    public static Position randomEdgePosition(int gridSize) {
        int side = randomInt(0, 3);

        switch (side) {
            case 0:  // Top edge (Row 0, Random Column)
                return new Position(0, randomInt(0, gridSize - 1));
            case 1:  // Bottom edge (Last Row, Random Column)
                return new Position(gridSize - 1, randomInt(0, gridSize - 1));
            case 2:  // Left edge (Random Row, Column 0)
                return new Position(randomInt(0, gridSize - 1), 0);
            case 3:  // Right edge (Random Row, Last Column)
                return new Position(randomInt(0, gridSize - 1), gridSize - 1);
            default:
                // Fallback safe position (Top-Left)
                return new Position(0, 0);
        }
    }

    /**
     * Generates a completely random Position anywhere within the grid boundaries.
     * Used for placing food and hazards.
     *
     * @param gridSize The size of the grid
     * @return A valid random Position within [0, gridSize-1]
     */
    public static Position randomPosition(int gridSize) {
        return new Position(randomInt(0, gridSize - 1), randomInt(0, gridSize - 1));
    }
}