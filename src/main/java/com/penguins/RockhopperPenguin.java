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
     * @param name The name/identifier (P1, P2, P3)
     * @param position The starting position (must not be null)
     * @throws IllegalArgumentException if name or position is null (from parent)
     */
    public RockhopperPenguin(String name, Position position) {
        super(name, position, PenguinType.ROCKHOPPER);
        this.preparedToJump = false;
    }

    /**
     * Uses the Rockhopper Penguin's special action.
     * Prepares the penguin to jump over one hazard in their sliding path.
     * The action is automatically used the first time the penguin moves toward a hazard.
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
     * @return true if prepared to jump, false otherwise
     */
    public boolean isPreparedToJump() {
        return preparedToJump;
    }

    /**
     * Sets the jump preparation state.
     * @param prepared true to prepare for jump
     */
    public void setPreparedToJump(boolean prepared) {
        // Only allow setting to true if special action is used
        if (prepared && !hasUsedSpecialAction) {
            // Invalid state - cannot be prepared without using action
            return;
        }
        this.preparedToJump = prepared;
    }

    /**
     * Executes the jump, consuming the prepared jump state.
     * This should be called when the penguin actually performs the jump.
     */
    public void executeJump() {
        this.preparedToJump = false;
    }

    /**
     * Useful for cases where the jump cannot be performed (e.g., no valid landing).
     */
    public void cancelJump() {
        this.preparedToJump = false;
    }

    /**
     * @return true if state is valid, false if inconsistent
     */
    public boolean isJumpStateValid() {
        // If prepared to jump, special action must be used
        if (preparedToJump && !hasUsedSpecialAction) {
            return false;
        }

        // If special action is used but not prepared, that's okay
        // (action was used but jump was already executed or cancelled)

        return true;
    }

    /**
     * @return true if penguin is in a state where jumping is possible
     */
    public boolean canJump() {
        return preparedToJump && isJumpStateValid();
    }

    /**
     * Returns a string representation of the Rockhopper Penguin for debugging.
     * @return String representation including jump preparation state
     */
    @Override
    public String toString() {
        return super.toString() +
                ", preparedToJump=" + preparedToJump +
                ", canJump=" + canJump();
    }

    /**
     * @return Formatted string with special ability status
     */
    public String getSpecialAbilitySummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rockhopper Penguin Special Ability:\n");
        sb.append("  - Can jump over one hazard\n");
        sb.append("  - Action Status: ");

        if (hasUsedSpecialAction) {
            sb.append("USED");
        } else {
            sb.append("AVAILABLE");
        }

        sb.append("\n  - Jump Prepared: ");
        if (preparedToJump) {
            sb.append("YES (ready to jump)");
        } else {
            sb.append("NO");
        }

        sb.append("\n  - Can Jump Now: ").append(canJump() ? "YES" : "NO");

        return sb.toString();
    }

    /**
     * @return Human-readable string explaining jump status
     */
    public String getJumpStatusExplanation() {
        if (canJump()) {
            return "Penguin is ready to jump over a hazard.";
        }

        if (!hasUsedSpecialAction) {
            return "Special action has not been used yet.";
        }

        if (!preparedToJump) {
            return "Jump was already executed or cancelled.";
        }

        if (!isJumpStateValid()) {
            return "Jump state is inconsistent (possible bug).";
        }

        return "Unknown jump status.";
    }
}