package com.hazards;

import com.enums.Direction;
import com.interfaces.IHazard;
import com.interfaces.ISlidable;
import com.utils.Position;

/**
 * Abstract base class for all hazards on the icy terrain.
 * Implements IHazard interface and provides common functionality.
 * Some hazards also implement ISlidable (LightIceBlock, SeaLion).
 * Demonstrates inheritance, polymorphism, and abstract classes.
 */
public abstract class Hazard implements IHazard {
    protected Position position;         // Current position on terrain
    protected boolean isActive;          // Whether hazard is currently active/dangerous
    protected String shorthand;          // Shorthand notation for display
    protected String displayName;        // Full name for messages

    /**
     * Constructor for Hazard.
     *
     * @param position The position of the hazard
     * @param shorthand The shorthand notation (e.g., "HB", "LB")
     * @param displayName The full display name
     */
    public Hazard(Position position, String shorthand, String displayName) {
        this.position = position;
        this.shorthand = shorthand;
        this.displayName = displayName;
        this.isActive = true;  // Most hazards start active
    }

    // ===== ITerrainObject Methods =====

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String getShorthand() {
        return shorthand;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    // ===== IHazard Methods =====

    @Override
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the hazard.
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
     * @param penguinName The name of the colliding penguin
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
}