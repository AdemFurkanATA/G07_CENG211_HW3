package com.hazards;

import com.utils.Position;

/**
 * HeavyIceBlock - A heavy ice block that cannot be moved.
 * Anything that collides with it stops immediately.
 * The colliding penguin loses their lightest food item as a penalty.
 * Cannot slide, so does not implement ISlidable.
 */
public class HeavyIceBlock extends Hazard {

    /**
     * Constructor for HeavyIceBlock.
     * @param position The position of the ice block (must not be null)
     * @throws IllegalArgumentException if position is null (from parent)
     */
    public HeavyIceBlock(Position position) {
        super(position, "HB", "HeavyIceBlock");
    }

    /**
     * Handles collision with a penguin.
     * The penguin stops and loses their lightest food item.
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
     * @return false
     */
    @Override
    public boolean canSlide() {
        return false;
    }


    /**
     * @return true (always immovable)
     */
    public boolean isImmovable() {
        return true;
    }

    /**
     * @return true (always causes food loss)
     */
    public boolean causesFoodLoss() {
        return true;
    }

    /**
     * @return String describing the penalty
     */
    public String getPenaltyType() {
        return "Loses lightest food item";
    }

    /**
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
     * @return String representation
     */
    @Override
    public String toString() {
        return super.toString() + ", immovable=true, causesFoodLoss=true";
    }

    /**
     * @return Formatted string with complete state details
     */
    @Override
    public String getStateSummary() {
        return super.getStateSummary() +
                String.format("\n  Immovable: YES\n  Penalty: %s\n  Can Slide: NO",
                        getPenaltyType());
    }

    /**
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
     * @param obj The object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof HeavyIceBlock)) {
            return false;
        }

        return super.equals(obj);
    }

    /**
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        // No additional fields, so just use parent hash code
        return super.hashCode();
    }

    /**
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
     * @return true (always blocks movement)
     */
    public boolean blocksMovement() {
        return true;
    }

    /**
     * @return "HEAVY" as a category indicator
     */
    public String getWeightCategory() {
        return "HEAVY";
    }

    /**
     * @return true (always heavier than light blocks)
     */
    public boolean isHeavierThanLight() {
        return true;
    }
}