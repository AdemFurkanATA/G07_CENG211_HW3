package com.penguins;

import com.enums.Direction;
import com.enums.PenguinType;
import com.food.Food;
import com.enums.FoodType;
import com.utils.Position;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Penguin Class and Subclasses Tests
 *
 * Location: src/test/java/com/penguins/PenguinTest.java
 *
 * Testing abstract classes, inheritance, and polymorphism
 */
@DisplayName("Penguin Classes Tests 🐧")
class PenguinTest {

    private Position startPosition;
    private KingPenguin kingPenguin;
    private EmperorPenguin emperorPenguin;
    private RoyalPenguin royalPenguin;
    private RockhopperPenguin rockhopperPenguin;

    @BeforeEach
    void setUp() {
        startPosition = new Position(0, 0);
        kingPenguin = new KingPenguin("P1", startPosition);
        emperorPenguin = new EmperorPenguin("P2", new Position(0, 5));
        royalPenguin = new RoyalPenguin("P3", new Position(5, 0));
        rockhopperPenguin = new RockhopperPenguin("P4", new Position(5, 5));
    }

    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================

    @Test
    @DisplayName("All penguin subclasses should be created successfully")
    void testConstructors_AllSubclasses() {
        // ASSERT
        assertNotNull(kingPenguin);
        assertNotNull(emperorPenguin);
        assertNotNull(royalPenguin);
        assertNotNull(rockhopperPenguin);

        // Verify types
        assertEquals(PenguinType.KING, kingPenguin.getType());
        assertEquals(PenguinType.EMPEROR, emperorPenguin.getType());
        assertEquals(PenguinType.ROYAL, royalPenguin.getType());
        assertEquals(PenguinType.ROCKHOPPER, rockhopperPenguin.getType());

        System.out.println("✓ All penguin types created successfully");
    }

    @Test
    @DisplayName("Creating penguin with null parameters should throw exception")
    void testConstructor_NullParameters() {
        // ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new KingPenguin(null, startPosition));

        assertThrows(IllegalArgumentException.class,
                () -> new KingPenguin("P1", null));

        assertThrows(IllegalArgumentException.class,
                () -> new KingPenguin("", startPosition));
    }

    @Test
    @DisplayName("Constructor should set initial state correctly")
    void testConstructor_InitialState() {
        // ARRANGE
        Penguin penguin = kingPenguin;

        // ASSERT
        assertEquals("P1", penguin.getName());
        assertEquals(startPosition, penguin.getPosition());
        assertFalse(penguin.hasUsedSpecialAction());
        assertFalse(penguin.isStunned());
        assertFalse(penguin.isRemoved());
        assertFalse(penguin.isSliding());
        assertFalse(penguin.isPlayerPenguin());
        assertEquals(0, penguin.getFoodCount());
        assertNull(penguin.getSlidingDirection());

        System.out.println("✓ Initial state is correct");
    }

    // ========================================
    // POSITION TESTS (Defensive Copy)
    // ========================================

    @Test
    @DisplayName("getPosition should return defensive copy")
    void testGetPosition_DefensiveCopy() {
        // ACT
        Position pos1 = kingPenguin.getPosition();
        Position pos2 = kingPenguin.getPosition();

        // ASSERT
        assertNotSame(pos1, pos2, "Each call should return new object");
        assertEquals(pos1, pos2, "But values should be the same");

        // Changing returned position should not affect penguin
        pos1.setRow(999);
        assertNotEquals(999, kingPenguin.getPosition().getRow());
    }

    @Test
    @DisplayName("setPosition should use defensive copy")
    void testSetPosition_DefensiveCopy() {
        // ARRANGE
        Position newPos = new Position(7, 8);

        // ACT
        kingPenguin.setPosition(newPos);

        // ASSERT
        assertEquals(newPos, kingPenguin.getPosition());

        // Changing original should not affect penguin
        newPos.setRow(999);
        assertNotEquals(999, kingPenguin.getPosition().getRow());
    }

    // ========================================
    // FOOD COLLECTION TESTS
    // ========================================

    @Test
    @DisplayName("Penguin should be able to collect food")
    void testCollectFood() {
        // ARRANGE
        Food food1 = new Food(new Position(1, 1), FoodType.KRILL, 2);
        Food food2 = new Food(new Position(2, 2), FoodType.SQUID, 4);

        // ACT
        kingPenguin.collectFood(food1);
        kingPenguin.collectFood(food2);

        // ASSERT
        assertEquals(2, kingPenguin.getFoodCount());
        assertEquals(6, kingPenguin.getTotalFoodWeight());
        assertTrue(kingPenguin.hasFood());

        System.out.println("✓ Food collection works");
    }

