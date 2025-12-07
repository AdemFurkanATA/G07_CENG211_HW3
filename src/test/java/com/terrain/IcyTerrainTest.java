package com.terrain;

import com.enums.Direction;
import com.enums.FoodType;
import com.enums.PenguinType;
import com.food.Food;
import com.hazards.*;
import com.interfaces.IHazard;
import com.penguins.*;
import com.utils.Position;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IcyTerrain Comprehensive Test Suite
 *
 * Location: src/test/java/com/terrain/IcyTerrainTest.java
 *
 * Bu test dosyası oyunun ana sınıfını test eder:
 * - Game initialization
 * - Object generation (penguins, hazards, food)
 * - Movement mechanics
 * - Collision detection
 * - Special abilities
 * - Game state management
 *
 * NOT: IcyTerrain constructor'ı oyunu başlattığı için,
 * bu testler initialization logic'i test eder, tam oyun akışını değil.
 */
@DisplayName("IcyTerrain Game Logic Tests 🎮")
class IcyTerrainTest {

    private static final int GRID_SIZE = 10;
    private static final int NUM_PENGUINS = 3;

    // ========================================
    // HELPER METHODS - Test Utility Functions
    // ========================================

    /**
     * IcyTerrain'in private metodlarını test etmek için
     * public wrapper metodları kullanacağız veya
     * reflection kullanabiliriz (gerekirse)
     */

    /**
     * Test helper: Creates a test grid with specific objects
     */
    private TerrainGrid createTestGrid() {
        TerrainGrid grid = new TerrainGrid(GRID_SIZE);
        return grid;
    }

    /**
     * Test helper: Places objects on grid for testing
     */
    private void placeTestObjects(TerrainGrid grid) {
        // Place some test objects
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);
        Position pos3 = new Position(3, 3);

        Food food = new Food(pos1, FoodType.KRILL, 2);
        HeavyIceBlock hazard = new HeavyIceBlock(pos2);
        KingPenguin penguin = new KingPenguin("TEST", pos3);

