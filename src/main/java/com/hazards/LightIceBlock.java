package com.hazards;

import com.enums.Direction;
import com.interfaces.ISlidable;
import com.utils.Position;

/**
 * LightIceBlock - A light ice block that can slide when hit.
 * When a penguin or sliding hazard collides with it, it starts sliding.
 * The colliding object stops, and the penguin gets stunned (skips next turn).
 * Implements ISlidable because it can slide on ice.
 *
 * SECURITY ENHANCED VERSION:
 * - Position safety inherited from Hazard base class
 * - Direction enum is immutable (safe)
 * - Primitive boolean fields (safe)
 * - Additional validation and utility methods
 * - State consistency checks
 */
public class LightIceBlock extends Hazard implements ISlidable {

    private boolean isSliding;           // Whether the ice block is currently sliding
    private Direction slidingDirection;  // Current sliding direction

    /**
     * Constructor for LightIceBlock.
     * SECURITY: Position is defensively copied by parent Hazard constructor.
     *
     * @param position The position of the ice block (must not be null)
     * @throws IllegalArgumentException if position is null (from parent)
     */
    public LightIceBlock(Position position) {
        super(position, "LB", "LightIceBlock");
        this.isSliding = false;
        this.slidingDirection = null;
    }

    /**
     * Handles collision with a penguin.
     * The ice block starts sliding, and the penguin is stunned.
     * SECURITY: Safe method - only returns String message.
     *
     * @param penguinName The name of the colliding penguin
     * @return A message describing the collision
     */
    @Override
    public String handleCollision(String penguinName) {
        if (penguinName == null || penguinName.trim().isEmpty()) {
            return "A penguin collided with a LightIceBlock and is stunned!";
        }
        return penguinName + " collided with a LightIceBlock and is stunned!";
    }

    /**
     * LightIceBlock can slide on ice.
     * Safe to return primitive boolean.
     *
     * @return true
     */
    @Override
    public boolean canSlide() {
        return true;
    }

    // ===== ISlidable Methods =====

    /**
     * Initiates sliding in a direction.
     * SECURITY: Direction enum is immutable, safe to store directly.
     *
     * @param direction The direction to slide
     */
    @Override
    public void slide(Direction direction) {
        this.isSliding = true;
        this.slidingDirection = direction;
    }

    /**
     * Checks if the ice block is currently sliding.
     * Safe to return primitive boolean.
     *
     * @return true if currently sliding, false otherwise
     */
    @Override
    public boolean isSliding() {
        return isSliding;
    }

    /**
     * Sets the sliding state.
     * Safe method - only modifies internal boolean.
     *
     * @param sliding true to mark as sliding
     */
    @Override
    public void setSliding(boolean sliding) {
        this.isSliding = sliding;
    }

    /**
     * Gets the current sliding direction.
     * Safe to return - Direction enum is immutable.
     *
     * @return The Direction of current sliding, or null if not sliding
     */
    @Override
    public Direction getSlidingDirection() {
        return slidingDirection;
    }

    /**
     * Sets the sliding direction.
     * SECURITY: Direction enum is immutable, safe to store directly.
     *
     * @param direction The Direction to slide
     */
    @Override
    public void setSlidingDirection(Direction direction) {
        this.slidingDirection = direction;
    }

    // ===== Additional Security & Utility Methods =====

    /**
     * SECURITY: Stops the ice block from sliding.
     * Convenience method that clears both sliding state and direction.
     */
    public void stopSliding() {
        this.isSliding = false;
        this.slidingDirection = null;
    }

    /**
     * SECURITY: Validates the sliding state consistency.
     * Ensures the ice block's sliding-related state makes sense.
     *
     * @return true if state is valid, false if inconsistent
     */
    public boolean isSlidingStateValid() {
        // If sliding, should have a direction
        if (isSliding && slidingDirection == null) {
            return false;
        }
        // If not sliding, direction should ideally be null (but not critical)
        return true;
    }

    /**
     * SECURITY: Checks if the ice block is actively sliding in a specific direction.
     * More specific than just isSliding().
     *
     * @param direction The direction to check
     * @return true if sliding in that direction, false otherwise
     */
    public boolean isSlidingInDirection(Direction direction) {
        if (direction == null) {
            return false;
        }
        return isSliding && direction.equals(slidingDirection);
    }

    /**
     * SECURITY: Gets the current sliding status as a readable string.
     * Safe method that doesn't expose mutable state.
     *
     * @return String describing the current sliding status
     */
    public String getSlidingStatus() {
        if (isSliding && slidingDirection != null) {
            return "Sliding " + slidingDirection;
        } else if (isSliding) {
            return "Sliding (direction unknown)";
        } else {
            return "Stationary";
        }
    }

    /**
     * SECURITY: Validates the entire state of the LightIceBlock.
     * Extends parent validation with sliding-specific checks.
     *
     * @return true if state is valid, false if corrupted
     */
    @Override
    public boolean validateState() {
        // Check parent state first
        if (!super.validateState()) {
            return false;
        }

        // Check sliding state consistency
        if (!isSlidingStateValid()) {
            return false;
        }

        return true;
    }

    /**
     * Returns a string representation of the LightIceBlock for debugging.
     * Safe method - returns formatted string with state information.
     *
     * @return String representation including sliding state
     */
    @Override
    public String toString() {
        return super.toString() +
                ", sliding=" + isSliding +
                ", direction=" + (slidingDirection != null ? slidingDirection : "none");
    }

    /**
     * SECURITY: Gets a detailed summary of the LightIceBlock's state.
     * Safe method that provides read-only comprehensive information.
     *
     * @return Formatted string with complete state details
     */
    @Override
    public String getStateSummary() {
        return super.getStateSummary() +
                String.format("\n  Sliding: %b\n  Direction: %s\n  Status: %s",
                        isSliding,
                        slidingDirection != null ? slidingDirection : "none",
                        getSlidingStatus());
    }

    /**
     * SECURITY: Gets detailed description including sliding information.
     * Extends parent method with LightIceBlock-specific details.
     *
     * @return Detailed string with all information
     */
    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder(super.getDetailedDescription());
        sb.append("\n  Sliding Status: ").append(getSlidingStatus());
        sb.append("\n  Can Cause Stun: YES");
        sb.append("\n  Behavior: Starts sliding when hit, stuns colliding penguin");
        return sb.toString();
    }

    /**
     * SECURITY: Compares two LightIceBlocks for equality.
     * Extends parent equality with sliding state comparison.
     *
     * @param obj The object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof LightIceBlock)) {
            return false;
        }

        LightIceBlock other = (LightIceBlock) obj;

        if (isSliding != other.isSliding) {
            return false;
        }

        // Compare directions (both could be null)
        if (slidingDirection == null) {
            return other.slidingDirection == null;
        }

        return slidingDirection.equals(other.slidingDirection);
    }

    /**
     * SECURITY: Hash code including sliding state.
     * Consistent with equals method.
     *
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (isSliding ? 1 : 0);
        result = 31 * result + (slidingDirection != null ? slidingDirection.hashCode() : 0);
        return result;
    }
}