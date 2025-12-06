package com.hazards;

import com.enums.Direction;
import com.interfaces.ISlidable;
import com.utils.Position;

/**
 * SeaLion - A sea lion that causes penguins to bounce back.
 * When a penguin hits it, the penguin bounces in the opposite direction,
 * and the SeaLion starts sliding in the penguin's original direction.
 * Implements ISlidable because it can slide when hit.
 *
 * MAXIMUM SECURITY VERSION:
 * - All position parameters and returns use defensive copying
 * - Direction enum is immutable (safe to store/return)
 * - Primitive boolean fields (safe)
 * - Comprehensive validation and utility methods
 * - State consistency checks throughout
 * - Protected internal state from external manipulation
 * - Error handling in all critical methods
 */
public class SeaLion extends Hazard implements ISlidable {

    private boolean isSliding;           // Whether the sea lion is currently sliding
    private Direction slidingDirection;  // Current sliding direction (can be null)

    /**
     * Constructor for SeaLion.
     * SECURITY: Position is defensively copied by parent Hazard constructor.
     *
     * @param position The position of the sea lion (must not be null)
     * @throws IllegalArgumentException if position is null (from parent)
     */
    public SeaLion(Position position) {
        super(position, "SL", "SeaLion");
        this.isSliding = false;
        this.slidingDirection = null;
    }

    /**
     * Handles collision with a penguin.
     * The penguin bounces back in the opposite direction,
     * and the SeaLion starts sliding in the penguin's original direction.
     * SECURITY: Safe method - only returns String message with null safety.
     *
     * @param penguinName The name of the colliding penguin
     * @return A message describing the collision
     */
    @Override
    public String handleCollision(String penguinName) {
        if (penguinName == null || penguinName.trim().isEmpty()) {
            return "A penguin bounced off a SeaLion!";
        }
        return penguinName + " bounced off a SeaLion!";
    }

    /**
     * SeaLion can slide when hit.
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
            System.err.println("WARNING: SeaLion.slide() called with null direction");
            return;
        }
        this.isSliding = true;
        this.slidingDirection = direction;
    }

    /**
     * Checks if the sea lion is currently sliding.
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
     * SECURITY: Stops the sea lion from sliding.
     * Convenience method that clears both sliding state and direction.
     * Ensures consistent state transition.
     */
    public void stopSliding() {
        this.isSliding = false;
        this.slidingDirection = null;
    }

    /**
     * SECURITY: Validates the sliding state consistency.
     * Ensures the sea lion's sliding-related state makes sense.
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
     * SECURITY: Checks if the sea lion is actively sliding in a specific direction.
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
     * SECURITY: Checks if the sea lion can bounce a penguin.
     * SeaLion always causes bouncing when not already sliding.
     *
     * @return true if can cause bounce, false if already sliding
     */
    public boolean canBounce() {
        return !isSliding;  // Can only bounce if stationary
    }

    /**
     * SECURITY: Prepares the sea lion to start sliding after a bounce.
     * Sets up the state for sliding in the given direction.
     *
     * @param direction The direction to prepare for (must not be null)
     * @return true if preparation successful, false if invalid direction
     */
    public boolean prepareSlide(Direction direction) {
        if (direction == null) {
            return false;
        }
        this.slidingDirection = direction;
        this.isSliding = true;
        return true;
    }

    /**
     * SECURITY: Validates the entire state of the SeaLion.
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

        // SeaLion should always be active (slidable hazards don't deactivate)
        if (!isActive()) {
            return false;
        }

        return true;
    }

    /**
     * Returns a string representation of the SeaLion for debugging.
     * Safe method - returns formatted string with state information.
     *
     * @return String representation including sliding state
     */
    @Override
    public String toString() {
        return super.toString() +
                ", sliding=" + isSliding +
                ", direction=" + (slidingDirection != null ? slidingDirection : "none") +
                ", canBounce=" + canBounce();
    }