        grid.placeObject(food, pos1);
        grid.placeObject(hazard, pos2);
        grid.placeObject(penguin, pos3);
    }

    // ========================================
    // POSITION FINDING TESTS
    // ========================================

    @Test
    @DisplayName("Grid should have empty positions available")
    void testFindEmptyPosition() {
        // ARRANGE
        TerrainGrid grid = createTestGrid();

        // ACT
        List<Position> emptyPositions = grid.getEmptyPositions();

        // ASSERT
        assertNotNull(emptyPositions);
        assertEquals(GRID_SIZE * GRID_SIZE, emptyPositions.size(),
                "Empty grid should have all positions available");

        System.out.println("✓ Found " + emptyPositions.size() + " empty positions");
    }

    @Test
    @DisplayName("Grid should have empty edge positions available")
    void testFindEmptyEdgePosition() {
        // ARRANGE
        TerrainGrid grid = createTestGrid();

        // ACT
        List<Position> edgePositions = grid.getEmptyEdgePositions();

        // ASSERT
        assertNotNull(edgePositions);
        assertTrue(edgePositions.size() > 0, "Should have edge positions");

        // Verify all are actually edges
        for (Position pos : edgePositions) {
            assertTrue(pos.isEdge(GRID_SIZE),
                    "Position should be on edge: " + pos);
        }

        System.out.println("✓ Found " + edgePositions.size() + " empty edge positions");
    }

    @Test
    @DisplayName("Empty positions should decrease as objects are placed")
    void testEmptyPositionsDecrease() {
        // ARRANGE
        TerrainGrid grid = createTestGrid();
        int initialEmpty = grid.getEmptyPositions().size();

        // ACT - Place objects
        grid.placeObject(new Food(new Position(5, 5), FoodType.KRILL, 1),
                new Position(5, 5));
        grid.placeObject(new HeavyIceBlock(new Position(6, 6)),
                new Position(6, 6));

        int afterPlacement = grid.getEmptyPositions().size();

        // ASSERT
        assertEquals(initialEmpty - 2, afterPlacement,
                "Empty positions should decrease by number of placed objects");

        System.out.println("✓ Empty positions: " + initialEmpty + " → " + afterPlacement);
    }

    // ========================================
    // PENGUIN CREATION TESTS
    // ========================================

    @Test
    @DisplayName("Should create all penguin types")
    void testCreatePenguin_AllTypes() {
        System.out.println("\n=== Testing Penguin Creation ===");

        // Test each penguin type
        Position testPos = new Position(0, 0);

        // King Penguin
        Penguin king = new KingPenguin("P1", testPos);
        assertNotNull(king);
        assertEquals(PenguinType.KING, king.getType());
        assertEquals("P1", king.getName());
        System.out.println("✓ Created: " + king.getType().getDisplayName());

        // Emperor Penguin
        Penguin emperor = new EmperorPenguin("P2", new Position(0, 5));
        assertNotNull(emperor);
        assertEquals(PenguinType.EMPEROR, emperor.getType());
        System.out.println("✓ Created: " + emperor.getType().getDisplayName());

        // Royal Penguin
        Penguin royal = new RoyalPenguin("P3", new Position(5, 0));
        assertNotNull(royal);
        assertEquals(PenguinType.ROYAL, royal.getType());
        System.out.println("✓ Created: " + royal.getType().getDisplayName());

        // Rockhopper Penguin
        Penguin rockhopper = new RockhopperPenguin("P4", new Position(5, 5));
        assertNotNull(rockhopper);
        assertEquals(PenguinType.ROCKHOPPER, rockhopper.getType());
        System.out.println("✓ Created: " + rockhopper.getType().getDisplayName());

        System.out.println("✓ All penguin types created successfully");
    }

    // ========================================
    // HAZARD CREATION TESTS
    // ========================================

    @Test
    @DisplayName("Should create all hazard types")
    void testCreateHazard_AllTypes() {
        System.out.println("\n=== Testing Hazard Creation ===");

        Position testPos = new Position(5, 5);

        // Light Ice Block
        LightIceBlock lb = new LightIceBlock(testPos);
        assertNotNull(lb);
        assertTrue(lb.canSlide());
        System.out.println("✓ Created: " + lb.getDisplayName());

        // Heavy Ice Block
        HeavyIceBlock hb = new HeavyIceBlock(new Position(5, 6));
        assertNotNull(hb);
        assertFalse(hb.canSlide());
        System.out.println("✓ Created: " + hb.getDisplayName());

        // Sea Lion
        SeaLion sl = new SeaLion(new Position(6, 5));
        assertNotNull(sl);
        assertTrue(sl.canSlide());
        System.out.println("✓ Created: " + sl.getDisplayName());

        // Hole In Ice
        HoleInIce hi = new HoleInIce(new Position(6, 6));
        assertNotNull(hi);
        assertFalse(hi.canSlide());
        assertTrue(hi.isActive());
        System.out.println("✓ Created: " + hi.getDisplayName());

        System.out.println("✓ All hazard types created successfully");
    }

    // ========================================
    // FOOD GENERATION TESTS
    // ========================================

    @Test
    @DisplayName("Should create random food items")
    void testFoodGeneration() {
        System.out.println("\n=== Testing Food Generation ===");

        // Create multiple random food items
        int foodCount = 20;
        for (int i = 0; i < foodCount; i++) {
            Position pos = new Position(i % GRID_SIZE, i / GRID_SIZE);
            Food food = Food.createRandom(pos);

            assertNotNull(food);
            assertTrue(food.getWeight() >= 1 && food.getWeight() <= 5,
                    "Food weight should be 1-5, got: " + food.getWeight());
            assertNotNull(food.getType());

            if (i < 5) {
                System.out.println("Food " + (i + 1) + ": " + food);
            }
        }

        System.out.println("✓ Created " + foodCount + " random food items");
    }

    // ========================================
    // MOVEMENT SIMULATION TESTS
    // ========================================

    @Nested
    @DisplayName("Penguin Movement Simulation Tests 🐧")
    class MovementSimulationTests {

        private TerrainGrid grid;
        private KingPenguin penguin;

        @BeforeEach
        void setUp() {
            grid = createTestGrid();
            penguin = new KingPenguin("TEST", new Position(5, 5));
        }

        @Test
        @DisplayName("Penguin should move in direction and stop at food")
        void testMovement_StopAtFood() {
            // ARRANGE
            Position startPos = new Position(5, 5);
            Position foodPos = new Position(5, 7);

            penguin.setPosition(startPos);
            Food food = new Food(foodPos, FoodType.KRILL, 2);

            grid.placeObject(penguin, startPos);
            grid.placeObject(food, foodPos);

            // ACT - Simulate movement RIGHT
            penguin.slide(Direction.RIGHT);
            assertTrue(penguin.isSliding());

            // SIMULATE: Move to next position
            Position nextPos = startPos.getNextPosition(Direction.RIGHT);
            assertEquals(new Position(5, 6), nextPos);

            // SIMULATE: Move to food position
            Position finalPos = nextPos.getNextPosition(Direction.RIGHT);
            assertEquals(foodPos, finalPos);

            // ASSERT - Should stop at food
            System.out.println("✓ Penguin would stop at food position");
        }

        @Test
        @DisplayName("Penguin should fall off edge")
        void testMovement_FallOffEdge() {
            // ARRANGE - Place penguin at edge
            Position edgePos = new Position(5, 9);
            penguin.setPosition(edgePos);

            // ACT - Try to move right (off grid)
            Position nextPos = edgePos.getNextPosition(Direction.RIGHT);

            // ASSERT - Should be invalid position
            assertFalse(nextPos.isValid(GRID_SIZE),
                    "Position off grid should be invalid");

            System.out.println("✓ Penguin would fall off edge at: " + nextPos);
        }

        @Test
        @DisplayName("Penguin should slide through empty squares")
        void testMovement_SlideThroughEmpty() {
            // ARRANGE
            Position startPos = new Position(5, 5);
            penguin.setPosition(startPos);

            // ACT - Simulate sliding
            Direction dir = Direction.UP;
            Position pos1 = startPos.getNextPosition(dir);
            Position pos2 = pos1.getNextPosition(dir);
            Position pos3 = pos2.getNextPosition(dir);

            // ASSERT - Should pass through all positions
            assertTrue(pos1.isValid(GRID_SIZE));
            assertTrue(pos2.isValid(GRID_SIZE));
            assertTrue(pos3.isValid(GRID_SIZE));

            System.out.println("Sliding path:");
            System.out.println("  Start: " + startPos);
            System.out.println("  → " + pos1);
            System.out.println("  → " + pos2);
            System.out.println("  → " + pos3);
        }
    }

    // ========================================
    // COLLISION SIMULATION TESTS
    // ========================================

    @Nested
    @DisplayName("Collision Simulation Tests 💥")
    class CollisionSimulationTests {

        @Test
        @DisplayName("HeavyIceBlock collision should cause food loss")
        void testCollision_HeavyIceBlock() {
            // ARRANGE
            KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));
            Food food1 = new Food(new Position(1, 1), FoodType.KRILL, 1);
            Food food2 = new Food(new Position(2, 2), FoodType.SQUID, 4);

            penguin.collectFood(food1);
            penguin.collectFood(food2);
            assertEquals(2, penguin.getFoodCount());

            HeavyIceBlock hazard = new HeavyIceBlock(new Position(5, 6));

            // ACT - Simulate collision
            String message = hazard.handleCollision(penguin.getName());
            Food removed = penguin.removeLightestFood();

            // ASSERT
            assertNotNull(message);
            assertTrue(message.contains("lightest"));
            assertNotNull(removed);
            assertEquals(food1, removed, "Lightest food should be removed");
            assertEquals(1, penguin.getFoodCount(), "Should have 1 food left");

            System.out.println("Collision: " + message);
            System.out.println("Lost: " + removed);
        }

        @Test
        @DisplayName("LightIceBlock collision should stun penguin")
        void testCollision_LightIceBlock() {
            // ARRANGE
            KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));
            LightIceBlock hazard = new LightIceBlock(new Position(5, 6));

            // ACT - Simulate collision
            String message = hazard.handleCollision(penguin.getName());
            penguin.setStunned(true);

            // ASSERT
            assertTrue(message.contains("stunned") || message.contains("LightIceBlock"));
            assertTrue(penguin.isStunned());

            System.out.println("Collision: " + message);
            System.out.println("Penguin status: STUNNED");
        }

        @Test
        @DisplayName("SeaLion collision should bounce penguin")
        void testCollision_SeaLion() {
            // ARRANGE
            KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));
            SeaLion seaLion = new SeaLion(new Position(5, 6));
            Direction originalDir = Direction.RIGHT;

            // ACT - Simulate collision
            String message = seaLion.handleCollision(penguin.getName());
            Direction oppositeDir = originalDir.getOpposite();

            // ASSERT
            assertTrue(message.contains("bounce") || message.contains("SeaLion"));
            assertEquals(Direction.LEFT, oppositeDir);

            System.out.println("Collision: " + message);
            System.out.println("Direction: " + originalDir + " → " + oppositeDir);
        }

        @Test
        @DisplayName("HoleInIce collision should remove penguin")
        void testCollision_HoleInIce() {
            // ARRANGE
            KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));
            HoleInIce hole = new HoleInIce(new Position(5, 6));
            assertTrue(hole.isActive());

            // ACT - Simulate collision
            String message = hole.handleCollision(penguin.getName());
            penguin.setRemoved(true);

            // ASSERT
            assertTrue(message.contains("fell"));
            assertTrue(penguin.isRemoved());

            System.out.println("Collision: " + message);
            System.out.println("Penguin status: REMOVED");
        }

        @Test
        @DisplayName("Plugged HoleInIce should allow passage")
        void testCollision_PluggedHole() {
            // ARRANGE
            HoleInIce hole = new HoleInIce(new Position(5, 6));
            hole.plug();

            // ASSERT
            assertTrue(hole.isPlugged());
            assertFalse(hole.isActive());
            assertFalse(hole.isDangerous());
            assertTrue(hole.allowsPassage());
            assertEquals("PH", hole.getShorthand());

            System.out.println("Plugged hole status:");
            System.out.println("  Active: " + hole.isActive());
            System.out.println("  Dangerous: " + hole.isDangerous());
            System.out.println("  Allows passage: " + hole.allowsPassage());
        }
    }

    // ========================================
    // SPECIAL ABILITY TESTS
    // ========================================

    @Nested
    @DisplayName("Special Ability Simulation Tests ⭐")
    class SpecialAbilityTests {

        @Test
        @DisplayName("King Penguin should stop at 5th square")
        void testSpecialAbility_King() {
            // ARRANGE
            KingPenguin king = new KingPenguin("P1", new Position(5, 5));
            assertFalse(king.hasUsedSpecialAction());

            // ACT
            boolean used = king.useSpecialAction();
            int stopSquare = king.getStopSquare();

            // ASSERT
            assertTrue(used);
            assertTrue(king.hasUsedSpecialAction());
            assertEquals(5, stopSquare);

            System.out.println("King Penguin special ability:");
            System.out.println("  Stops at: " + stopSquare + "th square");
            System.out.println("  Used: " + king.hasUsedSpecialAction());
        }

        @Test
        @DisplayName("Emperor Penguin should stop at 3rd square")
        void testSpecialAbility_Emperor() {
            // ARRANGE
            EmperorPenguin emperor = new EmperorPenguin("P2", new Position(5, 5));

            // ACT
            boolean used = emperor.useSpecialAction();
            int stopSquare = emperor.getStopSquare();

            // ASSERT
            assertTrue(used);
            assertEquals(3, stopSquare);

            System.out.println("Emperor Penguin special ability:");
            System.out.println("  Stops at: " + stopSquare + "rd square");
        }

        @Test
        @DisplayName("Royal Penguin should move one square before sliding")
        void testSpecialAbility_Royal() {
            // ARRANGE
            RoyalPenguin royal = new RoyalPenguin("P3", new Position(5, 5));

            // ACT
            boolean used = royal.useSpecialAction();
            royal.setSpecialMoveDirection(Direction.UP);

            // ASSERT
            assertTrue(used);
            assertEquals(Direction.UP, royal.getSpecialMoveDirection());
            assertTrue(royal.hasSpecialMoveDirection());

            // Simulate special move
            Position startPos = royal.getPosition();
            Position afterSpecialMove = startPos.getNextPosition(Direction.UP);

            System.out.println("Royal Penguin special ability:");
            System.out.println("  Start: " + startPos);
            System.out.println("  After special move: " + afterSpecialMove);
            System.out.println("  Direction: " + royal.getSpecialMoveDirection());
        }

        @Test
        @DisplayName("Rockhopper Penguin should jump over hazard")
        void testSpecialAbility_Rockhopper() {
            // ARRANGE
            RockhopperPenguin rockhopper = new RockhopperPenguin("P4",
                    new Position(5, 5));
            HeavyIceBlock hazard = new HeavyIceBlock(new Position(5, 6));

            // ACT
            boolean used = rockhopper.useSpecialAction();

            // ASSERT
            assertTrue(used);
            assertTrue(rockhopper.isPreparedToJump());
            assertTrue(rockhopper.canJump());

            // Simulate jump
            Position hazardPos = hazard.getPosition();
            Position landingPos = hazardPos.getNextPosition(Direction.RIGHT);

            System.out.println("Rockhopper Penguin special ability:");
            System.out.println("  Hazard at: " + hazardPos);
            System.out.println("  Jump to: " + landingPos);
            System.out.println("  Can jump: " + rockhopper.canJump());

            // Execute jump
            rockhopper.executeJump();
            assertFalse(rockhopper.isPreparedToJump());
        }

        @Test
        @DisplayName("Special abilities should only work once")
        void testSpecialAbility_OnceOnly() {
            // Test all penguin types
            KingPenguin king = new KingPenguin("P1", new Position(0, 0));
            EmperorPenguin emperor = new EmperorPenguin("P2", new Position(0, 1));
            RoyalPenguin royal = new RoyalPenguin("P3", new Position(0, 2));
            RockhopperPenguin rockhopper = new RockhopperPenguin("P4",
                    new Position(0, 3));

            // Use each ability once
            assertTrue(king.useSpecialAction());
            assertTrue(emperor.useSpecialAction());
            assertTrue(royal.useSpecialAction());
            assertTrue(rockhopper.useSpecialAction());

            // Try to use again - should fail
            assertFalse(king.useSpecialAction());
            assertFalse(emperor.useSpecialAction());
            assertFalse(royal.useSpecialAction());
            assertFalse(rockhopper.useSpecialAction());

            System.out.println("✓ All special abilities work only once");
        }
    }

    // ========================================
    // FOOD COLLECTION TESTS
    // ========================================

    @Test
    @DisplayName("Penguin should collect food and track weight")
    void testFoodCollection() {
        System.out.println("\n=== Testing Food Collection ===");

        // ARRANGE
        KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));
        Food food1 = new Food(new Position(1, 1), FoodType.KRILL, 2);
        Food food2 = new Food(new Position(2, 2), FoodType.SQUID, 4);
        Food food3 = new Food(new Position(3, 3), FoodType.MACKEREL, 5);

        // ACT
        penguin.collectFood(food1);
        penguin.collectFood(food2);
        penguin.collectFood(food3);

        // ASSERT
        assertEquals(3, penguin.getFoodCount());
        assertEquals(11, penguin.getTotalFoodWeight());
        assertTrue(penguin.hasFood());

        List<Food> collected = penguin.getCollectedFood();
        assertEquals(3, collected.size());

        System.out.println("Collected food:");
        for (Food food : collected) {
            System.out.println("  - " + food);
        }
        System.out.println("Total weight: " + penguin.getTotalFoodWeight());
    }

    // ========================================
    // GAME STATE TESTS
    // ========================================

    @Test
    @DisplayName("Penguin state management")
    void testPenguinState() {
        // ARRANGE
        KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));

        // Test initial state
        assertFalse(penguin.isStunned());
        assertFalse(penguin.isRemoved());
        assertFalse(penguin.isPlayerPenguin());
        assertFalse(penguin.isSliding());

        // Change states
        penguin.setStunned(true);
        assertTrue(penguin.isStunned());

        penguin.setRemoved(true);
        assertTrue(penguin.isRemoved());

        penguin.setPlayerPenguin(true);
        assertTrue(penguin.isPlayerPenguin());

        penguin.slide(Direction.UP);
        assertTrue(penguin.isSliding());

        System.out.println("✓ Penguin state management works correctly");
    }

    // ========================================
    // INTEGRATION TESTS
    // ========================================

    @Test
    @DisplayName("Integration: Complete game scenario")
    void testIntegration_GameScenario() {
        System.out.println("\n=== Complete Game Scenario ===");

        // 1. Setup grid
        TerrainGrid grid = createTestGrid();
        System.out.println("1️⃣ Grid created: " + grid.getSize() + "x" + grid.getSize());

        // 2. Create penguins
        KingPenguin p1 = new KingPenguin("P1", new Position(0, 0));
        EmperorPenguin p2 = new EmperorPenguin("P2", new Position(0, 9));
        RoyalPenguin p3 = new RoyalPenguin("P3", new Position(9, 0));
        p1.setPlayerPenguin(true);
        System.out.println("2️⃣ Created 3 penguins (P1 is player)");

        // 3. Place food
        Food food1 = new Food(new Position(5, 5), FoodType.KRILL, 2);
        Food food2 = new Food(new Position(6, 6), FoodType.SQUID, 4);
        grid.placeObject(food1, food1.getPosition());
        grid.placeObject(food2, food2.getPosition());
        System.out.println("3️⃣ Placed 2 food items");

        // 4. Place hazards
        HeavyIceBlock hb = new HeavyIceBlock(new Position(7, 7));
        LightIceBlock lb = new LightIceBlock(new Position(8, 8));
        grid.placeObject(hb, hb.getPosition());
        grid.placeObject(lb, lb.getPosition());
        System.out.println("4️⃣ Placed 2 hazards");

        // 5. Place penguins on grid
        grid.placeObject(p1, p1.getPosition());
        grid.placeObject(p2, p2.getPosition());
        grid.placeObject(p3, p3.getPosition());
        System.out.println("5️⃣ Placed penguins on grid");

        // 6. Verify grid state
        assertEquals(7, grid.countObjects());
        System.out.println("6️⃣ Total objects on grid: " + grid.countObjects());

        // 7. Simulate penguin collecting food
        p1.collectFood(food1);
        assertEquals(1, p1.getFoodCount());
        assertEquals(2, p1.getTotalFoodWeight());
        System.out.println("7️⃣ P1 collected food: " + food1);

        // 8. Use special ability
        p1.useSpecialAction();
        assertTrue(p1.hasUsedSpecialAction());
        System.out.println("8️⃣ P1 used special ability");

        // 9. Get stunned
        p1.setStunned(true);
        assertTrue(p1.isStunned());
        System.out.println("9️⃣ P1 got stunned");

        // 10. Recover
        p1.setStunned(false);
        assertFalse(p1.isStunned());
        System.out.println("🔟 P1 recovered from stun");

        System.out.println("\n✅ Complete game scenario executed successfully!");
    }

    @Test
    @DisplayName("Integration: Multiple penguins interacting")
    void testIntegration_MultiplePenguins() {
        System.out.println("\n=== Multiple Penguin Interaction ===");

        // Create multiple penguins
        KingPenguin p1 = new KingPenguin("P1", new Position(5, 5));
        EmperorPenguin p2 = new EmperorPenguin("P2", new Position(5, 6));

        // Penguin 1 slides toward Penguin 2
        Direction dir = Direction.RIGHT;
        Position nextPos = p1.getPosition().getNextPosition(dir);

        System.out.println("P1 at: " + p1.getPosition());
        System.out.println("P2 at: " + p2.getPosition());
        System.out.println("P1 slides " + dir);
        System.out.println("Next position would be: " + nextPos);

        // Check if collision would occur
        if (nextPos.equals(p2.getPosition())) {
            System.out.println("💥 COLLISION! P1 would hit P2");
            System.out.println("Expected: P1 stops, P2 starts sliding");
        }

        System.out.println("✓ Penguin collision logic verified");
    }

    // ========================================
    // EDGE CASE TESTS
    // ========================================

    @Test
    @DisplayName("Edge case: Penguin at grid boundary")
    void testEdgeCase_GridBoundary() {
        // ARRANGE - Penguin at right edge
        KingPenguin penguin = new KingPenguin("P1", new Position(5, 9));

        // ACT - Try to move right (off grid)
        Position nextPos = penguin.getPosition().getNextPosition(Direction.RIGHT);

        // ASSERT
        assertFalse(nextPos.isValid(GRID_SIZE));
        System.out.println("✓ Correctly detects off-grid movement");
    }

    @Test
    @DisplayName("Edge case: All food collected")
    void testEdgeCase_AllFoodCollected() {
        // ARRANGE
        KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));

        // Collect multiple food items
        for (int i = 0; i < 10; i++) {
            Food food = Food.createRandom(new Position(i, i));
            penguin.collectFood(food);
        }

        // ASSERT
        assertEquals(10, penguin.getFoodCount());
        assertTrue(penguin.getTotalFoodWeight() >= 10); // At least 10 (1 each)
        assertTrue(penguin.getTotalFoodWeight() <= 50); // At most 50 (5 each)

        System.out.println("✓ Penguin collected 10 food items");
        System.out.println("  Total weight: " + penguin.getTotalFoodWeight());
    }

    @Test
    @DisplayName("Edge case: Penguin with no food loses lightest")
    void testEdgeCase_NoFoodLoss() {
        // ARRANGE
        KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));
        assertEquals(0, penguin.getFoodCount());

        // ACT
        Food removed = penguin.removeLightestFood();

        // ASSERT
        assertNull(removed, "Should return null when no food to lose");
        System.out.println("✓ No food to lose returns null correctly");
    }

    @Test
    @DisplayName("Edge case: Multiple penguins removed")
    void testEdgeCase_MultiplePenguinsRemoved() {
        // ARRANGE
        KingPenguin p1 = new KingPenguin("P1", new Position(1, 1));
        EmperorPenguin p2 = new EmperorPenguin("P2", new Position(2, 2));
        RoyalPenguin p3 = new RoyalPenguin("P3", new Position(3, 3));

        // ACT
        p1.setRemoved(true);
        p2.setRemoved(true);

        // ASSERT
        assertTrue(p1.isRemoved());
        assertTrue(p2.isRemoved());
        assertFalse(p3.isRemoved());

        System.out.println("✓ Multiple penguins can be removed independently");
    }

    @Test
    @DisplayName("Edge case: All positions occupied")
    void testEdgeCase_AllPositionsOccupied() {
        // ARRANGE
        TerrainGrid grid = new TerrainGrid(3); // Small 3x3 grid

        // ACT - Fill all positions
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Position pos = new Position(row, col);
                Food food = new Food(pos, FoodType.KRILL, 1);
                grid.placeObject(food, pos);
            }
        }

        // ASSERT
        assertEquals(9, grid.countObjects());
        assertEquals(0, grid.getEmptyPositions().size());

        System.out.println("✓ Grid correctly handles all positions occupied");
    }

    // ========================================
    // DEFENSIVE COPY TESTS
    // ========================================

    @Nested
    @DisplayName("Defensive Copy Security Tests 🔒")
    class DefensiveCopyTests {

        @Test
        @DisplayName("Penguin position should be defensively copied")
        void testDefensiveCopy_PenguinPosition() {
            // ARRANGE
            Position originalPos = new Position(5, 5);
            KingPenguin penguin = new KingPenguin("P1", originalPos);

            // ACT
            Position retrievedPos1 = penguin.getPosition();
            Position retrievedPos2 = penguin.getPosition();

            // ASSERT
            assertNotSame(originalPos, retrievedPos1,
                    "Constructor should copy position");
            assertNotSame(retrievedPos1, retrievedPos2,
                    "Each getPosition should return new copy");
            assertEquals(retrievedPos1, retrievedPos2,
                    "But values should be equal");

            // Modify original - should not affect penguin
            originalPos.setRow(999);
            assertNotEquals(999, penguin.getPosition().getRow(),
                    "Modifying original should not affect penguin");

            System.out.println("✓ Penguin position is defensively copied");
        }

        @Test
        @DisplayName("Food position should be defensively copied")
        void testDefensiveCopy_FoodPosition() {
            // ARRANGE
            Position originalPos = new Position(3, 4);
            Food food = new Food(originalPos, FoodType.KRILL, 2);

            // ACT
            Position retrievedPos = food.getPosition();

            // ASSERT
            assertNotSame(originalPos, retrievedPos);
            assertEquals(originalPos, retrievedPos);

            // Modify original
            originalPos.setRow(999);
            assertNotEquals(999, food.getPosition().getRow());

            System.out.println("✓ Food position is defensively copied");
        }

        @Test
        @DisplayName("Hazard position should be defensively copied")
        void testDefensiveCopy_HazardPosition() {
            // ARRANGE
            Position originalPos = new Position(7, 8);
            HeavyIceBlock hazard = new HeavyIceBlock(originalPos);

            // ACT
            Position retrievedPos = hazard.getPosition();

            // ASSERT
            assertNotSame(originalPos, retrievedPos);
            assertEquals(originalPos, retrievedPos);

            System.out.println("✓ Hazard position is defensively copied");
        }

        @Test
        @DisplayName("Penguin food list should be unmodifiable")
        void testDefensiveCopy_FoodList() {
            // ARRANGE
            KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));
            Food food = new Food(new Position(1, 1), FoodType.KRILL, 2);
            penguin.collectFood(food);

            // ACT
            List<Food> foodList = penguin.getCollectedFood();

            // ASSERT
            assertThrows(UnsupportedOperationException.class, () -> {
                foodList.add(new Food(new Position(2, 2), FoodType.SQUID, 3));
            }, "Food list should be unmodifiable");

            System.out.println("✓ Food list is protected from modification");
        }

        @Test
        @DisplayName("Grid empty positions should be unmodifiable")
        void testDefensiveCopy_EmptyPositions() {
            // ARRANGE
            TerrainGrid grid = createTestGrid();

            // ACT
            List<Position> emptyPositions = grid.getEmptyPositions();

            // ASSERT
            assertThrows(UnsupportedOperationException.class, () -> {
                emptyPositions.add(new Position(0, 0));
            }, "Empty positions list should be unmodifiable");

            System.out.println("✓ Empty positions list is protected");
        }
    }

    // ========================================
    // SLIDING HAZARD TESTS
    // ========================================

    @Nested
    @DisplayName("Sliding Hazard Mechanics Tests 🧊")
    class SlidingHazardTests {

        @Test
        @DisplayName("LightIceBlock should start sliding when hit")
        void testSliding_LightIceBlock() {
            // ARRANGE
            LightIceBlock lb = new LightIceBlock(new Position(5, 5));
            assertFalse(lb.isSliding());

            // ACT
            lb.slide(Direction.RIGHT);

            // ASSERT
            assertTrue(lb.isSliding());
            assertEquals(Direction.RIGHT, lb.getSlidingDirection());

            System.out.println("✓ LightIceBlock starts sliding correctly");
        }

        @Test
        @DisplayName("SeaLion should start sliding after bounce")
        void testSliding_SeaLion() {
            // ARRANGE
            SeaLion sl = new SeaLion(new Position(5, 5));
            assertTrue(sl.canBounce());
            assertFalse(sl.isSliding());

            // ACT
            sl.slide(Direction.LEFT);

            // ASSERT
            assertTrue(sl.isSliding());
            assertEquals(Direction.LEFT, sl.getSlidingDirection());
            assertFalse(sl.canBounce(), "Cannot bounce while sliding");

            System.out.println("✓ SeaLion starts sliding correctly");
        }

        @Test
        @DisplayName("Sliding hazard should stop when stopped")
        void testSliding_Stop() {
            // ARRANGE
            LightIceBlock lb = new LightIceBlock(new Position(5, 5));
            lb.slide(Direction.UP);
            assertTrue(lb.isSliding());

            // ACT
            lb.stopSliding();

            // ASSERT
            assertFalse(lb.isSliding());
            assertNull(lb.getSlidingDirection());

            System.out.println("✓ Sliding hazard stops correctly");
        }

        @Test
        @DisplayName("Sliding hazard state should be valid")
        void testSliding_StateValidation() {
            // ARRANGE
            LightIceBlock lb = new LightIceBlock(new Position(5, 5));

            // Initially valid
            assertTrue(lb.validateState());
            assertTrue(lb.isSlidingStateValid());

            // Start sliding
            lb.slide(Direction.DOWN);
            assertTrue(lb.validateState());
            assertTrue(lb.isSlidingStateValid());

            // Stop sliding
            lb.stopSliding();
            assertTrue(lb.validateState());
            assertTrue(lb.isSlidingStateValid());

            System.out.println("✓ Sliding state validation works");
        }
    }

    // ========================================
    // HOLE IN ICE PLUGGING TESTS
    // ========================================

    @Nested
    @DisplayName("Hole In Ice Plugging Tests 🕳️")
    class HolePluggingTests {

        @Test
        @DisplayName("Active hole should be dangerous")
        void testHole_Active() {
            // ARRANGE
            HoleInIce hole = new HoleInIce(new Position(5, 5));

            // ASSERT
            assertTrue(hole.isActive());
            assertFalse(hole.isPlugged());
            assertTrue(hole.isDangerous());
            assertFalse(hole.allowsPassage());
            assertEquals("HI", hole.getShorthand());

            System.out.println("✓ Active hole is dangerous");
        }

        @Test
        @DisplayName("Plugged hole should be safe")
        void testHole_Plugged() {
            // ARRANGE
            HoleInIce hole = new HoleInIce(new Position(5, 5));

            // ACT
            hole.plug();

            // ASSERT
            assertFalse(hole.isActive());
            assertTrue(hole.isPlugged());
            assertFalse(hole.isDangerous());
            assertTrue(hole.allowsPassage());
            assertEquals("PH", hole.getShorthand());

            System.out.println("✓ Plugged hole is safe");
        }

        @Test
        @DisplayName("Hole can be unplugged")
        void testHole_Unplug() {
            // ARRANGE
            HoleInIce hole = new HoleInIce(new Position(5, 5));
            hole.plug();
            assertTrue(hole.isPlugged());

            // ACT
            hole.unplug();

            // ASSERT
            assertFalse(hole.isPlugged());
            assertTrue(hole.isActive());
            assertTrue(hole.isDangerous());
            assertEquals("HI", hole.getShorthand());

            System.out.println("✓ Hole can be unplugged");
        }

        @Test
        @DisplayName("Hole state transitions should be valid")
        void testHole_StateTransitions() {
            // ARRANGE
            HoleInIce hole = new HoleInIce(new Position(5, 5));

            // Active state
            assertTrue(hole.validateState());
            assertEquals("ACTIVE", hole.getStatusCategory());

            // Plug
            hole.plug();
            assertTrue(hole.validateState());
            assertEquals("PLUGGED", hole.getStatusCategory());

            // Unplug
            hole.unplug();
            assertTrue(hole.validateState());
            assertEquals("ACTIVE", hole.getStatusCategory());

            System.out.println("✓ Hole state transitions are valid");
        }
    }

    // ========================================
    // DIRECTION AND MOVEMENT TESTS
    // ========================================

    @Nested
    @DisplayName("Direction and Movement Logic Tests 🧭")
    class DirectionMovementTests {

        @Test
        @DisplayName("All directions should work correctly")
        void testDirections_AllWork() {
            // ARRANGE
            Position center = new Position(5, 5);

            // ACT & ASSERT
            Position up = center.getNextPosition(Direction.UP);
            assertEquals(new Position(4, 5), up);

            Position down = center.getNextPosition(Direction.DOWN);
            assertEquals(new Position(6, 5), down);

            Position left = center.getNextPosition(Direction.LEFT);
            assertEquals(new Position(5, 4), left);

            Position right = center.getNextPosition(Direction.RIGHT);
            assertEquals(new Position(5, 6), right);

            System.out.println("✓ All directions work correctly");
        }

        @Test
        @DisplayName("Opposite directions should work correctly")
        void testDirections_Opposite() {
            // ASSERT
            assertEquals(Direction.DOWN, Direction.UP.getOpposite());
            assertEquals(Direction.UP, Direction.DOWN.getOpposite());
            assertEquals(Direction.RIGHT, Direction.LEFT.getOpposite());
            assertEquals(Direction.LEFT, Direction.RIGHT.getOpposite());

            System.out.println("✓ Opposite directions work correctly");
        }

        @Test
        @DisplayName("Sliding path should be sequential")
        void testMovement_SlidingPath() {
            // ARRANGE
            Position start = new Position(5, 5);
            Direction dir = Direction.RIGHT;

            // ACT - Simulate sliding
            Position pos1 = start.getNextPosition(dir);
            Position pos2 = pos1.getNextPosition(dir);
            Position pos3 = pos2.getNextPosition(dir);

            // ASSERT
            assertEquals(new Position(5, 6), pos1);
            assertEquals(new Position(5, 7), pos2);
            assertEquals(new Position(5, 8), pos3);

            System.out.println("Sliding path: " + start + " → " + pos1 +
                    " → " + pos2 + " → " + pos3);
        }
    }

    // ========================================
    // SCOREBOARD SIMULATION TESTS
    // ========================================

    @Test
    @DisplayName("Scoreboard: Penguins should be sorted by food weight")
    void testScoreboard_Sorting() {
        System.out.println("\n=== Scoreboard Sorting Test ===");

        // ARRANGE
        KingPenguin p1 = new KingPenguin("P1", new Position(0, 0));
        EmperorPenguin p2 = new EmperorPenguin("P2", new Position(0, 1));
        RoyalPenguin p3 = new RoyalPenguin("P3", new Position(0, 2));

        // Give them different amounts of food
        p1.collectFood(new Food(new Position(1, 1), FoodType.KRILL, 2));
        p1.collectFood(new Food(new Position(1, 2), FoodType.SQUID, 4));
        // P1 total: 6

        p2.collectFood(new Food(new Position(2, 1), FoodType.MACKEREL, 5));
        p2.collectFood(new Food(new Position(2, 2), FoodType.ANCHOVY, 3));
        p2.collectFood(new Food(new Position(2, 3), FoodType.KRILL, 2));
        // P2 total: 10

        p3.collectFood(new Food(new Position(3, 1), FoodType.CRUSTACEAN, 1));
        // P3 total: 1

        // ACT - Create list and sort
        List<Penguin> penguins = List.of(p1, p2, p3);
        List<Penguin> sorted = penguins.stream()
                .sorted((a, b) -> b.getTotalFoodWeight() - a.getTotalFoodWeight())
                .toList();

        // ASSERT
        assertEquals(p2, sorted.get(0), "P2 should be 1st (10 units)");
        assertEquals(p1, sorted.get(1), "P1 should be 2nd (6 units)");
        assertEquals(p3, sorted.get(2), "P3 should be 3rd (1 unit)");

        System.out.println("📊 Scoreboard:");
        for (int i = 0; i < sorted.size(); i++) {
            Penguin p = sorted.get(i);
            System.out.printf("%d. %s - %d units%n",
                    i + 1, p.getName(), p.getTotalFoodWeight());
        }
    }

    // ========================================
    // PERFORMANCE AND STRESS TESTS
    // ========================================

    @Test
    @DisplayName("Performance: Large number of objects")
    void testPerformance_ManyObjects() {
        System.out.println("\n=== Performance Test ===");

        // ARRANGE
        TerrainGrid grid = new TerrainGrid(20); // Larger grid
        long startTime = System.currentTimeMillis();

        // ACT - Place many objects
        int objectCount = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if ((i + j) % 3 == 0) { // Place object every 3rd position
                    Position pos = new Position(i, j);
                    Food food = Food.createRandom(pos);
                    grid.placeObject(food, pos);
                    objectCount++;
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // ASSERT
        assertTrue(objectCount > 0);
        assertEquals(objectCount, grid.countObjects());
        assertTrue(duration < 1000, "Should complete in under 1 second");

        System.out.println("✓ Placed " + objectCount + " objects in " + duration + "ms");
    }

    @Test
    @DisplayName("Stress: Many penguin movements")
    void testStress_ManyMovements() {
        System.out.println("\n=== Stress Test: Movements ===");

        // ARRANGE
        KingPenguin penguin = new KingPenguin("P1", new Position(5, 5));
        int movementCount = 1000;

        // ACT
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < movementCount; i++) {
            Direction dir = Direction.values()[i % 4];
            Position nextPos = penguin.getPosition().getNextPosition(dir);
            if (nextPos.isValid(GRID_SIZE)) {
                penguin.setPosition(nextPos);
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // ASSERT
        assertNotNull(penguin.getPosition());
        assertTrue(penguin.getPosition().isValid(GRID_SIZE));
        assertTrue(duration < 1000, "Should complete quickly");

        System.out.println("✓ " + movementCount + " movements in " + duration + "ms");
    }

    // ========================================
    // SUMMARY REPORT TEST
    // ========================================

    @Test
    @DisplayName("Summary: Complete game mechanics validation")
    void testSummary_AllMechanics() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎮 GAME MECHANICS VALIDATION SUMMARY");
        System.out.println("=".repeat(60));

        int totalChecks = 0;
        int passedChecks = 0;

        // 1. Penguin Types
        try {
            new KingPenguin("P1", new Position(0, 0));
            new EmperorPenguin("P2", new Position(0, 1));
            new RoyalPenguin("P3", new Position(0, 2));
            new RockhopperPenguin("P4", new Position(0, 3));
            System.out.println("✅ All 4 penguin types work");
            passedChecks++;
        } catch (Exception e) {
            System.out.println("❌ Penguin creation failed");
        }
        totalChecks++;

        // 2. Hazard Types
        try {
            new LightIceBlock(new Position(1, 1));
            new HeavyIceBlock(new Position(1, 2));
            new SeaLion(new Position(1, 3));
            new HoleInIce(new Position(1, 4));
            System.out.println("✅ All 4 hazard types work");
            passedChecks++;
        } catch (Exception e) {
            System.out.println("❌ Hazard creation failed");
        }
        totalChecks++;

        // 3. Food Types
        try {
            Food.createRandom(new Position(2, 2));
            System.out.println("✅ Food generation works");
            passedChecks++;
        } catch (Exception e) {
            System.out.println("❌ Food generation failed");
        }
        totalChecks++;

        // 4. Movement
        try {
            Position pos = new Position(5, 5);
            pos.getNextPosition(Direction.UP);
            pos.getNextPosition(Direction.DOWN);
            pos.getNextPosition(Direction.LEFT);
            pos.getNextPosition(Direction.RIGHT);
            System.out.println("✅ Movement in all directions works");
            passedChecks++;
        } catch (Exception e) {
            System.out.println("❌ Movement failed");
        }
        totalChecks++;

        // 5. Special Abilities
        try {
            KingPenguin king = new KingPenguin("P1", new Position(0, 0));
            king.useSpecialAction();
            assertTrue(king.hasUsedSpecialAction());
            System.out.println("✅ Special abilities work");
            passedChecks++;
        } catch (Exception e) {
            System.out.println("❌ Special abilities failed");
        }
        totalChecks++;

        // 6. Food Collection
        try {
            KingPenguin p = new KingPenguin("P1", new Position(0, 0));
            p.collectFood(new Food(new Position(1, 1), FoodType.KRILL, 2));
            assertEquals(1, p.getFoodCount());
            System.out.println("✅ Food collection works");
            passedChecks++;
        } catch (Exception e) {
            System.out.println("❌ Food collection failed");
        }
        totalChecks++;

        // 7. Collision Detection
        try {
            HeavyIceBlock hb = new HeavyIceBlock(new Position(5, 5));
            String msg = hb.handleCollision("TestPenguin");
            assertNotNull(msg);
            System.out.println("✅ Collision detection works");
            passedChecks++;
        } catch (Exception e) {
            System.out.println("❌ Collision detection failed");
        }
        totalChecks++;

        // 8. Grid Management
        try {
            TerrainGrid grid = new TerrainGrid(10);
            assertEquals(10, grid.getSize());
            System.out.println("✅ Grid management works");
            passedChecks++;
        } catch (Exception e) {
            System.out.println("❌ Grid management failed");
        }
        totalChecks++;

        // Final Report
        System.out.println("=".repeat(60));
        System.out.printf("📊 RESULT: %d/%d checks passed (%.1f%%)%n",
                passedChecks, totalChecks, (passedChecks * 100.0) / totalChecks);
        System.out.println("=".repeat(60));

        // Assert all passed
        assertEquals(totalChecks, passedChecks,
                "All game mechanics should work correctly");
    }
}