    @Test
    @DisplayName("getCollectedFood should return unmodifiable list")
    void testGetCollectedFood_Unmodifiable() {
        // ARRANGE
        Food food = new Food(new Position(1, 1), FoodType.KRILL, 2);
        kingPenguin.collectFood(food);

        // ACT
        List<Food> foods = kingPenguin.getCollectedFood();

        // ASSERT
        assertThrows(UnsupportedOperationException.class, () -> {
            foods.add(new Food(new Position(2, 2), FoodType.SQUID, 3));
        }, "List should be unmodifiable");

        System.out.println("✓ Food list is protected");
    }

    @Test
    @DisplayName("removeLightestFood should remove lightest item")
    void testRemoveLightestFood() {
        // ARRANGE
        Food light = new Food(new Position(1, 1), FoodType.KRILL, 1);
        Food medium = new Food(new Position(2, 2), FoodType.ANCHOVY, 3);
        Food heavy = new Food(new Position(3, 3), FoodType.MACKEREL, 5);

        kingPenguin.collectFood(medium);
        kingPenguin.collectFood(heavy);
        kingPenguin.collectFood(light);

        // ACT
        Food removed = kingPenguin.removeLightestFood();

        // ASSERT
        assertEquals(light, removed);
        assertEquals(2, kingPenguin.getFoodCount());
        assertEquals(8, kingPenguin.getTotalFoodWeight()); // 3 + 5

        System.out.println("✓ Lightest food removed correctly");
    }

    @Test
    @DisplayName("removeLightestFood with no food should return null")
    void testRemoveLightestFood_NoFood() {
        // ACT
        Food removed = kingPenguin.removeLightestFood();

        // ASSERT
        assertNull(removed);
    }

    // ========================================
    // SLIDING MECHANISM TESTS
    // ========================================

    @Test
    @DisplayName("slide() should set sliding state")
    void testSlide() {
        // ACT
        kingPenguin.slide(Direction.RIGHT);

        // ASSERT
        assertTrue(kingPenguin.isSliding());
        assertEquals(Direction.RIGHT, kingPenguin.getSlidingDirection());
    }

    @Test
    @DisplayName("setSliding should update state")
    void testSetSliding() {
        // ACT
        kingPenguin.setSliding(true);

        // ASSERT
        assertTrue(kingPenguin.isSliding());

        // ACT
        kingPenguin.setSliding(false);

        // ASSERT
        assertFalse(kingPenguin.isSliding());
    }

    @Test
    @DisplayName("setSlidingDirection should update direction")
    void testSetSlidingDirection() {
        // ACT
        kingPenguin.setSlidingDirection(Direction.UP);

        // ASSERT
        assertEquals(Direction.UP, kingPenguin.getSlidingDirection());
    }

    // ========================================
    // STUN AND REMOVAL TESTS
    // ========================================

    @Test
    @DisplayName("setStunned should update stunned state")
    void testStunned() {
        // Initially not stunned
        assertFalse(kingPenguin.isStunned());

        // ACT
        kingPenguin.setStunned(true);

        // ASSERT
        assertTrue(kingPenguin.isStunned());

        // ACT
        kingPenguin.setStunned(false);

        // ASSERT
        assertFalse(kingPenguin.isStunned());
    }

    @Test
    @DisplayName("setRemoved should update removed state")
    void testRemoved() {
        // Initially not removed
        assertFalse(kingPenguin.isRemoved());

        // ACT
        kingPenguin.setRemoved(true);

        // ASSERT
        assertTrue(kingPenguin.isRemoved());
    }

    // ========================================
    // PLAYER PENGUIN TESTS
    // ========================================

    @Test
    @DisplayName("setPlayerPenguin should mark as player penguin")
    void testPlayerPenguin() {
        // Initially not player penguin
        assertFalse(kingPenguin.isPlayerPenguin());

        // ACT
        kingPenguin.setPlayerPenguin(true);

        // ASSERT
        assertTrue(kingPenguin.isPlayerPenguin());
    }

    // ========================================
    // SPECIAL ACTION TESTS - KING PENGUIN
    // ========================================

