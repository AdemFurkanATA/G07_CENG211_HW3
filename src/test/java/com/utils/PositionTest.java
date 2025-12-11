package com.utils;

import com.enums.Direction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Position Class Test File
 */
@DisplayName("Position Class Tests 🎯")
class PositionTest {

    // Test data that will be used across multiple tests
    private Position origin;
    private Position middle;
    private Position edge;

    /**
     * @BeforeEach runs BEFORE each test method
     * Used to prepare test data (Setup/Arrange phase)
     */
    @BeforeEach
    void setUp() {
        System.out.println("🔧 Setting up test...");
        origin = new Position(0, 0);     // Origin point
        middle = new Position(5, 5);     // Middle point
        edge = new Position(9, 9);       // Edge point (for 10x10 grid)
    }

    /**
     * @AfterEach runs AFTER each test method
     * Used for cleanup operations
     */
    @AfterEach
    void tearDown() {
        System.out.println("✓ Test completed!\n");
    }

    @Test
    @DisplayName("Constructor should create position with given coordinates")
    void testConstructor() {
        // ARRANGE (Prepare test data)
        int expectedRow = 3;
        int expectedColumn = 7;

        // ACT (Execute the code being tested)
        Position position = new Position(expectedRow, expectedColumn);

        // ASSERT (Verify the result)
        assertEquals(expectedRow, position.getRow(),
                "Row should match the value provided to constructor");
        assertEquals(expectedColumn, position.getColumn(),
                "Column should match the value provided to constructor");

        System.out.println("✓ Constructor test passed: " + position);
    }

    @Test
    @DisplayName("Copy constructor should create a copy of position")
    void testCopyConstructor() {
        // ARRANGE
        Position original = new Position(4, 6);

        // ACT
        Position copy = new Position(original);

        // ASSERT
        assertEquals(original.getRow(), copy.getRow());
        assertEquals(original.getColumn(), copy.getColumn());

        assertNotSame(original, copy,
                "Copy constructor should create a new object (different reference)");

        System.out.println("✓ Copy constructor test passed");
    }

    @Test
    @DisplayName("Copying null position should throw exception")
    void testCopyConstructorWithNull() {
        // ACT & ASSERT combined
        assertThrows(IllegalArgumentException.class, () -> {
            new Position(null);
        }, "Copying null position should throw IllegalArgumentException");

        System.out.println("✓ Null check test passed");
    }

    @Test
    @DisplayName("Getter methods should return correct values")
    void testGetters() {
        // ASSERT
        assertEquals(0, origin.getRow());
        assertEquals(0, origin.getColumn());
        assertEquals(5, middle.getRow());
        assertEquals(5, middle.getColumn());

        System.out.println("✓ Getter tests passed");
    }

    @Test
    @DisplayName("Setter methods should update values")
    void testSetters() {
        // ARRANGE
        Position pos = new Position(0, 0);

        // ACT
        pos.setRow(7);
        pos.setColumn(3);

        // ASSERT
        assertEquals(7, pos.getRow());
        assertEquals(3, pos.getColumn());

        System.out.println("✓ Setter tests passed");
    }


    @Test
    @DisplayName("isValid should return true for positions inside grid")
    void testIsValid_ValidPosition() {
        // ARRANGE
        int gridSize = 10;

        // ASSERT
        assertTrue(origin.isValid(gridSize), "Origin (0,0) should be valid");
        assertTrue(middle.isValid(gridSize), "Middle (5,5) should be valid");
        assertTrue(edge.isValid(gridSize), "Edge (9,9) should be valid");

        System.out.println("✓ Valid position test passed");
    }

    @Test
    @DisplayName("isValid should return false for positions outside grid")
    void testIsValid_InvalidPosition() {
        // ARRANGE
        int gridSize = 10;
        Position outOfBounds = new Position(10, 10); // Outside grid
        Position negative = new Position(-1, 5);      // Negative coordinate

        // ASSERT
        assertFalse(outOfBounds.isValid(gridSize),
                "Position outside grid should be invalid");
        assertFalse(negative.isValid(gridSize),
                "Negative coordinate should be invalid");

        System.out.println("✓ Invalid position test passed");
    }

    @Test
    @DisplayName("isEdge should correctly detect edge positions")
    void testIsEdge() {
        // ARRANGE
        int gridSize = 10;
        Position topEdge = new Position(0, 5);
        Position bottomEdge = new Position(9, 5);
        Position leftEdge = new Position(5, 0);
        Position rightEdge = new Position(5, 9);
        Position center = new Position(5, 5);

        // ASSERT
        assertTrue(topEdge.isEdge(gridSize), "Top edge should be detected");
        assertTrue(bottomEdge.isEdge(gridSize), "Bottom edge should be detected");
        assertTrue(leftEdge.isEdge(gridSize), "Left edge should be detected");
        assertTrue(rightEdge.isEdge(gridSize), "Right edge should be detected");
        assertTrue(edge.isEdge(gridSize), "Corner is also an edge");
        assertFalse(center.isEdge(gridSize), "Center is not an edge");

        System.out.println("✓ Edge detection test passed");
    }

