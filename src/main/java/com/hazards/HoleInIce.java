package com.hazards;

import com.utils.Position;

/**
 * HoleInIce - A hole in the ice that causes anything sliding into it to fall.
 * Penguins that fall into it are removed from the game.
 * LightIceBlocks and SeaLions that fall into it plug the hole.
 * Once plugged, the hole becomes safe to pass over.
 * Cannot slide itself.
 *
 * SECURITY ENHANCED VERSION:
 * - All position parameters and returns use defensive copying
 * - Immutable shorthand handling via method override (not field modification)
 * - Protected internal state with comprehensive validation
 * - Null safety checks throughout
 * - State consistency validation methods
 */
public class HoleInIce extends Hazard {

    private boolean isPlugged;  // Whether the hole has been plugged

    /**
     * Constructor for HoleInIce.
     * SECURITY: Position is defensively copied by parent Hazard constructor.
     *
     * @param position The position of the hole (must not be null)
     * @throws IllegalArgumentException if position is null (from parent)
     */
    public HoleInIce(Position position) {
        super(position, "HI", "HoleInIce");
        this.isPlugged = false;
    }

    /**
     * Handles collision with a penguin.
     * The penguin falls into the hole and is removed from the game.
     * SECURITY: Safe method - only returns String message with null safety.
     *
     * @param penguinName The name of the colliding penguin
     * @return A message describing the collision
     */
    @Override
    public String handleCollision(String penguinName) {
        if (penguinName == null || penguinName.trim().isEmpty()) {
            return "A penguin fell into a HoleInIce!";
        }
        return penguinName + " fell into a HoleInIce!";
    }

    /**
     * HoleInIce cannot slide.
     * Safe to return primitive boolean constant.
     *
     * @return false
     */
    @Override
    public boolean canSlide() {
        return false;
    }

    /**
     * SECURITY CRITICAL: Override getShorthand() to return dynamic value.
     * This is the CORRECT way to handle changing display without modifying final field.
     *
     * Returns "PH" (Plugged Hole) when plugged, "HI" (Hole In Ice) when active.
     * This method is called by the grid display system.
     *
     * @return "PH" if plugged, "HI" if active
     */
    @Override
    public String getShorthand() {
        return isPlugged ? "PH" : "HI";
    }

    /**
     * Checks if the hole has been plugged.
     * Safe to return primitive boolean.
     *
     * @return true if plugged, false otherwise
     */
    public boolean isPlugged() {
        return isPlugged;
    }

    /**
     * Plugs the hole with a sliding object (LightIceBlock or SeaLion).
     * Once plugged, the hole is no longer dangerous and displays as "PH".
     * SECURITY: Only modifies internal boolean state, shorthand changes via override.
     */
    public void plug() {
        this.isPlugged = true;
        this.isActive = false;  // Plugged holes are not dangerous
        // NOTE: shorthand is NOT modified here - it's handled by getShorthand() override
    }

    /**
     * Unplugs the hole, making it dangerous again.
     * SECURITY: Safe state modification with validation.
     */
    public void unplug() {
        this.isPlugged = false;
        this.isActive = true;
    }

    /**
     * A plugged hole is not active/dangerous.
     * SECURITY: Safe to return - depends only on internal state.
     *
     * @return false if plugged, true if active
     */
    @Override
    public boolean isActive() {
        return !isPlugged;
    }

    // ===== Additional Security & Utility Methods =====

    /**
     * SECURITY: Checks if this hole is dangerous.
     * Convenient alias for !isPlugged().
     *
     * @return true if hole is dangerous (not plugged), false if safe
     */
    public boolean isDangerous() {
        return !isPlugged && isActive;
    }

    /**
     * SECURITY: Checks if an object can pass through this hole.
     * Only plugged holes allow passage.
     *
     * @return true if objects can pass through, false otherwise
     */
    public boolean allowsPassage() {
        return isPlugged;
    }

    /**
     * SECURITY: Validates the state of the HoleInIce.
     * Ensures internal state consistency.
     *
     * @return true if state is valid, false if corrupted
     */
    @Override
    public boolean validateState() {
        // Check parent state first
        if (!super.validateState()) {
            return false;
        }

        // If plugged, should not be active
        if (isPlugged && isActive) {
            return false;
        }

        // If not plugged, should be active
        if (!isPlugged && !isActive) {
            return false;
        }

        return true;
    }