    @Test
    @DisplayName("KingPenguin special action should work once")
    void testKingPenguin_SpecialAction() {
        // Initially not used
        assertFalse(kingPenguin.hasUsedSpecialAction());

        // ACT - Use special action
        boolean result = kingPenguin.useSpecialAction();

        // ASSERT
        assertTrue(result, "Should return true on first use");
        assertTrue(kingPenguin.hasUsedSpecialAction());

        // Try to use again
        result = kingPenguin.useSpecialAction();
        assertFalse(result, "Should return false on second use");
    }

    @Test
    @DisplayName("KingPenguin should stop at 5th square")
    void testKingPenguin_StopSquare() {
        // ACT
        int stopSquare = kingPenguin.getStopSquare();

        // ASSERT
        assertEquals(5, stopSquare, "King should stop at 5th square");
    }

    // ========================================
    // SPECIAL ACTION TESTS - EMPEROR PENGUIN
    // ========================================

    @Test
    @DisplayName("EmperorPenguin special action should work once")
    void testEmperorPenguin_SpecialAction() {
        // Initially not used
        assertFalse(emperorPenguin.hasUsedSpecialAction());

        // ACT
        boolean result = emperorPenguin.useSpecialAction();

        // ASSERT
        assertTrue(result);
        assertTrue(emperorPenguin.hasUsedSpecialAction());
    }

    @Test
    @DisplayName("EmperorPenguin should stop at 3rd square")
    void testEmperorPenguin_StopSquare() {
        // ACT
        int stopSquare = emperorPenguin.getStopSquare();

        // ASSERT
        assertEquals(3, stopSquare, "Emperor should stop at 3rd square");
    }

    // ========================================
    // SPECIAL ACTION TESTS - ROYAL PENGUIN
    // ========================================

    @Test
    @DisplayName("RoyalPenguin special action should work once")
    void testRoyalPenguin_SpecialAction() {
        // Initially not used
        assertFalse(royalPenguin.hasUsedSpecialAction());

        // ACT
        boolean result = royalPenguin.useSpecialAction();

        // ASSERT
        assertTrue(result);
        assertTrue(royalPenguin.hasUsedSpecialAction());
    }

    @Test
    @DisplayName("RoyalPenguin should be able to set special move direction")
    void testRoyalPenguin_SpecialMoveDirection() {
        // ACT
        royalPenguin.setSpecialMoveDirection(Direction.UP);

        // ASSERT
        assertEquals(Direction.UP, royalPenguin.getSpecialMoveDirection());

        // ACT - Reset
        royalPenguin.resetSpecialMove();

        // ASSERT
        assertNull(royalPenguin.getSpecialMoveDirection());
    }

    @Test
    @DisplayName("RoyalPenguin hasSpecialMoveDirection should work correctly")
    void testRoyalPenguin_HasSpecialMoveDirection() {
        // Initially no direction
        assertFalse(royalPenguin.hasSpecialMoveDirection());

        // ACT
        royalPenguin.setSpecialMoveDirection(Direction.LEFT);

        // ASSERT
        assertTrue(royalPenguin.hasSpecialMoveDirection());
    }

    // ========================================
    // SPECIAL ACTION TESTS - ROCKHOPPER PENGUIN
    // ========================================

    @Test
    @DisplayName("RockhopperPenguin special action should work once")
    void testRockhopperPenguin_SpecialAction() {
        // Initially not used
        assertFalse(rockhopperPenguin.hasUsedSpecialAction());

        // ACT
        boolean result = rockhopperPenguin.useSpecialAction();

        // ASSERT
        assertTrue(result);
        assertTrue(rockhopperPenguin.hasUsedSpecialAction());
        assertTrue(rockhopperPenguin.isPreparedToJump());
    }

    @Test
    @DisplayName("RockhopperPenguin jump preparation should work")
    void testRockhopperPenguin_JumpPreparation() {
        // Initially not prepared
        assertFalse(rockhopperPenguin.isPreparedToJump());

        // ACT - Use special action
        rockhopperPenguin.useSpecialAction();

        // ASSERT
        assertTrue(rockhopperPenguin.isPreparedToJump());

        // Execute jump
        rockhopperPenguin.executeJump();
        assertFalse(rockhopperPenguin.isPreparedToJump());
    }

