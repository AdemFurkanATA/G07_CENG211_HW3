package com.enums;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FoodType Enum Tests
 *
 * Location: src/test/java/com/enums/FoodTypeTest.java
 */
@DisplayName("FoodType Enum Tests 🐟")
class FoodTypeTest {

    @Test
    @DisplayName("FoodType enum should have exactly 5 values")
    void testEnumValues() {
        // ACT
        FoodType[] types = FoodType.values();

        // ASSERT
        assertEquals(5, types.length, "Should have exactly 5 food types");

        System.out.println("✓ Found " + types.length + " food types");
    }

    @Test
    @DisplayName("All expected food type values should exist")
    void testExpectedValues() {
        // ACT & ASSERT
        assertNotNull(FoodType.KRILL);
        assertNotNull(FoodType.CRUSTACEAN);
        assertNotNull(FoodType.ANCHOVY);
        assertNotNull(FoodType.SQUID);
        assertNotNull(FoodType.MACKEREL);

        System.out.println("✓ All 5 food types exist");
    }

    // ========================================
    // getShorthand() TESTS
    // ========================================

    @ParameterizedTest
    @DisplayName("Each FoodType should have a 2-letter shorthand")
    @CsvSource({
            "KRILL, Kr",
            "CRUSTACEAN, Cr",
            "ANCHOVY, An",
            "SQUID, Sq",
            "MACKEREL, Ma"
    })
    void testGetShorthand(FoodType type, String expectedShorthand) {
        // ACT
        String shorthand = type.getShorthand();

        // ASSERT
        assertEquals(expectedShorthand, shorthand);
        assertEquals(2, shorthand.length(), "Shorthand should be exactly 2 characters");
    }

    @ParameterizedTest
    @EnumSource(FoodType.class)
    @DisplayName("All shorthands should be exactly 2 characters")
    void testShorthandLength(FoodType type) {
        // ACT
        String shorthand = type.getShorthand();

        // ASSERT
        assertNotNull(shorthand);
        assertEquals(2, shorthand.length(),
                type + " shorthand should be 2 characters, but was: " + shorthand);
    }

    @ParameterizedTest
    @EnumSource(FoodType.class)
    @DisplayName("All shorthands should be non-empty")
    void testShorthandNotEmpty(FoodType type) {
        // ACT
        String shorthand = type.getShorthand();

        // ASSERT
        assertNotNull(shorthand, "Shorthand should not be null");
        assertFalse(shorthand.isEmpty(), "Shorthand should not be empty");
        assertFalse(shorthand.isBlank(), "Shorthand should not be blank");
    }

    // ========================================
    // getRandomType() TESTS
    // ========================================

    @Test
    @DisplayName("getRandomType should return a valid FoodType")
    void testGetRandomType_ReturnsValidType() {
        // ACT
        FoodType randomType = FoodType.getRandomType();

        // ASSERT
        assertNotNull(randomType, "Random type should not be null");
        assertTrue(isValidFoodType(randomType), "Should return a valid FoodType");

        System.out.println("✓ Random type: " + randomType);
    }

    @Test
    @DisplayName("getRandomType should produce variety across multiple calls")
    void testGetRandomType_ProducesVariety() {
        // ACT - Generate 100 random types
        boolean foundDifferentTypes = false;
        FoodType firstType = FoodType.getRandomType();

        for (int i = 0; i < 100; i++) {
            FoodType type = FoodType.getRandomType();

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
        boolean[] seenTypes = new boolean[FoodType.values().length];
        int maxAttempts = 1000;
        int attempts = 0;

        while (attempts < maxAttempts && !allTypesSeen(seenTypes)) {
            FoodType type = FoodType.getRandomType();
            seenTypes[type.ordinal()] = true;
            attempts++;
        }

        // ASSERT
        assertTrue(allTypesSeen(seenTypes),
                "Should see all food types within " + maxAttempts + " attempts");

        System.out.println("✓ Saw all types in " + attempts + " attempts");
    }

    // ========================================
    // toString() TESTS
    // ========================================

    @ParameterizedTest
    @DisplayName("toString should return capitalized food name")
    @CsvSource({
            "KRILL, Krill",
            "CRUSTACEAN, Crustacean",
            "ANCHOVY, Anchovy",
            "SQUID, Squid",
            "MACKEREL, Mackerel"
    })
    void testToString(FoodType type, String expectedString) {
        // ACT
        String result = type.toString();

        // ASSERT
        assertEquals(expectedString, result);

        // First character should be uppercase
        assertTrue(Character.isUpperCase(result.charAt(0)),
                "First character should be uppercase");

        // Rest should be lowercase
        for (int i = 1; i < result.length(); i++) {
            assertTrue(Character.isLowerCase(result.charAt(i)),
                    "Characters after first should be lowercase");
        }
    }

    @ParameterizedTest
    @EnumSource(FoodType.class)
    @DisplayName("toString should not be empty")
    void testToString_NotEmpty(FoodType type) {
        // ACT
        String result = type.toString();

        // ASSERT
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.length() > 0);
    }