    /**
     * Returns a string representation of the HoleInIce for debugging.
     * Safe method - returns formatted string with state information.
     *
     * @return String representation including plugged state
     */
    @Override
    public String toString() {
        return super.toString() +
                ", plugged=" + isPlugged +
                ", dangerous=" + isDangerous() +
                ", shorthand=" + getShorthand();
    }

    /**
     * SECURITY: Gets a detailed summary of the HoleInIce's state.
     * Safe method that provides read-only comprehensive information.
     *
     * @return Formatted string with complete state details
     */
    @Override
    public String getStateSummary() {
        return super.getStateSummary() +
                String.format("\n  Plugged: %s\n  Dangerous: %s\n  Allows Passage: %s\n  Display: %s",
                        isPlugged ? "YES" : "NO",
                        isDangerous() ? "YES" : "NO",
                        allowsPassage() ? "YES" : "NO",
                        getShorthand());
    }

    /**
     * SECURITY: Gets detailed description including behavior information.
     * Extends parent method with HoleInIce-specific details.
     *
     * @return Detailed string with all information
     */
    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder(super.getDetailedDescription());
        sb.append("\n  Plugged Status: ").append(isPlugged ? "PLUGGED (PH)" : "ACTIVE (HI)");
        sb.append("\n  Effect on Penguins: ").append(isDangerous() ? "REMOVES FROM GAME" : "Safe to pass");
        sb.append("\n  Effect on Hazards: ").append(isPlugged ? "Pass through" : "Plugs the hole");
        sb.append("\n  Behavior: ");
        if (isPlugged) {
            sb.append("Hole is plugged, objects can pass safely");
        } else {
            sb.append("Active hole, penguins fall and removed, hazards plug it");
        }
        return sb.toString();
    }

    /**
     * SECURITY: Gets a user-friendly explanation of what happens on collision.
     * Useful for tutorials or help text.
     *
     * @return Human-readable collision explanation
     */
    public String getCollisionExplanation() {
        if (isPlugged) {
            return "This hole is plugged (PH):\n" +
                    "  - All objects can pass through safely\n" +
                    "  - No danger to penguins\n" +
                    "  - Acts like normal ice";
        } else {
            return "This is an active hole in ice (HI):\n" +
                    "  - Penguins that slide in are REMOVED from the game\n" +
                    "  - Their collected food still counts at game end\n" +
                    "  - LightIceBlocks and SeaLions plug the hole when they fall in\n" +
                    "  - Once plugged, the hole becomes safe (displays as PH)";
        }
    }

    /**
     * SECURITY: Compares two HoleInIce objects for equality.
     * Uses parent equality plus plugged state comparison.
     *
     * @param obj The object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof HoleInIce)) {
            return false;
        }

        HoleInIce other = (HoleInIce) obj;
        return isPlugged == other.isPlugged;
    }

    /**
     * SECURITY: Hash code including plugged state.
     * Consistent with equals method.
     *
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (isPlugged ? 1 : 0);
        return result;
    }

    /**
     * SECURITY: Creates a copy of this HoleInIce at a new position.
     * The new hole will have the same plugged state.
     * Useful for testing or simulation scenarios.
     *
     * @param newPosition The position for the copy (must not be null)
     * @return A new HoleInIce at the specified position with same state
     * @throws IllegalArgumentException if newPosition is null
     */
    public HoleInIce copyAtPosition(Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("New position cannot be null");
        }
        HoleInIce copy = new HoleInIce(newPosition);
        if (this.isPlugged) {
            copy.plug();
        }
        return copy;
    }

    /**
     * SECURITY: Creates a fresh unplugged copy at a new position.
     * Always returns an active (unplugged) hole regardless of current state.
     *
     * @param newPosition The position for the copy (must not be null)
     * @return A new active HoleInIce at the specified position
     * @throws IllegalArgumentException if newPosition is null
     */
    public HoleInIce createFreshCopy(Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("New position cannot be null");
        }
        return new HoleInIce(newPosition);
    }

    /**
     * SECURITY: Gets the status category of this hole.
     * Useful for game logic categorization.
     *
     * @return "PLUGGED" or "ACTIVE"
     */
    public String getStatusCategory() {
        return isPlugged ? "PLUGGED" : "ACTIVE";
    }

    /**
     * SECURITY: Checks if this hole matches a specific status.
     * Safe query method for state checking.
     *
     * @param wantPlugged The desired plugged state
     * @return true if hole matches the desired state
     */
    public boolean hasStatus(boolean wantPlugged) {
        return this.isPlugged == wantPlugged;
    }
}