    @Test
    @DisplayName("RockhopperPenguin canJump should check state correctly")
    void testRockhopperPenguin_CanJump() {
        // Initially cannot jump
        assertFalse(rockhopperPenguin.canJump());

        // ACT - Prepare
        rockhopperPenguin.useSpecialAction();

        // ASSERT
        assertTrue(rockhopperPenguin.canJump());
    }

    @Test
    @DisplayName("RockhopperPenguin cancelJump should clear preparation")
    void testRockhopperPenguin_CancelJump() {
        // ARRANGE
        rockhopperPenguin.useSpecialAction();
        assertTrue(rockhopperPenguin.isPreparedToJump());

        // ACT
        rockhopperPenguin.cancelJump();

        // ASSERT
        assertFalse(rockhopperPenguin.isPreparedToJump());
    }

    // ========================================
    // POLYMORPHISM TESTS
    // ========================================

    @Test
    @DisplayName("All penguins should implement Penguin interface correctly")
    void testPolymorphism() {
        // ARRANGE - Store in array of base type
        Penguin[] penguins = {
                kingPenguin,
                emperorPenguin,
                royalPenguin,
                rockhopperPenguin
        };

        // ACT & ASSERT - All should respond to common interface
        for (Penguin penguin : penguins) {
            assertNotNull(penguin.getName());
            assertNotNull(penguin.getType());
            assertNotNull(penguin.getPosition());
            assertFalse(penguin.isRemoved());
            assertEquals(0, penguin.getFoodCount());
        }

        System.out.println("✓ Polymorphism works correctly");
    }

    // ========================================
    // STATE VALIDATION TESTS
    // ========================================

    @Test
    @DisplayName("validateState should return true for valid penguin")
    void testValidateState() {
        // ASSERT
        assertTrue(kingPenguin.validateState());
        assertTrue(emperorPenguin.validateState());
        assertTrue(royalPenguin.validateState());
        assertTrue(rockhopperPenguin.validateState());
    }

    @Test
    @DisplayName("getStateSummary should provide readable summary")
    void testGetStateSummary() {
        // ACT
        String summary = kingPenguin.getStateSummary();

        // ASSERT
        assertNotNull(summary);
        assertTrue(summary.contains("P1"));
        assertTrue(summary.contains("King Penguin"));

        System.out.println("State Summary:\n" + summary);
    }

    // ========================================
    // INTEGRATION TESTS
    // ========================================

    @Test
    @DisplayName("Integration: Penguin lifecycle simulation")
    void testIntegration_PenguinLifecycle() {
        System.out.println("\n=== Penguin Lifecycle ===");

        // 1. Create penguin
        Penguin penguin = new KingPenguin("Test", new Position(0, 0));
        System.out.println("1️⃣ Created: " + penguin.getDisplayName());
        assertTrue(penguin.validateState());

        // 2. Collect food
        Food food1 = new Food(new Position(1, 1), FoodType.KRILL, 2);
        Food food2 = new Food(new Position(2, 2), FoodType.SQUID, 4);
        penguin.collectFood(food1);
        penguin.collectFood(food2);
        System.out.println("2️⃣ Collected food: " + penguin.getFoodCount() +
                " items, weight: " + penguin.getTotalFoodWeight());

        // 3. Start sliding
        penguin.slide(Direction.RIGHT);
        System.out.println("3️⃣ Started sliding: " + penguin.getSlidingDirection());
        assertTrue(penguin.isSliding());

        // 4. Use special action
        boolean used = penguin.useSpecialAction();
        System.out.println("4️⃣ Used special action: " + used);
        assertTrue(penguin.hasUsedSpecialAction());

        // 5. Get stunned
        penguin.setStunned(true);
        System.out.println("5️⃣ Got stunned");
        assertTrue(penguin.isStunned());

        // 6. Recover
        penguin.setStunned(false);
        System.out.println("6️⃣ Recovered from stun");
        assertFalse(penguin.isStunned());

        System.out.println("✓ Penguin lifecycle complete");
    }

    @Test
    @DisplayName("Integration: Different penguin types comparison")
    void testIntegration_PenguinComparison() {
        System.out.println("\n=== Penguin Types Comparison ===");

        Penguin[] penguins = {kingPenguin, emperorPenguin, royalPenguin, rockhopperPenguin};

        for (Penguin p : penguins) {
            System.out.printf("%-20s - Type: %s%n",
                    p.getName(), p.getType().getDisplayName());
        }

        System.out.println("✓ All penguin types listed");
    }
}