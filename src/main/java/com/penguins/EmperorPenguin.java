package com.penguins;

import com.enums.PenguinType;
import com.utils.Position;

/**
 * EmperorPenguin - can choose to stop at the 3rd square while sliding.
 * Special action: Stop sliding at the third square reached.
 * Demonstrates inheritance from abstract Penguin class.
 */
public class EmperorPenguin extends Penguin {

    private static final int STOP_SQUARE = 3;  // Stops at 3rd square

    /**
     * Constructor for EmperorPenguin.
     * @param name The name/identifier (P1, P2, P3)
     * @param position The starting position
     */
    public EmperorPenguin(String name, Position position) {
        super(name, position, PenguinType.EMPEROR);
    }

    /**
     * Uses the Emperor Penguin's special action.
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
     * @return 3 (the third square)
     */
    public int getStopSquare() {
        return STOP_SQUARE;
    }
}