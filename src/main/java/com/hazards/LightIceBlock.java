package com.hazards;

import com.enums.Direction;
import com.interfaces.ISlidable;
import com.utils.Position;

/**
 * LightIceBlock - A light ice block that can slide when hit.
 * When a penguin or sliding hazard collides with it, it starts sliding.
 * The colliding object stops, and the penguin gets stunned (skips next turn).
 * Implements ISlidable because it can slide on ice.
 */
public class LightIceBlock extends Hazard implements ISlidable {

    private boolean isSliding;           // Whether the ice block is currently sliding
    private Direction slidingDirection;  // Current sliding direction (can be null)

    /**
     * Constructor for LightIceBlock.
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
            System.err.println("WARNING: LightIceBlock.slide() called with null direction");
            return;
        }
        this.isSliding = true;
        this.slidingDirection = direction;
    }

    /**
     * Checks if the ice block is currently sliding.
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
     * @return true (always causes stun)
     */
    public boolean causesStun() {
        return true;
    }

    /**
     * @return String describing the stun effect
     */
    public String getStunEffect() {
        return "Colliding penguin is stunned and skips next turn";
    }

    /**
     * @return true (always movable)
     */
    public boolean isMovable() {
        return true;
    }

    /**
     * @return "LIGHT" as a category indicator
     */
    public String getWeightCategory() {
        return "LIGHT";
    }

    /**
     * @return true (always lighter than heavy blocks)
     */
    public boolean isLighterThanHeavy() {
        return true;
    }

    /**
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
     * @return "SLIDE_AND_STUN" as the interaction type
     */
    public String getInteractionType() {
        return "SLIDE_AND_STUN";
    }

    /**
     * @return false (moves when hit, doesn't permanently block)
     */
    public boolean blocksPermanently() {
        return false;
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

    /**
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
     * @return Description of momentum transfer
     */
    public String getMomentumTransferType() {
        return "ABSORBS_AND_CONTINUES";
    }

    /**
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