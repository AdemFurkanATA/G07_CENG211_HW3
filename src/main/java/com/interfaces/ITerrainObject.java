package com.interfaces;

import com.utils.Position;

/**
 * Interface for all objects that can exist on the icy terrain.
 * This includes penguins, food items, and hazards.
 * Demonstrates the use of interfaces for polymorphism.
 */
public interface ITerrainObject {

    /**
     * Gets the current position of the object on the terrain.
     * @return The Position of the object
     */
    Position getPosition();

    /**
     * Sets the position of the object on the terrain.
     * @param position The new Position
     */
    void setPosition(Position position);

    /**
     * Gets the shorthand notation for displaying the object on the grid.
     * @return The shorthand string
     */
    String getShorthand();

    /**
     * Gets the display name of the object.
     * Used for detailed messages and game information.
     * @return The full name of the object
     */
    String getDisplayName();
}