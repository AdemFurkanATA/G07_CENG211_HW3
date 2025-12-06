package com.hazards;

import com.interfaces.IHazard;
import com.interfaces.ISlidable;
import com.utils.PositionTest;

/**
 * Abstract base class for all hazards on the icy terrain.
 * Implements IHazard interface and provides common functionality.
 * Some hazards also implement ISlidable (LightIceBlock, SeaLion).
 * Demonstrates inheritance, polymorphism, and abstract classes.
 *
 * SECURITY ENHANCED VERSION:
 * - All position parameters and returns use defensive copying
 * - Protected fields with validation
 * - Null safety checks throughout
 * - State validation methods
 * - No direct exposure of mutable internal state
 */
public abstract class Hazard implements IHazard {
    protected PositionTest position;         // Current position on terrain
    protected boolean isActive;          // Whether hazard is currently active/dangerous
    protected final String shorthand;    // Shorthand notation for display (immutable)
    protected final String displayName;  // Full name for messages (immutable)

    /**
     * Constructor for Hazard.
     * SECURITY: Position parameter is defensively copied.
     * Shorthand and displayName are validated and made final.
     *
     * @param position The position of the hazard (must not be null)
     * @param shorthand The shorthand notation (e.g., "HB", "LB") (must not be null)
     * @param displayName The full display name (must not be null)
     * @throws IllegalArgumentException if any parameter is null or invalid
     */
    public Hazard(PositionTest position, String shorthand, String displayName) {
        if (position == null) {
            throw new IllegalArgumentException("Hazard position cannot be null");
        }
        if (shorthand == null || shorthand.trim().isEmpty()) {
            throw new IllegalArgumentException("Hazard shorthand cannot be null or empty");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Hazard displayName cannot be null or empty");
        }

        this.position = new PositionTest(position);  // 🔒 DEFENSIVE COPY!
        this.shorthand = shorthand;
        this.displayName = displayName;
        this.isActive = true;  // Most hazards start active
    }

    // ===== ITerrainObject Methods =====

    /**
     * Gets the position of the hazard.
     * SECURITY: Returns a defensive copy to prevent external modification.
     *
     * @return A new Position object (defensive copy)
     */
    @Override
    public PositionTest getPosition() {
        if (position == null) {
            return null;  // Safety check (should never happen)
        }
        return new PositionTest(position);  // 🔒 DEFENSIVE COPY!
    }

    /**
     * Sets the position of the hazard.
     * SECURITY: Position parameter is defensively copied.
     *
     * @param position The new Position (must not be null)
     * @throws IllegalArgumentException if position is null
     */
    @Override
    public void setPosition(PositionTest position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position = new PositionTest(position);  // 🔒 DEFENSIVE COPY!
    }

    /**
     * Gets the shorthand notation for displaying on the grid.
     * Safe to return - String is immutable and field is final.
     *
     * @return The shorthand string (e.g., "HB", "LB")
     */
    @Override
    public String getShorthand() {
        return shorthand;
    }

    /**
     * Gets the display name of the hazard.
     * Safe to return - String is immutable and field is final.
     *
     * @return The full display name
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    // ===== IHazard Methods =====

    /**
     * Checks if the hazard is currently active/dangerous.
     * Safe to return - primitive boolean.
     *
     * @return true if hazard is active, false otherwise
     */
    @Override
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the hazard.
     * Safe method - only modifies internal boolean.
     *
     * @param active true if hazard should be active
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Abstract method for handling collisions.
     * Each hazard type implements this differently.
     *
     * @param penguinName The name of the colliding penguin (must not be null)
     * @return A message describing the collision result
     */
    @Override
    public abstract String handleCollision(String penguinName);

    /**
     * Abstract method to check if hazard can slide.
     * LightIceBlock and SeaLion return true, others return false.
     *
     * @return true if hazard can slide, false otherwise
     */
    @Override
    public abstract boolean canSlide();

    // ===== Security & Utility Methods =====

    /**
     * SECURITY: Checks if this hazard is at the specified position.
     * Useful for position-based queries without exposing the position object.
     *
     * @param position The position to check (must not be null)
     * @return true if hazard is at this position, false otherwise
     * @throws IllegalArgumentException if position is null
     */
    public boolean isAtPosition(PositionTest position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        return this.position.equals(position);
    }

    /**
     * SECURITY: Validates the hazard's state integrity.
     * Useful for debugging and ensuring the hazard is in a valid state.
     *
     * @return true if hazard state is valid, false if corrupted
     */
    public boolean validateState() {
        if (position == null) {
            return false;
        }
        if (shorthand == null || shorthand.trim().isEmpty()) {
            return false;
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * SECURITY: Gets a safe summary of the hazard's state.
     * Returns formatted information without exposing mutable objects.
     *
     * @return Formatted string with hazard details
     */
    public String getStateSummary() {
        return String.format("Hazard[type=%s, shorthand=%s, position=%s, active=%b, canSlide=%b]",
                displayName, shorthand, position, isActive, canSlide());
    }

    /**
     * Returns a string representation of the hazard for debugging.
     * Safe method - returns formatted string with safe information.
     *
     * @return String representation of the hazard
     */
    @Override
    public String toString() {
        return displayName + " at " + (position != null ? position.toString() : "null") +
                " (active=" + isActive + ", canSlide=" + canSlide() + ")";
    }

    /**
     * Compares this hazard with another object for equality.
     * Two hazards are equal if they are the same type and at the same position.
     * SECURITY: Safe comparison method.
     *
     * @param obj The object to compare with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Hazard other = (Hazard) obj;

        if (isActive != other.isActive) {
            return false;
        }
        if (!shorthand.equals(other.shorthand)) {
            return false;
        }
        if (!displayName.equals(other.displayName)) {
            return false;
        }

        // Use equals for position comparison (defensive)
        return position != null ? position.equals(other.position) : other.position == null;
    }

    /**
     * Returns a hash code for this hazard.
     * SECURITY: Safe method using immutable or primitively-based fields.
     *
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + shorthand.hashCode();
        result = 31 * result + displayName.hashCode();
        return result;
    }

    /**
     * SECURITY: Activates the hazard.
     * Convenience method with clear naming.
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * SECURITY: Deactivates the hazard.
     * Convenience method with clear naming.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * SECURITY: Toggles the active state.
     * Useful for certain game mechanics.
     */
    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    /**
     * SECURITY: Checks if this is a slidable hazard.
     * Convenience method that calls the abstract canSlide().
     *
     * @return true if this hazard implements ISlidable
     */
    public boolean isSlidable() {
        return this instanceof ISlidable;
    }

    /**
     * SECURITY: Gets the type name of this hazard.
     * Safe method that returns the simple class name.
     *
     * @return The class name (e.g., "LightIceBlock", "SeaLion")
     */
    public String getTypeName() {
        return this.getClass().getSimpleName();
    }

    /**
     * SECURITY: Creates a detailed description of the hazard.
     * Safe method that returns formatted string without exposing mutable state.
     *
     * @return Detailed string with hazard information
     */
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(displayName);
        sb.append(" (").append(shorthand).append(")");
        sb.append("\n  Position: ").append(position);
        sb.append("\n  Status: ").append(isActive ? "ACTIVE" : "INACTIVE");
        sb.append("\n  Can Slide: ").append(canSlide() ? "YES" : "NO");
        sb.append("\n  Type: ").append(getTypeName());
        return sb.toString();
    }
}