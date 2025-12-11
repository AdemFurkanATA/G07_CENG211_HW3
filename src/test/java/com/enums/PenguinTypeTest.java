package com.enums;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PenguinType Enum Tests
 */
@DisplayName("PenguinType Enum Tests 🐧")
class PenguinTypeTest {

    @Test
    @DisplayName("PenguinType enum should have exactly 4 values")
    void testEnumValues() {
        // ACT
        PenguinType[] types = PenguinType.values();

        // ASSERT
        assertEquals(4, types.length, "Should have exactly 4 penguin types");
        
        System.out.println("✓ Found " + types.length + " penguin types");
    }

    @Test
    @DisplayName("All expected penguin type values should exist")
    void testExpectedValues() {
        // ACT & ASSERT
        assertNotNull(PenguinType.KING);
        assertNotNull(PenguinType.EMPEROR);
        assertNotNull(PenguinType.ROYAL);
        assertNotNull(PenguinType.ROCKHOPPER);
        
        System.out.println("✓ All 4 penguin types exist");
    }


    @ParameterizedTest
    @DisplayName("Each PenguinType should have correct display name")
    @CsvSource({
        "KING, King Penguin",
        "EMPEROR, Emperor Penguin",
        "ROYAL, Royal Penguin",
        "ROCKHOPPER, Rockhopper Penguin"
    })
    void testGetDisplayName(PenguinType type, String expectedName) {
        // ACT
        String displayName = type.getDisplayName();

        // ASSERT
        assertEquals(expectedName, displayName);
        assertTrue(displayName.contains("Penguin"), 
            "Display name should contain 'Penguin'");
    }

    @ParameterizedTest
    @EnumSource(PenguinType.class)
    @DisplayName("All display names should be non-empty")
    void testDisplayNameNotEmpty(PenguinType type) {
        // ACT
        String displayName = type.getDisplayName();

        // ASSERT
        assertNotNull(displayName, "Display name should not be null");
        assertFalse(displayName.isEmpty(), "Display name should not be empty");
        assertFalse(displayName.isBlank(), "Display name should not be blank");
    }

    @ParameterizedTest
    @EnumSource(PenguinType.class)
    @DisplayName("All display names should contain the word Penguin")
    void testDisplayNameContainsPenguin(PenguinType type) {
        // ACT
        String displayName = type.getDisplayName();

        // ASSERT
        assertTrue(displayName.contains("Penguin"), 
            "Display name should contain 'Penguin': " + displayName);
    }


    @Test
    @DisplayName("getRandomType should return a valid PenguinType")
    void testGetRandomType_ReturnsValidType() {
        // ACT
        PenguinType randomType = PenguinType.getRandomType();

        // ASSERT
        assertNotNull(randomType, "Random type should not be null");
        assertTrue(isValidPenguinType(randomType), "Should return a valid PenguinType");
        
        System.out.println("✓ Random type: " + randomType.getDisplayName());
    }

    @Test
    @DisplayName("getRandomType should produce variety across multiple calls")
    void testGetRandomType_ProducesVariety() {
        // ACT - Generate 100 random types
        boolean foundDifferentTypes = false;
        PenguinType firstType = PenguinType.getRandomType();
        
        for (int i = 0; i < 100; i++) {
            PenguinType type = PenguinType.getRandomType();
            
            // Should always be valid
            assertNotNull(type);
            
            // Check for variety
            if (type != firstType) {
                foundDifferentTypes = true;
                break;
            }
        }

        // ASSERT
        assertTrue(foundDifferentTypes, 
            "Should see different types in 100 random calls");
        
        System.out.println("✓ Random type produces variety");
    }

    @Test
    @DisplayName("getRandomType should eventually return all types")
    void testGetRandomType_CoverageOfAllTypes() {
        // ACT - Track which types we've seen
        boolean[] seenTypes = new boolean[PenguinType.values().length];
        int maxAttempts = 1000;
        int attempts = 0;
        
        while (attempts < maxAttempts && !allTypesSeen(seenTypes)) {
            PenguinType type = PenguinType.getRandomType();
            seenTypes[type.ordinal()] = true;
            attempts++;
        }

        // ASSERT
        assertTrue(allTypesSeen(seenTypes), 
            "Should see all penguin types within " + maxAttempts + " attempts");
        
        System.out.println("✓ Saw all types in " + attempts + " attempts");
    }

    @Test
    @DisplayName("toString should return display name")
    void testToString() {
        // ACT & ASSERT
        assertEquals("King Penguin", PenguinType.KING.toString());
        assertEquals("Emperor Penguin", PenguinType.EMPEROR.toString());
        assertEquals("Royal Penguin", PenguinType.ROYAL.toString());
        assertEquals("Rockhopper Penguin", PenguinType.ROCKHOPPER.toString());
    }

    @ParameterizedTest
    @EnumSource(PenguinType.class)
    @DisplayName("toString should match getDisplayName")
    void testToString_MatchesDisplayName(PenguinType type) {
        // ACT
        String toString = type.toString();
        String displayName = type.getDisplayName();

        // ASSERT
        assertEquals(displayName, toString, 
            "toString should match getDisplayName");
    }

