package com.hazards;

import com.enums.Direction;
import com.utils.Position;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Hazard Classes Tests
 */
@DisplayName("Hazard Classes Tests 🧊")
class HazardTest {

    private Position testPosition;

    @BeforeEach
    void setUp() {
        testPosition = new Position(5, 5);
    }


    @Nested
    @DisplayName("LightIceBlock Tests")
    class LightIceBlockTests {

        private LightIceBlock lightIceBlock;

        @BeforeEach
        void setUp() {
            lightIceBlock = new LightIceBlock(testPosition);
        }

        @Test
        @DisplayName("LightIceBlock should be created successfully")
        void testConstructor() {
            assertNotNull(lightIceBlock);
            assertEquals("LB", lightIceBlock.getShorthand());
            assertEquals("LightIceBlock", lightIceBlock.getDisplayName());
            assertTrue(lightIceBlock.isActive());
        }

        @Test
        @DisplayName("LightIceBlock should be slidable")
        void testCanSlide() {
            assertTrue(lightIceBlock.canSlide());
        }

        @Test
        @DisplayName("LightIceBlock should not be sliding initially")
        void testInitialSlidingState() {
            assertFalse(lightIceBlock.isSliding());
            assertNull(lightIceBlock.getSlidingDirection());
        }

        @Test
        @DisplayName("slide() should set sliding state")
        void testSlide() {
            // ACT
            lightIceBlock.slide(Direction.RIGHT);

            // ASSERT
            assertTrue(lightIceBlock.isSliding());
            assertEquals(Direction.RIGHT, lightIceBlock.getSlidingDirection());
        }

        @Test
        @DisplayName("stopSliding() should clear sliding state")
        void testStopSliding() {
            // ARRANGE
            lightIceBlock.slide(Direction.UP);

            // ACT
            lightIceBlock.stopSliding();

            // ASSERT
            assertFalse(lightIceBlock.isSliding());
            assertNull(lightIceBlock.getSlidingDirection());
        }

        @Test
        @DisplayName("handleCollision should return stun message")
        void testHandleCollision() {
            // ACT
            String message = lightIceBlock.handleCollision("P1");

            // ASSERT
            assertNotNull(message);
            assertTrue(message.contains("P1"));
            assertTrue(message.contains("stunned") || message.contains("LightIceBlock"));
        }

        @Test
        @DisplayName("Position should use defensive copy")
        void testDefensiveCopy() {
            // ACT
            Position pos1 = lightIceBlock.getPosition();
            Position pos2 = lightIceBlock.getPosition();

            // ASSERT
            assertNotSame(pos1, pos2);
            assertEquals(pos1, pos2);
        }

        @Test
        @DisplayName("validateState should return true for valid block")
        void testValidateState() {
            assertTrue(lightIceBlock.validateState());
        }

        @Test
        @DisplayName("causesStun should return true")
        void testCausesStun() {
            assertTrue(lightIceBlock.causesStun());
        }

        @Test
        @DisplayName("isMovable should return true")
        void testIsMovable() {
            assertTrue(lightIceBlock.isMovable());
        }

        @Test
        @DisplayName("getWeightCategory should return LIGHT")
        void testWeightCategory() {
            assertEquals("LIGHT", lightIceBlock.getWeightCategory());
        }
    }


    @Nested
    @DisplayName("HeavyIceBlock Tests")
    class HeavyIceBlockTests {

        private HeavyIceBlock heavyIceBlock;

        @BeforeEach
        void setUp() {
            heavyIceBlock = new HeavyIceBlock(testPosition);
        }

        @Test
        @DisplayName("HeavyIceBlock should be created successfully")
        void testConstructor() {
            assertNotNull(heavyIceBlock);
            assertEquals("HB", heavyIceBlock.getShorthand());
            assertEquals("HeavyIceBlock", heavyIceBlock.getDisplayName());
            assertTrue(heavyIceBlock.isActive());
        }

        @Test
        @DisplayName("HeavyIceBlock should not be slidable")
        void testCanSlide() {
            assertFalse(heavyIceBlock.canSlide());
        }

        @Test
        @DisplayName("handleCollision should return food loss message")
        void testHandleCollision() {
            // ACT
            String message = heavyIceBlock.handleCollision("P1");

            // ASSERT
            assertNotNull(message);
            assertTrue(message.contains("P1"));
            assertTrue(message.contains("lightest") || message.contains("food"));
        }

