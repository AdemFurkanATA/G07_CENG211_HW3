package com.penguins;

import com.enums.Direction;
import com.enums.PenguinType;
import com.utils.Position;

/**
 * RoyalPenguin - can safely move one square before sliding.
 * Special action: Move to an adjacent square (horizontally or vertically) before sliding.
 * Can accidentally step out of the grid while using this ability.
 * Demonstrates inheritance from abstract Penguin class.
 */
public class RoyalPenguin extends Penguin {

    private Direction specialMoveDirection;  // Direction of the special single-step move

    /**
     * Constructor for RoyalPenguin.
     *
     * @param name The name/identifier (P1, P2, P3)
     * @param position The starting position
     */
    public RoyalPenguin(String name, Position position) {
        super(name, position, PenguinType.ROYAL);
        this.specialMoveDirection = null;
    }

    /**
     * Uses the Royal Penguin's special action.
     * Allows the penguin to move one square in any direction before sliding.
     * The penguin can accidentally fall into water or hazards during this move.
     *
     * @return true if action is available to use, false if already used
     */
    @Override
    public boolean useSpecialAction() {
        if (hasUsedSpecialAction) {
            return false;
        }
        hasUsedSpecialAction = true;
        return true;
    }

    /**
     * Sets the direction for the special single-step move.
     *
     * @param direction The Direction to move one square
     */
    public void setSpecialMoveDirection(Direction direction) {
        this.specialMoveDirection = direction;
    }

    /**
     * Gets the direction of the special move.
     *
     * @return The Direction of the special move, or null if not set
     */
    public Direction getSpecialMoveDirection() {
        return specialMoveDirection;
    }

    /**
     * Resets the special move direction after it has been used.
     */
    public void resetSpecialMove() {
        this.specialMoveDirection = null;
    }
}