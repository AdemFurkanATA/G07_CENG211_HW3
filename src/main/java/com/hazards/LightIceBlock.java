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
    private Direction slidingDirection;  // Current sliding direction

    /**
     * Constructor for LightIceBlock.
     *
     * @param position The position of the ice block
     */
    public LightIceBlock(Position position) {
        super(position, "LB", "LightIceBlock");
        this.isSliding = false;
        this.slidingDirection = null;
    }

    /**
     * Handles collision with a penguin.
     * The ice block starts sliding, and the penguin is stunned.
     *
     * @param penguinName The name of the colliding penguin
     * @return A message describing the collision
     */
    @Override
    public String handleCollision(String penguinName) {
        return penguinName + " collided with a LightIceBlock and is stunned!";
    }

    /**
     * LightIceBlock can slide on ice.
     *
     * @return true
     */
    @Override
    public boolean canSlide() {
        return true;
    }

    // ===== ISlidable Methods =====

    @Override
    public void slide(Direction direction) {
        this.isSliding = true;
        this.slidingDirection = direction;
    }

    @Override
    public boolean isSliding() {
        return isSliding;
    }

    @Override
    public void setSliding(boolean sliding) {
        this.isSliding = sliding;
    }

    @Override
    public Direction getSlidingDirection() {
        return slidingDirection;
    }

    @Override
    public void setSlidingDirection(Direction direction) {
        this.slidingDirection = direction;
    }
}