        @Test
        @DisplayName("isImmovable should return true")
        void testIsImmovable() {
            assertTrue(heavyIceBlock.isImmovable());
        }

        @Test
        @DisplayName("causesFoodLoss should return true")
        void testCausesFoodLoss() {
            assertTrue(heavyIceBlock.causesFoodLoss());
        }

        @Test
        @DisplayName("getPenaltyType should describe penalty")
        void testGetPenaltyType() {
            String penalty = heavyIceBlock.getPenaltyType();
            assertNotNull(penalty);
            assertTrue(penalty.contains("lightest") || penalty.contains("food"));
        }

        @Test
        @DisplayName("blocksMovement should return true")
        void testBlocksMovement() {
            assertTrue(heavyIceBlock.blocksMovement());
        }

        @Test
        @DisplayName("getWeightCategory should return HEAVY")
        void testWeightCategory() {
            assertEquals("HEAVY", heavyIceBlock.getWeightCategory());
        }

        @Test
        @DisplayName("validateState should return true for valid block")
        void testValidateState() {
            assertTrue(heavyIceBlock.validateState());
        }
    }


    @Nested
    @DisplayName("SeaLion Tests")
    class SeaLionTests {

        private SeaLion seaLion;

        @BeforeEach
        void setUp() {
            seaLion = new SeaLion(testPosition);
        }

        @Test
        @DisplayName("SeaLion should be created successfully")
        void testConstructor() {
            assertNotNull(seaLion);
            assertEquals("SL", seaLion.getShorthand());
            assertEquals("SeaLion", seaLion.getDisplayName());
            assertTrue(seaLion.isActive());
        }

        @Test
        @DisplayName("SeaLion should be slidable")
        void testCanSlide() {
            assertTrue(seaLion.canSlide());
        }

        @Test
        @DisplayName("SeaLion should not be sliding initially")
        void testInitialSlidingState() {
            assertFalse(seaLion.isSliding());
            assertNull(seaLion.getSlidingDirection());
        }

        @Test
        @DisplayName("slide() should set sliding state")
        void testSlide() {
            // ACT
            seaLion.slide(Direction.LEFT);

            // ASSERT
            assertTrue(seaLion.isSliding());
            assertEquals(Direction.LEFT, seaLion.getSlidingDirection());
        }

        @Test
        @DisplayName("canBounce should return true when stationary")
        void testCanBounce_Stationary() {
            assertTrue(seaLion.canBounce());
        }

        @Test
        @DisplayName("canBounce should return false when sliding")
        void testCanBounce_Sliding() {
            // ARRANGE
            seaLion.slide(Direction.UP);

            // ASSERT
            assertFalse(seaLion.canBounce());
        }

        @Test
        @DisplayName("handleCollision should return bounce message")
        void testHandleCollision() {
            // ACT
            String message = seaLion.handleCollision("P1");

            // ASSERT
            assertNotNull(message);
            assertTrue(message.contains("P1"));
            assertTrue(message.contains("bounce") || message.contains("SeaLion"));
        }

        @Test
        @DisplayName("transfersMomentum should return true")
        void testTransfersMomentum() {
            assertTrue(seaLion.transfersMomentum());
        }

        @Test
        @DisplayName("getInteractionType should return BOUNCE")
        void testInteractionType() {
            assertEquals("BOUNCE", seaLion.getInteractionType());
        }

        @Test
        @DisplayName("validateState should return true for valid SeaLion")
        void testValidateState() {
            assertTrue(seaLion.validateState());
        }

        @Test
        @DisplayName("stopSliding should clear sliding state")
        void testStopSliding() {
            // ARRANGE
            seaLion.slide(Direction.DOWN);

            // ACT
            seaLion.stopSliding();

            // ASSERT
            assertFalse(seaLion.isSliding());
            assertNull(seaLion.getSlidingDirection());
        }
    }


    @Nested
    @DisplayName("HoleInIce Tests")
    class HoleInIceTests {

        private HoleInIce holeInIce;

        @BeforeEach
        void setUp() {
            holeInIce = new HoleInIce(testPosition);
        }

