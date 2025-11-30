package com.hazards;

import com.enums.Direction;
import com.interfaces.ISlidable;
import com.utils.Position;

/**
 * SeaLion - A sea lion that causes penguins to bounce back.
 * When a penguin hits it, the penguin bounces in the opposite direction,
 * and the SeaLion starts sliding in the penguin's original direction.
 * Implements ISlidable because it can slide when hit.
 */
public class SeaLion extends Hazard implements ISlidable {

    private boolean isSliding;           // Whether the sea lion is currently sliding
    private Direction slidingDirection;  // Current sliding direction

    /**
     * Constructor for SeaLion.
     *
     * @param position The position of the sea lion
     */
    public SeaLion(Position position) {
        super(position, "SL", "SeaLion");
        this.isSliding = false;
        this.slidingDirection = null;
    }

    /**
     * Handles collision with a penguin.
     * The penguin bounces back in the opposite direction,
     * and the SeaLion starts sliding in the penguin's original direction.
     *
     * @param penguinName The name of the colliding penguin
     * @return A message describing the collision
     */
    @Override
    public String handleCollision(String penguinName) {
        return penguinName + " bounced off a SeaLion!";
    }

    /**
     * SeaLion can slide when hit.
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