    /**
     * SECURITY: Gets a detailed summary of the SeaLion's state.
     * Safe method that provides read-only comprehensive information.
     *
     * @return Formatted string with complete state details
     */
    @Override
    public String getStateSummary() {
        return super.getStateSummary() +
                String.format("\n  Sliding: %b\n  Direction: %s\n  Status: %s\n  Can Bounce: %s",
                        isSliding,
                        slidingDirection != null ? slidingDirection : "none",
                        getSlidingStatus(),
                        canBounce() ? "YES" : "NO");
    }

    /**
     * SECURITY: Gets detailed description including sliding and bounce information.
     * Extends parent method with SeaLion-specific details.
     *
     * @return Detailed string with all information
     */
    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder(super.getDetailedDescription());
        sb.append("\n  Sliding Status: ").append(getSlidingStatus());
        sb.append("\n  Can Bounce Penguins: ").append(canBounce() ? "YES" : "NO (currently sliding)");
        sb.append("\n  Behavior: Causes penguins to bounce in opposite direction");
        sb.append("\n  When Hit: Starts sliding in penguin's original direction");
        return sb.toString();
    }

    /**
     * SECURITY: Gets a user-friendly explanation of what happens on collision.
     * Useful for tutorials or help text.
     *
     * @return Human-readable collision explanation
     */
    public String getCollisionExplanation() {
        return "When a penguin collides with a SeaLion:\n" +
                "  1. The penguin bounces back in the OPPOSITE direction\n" +
                "  2. The penguin continues sliding in that opposite direction\n" +
                "  3. The SeaLion starts sliding in the penguin's ORIGINAL direction\n" +
                "  4. Both the penguin and SeaLion can fall off edges or hit other objects\n" +
                "  5. The collision transfers momentum from penguin to SeaLion";
    }

    /**
     * SECURITY: Gets the bounce behavior type.
     * Useful for game logic categorization.
     *
     * @return "BOUNCE" as the interaction type
     */
    public String getInteractionType() {
        return "BOUNCE";
    }

    /**
     * SECURITY: Checks if this is a momentum-transferring hazard.
     * SeaLion always transfers momentum.
     *
     * @return true (always transfers momentum)
     */
    public boolean transfersMomentum() {
        return true;
    }

    /**
     * SECURITY: Compares two SeaLions for equality.
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

        if (!(obj instanceof SeaLion)) {
            return false;
        }

        SeaLion other = (SeaLion) obj;

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
     * SECURITY: Creates a copy of this SeaLion at a new position.
     * The new SeaLion will be stationary (not sliding).
     * Useful for testing or simulation scenarios.
     *
     * @param newPosition The position for the copy (must not be null)
     * @return A new SeaLion at the specified position
     * @throws IllegalArgumentException if newPosition is null
     */
    public SeaLion copyAtPosition(Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("New position cannot be null");
        }
        // New SeaLion starts stationary
        return new SeaLion(newPosition);
    }

    /**
     * SECURITY: Creates a copy with the same sliding state at a new position.
     * Useful for preserving state during complex operations.
     *
     * @param newPosition The position for the copy (must not be null)
     * @return A new SeaLion with same sliding state at new position
     * @throws IllegalArgumentException if newPosition is null
     */
    public SeaLion copyWithStateAtPosition(Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("New position cannot be null");
        }
        SeaLion copy = new SeaLion(newPosition);
        copy.isSliding = this.isSliding;
        copy.slidingDirection = this.slidingDirection;  // Safe - enum is immutable
        return copy;
    }

    /**
     * SECURITY: Resets the SeaLion to initial stationary state.
     * Useful for testing or special game events.
     */
    public void resetToStationary() {
        this.isSliding = false;
        this.slidingDirection = null;
        this.isActive = true;
    }

    /**
     * SECURITY: Checks if the SeaLion is in a valid operational state.
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
        sb.append("=== SeaLion Status Report ===\n");
        sb.append("Position: ").append(getPosition()).append("\n");
        sb.append("Active: ").append(isActive() ? "YES" : "NO").append("\n");
        sb.append("Sliding: ").append(isSliding ? "YES" : "NO").append("\n");
        sb.append("Direction: ").append(slidingDirection != null ? slidingDirection : "N/A").append("\n");
        sb.append("Can Bounce: ").append(canBounce() ? "YES" : "NO").append("\n");
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
}