    @Test
    @DisplayName("isCorner should correctly detect corner positions")
    void testIsCorner() {
        // ARRANGE
        int gridSize = 10;
        Position topLeft = new Position(0, 0);
        Position topRight = new Position(0, 9);
        Position bottomLeft = new Position(9, 0);
        Position bottomRight = new Position(9, 9);
        Position notCorner = new Position(0, 5);

        // ASSERT
        assertTrue(topLeft.isCorner(gridSize), "Top-left corner");
        assertTrue(topRight.isCorner(gridSize), "Top-right corner");
        assertTrue(bottomLeft.isCorner(gridSize), "Bottom-left corner");
        assertTrue(bottomRight.isCorner(gridSize), "Bottom-right corner");
        assertFalse(notCorner.isCorner(gridSize), "Edge but not corner");

        System.out.println("✓ Corner detection test passed");
    }

    @Test
    @DisplayName("getNextPosition should return correct next position")
    void testGetNextPosition() {
        // ARRANGE
        Position start = new Position(5, 5);

        // ACT & ASSERT
        Position up = start.getNextPosition(Direction.UP);
        assertEquals(4, up.getRow(), "UP: row should decrease");
        assertEquals(5, up.getColumn(), "UP: column should stay same");

        Position down = start.getNextPosition(Direction.DOWN);
        assertEquals(6, down.getRow(), "DOWN: row should increase");
        assertEquals(5, down.getColumn(), "DOWN: column should stay same");

        Position left = start.getNextPosition(Direction.LEFT);
        assertEquals(5, left.getRow(), "LEFT: row should stay same");
        assertEquals(4, left.getColumn(), "LEFT: column should decrease");

        Position right = start.getNextPosition(Direction.RIGHT);
        assertEquals(5, right.getRow(), "RIGHT: row should stay same");
        assertEquals(6, right.getColumn(), "RIGHT: column should increase");

        // Original position should not change (immutability test)
        assertEquals(5, start.getRow(), "Original position should not change");
        assertEquals(5, start.getColumn(), "Original position should not change");

        System.out.println("✓ GetNextPosition test passed");
    }

    @Test
    @DisplayName("distanceTo should calculate Manhattan distance correctly")
    void testDistanceTo() {
        // ARRANGE
        Position pos1 = new Position(2, 3);
        Position pos2 = new Position(5, 7);
        // Manhattan distance = |5-2| + |7-3| = 3 + 4 = 7

        // ACT
        int distance = pos1.distanceTo(pos2);

        // ASSERT
        assertEquals(7, distance, "Manhattan distance should be calculated correctly");

        // Distance should be symmetric
        assertEquals(distance, pos2.distanceTo(pos1),
                "Distance should be symmetric (A->B == B->A)");

        System.out.println("✓ Distance calculation test passed");
    }


    /**
     * @ParameterizedTest - Runs the same test with different parameters
     * Instead of writing many similar tests, we use parameterized tests
     */
    @ParameterizedTest
    @DisplayName("Validation for different grid sizes")
    @CsvSource({
            "0, 0, 5, true",     // Origin, 5x5 grid
            "4, 4, 5, true",     // Edge, 5x5 grid
            "5, 5, 5, false",    // Out of bounds, 5x5 grid
            "-1, 0, 5, false",   // Negative row
            "0, -1, 5, false",   // Negative column
            "0, 0, 10, true",    // Origin, 10x10 grid
            "9, 9, 10, true",    // Edge, 10x10 grid
            "10, 10, 10, false"  // Out of bounds, 10x10 grid
    })
    void testIsValidWithMultipleScenarios(int row, int col, int gridSize, boolean expected) {
        // ARRANGE
        Position pos = new Position(row, col);

        // ACT
        boolean result = pos.isValid(gridSize);

        // ASSERT
        assertEquals(expected, result,
                String.format("Position(%d,%d) for grid(%d) should be %s",
                        row, col, gridSize, expected ? "valid" : "invalid"));
    }