        @Test
        @DisplayName("HoleInIce should be created successfully")
        void testConstructor() {
            assertNotNull(holeInIce);
            assertEquals("HI", holeInIce.getShorthand());
            assertEquals("HoleInIce", holeInIce.getDisplayName());
            assertTrue(holeInIce.isActive());
            assertFalse(holeInIce.isPlugged());
        }

        @Test
        @DisplayName("HoleInIce should not be slidable")
        void testCanSlide() {
            assertFalse(holeInIce.canSlide());
        }

        @Test
        @DisplayName("Active hole should be dangerous")
        void testIsDangerous_Active() {
            assertTrue(holeInIce.isDangerous());
            assertTrue(holeInIce.isActive());
        }

        @Test
        @DisplayName("plug() should plug the hole")
        void testPlug() {
            // ACT
            holeInIce.plug();

            // ASSERT
            assertTrue(holeInIce.isPlugged());
            assertFalse(holeInIce.isActive());
            assertFalse(holeInIce.isDangerous());
            assertEquals("PH", holeInIce.getShorthand());
        }

        @Test
        @DisplayName("Plugged hole should allow passage")
        void testAllowsPassage_Plugged() {
            // ARRANGE
            holeInIce.plug();

            // ASSERT
            assertTrue(holeInIce.allowsPassage());
        }

        @Test
        @DisplayName("Active hole should not allow passage")
        void testAllowsPassage_Active() {
            assertFalse(holeInIce.allowsPassage());
        }

        @Test
        @DisplayName("unplug() should unplug the hole")
        void testUnplug() {
            // ARRANGE
            holeInIce.plug();

            // ACT
            holeInIce.unplug();

            // ASSERT
            assertFalse(holeInIce.isPlugged());
            assertTrue(holeInIce.isActive());
            assertTrue(holeInIce.isDangerous());
            assertEquals("HI", holeInIce.getShorthand());
        }

        @Test
        @DisplayName("handleCollision should return fall message")
        void testHandleCollision() {
            // ACT
            String message = holeInIce.handleCollision("P1");

            // ASSERT
            assertNotNull(message);
            assertTrue(message.contains("P1"));
            assertTrue(message.contains("fell") || message.contains("HoleInIce"));
        }

        @Test
        @DisplayName("Shorthand should change when plugged")
        void testShorthandChanges() {
            // Initially HI
            assertEquals("HI", holeInIce.getShorthand());

            // After plugging: PH
            holeInIce.plug();
            assertEquals("PH", holeInIce.getShorthand());
        }

        @Test
        @DisplayName("validateState should return true for valid hole")
        void testValidateState_Active() {
            assertTrue(holeInIce.validateState());
        }

        @Test
        @DisplayName("validateState should return true for plugged hole")
        void testValidateState_Plugged() {
            holeInIce.plug();
            assertTrue(holeInIce.validateState());
        }

        @Test
        @DisplayName("getStatusCategory should reflect state")
        void testGetStatusCategory() {
            assertEquals("ACTIVE", holeInIce.getStatusCategory());

            holeInIce.plug();
            assertEquals("PLUGGED", holeInIce.getStatusCategory());
        }

        @Test
        @DisplayName("hasStatus should check state correctly")
        void testHasStatus() {
            assertTrue(holeInIce.hasStatus(false)); // Not plugged
            assertFalse(holeInIce.hasStatus(true)); // Not plugged

            holeInIce.plug();
            assertTrue(holeInIce.hasStatus(true)); // Plugged
            assertFalse(holeInIce.hasStatus(false)); // Plugged
        }

        @Test
        @DisplayName("copyAtPosition should preserve plugged state")
        void testCopyAtPosition_PreservesState() {
            // ARRANGE
            holeInIce.plug();
            Position newPos = new Position(7, 7);

            // ACT
            HoleInIce copy = holeInIce.copyAtPosition(newPos);

            // ASSERT
            assertTrue(copy.isPlugged());
            assertEquals(newPos, copy.getPosition());
        }

        @Test
        @DisplayName("createFreshCopy should return unplugged hole")
        void testCreateFreshCopy() {
            // ARRANGE
            holeInIce.plug();
            Position newPos = new Position(8, 8);

            // ACT
            HoleInIce fresh = holeInIce.createFreshCopy(newPos);

            // ASSERT
            assertFalse(fresh.isPlugged());
            assertTrue(fresh.isActive());
            assertEquals(newPos, fresh.getPosition());
        }
    }

