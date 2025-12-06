package com.utils;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameHelper Utility Class Tests
 *
 * Location: src/test/java/com/utils/GameHelperTest.java
 *
 * Testing utility/helper classes with static methods
 */
@DisplayName("GameHelper Utility Tests 🎲")
class GameHelperTest {

    // ========================================
    // randomInt() TESTS
    // ========================================

    @Test
    @DisplayName("randomInt should return value within range")
    void testRandomInt_WithinRange() {
        // ARRANGE
        int min = 1;
        int max = 10;

        // ACT & ASSERT - Try multiple times
        for (int i = 0; i < 100; i++) {
            int result = GameHelper.randomInt(min, max);

            assertTrue(result >= min, "Result should be >= min: " + result);
            assertTrue(result <= max, "Result should be <= max: " + result);
        }

        System.out.println("✓ randomInt stays within range");
    }

    @Test
    @DisplayName("randomInt with min == max should return that value")
    void testRandomInt_MinEqualsMax() {
        // ARRANGE
        int value = 5;

        // ACT
        int result = GameHelper.randomInt(value, value);

        // ASSERT
        assertEquals(value, result, "Should return the only possible value");
    }

    @Test
    @DisplayName("randomInt with min > max should throw exception")
    void testRandomInt_MinGreaterThanMax() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            GameHelper.randomInt(10, 5);
        }, "min > max should throw exception");
    }

    @Test
    @DisplayName("randomInt should produce variety")
    void testRandomInt_ProducesVariety() {
        // ACT - Generate many random numbers
        boolean foundDifferent = false;
        int first = GameHelper.randomInt(1, 100);

        for (int i = 0; i < 100; i++) {
            int result = GameHelper.randomInt(1, 100);
            if (result != first) {
                foundDifferent = true;
                break;
            }
        }

        // ASSERT
        assertTrue(foundDifferent, "Should produce different values");
    }

    @Test
    @DisplayName("randomInt should handle negative numbers")
    void testRandomInt_NegativeNumbers() {
        // ACT & ASSERT
        for (int i = 0; i < 50; i++) {
            int result = GameHelper.randomInt(-10, -1);
            assertTrue(result >= -10 && result <= -1,
                    "Should handle negative range: " + result);
        }

        System.out.println("✓ randomInt handles negative numbers");
    }

    @Test
    @DisplayName("randomInt should handle zero")
    void testRandomInt_WithZero() {
        // ACT & ASSERT
        for (int i = 0; i < 50; i++) {
            int result = GameHelper.randomInt(0, 5);
            assertTrue(result >= 0 && result <= 5,
                    "Should handle zero in range: " + result);
        }

        int result2 = GameHelper.randomInt(-5, 5);
        assertTrue(result2 >= -5 && result2 <= 5);

        System.out.println("✓ randomInt handles zero");
    }

    // ========================================
    // randomChance() TESTS
    // ========================================

    @Test
    @DisplayName("randomChance with 0.0 should always return false")
    void testRandomChance_Zero() {
        // ACT & ASSERT
        for (int i = 0; i < 50; i++) {
            assertFalse(GameHelper.randomChance(0.0),
                    "0.0 probability should always return false");
        }

        System.out.println("✓ randomChance(0.0) always false");
    }

    @Test
    @DisplayName("randomChance with 1.0 should always return true")
    void testRandomChance_One() {
        // ACT & ASSERT
        for (int i = 0; i < 50; i++) {
            assertTrue(GameHelper.randomChance(1.0),
                    "1.0 probability should always return true");
        }

        System.out.println("✓ randomChance(1.0) always true");
    }

    @Test
    @DisplayName("randomChance with 0.5 should return mix of true/false")
    void testRandomChance_Half() {
        // ACT - Try many times
        int trueCount = 0;
        int totalTries = 1000;

        for (int i = 0; i < totalTries; i++) {
            if (GameHelper.randomChance(0.5)) {
                trueCount++;
            }
        }

        // ASSERT - Should be roughly 50% (allow 40-60%)
        double percentage = (trueCount * 100.0) / totalTries;
        assertTrue(percentage >= 40 && percentage <= 60,
                "0.5 probability should give ~50%, got: " + percentage + "%");

        System.out.printf("✓ randomChance(0.5) gave %.1f%% true%n", percentage);
    }

    @Test
    @DisplayName("randomChance with invalid probability should throw exception")
    void testRandomChance_InvalidProbability() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            GameHelper.randomChance(-0.1);
        }, "Negative probability should throw exception");

        assertThrows(IllegalArgumentException.class, () -> {
            GameHelper.randomChance(1.1);
        }, "Probability > 1.0 should throw exception");
    }

    // ========================================
    // randomPosition() TESTS
    // ========================================

    @Test
    @DisplayName("randomPosition should return valid position")
    void testRandomPosition_Valid() {
        // ARRANGE
        int gridSize = 10;

        // ACT & ASSERT
        for (int i = 0; i < 100; i++) {
            Position pos = GameHelper.randomPosition(gridSize);

            assertNotNull(pos, "Position should not be null");
            assertTrue(pos.isValid(gridSize),
                    "Position should be valid: " + pos);
        }

        System.out.println("✓ randomPosition generates valid positions");
    }

    @Test
    @DisplayName("randomPosition should produce variety")
    void testRandomPosition_Variety() {
        // ARRANGE
        int gridSize = 10;

        // ACT - Generate positions
        boolean foundDifferent = false;
        Position first = GameHelper.randomPosition(gridSize);

        for (int i = 0; i < 100; i++) {
            Position pos = GameHelper.randomPosition(gridSize);
            if (!pos.equals(first)) {
                foundDifferent = true;
                break;
            }
        }

        // ASSERT
        assertTrue(foundDifferent, "Should generate different positions");
    }

    @Test
    @DisplayName("randomPosition with invalid grid size should throw exception")
    void testRandomPosition_InvalidGridSize() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            GameHelper.randomPosition(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            GameHelper.randomPosition(-1);
        });
    }

    @Test
    @DisplayName("randomPosition with size 1 should return (0,0)")
    void testRandomPosition_Size1() {
        // ACT
        Position pos = GameHelper.randomPosition(1);

        // ASSERT
        assertEquals(new Position(0, 0), pos,
                "1x1 grid should only have (0,0)");
    }

    // ========================================
    // randomEdgePosition() TESTS
    // ========================================

    @Test
    @DisplayName("randomEdgePosition should return edge position")
    void testRandomEdgePosition_IsEdge() {
        // ARRANGE
        int gridSize = 10;

        // ACT & ASSERT
        for (int i = 0; i < 100; i++) {
            Position pos = GameHelper.randomEdgePosition(gridSize);

            assertNotNull(pos, "Position should not be null");
            assertTrue(pos.isEdge(gridSize),
                    "Position should be on edge: " + pos);
        }

        System.out.println("✓ randomEdgePosition generates edge positions");
    }

    @Test
    @DisplayName("randomEdgePosition should produce variety")
    void testRandomEdgePosition_Variety() {
        // ARRANGE
        int gridSize = 10;

        // ACT - Generate positions
        boolean foundDifferent = false;
        Position first = GameHelper.randomEdgePosition(gridSize);

        for (int i = 0; i < 100; i++) {
            Position pos = GameHelper.randomEdgePosition(gridSize);
            if (!pos.equals(first)) {
                foundDifferent = true;
                break;
            }
        }

        // ASSERT
        assertTrue(foundDifferent, "Should generate different edge positions");
    }

    @Test
    @DisplayName("randomEdgePosition with invalid grid size should throw exception")
    void testRandomEdgePosition_InvalidGridSize() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            GameHelper.randomEdgePosition(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            GameHelper.randomEdgePosition(-1);
        });
    }

    @Test
    @DisplayName("randomEdgePosition with size 1 should return (0,0)")
    void testRandomEdgePosition_Size1() {
        // ACT
        Position pos = GameHelper.randomEdgePosition(1);

        // ASSERT
        assertEquals(new Position(0, 0), pos,
                "1x1 grid has only one position which is edge");
    }

    @Test
    @DisplayName("randomEdgePosition should cover all edges")
    void testRandomEdgePosition_CoverageAllEdges() {
        // ARRANGE
        int gridSize = 10;
        boolean foundTop = false;
        boolean foundBottom = false;
        boolean foundLeft = false;
        boolean foundRight = false;

        // ACT - Generate many positions
        for (int i = 0; i < 1000 && !(foundTop && foundBottom && foundLeft && foundRight); i++) {
            Position pos = GameHelper.randomEdgePosition(gridSize);

            if (pos.getRow() == 0) foundTop = true;
            if (pos.getRow() == gridSize - 1) foundBottom = true;
            if (pos.getColumn() == 0) foundLeft = true;
            if (pos.getColumn() == gridSize - 1) foundRight = true;
        }

        // ASSERT
        assertTrue(foundTop, "Should generate positions on top edge");
        assertTrue(foundBottom, "Should generate positions on bottom edge");
        assertTrue(foundLeft, "Should generate positions on left edge");
        assertTrue(foundRight, "Should generate positions on right edge");

        System.out.println("✓ All edges covered");
    }

    // ========================================
    // MANHATTAN DISTANCE TESTS
    // ========================================

    @Test
    @DisplayName("manhattanDistance should calculate correct distance")
    void testManhattanDistance() {
        // ARRANGE
        Position pos1 = new Position(2, 3);
        Position pos2 = new Position(5, 7);
        // Distance = |5-2| + |7-3| = 3 + 4 = 7

        // ACT
        int distance = GameHelper.manhattanDistance(pos1, pos2);

        // ASSERT
        assertEquals(7, distance);
    }

    @Test
    @DisplayName("manhattanDistance should be symmetric")
    void testManhattanDistance_Symmetric() {
        // ARRANGE
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(5, 5);

        // ACT
        int dist1to2 = GameHelper.manhattanDistance(pos1, pos2);
        int dist2to1 = GameHelper.manhattanDistance(pos2, pos1);

        // ASSERT
        assertEquals(dist1to2, dist2to1, "Distance should be symmetric");
    }

    @Test
    @DisplayName("manhattanDistance to same position should be 0")
    void testManhattanDistance_SamePosition() {
        // ARRANGE
        Position pos = new Position(5, 5);

        // ACT
        int distance = GameHelper.manhattanDistance(pos, pos);

        // ASSERT
        assertEquals(0, distance, "Distance to same position should be 0");
    }

    @Test
    @DisplayName("manhattanDistance with null should throw exception")
    void testManhattanDistance_NullPosition() {
        // ARRANGE
        Position pos = new Position(5, 5);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            GameHelper.manhattanDistance(pos, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            GameHelper.manhattanDistance(null, pos);
        });
    }

    // ========================================
    // ADJACENT POSITIONS TESTS
    // ========================================

    @Test
    @DisplayName("areAdjacent should detect adjacent positions")
    void testAreAdjacent_True() {
        // ARRANGE & ACT & ASSERT
        assertTrue(GameHelper.areAdjacent(
                new Position(5, 5), new Position(5, 6)), "Horizontally adjacent");
        assertTrue(GameHelper.areAdjacent(
                new Position(5, 5), new Position(6, 5)), "Vertically adjacent");
        assertTrue(GameHelper.areAdjacent(
                new Position(5, 6), new Position(5, 5)), "Symmetric");
    }

    @Test
    @DisplayName("areAdjacent should return false for non-adjacent")
    void testAreAdjacent_False() {
        // ARRANGE & ACT & ASSERT
        assertFalse(GameHelper.areAdjacent(
                new Position(5, 5), new Position(7, 7)), "Diagonal not adjacent");
        assertFalse(GameHelper.areAdjacent(
                new Position(5, 5), new Position(5, 5)), "Same position not adjacent");
        assertFalse(GameHelper.areAdjacent(
                new Position(0, 0), new Position(9, 9)), "Far apart");
    }

    // ========================================
    // INTEGRATION TESTS
    // ========================================

    @Test
    @DisplayName("Integration: Random position generation workflow")
    void testIntegration_RandomWorkflow() {
        System.out.println("\n=== Random Position Generation Workflow ===");

        int gridSize = 10;

        // Generate random position
        Position anyPos = GameHelper.randomPosition(gridSize);
        System.out.println("Random position: " + anyPos);
        assertTrue(anyPos.isValid(gridSize));

        // Generate edge position
        Position edgePos = GameHelper.randomEdgePosition(gridSize);
        System.out.println("Edge position: " + edgePos);
        assertTrue(edgePos.isEdge(gridSize));

        // Calculate distance
        int distance = GameHelper.manhattanDistance(anyPos, edgePos);
        System.out.println("Distance: " + distance);
        assertTrue(distance >= 0);

        // Check if adjacent
        boolean adjacent = GameHelper.areAdjacent(anyPos, edgePos);
        System.out.println("Adjacent: " + adjacent);

        System.out.println("✓ Random workflow complete");
    }

    // ========================================
    // CONSTRUCTOR TEST (Utility Class)
    // ========================================

    @Test
    @DisplayName("GameHelper constructor should throw AssertionError")
    void testConstructor_ShouldThrow() {
        // Utility classes should not be instantiable
        // This tests the private constructor with AssertionError

        try {
            // We need reflection to test private constructor
            java.lang.reflect.Constructor<GameHelper> constructor =
                    GameHelper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            // ACT & ASSERT
            assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
                constructor.newInstance();
            }, "Private constructor should throw exception");

        } catch (NoSuchMethodException e) {
            fail("GameHelper should have a private constructor");
        }
    }
}