    @ParameterizedTest
    @DisplayName("Invalid grid sizes should throw exception")
    @ValueSource(ints = {0, -1, -10, -100})
    void testInvalidGridSizes(int invalidSize) {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            origin.isValid(invalidSize);
        }, "Invalid grid size should throw exception: " + invalidSize);
    }


    @Test
    @DisplayName("Same positions should be equal (equals)")
    void testEquals_SamePosition() {
        // ARRANGE
        Position pos1 = new Position(3, 4);
        Position pos2 = new Position(3, 4);
        Position pos3 = pos1; // Same reference

        // ASSERT
        assertEquals(pos1, pos2, "Same coordinates should be equal");
        assertEquals(pos1, pos3, "Same reference should be equal");
        assertEquals(pos1, pos1, "Should equal itself");

        System.out.println("✓ Equals test passed");
    }

    @Test
    @DisplayName("Different positions should not be equal")
    void testEquals_DifferentPosition() {
        // ARRANGE
        Position pos1 = new Position(3, 4);
        Position pos2 = new Position(3, 5); // Different column
        Position pos3 = new Position(4, 4); // Different row

        // ASSERT
        assertNotEquals(pos1, pos2, "Different column should not be equal");
        assertNotEquals(pos1, pos3, "Different row should not be equal");
        assertNotEquals(pos1, null, "Should not equal null");

        System.out.println("✓ Not equals test passed");
    }

    @Test
    @DisplayName("hashCode should be consistent with equals")
    void testHashCode_ConsistentWithEquals() {
        // ARRANGE
        Position pos1 = new Position(5, 7);
        Position pos2 = new Position(5, 7);
        Position pos3 = new Position(5, 8);

        // ASSERT
        // Equal objects should have same hashCode
        assertEquals(pos1.hashCode(), pos2.hashCode(),
                "Equal objects should have same hashCode");

        // Different objects should (usually) have different hashCode
        assertNotEquals(pos1.hashCode(), pos3.hashCode(),
                "Different objects should have different hashCode (usually)");

        System.out.println("✓ HashCode test passed");
    }

    @Test
    @DisplayName("Null parameters should be handled defensively")
    void testNullParameters() {
        // ASSERT - Various null scenarios
        assertThrows(IllegalArgumentException.class, () ->
                origin.distanceTo(null), "distanceTo(null) should throw exception");

        assertThrows(IllegalArgumentException.class, () ->
                origin.isAdjacentTo(null), "isAdjacentTo(null) should throw exception");

        // getNextPosition with null direction returns copy (defensive)
        Position result = origin.getNextPosition(null);
        assertNotNull(result, "Should return a result even with null direction");

        System.out.println("✓ Null parameter tests passed");
    }


    @Nested
    @DisplayName("Direction Related Tests 🧭")
    class DirectionTests {

        @Test
        @DisplayName("getDirectionTo should return correct direction for same row")
        void testGetDirectionTo_SameRow() {
            // ARRANGE
            Position pos1 = new Position(5, 3);
            Position pos2 = new Position(5, 7);

            // ACT & ASSERT
            assertEquals(Direction.RIGHT, pos1.getDirectionTo(pos2));
            assertEquals(Direction.LEFT, pos2.getDirectionTo(pos1));

            System.out.println("✓ Same row direction test passed");
        }

        @Test
        @DisplayName("getDirectionTo should return correct direction for same column")
        void testGetDirectionTo_SameColumn() {
            // ARRANGE
            Position pos1 = new Position(3, 5);
            Position pos2 = new Position(7, 5);

            // ACT & ASSERT
            assertEquals(Direction.DOWN, pos1.getDirectionTo(pos2));
            assertEquals(Direction.UP, pos2.getDirectionTo(pos1));

            System.out.println("✓ Same column direction test passed");
        }

        @Test
        @DisplayName("getDirectionTo should return null for diagonal positions")
        void testGetDirectionTo_Diagonal() {
            // ARRANGE
            Position pos1 = new Position(3, 3);
            Position pos2 = new Position(5, 7); // Diagonal

            // ACT
            Direction result = pos1.getDirectionTo(pos2);

            // ASSERT
            assertNull(result, "Should return null for diagonal positions");

            System.out.println("✓ Diagonal direction test passed");
        }
    }


    @Test
    @DisplayName("Integration: Penguin movement scenario")
    void testIntegration_MovementScenario() {
        System.out.println("\n=== Penguin Movement Scenario ===");

        // ARRANGE
        Position penguinPos = new Position(0, 0); // Starting position
        int gridSize = 10;

        System.out.println("Start: " + penguinPos);

        // ACT & ASSERT - Movement scenario
        // 1. Move right
        penguinPos = penguinPos.getNextPosition(Direction.RIGHT);
        assertEquals(new Position(0, 1), penguinPos);
        assertTrue(penguinPos.isValid(gridSize));
        System.out.println("Right: " + penguinPos);

        // 2. Move down
        penguinPos = penguinPos.getNextPosition(Direction.DOWN);
        assertEquals(new Position(1, 1), penguinPos);
        assertTrue(penguinPos.isValid(gridSize));
        System.out.println("Down: " + penguinPos);

        // 3. Check edge status
        assertTrue(penguinPos.isValid(gridSize), "Still inside grid");
        assertFalse(penguinPos.isEdge(gridSize), "No longer on edge");

        System.out.println("✓ Movement scenario passed");
    }
}