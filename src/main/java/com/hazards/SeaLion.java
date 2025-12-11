package com.hazards;

import com.enums.Direction;
import com.interfaces.ISlidable;
import com.utils.Position;

/**
 * SeaLion - A sea lion that causes penguins to bounce back.
 * When a penguin hits it, the penguin bounces in the opposite direction, and the SeaLion starts sliding in the penguin's original direction.
 * Implements ISlidable because it can slide when hit.
 */
public class SeaLion extends Hazard implements ISlidable {

    private boolean isSliding;           // Whether the sea lion is currently sliding
    private Direction slidingDirection;  // Current sliding direction (can be null)

    /**
     * Constructor for SeaLion.
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
     * The penguin bounces back in the opposite direction, and the SeaLion starts sliding in the penguin's original direction.
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
     * @return true
     */
    @Override
    public boolean canSlide() {
        return true;
    }

    /**
     * Initiates sliding in a direction.
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
     * @return true if currently sliding, false otherwise
     */
    @Override
    public boolean isSliding() {
        return isSliding;
    }

    /**
     * Sets the sliding state.
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
     * @return The Direction of current sliding, or null if not sliding
     */
    @Override
    public Direction getSlidingDirection() {
        return slidingDirection;
    }

    /**
     * Sets the sliding direction.
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


    /**
     * Convenience method that clears both sliding state and direction.
     * Ensures consistent state transition.
     */
    public void stopSliding() {
        this.isSliding = false;
        this.slidingDirection = null;
    }

    /**
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
     * @return true if can cause bounce, false if already sliding
     */
    public boolean canBounce() {
        return !isSliding;  // Can only bounce if stationary
    }

    /**
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
     * @return "BOUNCE" as the interaction type
     */
    public String getInteractionType() {
        return "BOUNCE";
    }

    /**
     * @return true (always transfers momentum)
     */
    public boolean transfersMomentum() {
        return true;
    }

    /**
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
     * Useful for testing or special game events.
     */
    public void resetToStationary() {
        this.isSliding = false;
        this.slidingDirection = null;
        this.isActive = true;
    }

    /**
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