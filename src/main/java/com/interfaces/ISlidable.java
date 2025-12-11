package com.interfaces;

import com.enums.Direction;

/**
 * Interface for objects that can slide on the icy terrain.
 * This includes all penguins, LightIceBlocks, and SeaLions.
 * Demonstrates polymorphism through interface implementation.
 */
public interface ISlidable {

    /**
     * Initiates the sliding motion in a given direction.
     * The object continues sliding until it hits another object, falls off the edge, or is stopped by special conditions.
     * @param direction The direction to slide
     */
    void slide(Direction direction);

    /**
     * Checks if the object is currently in a sliding state.
     * @return true if currently sliding, false otherwise
     */
    boolean isSliding();

    /**
     * Sets the sliding state of the object.
     * @param sliding true to mark as sliding, false otherwise
     */
    void setSliding(boolean sliding);

    /**
     * Gets the current direction of sliding.
     * @return The Direction of current sliding movement, or null if not sliding
     */
    Direction getSlidingDirection();

    /**
     * Sets the direction of sliding.
     * @param direction The Direction to slide
     */
    void setSlidingDirection(Direction direction);
}