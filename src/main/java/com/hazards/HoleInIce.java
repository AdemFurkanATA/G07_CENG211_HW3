package com.hazards;

import com.utils.Position;

/**
 * HoleInIce - A hole in the ice that causes anything sliding into it to fall.
 * Penguins that fall into it are removed from the game.
 * LightIceBlocks and SeaLions that fall into it plug the hole.
 * Once plugged, the hole becomes safe to pass over.
 * Cannot slide itself.
 */
public class HoleInIce extends Hazard {

    private boolean isPlugged;  // Whether the hole has been plugged

    /**
     * Constructor for HoleInIce.
     *
     * @param position The position of the hole
     */
    public HoleInIce(Position position) {
        super(position, "HI", "HoleInIce");
        this.isPlugged = false;
    }

    /**
     * Handles collision with a penguin.
     * The penguin falls into the hole and is removed from the game.
     *
     * @param penguinName The name of the colliding penguin
     * @return A message describing the collision
     */
    @Override
    public String handleCollision(String penguinName) {
        return penguinName + " fell into a HoleInIce!";
    }

    /**
     * HoleInIce cannot slide.
     *
     * @return false
     */
    @Override
    public boolean canSlide() {
        return false;
    }

    /**
     * Checks if the hole has been plugged.
     *
     * @return true if plugged, false otherwise
     */
    public boolean isPlugged() {
        return isPlugged;
    }

    /**
     * Plugs the hole with a sliding object.
     * Once plugged, the hole is no longer dangerous.
     */
    public void plug() {
        this.isPlugged = true;
        this.isActive = false;  // Plugged holes are not dangerous
        this.shorthand = "PH";  // Update shorthand to show it's plugged
    }

    /**
     * A plugged hole is not active/dangerous.
     *
     * @return false if plugged, true otherwise
     */
    @Override
    public boolean isActive() {
        return !isPlugged;
    }
}