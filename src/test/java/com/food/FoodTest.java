package com.food;

import com.enums.FoodType;
import com.utils.Position;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Food Test
 */
@DisplayName("Food Class Tests 🐟")
class FoodTest {

    private Position testPosition;

    @BeforeEach
    void setUp() {
        testPosition = new Position(5, 5);
    }


    @Test
    @DisplayName("Food object should be created with valid parameters")
    void testConstructor_ValidParameters() {
        // ARRANGE
        FoodType type = FoodType.KRILL;
        int weight = 3;

        // ACT
        Food food = new Food(testPosition, type, weight);

        // ASSERT
        assertNotNull(food, "Food object should not be null");
        assertEquals(type, food.getType(), "FoodType should be set correctly");
        assertEquals(weight, food.getWeight(), "Weight should be set correctly");

        Position returnedPos = food.getPosition();
        assertNotSame(testPosition, returnedPos,
                "Position should be a defensive copy (different reference)");
        assertEquals(testPosition, returnedPos,
                "But should be equal in value");

        System.out.println("✓ Constructor test successful: " + food);
    }

    @Test
    @DisplayName("Creating Food with null position should throw exception")
    void testConstructor_NullPosition() {
        // ACT & ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Food(null, FoodType.KRILL, 3),
                "Exception should be thrown for null position"
        );

        assertTrue(exception.getMessage().contains("position"),
                "Exception message should contain 'position'");

