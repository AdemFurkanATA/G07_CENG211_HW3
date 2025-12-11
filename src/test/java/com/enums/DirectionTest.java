package com.enums;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing an enum is simpler than testing classes.
 * We mainly test the custom methods and enum values.
 */
@DisplayName("Direction Enum Tests 🧭")
class DirectionTest {

    @Test
    @DisplayName("Direction enum should have exactly 4 values")
    void testEnumValues() {
        // ACT
        Direction[] directions = Direction.values();

        // ASSERT
        assertEquals(4, directions.length, "Should have exactly 4 directions");
        
        System.out.println("✓ Found " + directions.length + " directions");
    }

    @Test
    @DisplayName("All expected direction values should exist")
    void testExpectedValues() {
        // ACT & ASSERT
        assertNotNull(Direction.UP, "UP should exist");
        assertNotNull(Direction.DOWN, "DOWN should exist");
        assertNotNull(Direction.LEFT, "LEFT should exist");
        assertNotNull(Direction.RIGHT, "RIGHT should exist");
        
        System.out.println("✓ All 4 directions exist");
    }

    @Test
    @DisplayName("valueOf should return correct enum constant")
    void testValueOf() {
        // ACT & ASSERT
        assertEquals(Direction.UP, Direction.valueOf("UP"));
        assertEquals(Direction.DOWN, Direction.valueOf("DOWN"));
        assertEquals(Direction.LEFT, Direction.valueOf("LEFT"));
        assertEquals(Direction.RIGHT, Direction.valueOf("RIGHT"));
        
        System.out.println("✓ valueOf works correctly");
    }

