package com.hazards;

import com.utils.Position;

/**
 * HeavyIceBlock - A heavy ice block that cannot be moved.
 * Anything that collides with it stops immediately.
 * The colliding penguin loses their lightest food item as a penalty.
 * Cannot slide, so does not implement ISlidable.
 */
public class HeavyIceBlock extends Hazard {

    /**
     * Constructor for HeavyIceBlock.
     *
     * @param position The position of the ice block
     */
    public HeavyIceBlock(Position position) {
        super(position, "HB", "HeavyIceBlock");
    }

    /**
     * Handles collision with a penguin.
     * The penguin stops and loses their lightest food item.
     *
     * @param penguinName The name of the colliding penguin
     * @return A message describing the collision
     */
    @Override
    public String handleCollision(String penguinName) {
        return penguinName + " collided with a HeavyIceBlock and lost their lightest food item!";
    }

    /**
     * HeavyIceBlock cannot slide.
     *
     * @return false
     */
    @Override
    public boolean canSlide() {
        return false;
    }
}