    @Test
    @DisplayName("Integration: All hazard types should be creatable")
    void testIntegration_AllHazardTypes() {
        System.out.println("\n=== All Hazard Types ===");

        LightIceBlock lb = new LightIceBlock(new Position(1, 1));
        HeavyIceBlock hb = new HeavyIceBlock(new Position(2, 2));
        SeaLion sl = new SeaLion(new Position(3, 3));
        HoleInIce hi = new HoleInIce(new Position(4, 4));

        System.out.println("LightIceBlock: " + lb.getShorthand() + " - Can slide: " + lb.canSlide());
        System.out.println("HeavyIceBlock: " + hb.getShorthand() + " - Can slide: " + hb.canSlide());
        System.out.println("SeaLion: " + sl.getShorthand() + " - Can slide: " + sl.canSlide());
        System.out.println("HoleInIce: " + hi.getShorthand() + " - Can slide: " + hi.canSlide());

        assertNotNull(lb);
        assertNotNull(hb);
        assertNotNull(sl);
        assertNotNull(hi);

        System.out.println("✓ All hazard types created successfully");
    }

    @Test
    @DisplayName("Integration: Slidable vs non-slidable hazards")
    void testIntegration_SlidableClassification() {
        // ARRANGE
        LightIceBlock lb = new LightIceBlock(testPosition);
        HeavyIceBlock hb = new HeavyIceBlock(testPosition);
        SeaLion sl = new SeaLion(testPosition);
        HoleInIce hi = new HoleInIce(testPosition);

        // ASSERT - Slidable
        assertTrue(lb.canSlide(), "LightIceBlock should slide");
        assertTrue(sl.canSlide(), "SeaLion should slide");

        // ASSERT - Not slidable
        assertFalse(hb.canSlide(), "HeavyIceBlock should not slide");
        assertFalse(hi.canSlide(), "HoleInIce should not slide");

        System.out.println("✓ Slidable classification correct");
    }

    @Test
    @DisplayName("Integration: Hazard state transitions")
    void testIntegration_StateTransitions() {
        System.out.println("\n=== Hazard State Transitions ===");

        // LightIceBlock: stationary → sliding
        LightIceBlock lb = new LightIceBlock(testPosition);
        assertFalse(lb.isSliding());
        lb.slide(Direction.UP);
        assertTrue(lb.isSliding());
        System.out.println("✓ LightIceBlock state transition");

        // HoleInIce: active → plugged
        HoleInIce hi = new HoleInIce(testPosition);
        assertTrue(hi.isActive());
        hi.plug();
        assertFalse(hi.isActive());
        assertTrue(hi.isPlugged());
        System.out.println("✓ HoleInIce state transition");

        // SeaLion: stationary → sliding
        SeaLion sl = new SeaLion(testPosition);
        assertTrue(sl.canBounce());
        sl.slide(Direction.DOWN);
        assertFalse(sl.canBounce());
        System.out.println("✓ SeaLion state transition");

        System.out.println("✓ All state transitions work correctly");
    }

    @Test
    @DisplayName("Integration: Collision messages for all hazards")
    void testIntegration_CollisionMessages() {
        System.out.println("\n=== Collision Messages ===");

        LightIceBlock lb = new LightIceBlock(testPosition);
        HeavyIceBlock hb = new HeavyIceBlock(testPosition);
        SeaLion sl = new SeaLion(testPosition);
        HoleInIce hi = new HoleInIce(testPosition);

        String lbMsg = lb.handleCollision("TestPenguin");
        String hbMsg = hb.handleCollision("TestPenguin");
        String slMsg = sl.handleCollision("TestPenguin");
        String hiMsg = hi.handleCollision("TestPenguin");

        System.out.println("LightIceBlock: " + lbMsg);
        System.out.println("HeavyIceBlock: " + hbMsg);
        System.out.println("SeaLion: " + slMsg);
        System.out.println("HoleInIce: " + hiMsg);

        assertNotNull(lbMsg);
        assertNotNull(hbMsg);
        assertNotNull(slMsg);
        assertNotNull(hiMsg);

        System.out.println("✓ All collision messages work");
    }
}