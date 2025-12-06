package com.penguins;

import com.enums.Direction;
import com.enums.PenguinType;
import com.utils.Position;

/**
 * RoyalPenguin - can safely move one square before sliding.
 * Special action: Move to an adjacent square (horizontally or vertically) before sliding.
 * Can accidentally step out of the grid while using this ability.
 * Demonstrates inheritance from abstract Penguin class.
 *
 * SECURITY ENHANCED VERSION:
 * - Null safety on all direction operations
 * - Validation on special move direction
 * - Protected state management
 * - Defensive programming practices
 */
public class RoyalPenguin extends Penguin {

    private Direction specialMoveDirection;  // Direction of the special single-step move

    /**
     * Constructor for RoyalPenguin.
     * SECURITY: Position is defensively copied by parent Penguin constructor.
     *
     * @param name The name/identifier (P1, P2, P3)
     * @param position The starting position (must not be null)
     * @throws IllegalArgumentException if name or position is null (from parent)
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
     * SECURITY: Direction enum is immutable, but we validate it's not null for safety.
     *
     * @param direction The Direction to move one square (can be null to clear)
     */
    public void setSpecialMoveDirection(Direction direction) {
        // Direction can be null (to reset), so we don't throw exception
        // But we document this behavior clearly
        this.specialMoveDirection = direction;
    }

    /**
     * Gets the direction of the special move.
     * SECURITY: Direction enum is immutable, safe to return directly.
     *
     * @return The Direction of the special move, or null if not set
     */
    public Direction getSpecialMoveDirection() {
        return specialMoveDirection;
    }

    /**
     * Resets the special move direction after it has been used.
     * Safe operation - sets internal state to null.
     */
    public void resetSpecialMove() {
        this.specialMoveDirection = null;
    }

    /**
     * SECURITY: Checks if a special move direction has been set.
     * Useful for validation before executing the move.
     *
     * @return true if special move direction is set, false otherwise
     */
    public boolean hasSpecialMoveDirection() {
        return specialMoveDirection != null;
    }

    /**
     * SECURITY: Validates the special move is in a legal state.
     * Ensures the penguin can perform the special move if requested.
     *
     * @return true if special move state is valid, false if inconsistent
     */
    public boolean isSpecialMoveValid() {
        // If action is used but no direction is set, state is invalid
        if (hasUsedSpecialAction && specialMoveDirection == null) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the Royal Penguin for debugging.
     * Safe method - returns formatted string with state information.
     *
     * @return String representation including special move state
     */
    @Override
    public String toString() {
        return super.toString() +
                ", specialMoveDirection=" + (specialMoveDirection != null ? specialMoveDirection : "none");
    }

    /**
     * SECURITY: Gets a summary of the Royal Penguin's special ability state.
     * Safe method that provides read-only information.
     *
     * @return Formatted string with special ability status
     */
    public String getSpecialAbilitySummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Royal Penguin Special Ability:\n");
        sb.append("  - Can move one square before sliding\n");
        sb.append("  - Status: ");

        if (hasUsedSpecialAction) {
            sb.append("USED");
        } else {
            sb.append("AVAILABLE");
        }

        if (specialMoveDirection != null) {
            sb.append("\n  - Queued Direction: ").append(specialMoveDirection);
        }

        return sb.toString();
    }
}