package com.hazards;

import com.utils.Position;

/**
 * HeavyIceBlock - A heavy ice block that cannot be moved.
 * Anything that collides with it stops immediately.
 * The colliding penguin loses their lightest food item as a penalty.
 * Cannot slide, so does not implement ISlidable.
 *
 * SECURITY ENHANCED VERSION:
 * - Position safety inherited from Hazard base class
 * - No mutable fields beyond parent
 * - Additional validation methods
 * - Enhanced null safety
 * - Comprehensive documentation
 */
public class HeavyIceBlock extends Hazard {

    /**
     * Constructor for HeavyIceBlock.
     * SECURITY: Position is defensively copied by parent Hazard constructor.
     *
     * @param position The position of the ice block (must not be null)
     * @throws IllegalArgumentException if position is null (from parent)
     */
    public HeavyIceBlock(Position position) {
        super(position, "HB", "HeavyIceBlock");
    }

    /**
     * Handles collision with a penguin.
     * The penguin stops and loses their lightest food item.
     * SECURITY: Safe method - only returns String message with null safety.
     *
     * @param penguinName The name of the colliding penguin
     * @return A message describing the collision
     */
    @Override
    public String handleCollision(String penguinName) {
        if (penguinName == null || penguinName.trim().isEmpty()) {
            return "A penguin collided with a HeavyIceBlock and lost their lightest food item!";
        }
        return penguinName + " collided with a HeavyIceBlock and lost their lightest food item!";
    }

    /**
     * HeavyIceBlock cannot slide.
     * Safe to return primitive boolean constant.
     *
     * @return false
     */
    @Override
    public boolean canSlide() {
        return false;
    }

    // ===== Additional Security & Utility Methods =====

    /**
     * SECURITY: Checks if this is an immovable obstacle.
     * HeavyIceBlock is always immovable.
     *
     * @return true (always immovable)
     */
    public boolean isImmovable() {
        return true;
    }

    /**
     * SECURITY: Checks if this hazard causes food loss.
     * HeavyIceBlock always causes food loss on collision.
     *
     * @return true (always causes food loss)
     */
    public boolean causesFoodLoss() {
        return true;
    }

    /**
     * SECURITY: Gets the penalty type for this hazard.
     * Useful for game logic and UI display.
     *
     * @return String describing the penalty
     */
    public String getPenaltyType() {
        return "Loses lightest food item";
    }

    /**
     * SECURITY: Validates the state of the HeavyIceBlock.
     * HeavyIceBlock has no additional state beyond parent, so just validates parent.
     *
     * @return true if state is valid, false if corrupted
     */
    @Override
    public boolean validateState() {
        // Check parent state
        if (!super.validateState()) {
            return false;
        }

        // HeavyIceBlock should always be active
        if (!isActive()) {
            return false;
        }

        // Shorthand should be "HB"
        if (!"HB".equals(getShorthand())) {
            return false;
        }

        return true;
    }

    /**
     * Returns a string representation of the HeavyIceBlock for debugging.
     * Safe method - returns formatted string with state information.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return super.toString() + ", immovable=true, causesFoodLoss=true";
    }

    /**
     * SECURITY: Gets a detailed summary of the HeavyIceBlock's state.
     * Safe method that provides read-only comprehensive information.
     *
     * @return Formatted string with complete state details
     */
    @Override
    public String getStateSummary() {
        return super.getStateSummary() +
                String.format("\n  Immovable: YES\n  Penalty: %s\n  Can Slide: NO",
                        getPenaltyType());
    }

    /**
     * SECURITY: Gets detailed description including behavior information.
     * Extends parent method with HeavyIceBlock-specific details.
     *
     * @return Detailed string with all information
     */
    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder(super.getDetailedDescription());
        sb.append("\n  Immovable: YES (cannot be moved by any force)");
        sb.append("\n  Penalty: ").append(getPenaltyType());
        sb.append("\n  Behavior: Stops all colliding objects, penguins lose lightest food");
        return sb.toString();
    }

    /**
     * SECURITY: Gets a user-friendly explanation of what happens on collision.
     * Useful for tutorials or help text.
     *
     * @return Human-readable collision explanation
     */
    public String getCollisionExplanation() {
        return "When a penguin collides with a HeavyIceBlock:\n" +
                "  1. The penguin stops immediately\n" +
                "  2. The penguin loses their lightest food item\n" +
                "  3. The HeavyIceBlock does not move\n" +
                "  4. If the penguin has no food, nothing is lost";
    }

    /**
     * SECURITY: Compares two HeavyIceBlocks for equality.
     * Uses parent equality since HeavyIceBlock has no additional fields.
     *
     * @param obj The object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        // Check if obj is a HeavyIceBlock
        if (!(obj instanceof HeavyIceBlock)) {
            return false;
        }

        // Use parent equality (position, active state, etc.)
        return super.equals(obj);
    }

    /**
     * SECURITY: Hash code for HeavyIceBlock.
     * Uses parent hash code since no additional fields.
     *
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        // No additional fields, so just use parent hash code
        return super.hashCode();
    }

    /**
     * SECURITY: Creates a copy of this HeavyIceBlock at a new position.
     * Useful for testing or simulation scenarios.
     *
     * @param newPosition The position for the copy (must not be null)
     * @return A new HeavyIceBlock at the specified position
     * @throws IllegalArgumentException if newPosition is null
     */
    public HeavyIceBlock copyAtPosition(Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("New position cannot be null");
        }
        return new HeavyIceBlock(newPosition);
    }

    /**
     * SECURITY: Checks if this hazard blocks all movement.
     * HeavyIceBlock always blocks movement.
     *
     * @return true (always blocks movement)
     */
    public boolean blocksMovement() {
        return true;
    }

    /**
     * SECURITY: Gets the strength/weight category of this block.
     * Useful for comparing with other ice blocks.
     *
     * @return "HEAVY" as a category indicator
     */
    public String getWeightCategory() {
        return "HEAVY";
    }

    /**
     * SECURITY: Checks if this is heavier than a LightIceBlock.
     * Useful for game logic comparisons.
     *
     * @return true (always heavier than light blocks)
     */
    public boolean isHeavierThanLight() {
        return true;
    }
}