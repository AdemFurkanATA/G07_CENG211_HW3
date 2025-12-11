package com.utils;

import java.util.Random;

/**
 * Utility class providing helper methods for game operations.
 * Contains random number generation and other utility functions.
 */
public class GameHelper {

    // SECURITY: Private final Random instance - cannot be accessed or modified externally
    // Thread-safe for game operations
    private static final Random random = new Random();

    /**
     * This is a utility class and should never be instantiated.
     * Throws exception if someone tries to use reflection.
     */
    private GameHelper() {
        throw new AssertionError("GameHelper is a utility class and cannot be instantiated");
    }

    /**
     * Generates a random integer between min (inclusive) and max (inclusive).
     * @param min The minimum value (inclusive)
     * @param max The maximum value (inclusive)
     * @return A random integer in the range [min, max]
     * @throws IllegalArgumentException if min > max
     */
    public static int randomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    String.format("Invalid range: min (%d) cannot be greater than max (%d)", min, max)
            );
        }

        if (min == max) {
            return min;
        }

        try {
            long range = (long) max - (long) min + 1;

            if (range > Integer.MAX_VALUE) {
                return random.nextInt(Integer.MAX_VALUE) % (int) range + min;
            }

            return random.nextInt((int) range) + min;

        } catch (Exception e) {
            System.err.println("ERROR in randomInt: " + e.getMessage());
            return min;
        }
    }

    /**
     * Returns a random boolean with the specified probability of being true.
     * @param probability The probability (0.0 to 1.0) of returning true
     * @return true with the given probability, false otherwise
     * @throws IllegalArgumentException if probability is not in [0.0, 1.0]
     */
    public static boolean randomChance(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException(
                    String.format("Invalid probability: %.4f (must be between 0.0 and 1.0)", probability)
            );
        }

        if (probability == 0.0) {
            return false;  // 0% chance always returns false
        }
        if (probability == 1.0) {
            return true;   // 100% chance always returns true
        }

        try {
            return random.nextDouble() < probability;
        } catch (Exception e) {
            System.err.println("ERROR in randomChance: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a random Position on the edge of a grid.
     * Edge positions are those where row or column is 0 or gridSize-1.
     * @param gridSize The size of the grid (must be > 0)
     * @return A random edge Position (NEW object, never null)
     * @throws IllegalArgumentException if gridSize <= 0
     */
    public static Position randomEdgePosition(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        if (gridSize == 1) {
            return new Position(0, 0);
        }

        try {
            // Generate random side: 0=top, 1=bottom, 2=left, 3=right
            int side = randomInt(0, 3);

            switch (side) {
                case 0:  // Top edge (row = 0, column = random)
                    return new Position(0, randomInt(0, gridSize - 1));

                case 1:  // Bottom edge (row = gridSize-1, column = random)
                    return new Position(gridSize - 1, randomInt(0, gridSize - 1));

                case 2:  // Left edge (row = random, column = 0)
                    return new Position(randomInt(0, gridSize - 1), 0);

                case 3:  // Right edge (row = random, column = gridSize-1)
                    return new Position(randomInt(0, gridSize - 1), gridSize - 1);

                default:
                    return new Position(0, 0);
            }
        } catch (Exception e) {
            System.err.println("ERROR in randomEdgePosition: " + e.getMessage());
            return new Position(0, 0);
        }
    }

    /**
     * Generates a random Position anywhere on the grid.
     * @param gridSize The size of the grid (must be > 0)
     * @return A random Position (NEW object, never null)
     * @throws IllegalArgumentException if gridSize <= 0
     */
    public static Position randomPosition(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        if (gridSize == 1) {
            return new Position(0, 0);
        }

        try {
            int row = randomInt(0, gridSize - 1);
            int column = randomInt(0, gridSize - 1);

            return new Position(row, column);

        } catch (Exception e) {
            System.err.println("ERROR in randomPosition: " + e.getMessage());
            int center = gridSize / 2;
            return new Position(center, center);
        }
    }


    /**
     * @param max The maximum value (inclusive, must be >= 0)
     * @return A random integer in the range [0, max]
     * @throws IllegalArgumentException if max < 0
     */
    public static int randomNonNegativeInt(int max) {
        if (max < 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid max value: %d (must be >= 0)", max)
            );
        }
        return randomInt(0, max);
    }

    /**
     * @param minValue The minimum guaranteed value
     * @param range The range above minimum (must be >= 0)
     * @return A random integer >= minValue
     * @throws IllegalArgumentException if range < 0
     */
    public static int randomIntWithMin(int minValue, int range) {
        if (range < 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid range: %d (must be >= 0)", range)
            );
        }
        return minValue + randomInt(0, range);
    }

    /**
     * @param position The position to validate (must not be null)
     * @param gridSize The grid size (must be > 0)
     * @return true if position is valid, false otherwise
     * @throws IllegalArgumentException if position is null or gridSize <= 0
     */
    public static boolean isValidPosition(Position position, int gridSize) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        return position.isValid(gridSize);
    }

    /**
     * @param position The position to check (must not be null)
     * @param gridSize The grid size (must be > 0)
     * @return true if position is on an edge, false otherwise
     * @throws IllegalArgumentException if position is null or gridSize <= 0
     */
    public static boolean isEdgePosition(Position position, int gridSize) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (gridSize <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid grid size: %d (must be > 0)", gridSize)
            );
        }

        return position.isEdge(gridSize);
    }

    /**
     * @param value The value to clamp
     * @param min The minimum allowed value
     * @param max The maximum allowed value
     * @return The clamped value
     * @throws IllegalArgumentException if min > max
     */
    public static int clamp(int value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    String.format("Invalid range: min (%d) > max (%d)", min, max)
            );
        }

        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * @param value The value to clamp
     * @param min The minimum allowed value
     * @param max The maximum allowed value
     * @return The clamped value
     * @throws IllegalArgumentException if min > max
     */
    public static double clamp(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    String.format("Invalid range: min (%.4f) > max (%.4f)", min, max)
            );
        }

        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * @param minRow Minimum row (inclusive)
     * @param maxRow Maximum row (inclusive)
     * @param minCol Minimum column (inclusive)
     * @param maxCol Maximum column (inclusive)
     * @return A random Position within the specified region (NEW object)
     * @throws IllegalArgumentException if any min > max or if values are negative
     */
    public static Position randomPositionInRegion(int minRow, int maxRow, int minCol, int maxCol) {
        if (minRow < 0 || minCol < 0) {
            throw new IllegalArgumentException("Row and column minimums must be non-negative");
        }
        if (minRow > maxRow) {
            throw new IllegalArgumentException(
                    String.format("Invalid row range: min (%d) > max (%d)", minRow, maxRow)
            );
        }
        if (minCol > maxCol) {
            throw new IllegalArgumentException(
                    String.format("Invalid column range: min (%d) > max (%d)", minCol, maxCol)
            );
        }

        try {
            int row = randomInt(minRow, maxRow);
            int col = randomInt(minCol, maxCol);
            return new Position(row, col);
        } catch (Exception e) {
            System.err.println("ERROR in randomPositionInRegion: " + e.getMessage());
            // Fallback to the minimum position in the region
            return new Position(minRow, minCol);
        }
    }

    /**
     * @param pos1 First position (must not be null)
     * @param pos2 Second position (must not be null)
     * @return The Manhattan distance (always non-negative)
     * @throws IllegalArgumentException if either position is null
     */
    public static int manhattanDistance(Position pos1, Position pos2) {
        if (pos1 == null || pos2 == null) {
            throw new IllegalArgumentException("Positions cannot be null");
        }

        try {
            int rowDiff = Math.abs(pos1.getRow() - pos2.getRow());
            int colDiff = Math.abs(pos1.getColumn() - pos2.getColumn());
            return rowDiff + colDiff;
        } catch (Exception e) {
            System.err.println("ERROR in manhattanDistance: " + e.getMessage());
            return Integer.MAX_VALUE; // Safe fallback (maximum distance)
        }
    }

    /**
     * @param pos1 First position (must not be null)
     * @param pos2 Second position (must not be null)
     * @return true if positions are adjacent, false otherwise
     * @throws IllegalArgumentException if either position is null
     */
    public static boolean areAdjacent(Position pos1, Position pos2) {
        if (pos1 == null || pos2 == null) {
            throw new IllegalArgumentException("Positions cannot be null");
        }

        try {
            return manhattanDistance(pos1, pos2) == 1;
        } catch (Exception e) {
            System.err.println("ERROR in areAdjacent: " + e.getMessage());
            return false; // Safe fallback
        }
    }

    /**
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return A random double in [min, max]
     * @throws IllegalArgumentException if min > max
     */
    public static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    String.format("Invalid range: min (%.4f) > max (%.4f)", min, max)
            );
        }

        if (min == max) {
            return min;
        }

        try {
            return min + (random.nextDouble() * (max - min));
        } catch (Exception e) {
            System.err.println("ERROR in randomDouble: " + e.getMessage());
            return min; // Safe fallback
        }
    }

    /**
     * @param array The array to shuffle (must not be null)
     * @throws IllegalArgumentException if array is null
     */
    public static void shuffle(Object[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        if (array.length <= 1) {
            return; // Nothing to shuffle
        }

        try {
            // Fisher-Yates shuffle algorithm
            for (int i = array.length - 1; i > 0; i--) {
                int j = randomInt(0, i);
                // Swap array[i] and array[j]
                Object temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        } catch (Exception e) {
            System.err.println("ERROR in shuffle: " + e.getMessage());
            // Array remains in original order on error (safe fallback)
        }
    }

    /**
     * @param array The array to select from (must not be null or empty)
     * @param <T> The type of elements in the array
     * @return A random element from the array
     * @throws IllegalArgumentException if array is null or empty
     */
    public static <T> T randomElement(T[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }

        try {
            int index = randomInt(0, array.length - 1);
            return array[index];
        } catch (Exception e) {
            System.err.println("ERROR in randomElement: " + e.getMessage());
            return array[0]; // Safe fallback (first element)
        }
    }

    /**
     * @param percentage The percentage to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPercentage(int percentage) {
        return percentage >= 0 && percentage <= 100;
    }

    /**
     * @param percentage The percentage value (must be in [0, 100])
     * @return The probability value
     * @throws IllegalArgumentException if percentage is invalid
     */
    public static double percentageToProbability(int percentage) {
        if (!isValidPercentage(percentage)) {
            throw new IllegalArgumentException(
                    String.format("Invalid percentage: %d (must be in [0, 100])", percentage)
            );
        }
        return percentage / 100.0;
    }

    /**
     * @return A string describing the Random generator state
     */
    public static String getRandomGeneratorInfo() {
        return "Random generator initialized and operational";
    }
}