        System.out.println("✓ Exception message: " + exception.getMessage());
    }

    @Test
    @DisplayName("Creating Food with null FoodType should throw exception")
    void testConstructor_NullType() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new Food(testPosition, null, 3),
                "Exception should be thrown for null type");
    }

    @ParameterizedTest
    @DisplayName("Exception for invalid weight values")
    @org.junit.jupiter.params.provider.ValueSource(ints = {0, -1, -5, 6, 10, 100})
    void testConstructor_InvalidWeight(int invalidWeight) {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new Food(testPosition, FoodType.KRILL, invalidWeight),
                "Exception should be thrown for weight " + invalidWeight);
    }

    @ParameterizedTest
    @DisplayName("Food should be created with valid weight values (1-5)")
    @org.junit.jupiter.params.provider.ValueSource(ints = {1, 2, 3, 4, 5})
    void testConstructor_ValidWeights(int validWeight) {
        // ACT
        Food food = new Food(testPosition, FoodType.SQUID, validWeight);

        // ASSERT
        assertNotNull(food);
        assertEquals(validWeight, food.getWeight());

        System.out.println("✓ Weight " + validWeight + " successful");
    }


    @Test
    @DisplayName("createRandom method should create a random Food")
    void testCreateRandom() {
        // ACT
        Food food1 = Food.createRandom(testPosition);
        Food food2 = Food.createRandom(testPosition);
        Food food3 = Food.createRandom(testPosition);

        // ASSERT
        assertNotNull(food1);
        assertNotNull(food2);
        assertNotNull(food3);

        assertTrue(food1.getWeight() >= 1 && food1.getWeight() <= 5,
                "Random weight should be between 1-5");

        assertNotNull(food1.getType(), "Random FoodType should not be null");

        System.out.println("✓ Random food 1: " + food1);
        System.out.println("✓ Random food 2: " + food2);
        System.out.println("✓ Random food 3: " + food3);
    }

    @Test
    @DisplayName("Multiple calls to createRandom should show variety")
    void testCreateRandom_Variety() {
        boolean foundDifferentTypes = false;
        boolean foundDifferentWeights = false;

        FoodType firstType = null;
        int firstWeight = 0;

        for (int i = 0; i < 100; i++) {
            Food food = Food.createRandom(testPosition);

            if (i == 0) {
                firstType = food.getType();
                firstWeight = food.getWeight();
            } else {
                if (food.getType() != firstType) foundDifferentTypes = true;
                if (food.getWeight() != firstWeight) foundDifferentWeights = true;
            }

            if (foundDifferentTypes && foundDifferentWeights) break;
        }

        assertTrue(foundDifferentTypes,
                "Different types should be seen in 100 random foods");
        assertTrue(foundDifferentWeights,
                "Different weights should be seen in 100 random foods");

        System.out.println("✓ Random variety test successful");
    }

    @Test
    @DisplayName("create static method should create Food with specified parameters")
    void testCreateMethod() {
        // ACT
        Food food = Food.create(testPosition, FoodType.MACKEREL, 4);

        // ASSERT
        assertNotNull(food);
        assertEquals(FoodType.MACKEREL, food.getType());
        assertEquals(4, food.getWeight());

        System.out.println("✓ Static create method successful");
    }


    @ParameterizedTest
    @DisplayName("Getter methods should work for all FoodTypes")
    @EnumSource(FoodType.class)
    void testGetters_AllFoodTypes(FoodType type) {
        // ACT
        Food food = new Food(testPosition, type, 3);

        // ASSERT
        assertEquals(type, food.getType());
        assertEquals(type.getShorthand(), food.getShorthand());
        assertEquals(type.toString(), food.getDisplayName());

        System.out.println("✓ Getters successful for " + type);
    }

    @Test
    @DisplayName("getPosition should return defensive copy")
    void testGetPosition_DefensiveCopy() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.ANCHOVY, 2);

        // ACT
        Position pos1 = food.getPosition();
        Position pos2 = food.getPosition();

        // ASSERT
        assertNotSame(pos1, pos2,
                "Each getPosition call should return a new object");

        assertEquals(pos1, pos2, "Values should be the same");

        assertEquals(testPosition, pos1);
        assertNotSame(testPosition, pos1);

        System.out.println("✓ Defensive copy test successful");
    }

    @Test
    @DisplayName("setPosition should also make defensive copy")
    void testSetPosition_DefensiveCopy() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.KRILL, 1);
        Position newPos = new Position(7, 8);

        // ACT
        food.setPosition(newPos);
        Position retrieved = food.getPosition();

        // ASSERT
        assertEquals(newPos, retrieved, "New position should be set");
        assertNotSame(newPos, retrieved, "Defensive copy should be made");

        newPos.setRow(999);
        assertNotEquals(999, retrieved.getRow(),
                "External position change should not affect Food");

        System.out.println("✓ SetPosition defensive copy test successful");
    }


    @Test
    @DisplayName("isAtPosition method should check correct position")
    void testIsAtPosition() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.CRUSTACEAN, 3);
        Position samePos = new Position(5, 5);
        Position differentPos = new Position(3, 4);

        // ASSERT
        assertTrue(food.isAtPosition(samePos),
                "Should return true for same coordinates");
        assertFalse(food.isAtPosition(differentPos),
                "Should return false for different coordinates");

        System.out.println("✓ isAtPosition test successful");
    }

    @Test
    @DisplayName("isType method should check type correctly")
    void testIsType() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.SQUID, 4);

        // ASSERT
        assertTrue(food.isType(FoodType.SQUID), "True for its own type");
        assertFalse(food.isType(FoodType.KRILL), "False for different type");
        assertFalse(food.isType(null), "False for null");

        System.out.println("✓ isType test successful");
    }

    @Test
    @DisplayName("isWeightInRange method should check range correctly")
    void testIsWeightInRange() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.MACKEREL, 3);

        // ASSERT
        assertTrue(food.isWeightInRange(1, 5), "In 1-5 range");
        assertTrue(food.isWeightInRange(3, 3), "Exactly at 3");
        assertTrue(food.isWeightInRange(2, 4), "In 2-4 range");
        assertFalse(food.isWeightInRange(4, 5), "Not in 4-5 range");
        assertFalse(food.isWeightInRange(1, 2), "Not in 1-2 range");

        System.out.println("✓ Weight range test successful");
    }

    @Test
    @DisplayName("isLightweight method should detect light foods")
    void testIsLightweight() {
        // ARRANGE & ACT & ASSERT
        assertTrue(new Food(testPosition, FoodType.KRILL, 1).isLightweight());
        assertTrue(new Food(testPosition, FoodType.KRILL, 2).isLightweight());
        assertFalse(new Food(testPosition, FoodType.KRILL, 3).isLightweight());
        assertFalse(new Food(testPosition, FoodType.KRILL, 4).isLightweight());
        assertFalse(new Food(testPosition, FoodType.KRILL, 5).isLightweight());

        System.out.println("✓ Lightweight test successful");
    }

    @Test
    @DisplayName("isHeavyweight method should detect heavy foods")
    void testIsHeavyweight() {
        // ARRANGE & ACT & ASSERT
        assertFalse(new Food(testPosition, FoodType.SQUID, 1).isHeavyweight());
        assertFalse(new Food(testPosition, FoodType.SQUID, 2).isHeavyweight());
        assertFalse(new Food(testPosition, FoodType.SQUID, 3).isHeavyweight());
        assertTrue(new Food(testPosition, FoodType.SQUID, 4).isHeavyweight());
        assertTrue(new Food(testPosition, FoodType.SQUID, 5).isHeavyweight());

        System.out.println("✓ Heavyweight test successful");
    }


    @Test
    @DisplayName("validateState method should return true for valid Food")
    void testValidateState_ValidFood() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.ANCHOVY, 3);

        // ACT & ASSERT
        assertTrue(food.validateState(), "Valid Food state should be valid");
    }

    @Test
    @DisplayName("getStateSummary and getDetailedDescription methods should work")
    void testDescriptionMethods() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.MACKEREL, 5);

        // ACT
        String summary = food.getStateSummary();
        String detailed = food.getDetailedDescription();
        String toString = food.toString();

        // ASSERT
        assertNotNull(summary, "Summary should not be null");
        assertNotNull(detailed, "Detailed should not be null");
        assertNotNull(toString, "toString should not be null");

        assertTrue(summary.contains("Mackerel"), "Summary should contain type");
        assertTrue(summary.contains("5"), "Summary should contain weight");
        assertTrue(detailed.contains("position"), "Detailed should contain position");

        System.out.println("📋 Summary: " + summary);
        System.out.println("📋 Detailed: " + detailed);
        System.out.println("📋 toString: " + toString);
    }


    @Test
    @DisplayName("equals method should return true for Foods with same properties")
    void testEquals_SameProperties() {
        // ARRANGE
        Food food1 = new Food(testPosition, FoodType.KRILL, 2);
        Food food2 = new Food(new Position(5, 5), FoodType.KRILL, 2);

        // ASSERT
        assertEquals(food1, food2, "Same properties should be equal");
        assertEquals(food1.hashCode(), food2.hashCode(),
                "Equal objects should have same hashCode");
    }

    @Test
    @DisplayName("equals method should return false for Foods with different properties")
    void testEquals_DifferentProperties() {
        // ARRANGE
        Food food1 = new Food(testPosition, FoodType.KRILL, 2);
        Food food2 = new Food(testPosition, FoodType.SQUID, 2);  // Different type
        Food food3 = new Food(testPosition, FoodType.KRILL, 3);  // Different weight
        Food food4 = new Food(new Position(7, 7), FoodType.KRILL, 2); // Different pos

        // ASSERT
        assertNotEquals(food1, food2, "Different type should not be equal");
        assertNotEquals(food1, food3, "Different weight should not be equal");
        assertNotEquals(food1, food4, "Different position should not be equal");
        assertNotEquals(food1, null, "Should not be equal to null");
    }

    @Test
    @DisplayName("copyAtPosition method should create copy at new position")
    void testCopyAtPosition() {
        // ARRANGE
        Food original = new Food(testPosition, FoodType.ANCHOVY, 4);
        Position newPos = new Position(8, 9);

        // ACT
        Food copy = original.copyAtPosition(newPos);

        // ASSERT
        assertNotSame(original, copy, "Should be different objects");
        assertEquals(original.getType(), copy.getType(), "Type should be same");
        assertEquals(original.getWeight(), copy.getWeight(), "Weight should be same");
        assertEquals(newPos, copy.getPosition(), "New position should be set");
        assertNotEquals(original.getPosition(), copy.getPosition(),
                "Position should be different");
    }

    @Test
    @DisplayName("compareByWeight method should compare by weight")
    void testCompareByWeight() {
        // ARRANGE
        Food light = new Food(testPosition, FoodType.KRILL, 1);
        Food medium = new Food(testPosition, FoodType.SQUID, 3);
        Food heavy = new Food(testPosition, FoodType.MACKEREL, 5);

        // ASSERT
        assertTrue(light.compareByWeight(medium) < 0,
                "Light < Medium");
        assertTrue(medium.compareByWeight(heavy) < 0,
                "Medium < Heavy");
        assertTrue(heavy.compareByWeight(light) > 0,
                "Heavy > Light");
        assertEquals(0, medium.compareByWeight(
                        new Food(new Position(1, 1), FoodType.KRILL, 3)),
                "Same weight = 0");
    }


    @Test
    @DisplayName("Integration: Food lifecycle")
    void testIntegration_FoodLifecycle() {
        System.out.println("\n=== Food Lifecycle ===");

        Food food = Food.createRandom(new Position(3, 4));
        System.out.println("1️⃣ Created: " + food.getDetailedDescription());
        assertTrue(food.validateState(), "State should be valid");

        Position newPos = new Position(7, 8);
        food.setPosition(newPos);
        System.out.println("2️⃣ Moved: " + food.getPosition());
        assertTrue(food.isAtPosition(newPos), "Should be at new position");

        boolean isLight = food.isLightweight();
        boolean isHeavy = food.isHeavyweight();
        System.out.println("3️⃣ Lightweight: " + isLight + ", Heavyweight: " + isHeavy);
        assertNotEquals(isLight, isHeavy, "Cannot be both light and heavy");

        Food copy = food.copyAtPosition(new Position(1, 1));
        System.out.println("4️⃣ Copy created: " + copy);
        assertNotSame(food, copy, "Different objects");
        assertEquals(food.getType(), copy.getType(), "Same properties");

        System.out.println("✓ Food lifecycle test successful");
    }


    @Test
    @DisplayName("Edge case: Minimum and maximum weight values")
    void testEdgeCase_MinMaxWeight() {
        // ARRANGE & ACT
        Food minFood = new Food(testPosition, FoodType.KRILL, 1);
        Food maxFood = new Food(testPosition, FoodType.MACKEREL, 5);

        // ASSERT
        assertEquals(1, minFood.getWeight(), "Minimum weight is 1");
        assertEquals(5, maxFood.getWeight(), "Maximum weight is 5");
        assertTrue(minFood.isLightweight(), "Weight 1 is lightweight");
        assertTrue(maxFood.isHeavyweight(), "Weight 5 is heavyweight");
    }

    @Test
    @DisplayName("Edge case: All FoodType combinations should be creatable")
    void testEdgeCase_AllFoodTypeCombinations() {
        // ACT & ASSERT
        for (FoodType type : FoodType.values()) {
            for (int weight = 1; weight <= 5; weight++) {
                Food food = new Food(testPosition, type, weight);
                assertNotNull(food,
                        "Food(" + type + ", " + weight + ") should be creatable");
                assertEquals(type, food.getType());
                assertEquals(weight, food.getWeight());
            }
        }

        System.out.println("✓ " + (FoodType.values().length * 5) +
                " different combinations tested");
    }
}