    @Test
    @DisplayName("Integration: All PenguinTypes should have unique display names")
    void testIntegration_UniqueDisplayNames() {
        // ARRANGE
        PenguinType[] types = PenguinType.values();
        String[] displayNames = new String[types.length];
        
        // ACT - Collect all display names
        for (int i = 0; i < types.length; i++) {
            displayNames[i] = types[i].getDisplayName();
        }

        // ASSERT - No duplicates
        for (int i = 0; i < displayNames.length; i++) {
            for (int j = i + 1; j < displayNames.length; j++) {
                assertNotEquals(displayNames[i], displayNames[j], 
                    "Each PenguinType should have unique display name");
            }
        }
        
        System.out.println("✓ All display names are unique");
    }

    @Test
    @DisplayName("Integration: Random type distribution")
    void testIntegration_RandomDistribution() {
        System.out.println("\n=== Random Type Distribution ===");
        
        // ACT - Generate 1000 random types and count
        int[] counts = new int[PenguinType.values().length];
        int totalCalls = 1000;
        
        for (int i = 0; i < totalCalls; i++) {
            PenguinType type = PenguinType.getRandomType();
            counts[type.ordinal()]++;
        }

        // ASSERT & Display
        PenguinType[] types = PenguinType.values();
        for (int i = 0; i < types.length; i++) {
            double percentage = (counts[i] * 100.0) / totalCalls;
            System.out.printf("%s: %d (%.1f%%)%n", 
                types[i].getDisplayName(), counts[i], percentage);
            
            // Each type should appear at least once
            assertTrue(counts[i] > 0, 
                types[i] + " should appear at least once");
            
            // Rough distribution check (allow for randomness)
            // Expected: ~25% each (250/1000), allow 15-35%
            assertTrue(counts[i] >= 150 && counts[i] <= 350, 
                types[i] + " distribution seems off: " + counts[i]);
        }
        
        System.out.println("✓ Distribution looks reasonable");
    }

    @Test
    @DisplayName("Integration: Penguin type descriptions")
    void testIntegration_TypeDescriptions() {
        System.out.println("\n=== Penguin Type Descriptions ===");
        
        for (PenguinType type : PenguinType.values()) {
            System.out.printf("%-20s (Enum: %s)%n", 
                type.getDisplayName(), type.name());
        }
        
        System.out.println("✓ All types listed");
    }


    @Test
    @DisplayName("Each PenguinType should be unique")
    void testUniqueness() {
        // ARRANGE
        PenguinType[] types = PenguinType.values();

        // ASSERT
        for (int i = 0; i < types.length; i++) {
            for (int j = i + 1; j < types.length; j++) {
                assertNotEquals(types[i], types[j], 
                    "Each PenguinType should be unique");
            }
        }
        
        System.out.println("✓ All penguin types are unique");
    }

    @Test
    @DisplayName("valueOf should work for all PenguinTypes")
    void testValueOf() {
        // ACT & ASSERT
        assertEquals(PenguinType.KING, PenguinType.valueOf("KING"));
        assertEquals(PenguinType.EMPEROR, PenguinType.valueOf("EMPEROR"));
        assertEquals(PenguinType.ROYAL, PenguinType.valueOf("ROYAL"));
        assertEquals(PenguinType.ROCKHOPPER, PenguinType.valueOf("ROCKHOPPER"));
    }

    @Test
    @DisplayName("valueOf with invalid name should throw exception")
    void testValueOf_InvalidName() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            PenguinType.valueOf("INVALID");
        });
    }

    @Test
    @DisplayName("Penguin types should match game requirements")
    void testGameRequirements() {
        // According to the game, there are 4 penguin types with special abilities
        // KING: Can stop at 5th square
        // EMPEROR: Can stop at 3rd square
        // ROYAL: Can move one square before sliding
        // ROCKHOPPER: Can jump over one hazard
        
        PenguinType[] types = PenguinType.values();
        assertEquals(4, types.length, "Game requires exactly 4 penguin types");
        
        // Verify all required types exist
        boolean hasKing = false;
        boolean hasEmperor = false;
        boolean hasRoyal = false;
        boolean hasRockhopper = false;
        
        for (PenguinType type : types) {
            if (type == PenguinType.KING) hasKing = true;
            if (type == PenguinType.EMPEROR) hasEmperor = true;
            if (type == PenguinType.ROYAL) hasRoyal = true;
            if (type == PenguinType.ROCKHOPPER) hasRockhopper = true;
        }
        
        assertTrue(hasKing, "Should have KING penguin");
        assertTrue(hasEmperor, "Should have EMPEROR penguin");
        assertTrue(hasRoyal, "Should have ROYAL penguin");
        assertTrue(hasRockhopper, "Should have ROCKHOPPER penguin");
        
        System.out.println("✓ All required penguin types are present");
    }


    private boolean isValidPenguinType(PenguinType type) {
        for (PenguinType validType : PenguinType.values()) {
            if (validType == type) {
                return true;
            }
        }
        return false;
    }

    private boolean allTypesSeen(boolean[] seen) {
        for (boolean b : seen) {
            if (!b) return false;
        }
        return true;
    }
}