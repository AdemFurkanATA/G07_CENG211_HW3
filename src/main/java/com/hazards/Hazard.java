package com.hazards;

import com.enums.Direction;
import com.interfaces.IHazard;
import com.interfaces.ISlidable;
import com.utils.Position;

/**
 * Abstract base class for all hazards on the icy terrain.
 * Implements IHazard interface and provides common functionality.
 * Some hazards also implement ISlidable (LightIceBlock, SeaLion).
 */
public abstract class Hazard implements IHazard {
    protected Position position;         // Current position on terrain
    protected boolean isActive;          // Whether hazard is currently active/dangerous
    protected final String shorthand;    // Shorthand notation for display (immutable)
    protected final String displayName;  // Full name for messages (immutable)

    /**
     * Constructor for Hazard.
     * @param position The position of the hazard (must not be null)
     * @param shorthand The shorthand notation (e.g., "HB", "LB") (must not be null)
     * @param displayName The full display name (must not be null)
     * @throws IllegalArgumentException if any parameter is null or invalid
     */
    public Hazard(Position position, String shorthand, String displayName) {
        if (position == null) {
            throw new IllegalArgumentException("Hazard position cannot be null");
        }
        if (shorthand == null || shorthand.trim().isEmpty()) {
            throw new IllegalArgumentException("Hazard shorthand cannot be null or empty");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Hazard displayName cannot be null or empty");
        }

        this.position = new Position(position);  // 🔒 DEFENSIVE COPY!
        this.shorthand = shorthand;
        this.displayName = displayName;
        this.isActive = true;  // Most hazards start active
    }

    /**
     * Gets the position of the hazard.
     * @return A new Position object (defensive copy)
     */
    @Override
    public Position getPosition() {
        if (position == null) {
            return null;  // Safety check (should never happen)
        }
        return new Position(position);  // 🔒 DEFENSIVE COPY!
    }

    /**
     * Sets the position of the hazard.
     * @param position The new Position (must not be null)
     * @throws IllegalArgumentException if position is null
     */
    @Override
    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position = new Position(position);  // 🔒 DEFENSIVE COPY!
    }

    /**
     * Gets the shorthand notation for displaying on the grid.
     * @return The shorthand string (e.g., "HB", "LB")
     */
    @Override
    public String getShorthand() {
        return shorthand;
    }

    /**
     * Gets the display name of the hazard.
     * @return The full display name
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Checks if the hazard is currently active/dangerous.
     * @return true if hazard is active, false otherwise
     */
    @Override
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the hazard.
     * @param active true if hazard should be active
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Abstract method for handling collisions.
     * Each hazard type implements this differently.
     * @param penguinName The name of the colliding penguin (must not be null)
     * @return A message describing the collision result
     */
    @Override
    public abstract String handleCollision(String penguinName);

    /**
     * Abstract method to check if hazard can slide.
     * LightIceBlock and SeaLion return true, others return false.
     * @return true if hazard can slide, false otherwise
     */
    @Override
    public abstract boolean canSlide();

    /**
     * @param position The position to check (must not be null)
     * @return true if hazard is at this position, false otherwise
     * @throws IllegalArgumentException if position is null
     */
    public boolean isAtPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        return this.position.equals(position);
    }

    /**
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
     * @return Formatted string with hazard details
     */
    public String getStateSummary() {
        return String.format("Hazard[type=%s, shorthand=%s, position=%s, active=%b, canSlide=%b]",
                displayName, shorthand, position, isActive, canSlide());
    }

    /**
     * @return String representation of the hazard
     */
    @Override
    public String toString() {
        return displayName + " at " + (position != null ? position.toString() : "null") +
                " (active=" + isActive + ", canSlide=" + canSlide() + ")";
    }

    /**
     * Compares this hazard with another object for equality.
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
     * Convenience method with clear naming.
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Convenience method with clear naming.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Useful for certain game mechanics.
     */
    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    /**
     * Convenience method that calls the abstract canSlide().
     * @return true if this hazard implements ISlidable
     */
    public boolean isSlidable() {
        return this instanceof ISlidable;
    }

    /**
     * @return The class name (e.g., "LightIceBlock", "SeaLion")
     */
    public String getTypeName() {
        return this.getClass().getSimpleName();
    }

    /**
     * @return Detailed string with hazard information
     */
    public String getDetailedDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(displayName);
        stringBuilder.append(" (").append(shorthand).append(")");
        stringBuilder.append("\n  Position: ").append(position);
        stringBuilder.append("\n  Status: ").append(isActive ? "ACTIVE" : "INACTIVE");
        stringBuilder.append("\n  Can Slide: ").append(canSlide() ? "YES" : "NO");
        stringBuilder.append("\n  Type: ").append(getTypeName());
        return stringBuilder.toString();
    }
}