    // ========================================
    // INTEGRATION TESTS
    // ========================================

    @Test
    @DisplayName("Integration: All FoodTypes should have unique shorthands")
    void testIntegration_UniqueShorthands() {
        // ARRANGE
        FoodType[] types = FoodType.values();
        String[] shorthands = new String[types.length];

        // ACT - Collect all shorthands
        for (int i = 0; i < types.length; i++) {
            shorthands[i] = types[i].getShorthand();
        }

        // ASSERT - No duplicates
        for (int i = 0; i < shorthands.length; i++) {
            for (int j = i + 1; j < shorthands.length; j++) {
                assertNotEquals(shorthands[i], shorthands[j],
                        "Each FoodType should have unique shorthand: " +
                                types[i] + " and " + types[j] + " both have '" + shorthands[i] + "'");
            }
        }

        System.out.println("✓ All shorthands are unique");
    }

    @Test
    @DisplayName("Integration: Random type distribution")
    void testIntegration_RandomDistribution() {
        System.out.println("\n=== Random Type Distribution ===");

        // ACT - Generate 1000 random types and count
        int[] counts = new int[FoodType.values().length];
        int totalCalls = 1000;

        for (int i = 0; i < totalCalls; i++) {
            FoodType type = FoodType.getRandomType();
            counts[type.ordinal()]++;
        }

        // ASSERT & Display
        FoodType[] types = FoodType.values();
        for (int i = 0; i < types.length; i++) {
            double percentage = (counts[i] * 100.0) / totalCalls;
            System.out.printf("%s: %d (%.1f%%)%n",
                    types[i], counts[i], percentage);

            // Each type should appear at least once
            assertTrue(counts[i] > 0,
                    types[i] + " should appear at least once");

            // Rough distribution check (allow for randomness)
            // Expected: ~20% each (200/1000), allow 10-30%
            assertTrue(counts[i] >= 100 && counts[i] <= 300,
                    types[i] + " distribution seems off: " + counts[i]);
        }

        System.out.println("✓ Distribution looks reasonable");
    }

    // ========================================
    // EDGE CASES
    // ========================================

    @Test
    @DisplayName("Each FoodType should be unique")
    void testUniqueness() {
        // ARRANGE
        FoodType[] types = FoodType.values();

        // ASSERT
        for (int i = 0; i < types.length; i++) {
            for (int j = i + 1; j < types.length; j++) {
                assertNotEquals(types[i], types[j],
                        "Each FoodType should be unique");
            }
        }

        System.out.println("✓ All food types are unique");
    }

    @Test
    @DisplayName("valueOf should work for all FoodTypes")
    void testValueOf() {
        // ACT & ASSERT
        assertEquals(FoodType.KRILL, FoodType.valueOf("KRILL"));
        assertEquals(FoodType.CRUSTACEAN, FoodType.valueOf("CRUSTACEAN"));
        assertEquals(FoodType.ANCHOVY, FoodType.valueOf("ANCHOVY"));
        assertEquals(FoodType.SQUID, FoodType.valueOf("SQUID"));
        assertEquals(FoodType.MACKEREL, FoodType.valueOf("MACKEREL"));
    }

    @Test
    @DisplayName("valueOf with invalid name should throw exception")
    void testValueOf_InvalidName() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            FoodType.valueOf("INVALID");
        });
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    private boolean isValidFoodType(FoodType type) {
        for (FoodType validType : FoodType.values()) {
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