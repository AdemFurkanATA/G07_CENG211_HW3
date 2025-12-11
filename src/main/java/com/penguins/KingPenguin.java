package com.penguins;

import com.enums.PenguinType;
import com.utils.Position;

/**
 * KingPenguin - can choose to stop at the 5th square while sliding.
 * Special action: Stop sliding at the fifth square reached.
 * Demonstrates inheritance from abstract Penguin class.
 */
public class KingPenguin extends Penguin {

    private static final int STOP_SQUARE = 5;  // Stops at 5th square

    /**
     * Constructor for KingPenguin.
     * @param name The name/identifier (P1, P2, P3)
     * @param position The starting position
     */
    public KingPenguin(String name, Position position) {
        super(name, position, PenguinType.KING);
    }

    /**
     * Uses the King Penguin's special action.
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
     * Gets the square number where this penguin should stop when using special action.
     * @return 5 (the fifth square)
     */
    public int getStopSquare() {
        return STOP_SQUARE;
    }
}