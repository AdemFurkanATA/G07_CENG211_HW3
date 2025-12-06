package com.terrain;

import com.food.Food;
import com.enums.FoodType;
import com.hazards.HeavyIceBlock;
import com.interfaces.ITerrainObject;
import com.penguins.KingPenguin;
import com.utils.Position;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TerrainGrid Class Tests
 *
 * Location: src/test/java/com/terrain/TerrainGridTest.java
 *
 * Testing grid operations and object placement
 */
@DisplayName("TerrainGrid Tests 🗺️")
class TerrainGridTest {

    private TerrainGrid grid;
    private static final int TEST_GRID_SIZE = 10;

    @BeforeEach
    void setUp() {
        grid = new TerrainGrid(TEST_GRID_SIZE);
    }

    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================

    @Test
    @DisplayName("TerrainGrid should be created with correct size")
    void testConstructor() {
        // ASSERT
        assertNotNull(grid);
        assertEquals(TEST_GRID_SIZE, grid.getSize());

        System.out.println("✓ Grid created with size " + TEST_GRID_SIZE);
    }

    @Test
    @DisplayName("Creating grid with invalid size should throw exception")
    void testConstructor_InvalidSize() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            new TerrainGrid(0);
        }, "Size 0 should throw exception");

        assertThrows(IllegalArgumentException.class, () -> {
            new TerrainGrid(-1);
        }, "Negative size should throw exception");
    }

    @Test
    @DisplayName("New grid should be empty")
    void testConstructor_EmptyGrid() {
        // ACT
        int objectCount = grid.countObjects();

        // ASSERT
        assertEquals(0, objectCount, "New grid should have no objects");
    }

    // ========================================
    // PLACE OBJECT TESTS
    // ========================================

    @Test
    @DisplayName("placeObject should place object at valid position")
    void testPlaceObject_ValidPosition() {
        // ARRANGE
        Position pos = new Position(5, 5);
        Food food = new Food(pos, FoodType.KRILL, 2);

        // ACT
        boolean result = grid.placeObject(food, pos);

        // ASSERT
        assertTrue(result, "Should return true on success");
        assertEquals(food, grid.getObjectAt(pos));
        assertEquals(1, grid.countObjects());
    }

    @Test
    @DisplayName("placeObject with invalid position should return false")
    void testPlaceObject_InvalidPosition() {
        // ARRANGE
        Position invalidPos = new Position(20, 20); // Outside 10x10 grid
        Food food = new Food(new Position(0, 0), FoodType.KRILL, 2);

        // ACT
        boolean result = grid.placeObject(food, invalidPos);

        // ASSERT
        assertFalse(result, "Should return false for invalid position");
        assertEquals(0, grid.countObjects());
    }

    @Test
    @DisplayName("placeObject with null position should throw exception")
    void testPlaceObject_NullPosition() {
        // ARRANGE
        Food food = new Food(new Position(0, 0), FoodType.KRILL, 2);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            grid.placeObject(food, null);
        });
    }

    @Test
    @DisplayName("placeObject should replace existing object")
    void testPlaceObject_ReplaceExisting() {
        // ARRANGE
        Position pos = new Position(3, 3);
        Food food1 = new Food(pos, FoodType.KRILL, 1);
        Food food2 = new Food(pos, FoodType.SQUID, 5);

        // ACT
        grid.placeObject(food1, pos);
        grid.placeObject(food2, pos);

        // ASSERT
        assertEquals(food2, grid.getObjectAt(pos), "Second object should replace first");
        assertEquals(1, grid.countObjects(), "Should still have only 1 object");
    }

    @Test
    @DisplayName("placeObject with null object should clear position")
    void testPlaceObject_NullObject() {
        // ARRANGE
        Position pos = new Position(4, 4);
        Food food = new Food(pos, FoodType.ANCHOVY, 3);
        grid.placeObject(food, pos);

        // ACT
        grid.placeObject(null, pos);

        // ASSERT
        assertNull(grid.getObjectAt(pos), "Position should be cleared");
        assertEquals(0, grid.countObjects());
    }

    // ========================================
    // GET OBJECT TESTS
    // ========================================

    @Test
    @DisplayName("getObjectAt should return placed object")
    void testGetObjectAt_ExistingObject() {
        // ARRANGE
        Position pos = new Position(7, 7);
        HeavyIceBlock hazard = new HeavyIceBlock(pos);
        grid.placeObject(hazard, pos);

        // ACT
        ITerrainObject retrieved = grid.getObjectAt(pos);

        // ASSERT
        assertNotNull(retrieved);
        assertEquals(hazard, retrieved);
    }

    @Test
    @DisplayName("getObjectAt empty position should return null")
    void testGetObjectAt_EmptyPosition() {
        // ARRANGE
        Position pos = new Position(2, 2);

        // ACT
        ITerrainObject retrieved = grid.getObjectAt(pos);

        // ASSERT
        assertNull(retrieved);
    }

    @Test
    @DisplayName("getObjectAt invalid position should return null")
    void testGetObjectAt_InvalidPosition() {
        // ARRANGE
        Position invalidPos = new Position(15, 15);

        // ACT
        ITerrainObject retrieved = grid.getObjectAt(invalidPos);

        // ASSERT
        assertNull(retrieved);
    }

    @Test
    @DisplayName("getObjectAt with null should throw exception")
    void testGetObjectAt_NullPosition() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            grid.getObjectAt(null);
        });
    }

    // ========================================
    // REMOVE OBJECT TESTS
    // ========================================

    @Test
    @DisplayName("removeObject should remove and return object")
    void testRemoveObject() {
        // ARRANGE
        Position pos = new Position(6, 6);
        Food food = new Food(pos, FoodType.MACKEREL, 5);
        grid.placeObject(food, pos);

        // ACT
        ITerrainObject removed = grid.removeObject(pos);

        // ASSERT
        assertEquals(food, removed);
        assertNull(grid.getObjectAt(pos), "Position should be empty after removal");
        assertEquals(0, grid.countObjects());
    }

    @Test
    @DisplayName("removeObject from empty position should return null")
    void testRemoveObject_EmptyPosition() {
        // ARRANGE
        Position pos = new Position(1, 1);

        // ACT
        ITerrainObject removed = grid.removeObject(pos);

        // ASSERT
        assertNull(removed);
    }

    @Test
    @DisplayName("removeObject with null should throw exception")
    void testRemoveObject_NullPosition() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            grid.removeObject(null);
        });
    }

    // ========================================
    // VALIDATION TESTS
    // ========================================

    @Test
    @DisplayName("isValidPosition should validate positions correctly")
    void testIsValidPosition() {
        // Valid positions
        assertTrue(grid.isValidPosition(new Position(0, 0)));
        assertTrue(grid.isValidPosition(new Position(5, 5)));
        assertTrue(grid.isValidPosition(new Position(9, 9)));

        // Invalid positions
        assertFalse(grid.isValidPosition(new Position(-1, 0)));
        assertFalse(grid.isValidPosition(new Position(0, -1)));
        assertFalse(grid.isValidPosition(new Position(10, 10)));
        assertFalse(grid.isValidPosition(new Position(15, 5)));
    }

    @Test
    @DisplayName("isEmpty should check positions correctly")
    void testIsEmpty() {
        // ARRANGE
        Position emptyPos = new Position(1, 1);
        Position occupiedPos = new Position(2, 2);
        grid.placeObject(new Food(occupiedPos, FoodType.KRILL, 1), occupiedPos);

        // ASSERT
        assertTrue(grid.isEmpty(emptyPos), "Empty position should return true");
        assertFalse(grid.isEmpty(occupiedPos), "Occupied position should return false");
    }

    @Test
    @DisplayName("isEdge should detect edge positions")
    void testIsEdge() {
        // Edge positions
        assertTrue(grid.isEdge(new Position(0, 5)), "Top edge");
        assertTrue(grid.isEdge(new Position(9, 5)), "Bottom edge");
        assertTrue(grid.isEdge(new Position(5, 0)), "Left edge");
        assertTrue(grid.isEdge(new Position(5, 9)), "Right edge");
        assertTrue(grid.isEdge(new Position(0, 0)), "Corner is also edge");

        // Not edge
        assertFalse(grid.isEdge(new Position(5, 5)), "Center is not edge");
    }

    // ========================================
    // CLEAR TESTS
    // ========================================

    @Test
    @DisplayName("clear should remove all objects")
    void testClear() {
        // ARRANGE - Place multiple objects
        grid.placeObject(new Food(new Position(1, 1), FoodType.KRILL, 1), new Position(1, 1));
        grid.placeObject(new Food(new Position(2, 2), FoodType.SQUID, 2), new Position(2, 2));
        grid.placeObject(new Food(new Position(3, 3), FoodType.ANCHOVY, 3), new Position(3, 3));
        assertEquals(3, grid.countObjects());

        // ACT
        grid.clear();

        // ASSERT
        assertEquals(0, grid.countObjects());
        assertTrue(grid.isEmpty(new Position(1, 1)));
        assertTrue(grid.isEmpty(new Position(2, 2)));
        assertTrue(grid.isEmpty(new Position(3, 3)));
    }

    // ========================================
    // COUNT OBJECTS TESTS
    // ========================================

    @Test
    @DisplayName("countObjects should count correctly")
    void testCountObjects() {
        // Initially 0
        assertEquals(0, grid.countObjects());

        // Add objects
        grid.placeObject(new Food(new Position(0, 0), FoodType.KRILL, 1), new Position(0, 0));
        assertEquals(1, grid.countObjects());

        grid.placeObject(new Food(new Position(1, 1), FoodType.SQUID, 2), new Position(1, 1));
        assertEquals(2, grid.countObjects());

        grid.placeObject(new Food(new Position(2, 2), FoodType.ANCHOVY, 3), new Position(2, 2));
        assertEquals(3, grid.countObjects());

        // Remove one
        grid.removeObject(new Position(1, 1));
        assertEquals(2, grid.countObjects());
    }

    // ========================================
    // GET EMPTY POSITIONS TESTS
    // ========================================

    @Test
    @DisplayName("getEmptyPositions should return all empty positions")
    void testGetEmptyPositions() {
        // ARRANGE - Place one object
        Position occupiedPos = new Position(5, 5);
        grid.placeObject(new Food(occupiedPos, FoodType.KRILL, 1), occupiedPos);

        // ACT
        List<Position> emptyPositions = grid.getEmptyPositions();

        // ASSERT
        assertEquals(99, emptyPositions.size(), "10x10 grid - 1 occupied = 99 empty");
        assertFalse(emptyPositions.contains(occupiedPos), "Should not contain occupied position");
    }

    @Test
    @DisplayName("getEmptyPositions should return unmodifiable list")
    void testGetEmptyPositions_Unmodifiable() {
        // ACT
        List<Position> emptyPositions = grid.getEmptyPositions();

        // ASSERT
        assertThrows(UnsupportedOperationException.class, () -> {
            emptyPositions.add(new Position(0, 0));
        }, "List should be unmodifiable");
    }

    @Test
    @DisplayName("getEmptyPositions should return defensive copy")
    void testGetEmptyPositions_DefensiveCopy() {
        // ACT
        List<Position> list1 = grid.getEmptyPositions();
        List<Position> list2 = grid.getEmptyPositions();

        // ASSERT - Lists should be different objects
        assertNotSame(list1, list2);
        assertEquals(list1.size(), list2.size());
    }

    // ========================================
    // GET EMPTY EDGE POSITIONS TESTS
    // ========================================

    @Test
    @DisplayName("getEmptyEdgePositions should return only edge positions")
    void testGetEmptyEdgePositions() {
        // ACT
        List<Position> edgePositions = grid.getEmptyEdgePositions();

        // ASSERT
        assertTrue(edgePositions.size() > 0, "Should have edge positions");

        // All should be edges
        for (Position pos : edgePositions) {
            assertTrue(pos.isEdge(TEST_GRID_SIZE),
                    "All positions should be edges: " + pos);
        }
    }

    @Test
    @DisplayName("getEmptyEdgePositions should exclude occupied edges")
    void testGetEmptyEdgePositions_ExcludesOccupied() {
        // ARRANGE - Occupy an edge position
        Position edgePos = new Position(0, 5);
        grid.placeObject(new Food(edgePos, FoodType.KRILL, 1), edgePos);

        // ACT
        List<Position> edgePositions = grid.getEmptyEdgePositions();

        // ASSERT
        assertFalse(edgePositions.contains(edgePos),
                "Should not contain occupied edge position");
    }

    // ========================================
    // GET ALL OBJECTS TESTS
    // ========================================

    @Test
    @DisplayName("getAllObjects should return all placed objects")
    void testGetAllObjects() {
        // ARRANGE
        Food food1 = new Food(new Position(1, 1), FoodType.KRILL, 1);
        Food food2 = new Food(new Position(2, 2), FoodType.SQUID, 2);
        Food food3 = new Food(new Position(3, 3), FoodType.ANCHOVY, 3);

        grid.placeObject(food1, new Position(1, 1));
        grid.placeObject(food2, new Position(2, 2));
        grid.placeObject(food3, new Position(3, 3));

        // ACT
        List<ITerrainObject> allObjects = grid.getAllObjects();

        // ASSERT
        assertEquals(3, allObjects.size());
        assertTrue(allObjects.contains(food1));
        assertTrue(allObjects.contains(food2));
        assertTrue(allObjects.contains(food3));
    }

    @Test
    @DisplayName("getAllObjects should return unmodifiable list")
    void testGetAllObjects_Unmodifiable() {
        // ACT
        List<ITerrainObject> objects = grid.getAllObjects();

        // ASSERT
        assertThrows(UnsupportedOperationException.class, () -> {
            objects.add(new Food(new Position(0, 0), FoodType.KRILL, 1));
        });
    }

    // ========================================
    // VALIDATE GRID INTEGRITY TESTS
    // ========================================

    @Test
    @DisplayName("validateGridIntegrity should return true for valid grid")
    void testValidateGridIntegrity() {
        // ASSERT
        assertTrue(grid.validateGridIntegrity());
    }

    @Test
    @DisplayName("Grid with objects should still be valid")
    void testValidateGridIntegrity_WithObjects() {
        // ARRANGE - Add objects
        grid.placeObject(new Food(new Position(1, 1), FoodType.KRILL, 1), new Position(1, 1));
        grid.placeObject(new Food(new Position(2, 2), FoodType.SQUID, 2), new Position(2, 2));

        // ASSERT
        assertTrue(grid.validateGridIntegrity());
    }

    // ========================================
    // GET GRID TESTS (Defensive Copy)
    // ========================================

    @Test
    @DisplayName("getGrid should return defensive copy")
    void testGetGrid_DefensiveCopy() {
        // ACT
        List<List<ITerrainObject>> grid1 = grid.getGrid();
        List<List<ITerrainObject>> grid2 = grid.getGrid();

        // ASSERT
        assertNotSame(grid1, grid2, "Should return different list objects");
        assertEquals(grid1.size(), grid2.size());
    }

    @Test
    @DisplayName("getGrid should return unmodifiable list")
    void testGetGrid_Unmodifiable() {
        // ACT
        List<List<ITerrainObject>> gridList = grid.getGrid();

        // ASSERT
        assertThrows(UnsupportedOperationException.class, () -> {
            gridList.add(null);
        }, "Outer list should be unmodifiable");
    }

    // ========================================
    // INTEGRATION TESTS
    // ========================================

    @Test
    @DisplayName("Integration: Complete grid workflow")
    void testIntegration_GridWorkflow() {
        System.out.println("\n=== Grid Workflow ===");

        // 1. Create and verify grid
        TerrainGrid testGrid = new TerrainGrid(10);
        System.out.println("1️⃣ Created 10x10 grid");
        assertEquals(10, testGrid.getSize());

        // 2. Place objects
        Food food = new Food(new Position(5, 5), FoodType.KRILL, 2);
        KingPenguin penguin = new KingPenguin("P1", new Position(0, 0));
        HeavyIceBlock hazard = new HeavyIceBlock(new Position(9, 9));

        testGrid.placeObject(food, new Position(5, 5));
        testGrid.placeObject(penguin, new Position(0, 0));
        testGrid.placeObject(hazard, new Position(9, 9));
        System.out.println("2️⃣ Placed 3 objects");
        assertEquals(3, testGrid.countObjects());

        // 3. Retrieve objects
        assertEquals(food, testGrid.getObjectAt(new Position(5, 5)));
        assertEquals(penguin, testGrid.getObjectAt(new Position(0, 0)));
        assertEquals(hazard, testGrid.getObjectAt(new Position(9, 9)));
        System.out.println("3️⃣ Retrieved objects successfully");

        // 4. Remove one object
        testGrid.removeObject(new Position(5, 5));
        System.out.println("4️⃣ Removed one object");
        assertEquals(2, testGrid.countObjects());

        // 5. Clear grid
        testGrid.clear();
        System.out.println("5️⃣ Cleared grid");
        assertEquals(0, testGrid.countObjects());

        System.out.println("✓ Grid workflow complete");
    }

    @Test
    @DisplayName("Integration: Position validation across operations")
    void testIntegration_PositionValidation() {
        // Valid operations
        Position validPos = new Position(5, 5);
        assertTrue(grid.isValidPosition(validPos));
        assertTrue(grid.isEmpty(validPos));

        Food food = new Food(validPos, FoodType.KRILL, 1);
        assertTrue(grid.placeObject(food, validPos));
        assertFalse(grid.isEmpty(validPos));

        // Invalid operations
        Position invalidPos = new Position(20, 20);
        assertFalse(grid.isValidPosition(invalidPos));
        assertFalse(grid.placeObject(food, invalidPos));

        System.out.println("✓ Position validation consistent");
    }

    @Test
    @DisplayName("Integration: Edge position handling")
    void testIntegration_EdgePositions() {
        System.out.println("\n=== Edge Position Handling ===");

        // Get all edge positions
        List<Position> edgePositions = grid.getEmptyEdgePositions();
        System.out.println("Total edge positions: " + edgePositions.size());

        // Verify they're all edges
        for (Position pos : edgePositions) {
            assertTrue(grid.isEdge(pos));
            assertTrue(pos.isEdge(TEST_GRID_SIZE));
        }

        // Occupy some edge positions
        int occupied = 0;
        for (int i = 0; i < 5 && i < edgePositions.size(); i++) {
            Position pos = edgePositions.get(i);
            grid.placeObject(new Food(pos, FoodType.KRILL, 1), pos);
            occupied++;
        }

        // Check updated edge positions
        List<Position> newEdgePositions = grid.getEmptyEdgePositions();
        assertEquals(edgePositions.size() - occupied, newEdgePositions.size());

        System.out.println("✓ Edge position handling correct");
    }
}