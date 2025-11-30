package com.penguins;

import com.enums.PenguinType;
import com.utils.Position;

/**
 * RockhopperPenguin - can jump over one hazard in their path.
 * Special action: Prepare to jump over one hazard while sliding.
 * Can only jump to an empty square. If landing square is occupied, jump fails.
 * Demonstrates inheritance from abstract Penguin class.
 */
public class RockhopperPenguin extends Penguin {

    private boolean preparedToJump;  // Whether the penguin is prepared to jump

    /**
     * Constructor for RockhopperPenguin.
     *
     * @param name The name/identifier (P1, P2, P3)
     * @param position The starting position
     */
    public RockhopperPenguin(String name, Position position) {
        super(name, position, PenguinType.ROCKHOPPER);
        this.preparedToJump = false;
    }

    /**
     * Uses the Rockhopper Penguin's special action.
     * Prepares the penguin to jump over one hazard in their sliding path.
     * The action is automatically used the first time the penguin moves toward a hazard.
     *
     * @return true if action is available to use, false if already used
     */
    @Override
    public boolean useSpecialAction() {
        if (hasUsedSpecialAction) {
            return false;
        }
        hasUsedSpecialAction = true;
        preparedToJump = true;
        return true;
    }

    /**
     * Checks if the penguin is prepared to jump.
     *
     * @return true if prepared to jump, false otherwise
     */
    public boolean isPreparedToJump() {
        return preparedToJump;
    }

    /**
     * Sets the jump preparation state.
     *
     * @param prepared true to prepare for jump
     */
    public void setPreparedToJump(boolean prepared) {
        this.preparedToJump = prepared;
    }

    /**
     * Executes the jump, consuming the prepared jump state.
     * This should be called when the penguin actually performs the jump.
     */
    public void executeJump() {
        this.preparedToJump = false;
    }
}