    @Test
    @DisplayName("valueOf with invalid name should throw exception")
    void testValueOf_InvalidName() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            Direction.valueOf("INVALID");
        }, "Invalid name should throw IllegalArgumentException");
    }


    @Test
    @DisplayName("getOpposite should return correct opposite for UP")
    void testGetOpposite_Up() {
        // ACT
        Direction opposite = Direction.UP.getOpposite();

        // ASSERT
        assertEquals(Direction.DOWN, opposite, "Opposite of UP should be DOWN");
    }

    @Test
    @DisplayName("getOpposite should return correct opposite for DOWN")
    void testGetOpposite_Down() {
        // ACT
        Direction opposite = Direction.DOWN.getOpposite();

        // ASSERT
        assertEquals(Direction.UP, opposite, "Opposite of DOWN should be UP");
    }

    @Test
    @DisplayName("getOpposite should return correct opposite for LEFT")
    void testGetOpposite_Left() {
        // ACT
        Direction opposite = Direction.LEFT.getOpposite();

        // ASSERT
        assertEquals(Direction.RIGHT, opposite, "Opposite of LEFT should be RIGHT");
    }

    @Test
    @DisplayName("getOpposite should return correct opposite for RIGHT")
    void testGetOpposite_Right() {
        // ACT
        Direction opposite = Direction.RIGHT.getOpposite();

        // ASSERT
        assertEquals(Direction.LEFT, opposite, "Opposite of RIGHT should be LEFT");
    }

    @Test
    @DisplayName("Opposite of opposite should return original direction")
    void testGetOpposite_DoubleOpposite() {
        // ACT & ASSERT - Apply getOpposite twice
        assertEquals(Direction.UP, Direction.UP.getOpposite().getOpposite());
        assertEquals(Direction.DOWN, Direction.DOWN.getOpposite().getOpposite());
        assertEquals(Direction.LEFT, Direction.LEFT.getOpposite().getOpposite());
        assertEquals(Direction.RIGHT, Direction.RIGHT.getOpposite().getOpposite());
        
        System.out.println("✓ Double opposite returns original");
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    @DisplayName("Each direction should have a valid opposite")
    void testGetOpposite_AllDirections(Direction direction) {
        // ACT
        Direction opposite = direction.getOpposite();

        // ASSERT
        assertNotNull(opposite, "Opposite should not be null for " + direction);
        assertNotEquals(direction, opposite, "Opposite should be different from original");
        
        System.out.println("✓ " + direction + " → " + opposite);
    }

    @Test
    @DisplayName("fromString should parse uppercase letters correctly")
    void testFromString_Uppercase() {
        // ACT & ASSERT
        assertEquals(Direction.UP, Direction.fromString("U"));
        assertEquals(Direction.DOWN, Direction.fromString("D"));
        assertEquals(Direction.LEFT, Direction.fromString("L"));
        assertEquals(Direction.RIGHT, Direction.fromString("R"));
        
        System.out.println("✓ Uppercase letters parsed correctly");
    }

    @Test
    @DisplayName("fromString should parse lowercase letters correctly")
    void testFromString_Lowercase() {
        // ACT & ASSERT
        assertEquals(Direction.UP, Direction.fromString("u"));
        assertEquals(Direction.DOWN, Direction.fromString("d"));
        assertEquals(Direction.LEFT, Direction.fromString("l"));
        assertEquals(Direction.RIGHT, Direction.fromString("r"));
        
        System.out.println("✓ Lowercase letters parsed correctly");
    }

    @Test
    @DisplayName("fromString should be case-insensitive")
    void testFromString_CaseInsensitive() {
        // ACT & ASSERT
        assertEquals(Direction.UP, Direction.fromString("U"));
        assertEquals(Direction.UP, Direction.fromString("u"));
        assertEquals(Direction.DOWN, Direction.fromString("D"));
        assertEquals(Direction.DOWN, Direction.fromString("d"));
        
        System.out.println("✓ Case-insensitive parsing works");
    }

    @Test
    @DisplayName("fromString should return null for invalid input")
    void testFromString_InvalidInput() {
        // ACT & ASSERT
        assertNull(Direction.fromString("X"), "Invalid letter should return null");
        assertNull(Direction.fromString("UP"), "Full word should return null");
        assertNull(Direction.fromString(""), "Empty string should return null");
        assertNull(Direction.fromString("123"), "Number should return null");
        
        System.out.println("✓ Invalid inputs return null");
    }

    @Test
    @DisplayName("fromString should handle null input")
    void testFromString_NullInput() {
        // ACT
        Direction result = Direction.fromString(null);

        // ASSERT
        assertNull(result, "Null input should return null");
    }

    @Test
    @DisplayName("toString should return uppercase direction name")
    void testToString() {
        // ACT & ASSERT
        assertEquals("UP", Direction.UP.toString());
        assertEquals("DOWN", Direction.DOWN.toString());
        assertEquals("LEFT", Direction.LEFT.toString());
        assertEquals("RIGHT", Direction.RIGHT.toString());
        
        System.out.println("✓ toString returns uppercase names");
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    @DisplayName("toString should match enum name for all directions")
    void testToString_MatchesName(Direction direction) {
        // ACT
        String toString = direction.toString();
        String name = direction.name();

        // ASSERT
        assertEquals(name, toString, "toString should match enum name");
    }


    @Test
    @DisplayName("Integration: Round-trip from string to Direction and back")
    void testIntegration_RoundTrip() {
        // ARRANGE
        String[] inputs = {"U", "D", "L", "R"};
        Direction[] expected = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        // ACT & ASSERT
        for (int i = 0; i < inputs.length; i++) {
            Direction parsed = Direction.fromString(inputs[i]);
            assertEquals(expected[i], parsed, "Should parse " + inputs[i]);
            
            // Back to string
            String backToString = parsed.toString();
            assertNotNull(backToString);
        }
        
        System.out.println("✓ Round-trip conversion works");
    }

    @Test
    @DisplayName("Integration: User input simulation")
    void testIntegration_UserInputSimulation() {
        System.out.println("\n=== Simulating User Input ===");

        // Simulate user typing different inputs
        String[] userInputs = {"u", "D", "l", "R", "x", "UP"};
        
        for (String input : userInputs) {
            Direction dir = Direction.fromString(input);
            
            if (dir != null) {
                System.out.println("User typed '" + input + "' → " + dir + " → Opposite: " + dir.getOpposite());
            } else {
                System.out.println("User typed '" + input + "' → Invalid input");
            }
        }
        
        System.out.println("✓ User input simulation complete");
    }

    @Test
    @DisplayName("Edge case: Opposite relationships should be symmetric")
    void testEdgeCase_OppositeSymmetry() {
        // ARRANGE
        Direction[] directions = Direction.values();

        // ACT & ASSERT
        for (Direction dir : directions) {
            Direction opposite = dir.getOpposite();
            Direction oppositeOfOpposite = opposite.getOpposite();
            
            assertEquals(dir, oppositeOfOpposite, 
                "Opposite of opposite should be original: " + dir);
        }
        
        System.out.println("✓ Opposite relationships are symmetric");
    }

    @Test
    @DisplayName("Edge case: fromString with whitespace")
    void testEdgeCase_WhitespaceInInput() {
        // ACT & ASSERT
        assertEquals(Direction.UP, Direction.fromString("U "));
        assertEquals(Direction.DOWN, Direction.fromString(" D"));
        assertEquals(Direction.LEFT, Direction.fromString(" L "));
        
        System.out.println("✓ Whitespace handling works");
    }

    @Test
    @DisplayName("Each direction should be unique")
    void testUniqueness() {
        // ARRANGE
        Direction[] directions = Direction.values();

        // ASSERT - No two directions should be the same
        for (int i = 0; i < directions.length; i++) {
            for (int j = i + 1; j < directions.length; j++) {
                assertNotEquals(directions[i], directions[j], 
                    "Each direction should be unique");
            }
        }
        
        System.out.println("✓ All directions are unique");
    }
}