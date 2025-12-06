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
 * MAXIMUM SECURITY VERSION:
 * - All position parameters and returns use defensive copying
 * - Direction enum is immutable (safe to store/return)
 * - Primitive boolean fields (safe)
 * - Comprehensive validation and utility methods
 * - State consistency checks throughout
 * - Protected internal state from external manipulation
 * - Error handling in all critical methods
 * - Rollback mechanism for state transitions
 */
public class LightIceBlock extends Hazard implements ISlidable {

    private boolean isSliding;           // Whether the ice block is currently sliding
    private Direction slidingDirection;  // Current sliding direction (can be null)

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
     * SECURITY: Safe method - only returns String message with null safety.
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
     * Safe to return primitive boolean constant.
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
     * Validates that direction is not null for safety.
     *
     * @param direction The direction to slide (should not be null)
     */
    @Override
    public void slide(Direction direction) {
        if (direction == null) {
            System.err.println("WARNING: LightIceBlock.slide() called with null direction");
            return;
        }
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
     * SECURITY: Clears direction when stopping for consistency.
     *
     * @param sliding true to mark as sliding
     */
    @Override
    public void setSliding(boolean sliding) {
        this.isSliding = sliding;
        // If stopping, also clear direction for consistency
        if (!sliding) {
            this.slidingDirection = null;
        }
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
     * Validates consistency with sliding state.
     *
     * @param direction The Direction to slide (can be null)
     */
    @Override
    public void setSlidingDirection(Direction direction) {
        this.slidingDirection = direction;
        // If direction is being set, mark as sliding for consistency
        if (direction != null && !isSliding) {
            this.isSliding = true;
        }
    }

    // ===== Additional Security & Utility Methods =====

    /**
     * SECURITY: Stops the ice block from sliding.
     * Convenience method that clears both sliding state and direction.
     * Ensures consistent state transition.
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
        // If sliding, should have a direction (strongly recommended)
        if (isSliding && slidingDirection == null) {
            return false;  // Inconsistent state
        }
        // If not sliding, direction should ideally be null (but not critical)
        // We don't fail validation if direction exists but not sliding
        return true;
    }

    /**
     * SECURITY: Checks if the ice block is actively sliding in a specific direction.
     * More specific than just isSliding().
     *
     * @param direction The direction to check (must not be null)
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
            return "Sliding (direction unknown - INVALID STATE)";
        } else {
            return "Stationary";
        }
    }

    /**
     * SECURITY: Checks if the ice block can cause stun.
     * LightIceBlock always causes stun when hit by a penguin.
     *
     * @return true (always causes stun)
     */
    public boolean causesStun() {
        return true;
    }

    /**
     * SECURITY: Gets the stun effect description.
     * Useful for game logic and UI display.
     *
     * @return String describing the stun effect
     */
    public String getStunEffect() {
        return "Colliding penguin is stunned and skips next turn";
    }

    /**
     * SECURITY: Checks if the ice block is movable.
     * LightIceBlock is always movable (that's its main feature).
     *
     * @return true (always movable)
     */
    public boolean isMovable() {
        return true;
    }

    /**
     * SECURITY: Gets the weight category of this block.
     * Useful for comparing with HeavyIceBlock.
     *
     * @return "LIGHT" as a category indicator
     */
    public String getWeightCategory() {
        return "LIGHT";
    }

    /**
     * SECURITY: Checks if this is lighter than a HeavyIceBlock.
     * Useful for game logic comparisons.
     *
     * @return true (always lighter than heavy blocks)
     */
    public boolean isLighterThanHeavy() {
        return true;
    }

    /**
     * SECURITY: Prepares the ice block to start sliding.
     * Sets up the state for sliding in the given direction.
     *
     * @param direction The direction to prepare for (must not be null)
     * @return true if preparation successful, false if invalid direction
     */
    public boolean prepareSlide(Direction direction) {
        if (direction == null) {
            System.err.println("ERROR: Cannot prepare slide with null direction");
            return false;
        }
        this.slidingDirection = direction;
        this.isSliding = true;
        return true;
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

        // LightIceBlock should always be active
        if (!isActive()) {
            return false;
        }

        // Shorthand should be "LB"
        if (!"LB".equals(getShorthand())) {
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
                ", direction=" + (slidingDirection != null ? slidingDirection : "none") +
                ", movable=" + isMovable() +
                ", causesStun=" + causesStun();
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
                String.format("\n  Sliding: %b\n  Direction: %s\n  Status: %s\n  Weight: %s\n  Causes Stun: YES",
                        isSliding,
                        slidingDirection != null ? slidingDirection : "none",
                        getSlidingStatus(),
                        getWeightCategory());
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
        sb.append("\n  Weight Category: ").append(getWeightCategory());
        sb.append("\n  Movable: YES (slides when hit)");
        sb.append("\n  Causes Stun: YES (colliding penguin skips next turn)");
        sb.append("\n  Behavior: Starts sliding when hit, stuns colliding penguin");
        return sb.toString();
    }

    /**
     * SECURITY: Gets a user-friendly explanation of what happens on collision.
     * Useful for tutorials or help text.
     *
     * @return Human-readable collision explanation
     */
    public String getCollisionExplanation() {
        return "When a penguin or sliding hazard collides with a LightIceBlock:\n" +
                "  1. The colliding object STOPS at its current position\n" +
                "  2. The LightIceBlock starts SLIDING in the collision direction\n" +
                "  3. If a penguin collided: penguin is STUNNED (skips next turn)\n" +
                "  4. The sliding LightIceBlock continues until it hits something or falls off\n" +
                "  5. LightIceBlocks can fall from edges and plug HoleInIce";
    }

    /**
     * SECURITY: Gets the interaction type of this hazard.
     * Useful for game logic categorization.
     *
     * @return "SLIDE_AND_STUN" as the interaction type
     */
    public String getInteractionType() {
        return "SLIDE_AND_STUN";
    }

    /**
     * SECURITY: Checks if this hazard blocks movement permanently.
     * LightIceBlock doesn't block permanently (it moves away).
     *
     * @return false (moves when hit, doesn't permanently block)
     */
    public boolean blocksPermanently() {
        return false;
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

    /**
     * SECURITY: Creates a copy of this LightIceBlock at a new position.
     * The new block will be stationary (not sliding).
     * Useful for testing or simulation scenarios.
     *
     * @param newPosition The position for the copy (must not be null)
     * @return A new LightIceBlock at the specified position
     * @throws IllegalArgumentException if newPosition is null
     */
    public LightIceBlock copyAtPosition(Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("New position cannot be null");
        }
        // New LightIceBlock starts stationary
        return new LightIceBlock(newPosition);
    }

    /**
     * SECURITY: Creates a copy with the same sliding state at a new position.
     * Useful for preserving state during complex operations.
     *
     * @param newPosition The position for the copy (must not be null)
     * @return A new LightIceBlock with same sliding state at new position
     * @throws IllegalArgumentException if newPosition is null
     */
    public LightIceBlock copyWithStateAtPosition(Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("New position cannot be null");
        }
        LightIceBlock copy = new LightIceBlock(newPosition);
        copy.isSliding = this.isSliding;
        copy.slidingDirection = this.slidingDirection;  // Safe - enum is immutable
        return copy;
    }

    /**
     * SECURITY: Resets the ice block to initial stationary state.
     * Useful for testing or special game events.
     */
    public void resetToStationary() {
        this.isSliding = false;
        this.slidingDirection = null;
        this.isActive = true;
    }

    /**
     * SECURITY: Checks if the ice block is in a valid operational state.
     * More comprehensive than validateState().
     *
     * @return true if ready for normal operations, false if needs reset
     */
    public boolean isOperational() {
        if (!validateState()) {
            return false;
        }
        if (!isActive()) {
            return false;
        }
        if (isSliding && slidingDirection == null) {
            return false;  // Invalid sliding state
        }
        return true;
    }

    /**
     * SECURITY: Gets a status report for debugging.
     * Combines multiple state checks into one report.
     *
     * @return Formatted status report string
     */
    public String getStatusReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LightIceBlock Status Report ===\n");
        sb.append("Position: ").append(getPosition()).append("\n");
        sb.append("Active: ").append(isActive() ? "YES" : "NO").append("\n");
        sb.append("Sliding: ").append(isSliding ? "YES" : "NO").append("\n");
        sb.append("Direction: ").append(slidingDirection != null ? slidingDirection : "N/A").append("\n");
        sb.append("Weight Category: ").append(getWeightCategory()).append("\n");
        sb.append("Movable: ").append(isMovable() ? "YES" : "NO").append("\n");
        sb.append("Causes Stun: ").append(causesStun() ? "YES" : "NO").append("\n");
        sb.append("State Valid: ").append(validateState() ? "YES" : "NO").append("\n");
        sb.append("Operational: ").append(isOperational() ? "YES" : "NO").append("\n");
        sb.append("Shorthand: ").append(getShorthand()).append("\n");
        return sb.toString();
    }

    /**
     * SECURITY: Performs a safe state transition to sliding.
     * Validates the transition before executing it.
     *
     * @param direction The direction to slide (must not be null)
     * @return true if transition successful, false if invalid
     */
    public boolean transitionToSliding(Direction direction) {
        if (direction == null) {
            System.err.println("ERROR: Cannot transition to sliding with null direction");
            return false;
        }

        // Save old state for potential rollback
        boolean oldSliding = this.isSliding;
        Direction oldDirection = this.slidingDirection;

        // Attempt transition
        this.isSliding = true;
        this.slidingDirection = direction;

        // Validate new state
        if (!validateState()) {
            // Rollback on validation failure
            this.isSliding = oldSliding;
            this.slidingDirection = oldDirection;
            System.err.println("ERROR: State validation failed during transition");
            return false;
        }

        return true;
    }

    /**
     * SECURITY: Performs a safe state transition to stationary.
     * Validates the transition before executing it.
     *
     * @return true if transition successful, false if validation fails
     */
    public boolean transitionToStationary() {
        // Save old state for potential rollback
        boolean oldSliding = this.isSliding;
        Direction oldDirection = this.slidingDirection;

        // Attempt transition
        this.isSliding = false;
        this.slidingDirection = null;

        // Validate new state
        if (!validateState()) {
            // Rollback on validation failure
            this.isSliding = oldSliding;
            this.slidingDirection = oldDirection;
            System.err.println("ERROR: State validation failed during transition");
            return false;
        }

        return true;
    }

    /**
     * SECURITY: Checks if this block can be pushed by the given object type.
     * LightIceBlock can be pushed by penguins and other sliding hazards.
     *
     * @param objectType The type name of the object (e.g., "Penguin", "SeaLion")
     * @return true if object can push this block
     */
    public boolean canBePushedBy(String objectType) {
        if (objectType == null) {
            return false;
        }
        // Can be pushed by penguins and other sliding hazards
        return objectType.contains("Penguin") ||
                objectType.equals("LightIceBlock") ||
                objectType.equals("SeaLion");
    }

    /**
     * SECURITY: Gets the momentum transfer type.
     * Describes how momentum is transferred during collision.
     *
     * @return Description of momentum transfer
     */
    public String getMomentumTransferType() {
        return "ABSORBS_AND_CONTINUES";
    }

    /**
     * SECURITY: Validates that this block can safely slide in a direction.
     * Checks if the direction is valid and state allows sliding.
     *
     * @param direction The direction to validate (must not be null)
     * @return true if can safely slide, false otherwise
     */
    public boolean canSafelySlide(Direction direction) {
        if (direction == null) {
            return false;
        }
        if (!isActive()) {
            return false;
        }
        if (!isOperational()) {
            return false;
        }
        return true;
    }
}