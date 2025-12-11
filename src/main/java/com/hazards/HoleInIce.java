package com.hazards;

import com.utils.Position;

/**
 * HoleInIce - A hole in the ice that causes anything sliding into it to fall.
 * Penguins that fall into it are removed from the game.
 * LightIceBlocks and SeaLions that fall into it plug the hole.
 * Once plugged, the hole becomes safe to pass over.
 * Cannot slide itself.
 */
public class HoleInIce extends Hazard {

    private boolean isPlugged;  // Whether the hole has been plugged

    /**
     * Constructor for HoleInIce.
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
     * @return false
     */
    @Override
    public boolean canSlide() {
        return false;
    }

    /**
     * Returns "PH" (Plugged Hole) when plugged, "HI" (Hole In Ice) when active.
     * This method is called by the grid display system.
     * @return "PH" if plugged, "HI" if active
     */
    @Override
    public String getShorthand() {
        return isPlugged ? "PH" : "HI";
    }

    /**
     * Checks if the hole has been plugged.
     * @return true if plugged, false otherwise
     */
    public boolean isPlugged() {
        return isPlugged;
    }

    /**
     * Plugs the hole with a sliding object (LightIceBlock or SeaLion).
     * Once plugged, the hole is no longer dangerous and displays as "PH".
     */
    public void plug() {
        this.isPlugged = true;
        this.isActive = false;  // Plugged holes are not dangerous
        // NOTE: shorthand is NOT modified here - it's handled by getShorthand() override
    }

    /**
     * Unplugs the hole, making it dangerous again.
     */
    public void unplug() {
        this.isPlugged = false;
        this.isActive = true;
    }

    /**
     * A plugged hole is not active/dangerous.
     * @return false if plugged, true if active
     */
    @Override
    public boolean isActive() {
        return !isPlugged;
    }


    /**
     * @return true if hole is dangerous (not plugged), false if safe
     */
    public boolean isDangerous() {
        return !isPlugged && isActive;
    }

    /**
     * @return true if objects can pass through, false otherwise
     */
    public boolean allowsPassage() {
        return isPlugged;
    }

    /**
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
     * @return Detailed string with all information
     */
    @Override
    public String getDetailedDescription() {
        StringBuilder stringBuilder = new StringBuilder(super.getDetailedDescription());
        stringBuilder.append("\n  Plugged Status: ").append(isPlugged ? "PLUGGED (PH)" : "ACTIVE (HI)");
        stringBuilder.append("\n  Effect on Penguins: ").append(isDangerous() ? "REMOVES FROM GAME" : "Safe to pass");
        stringBuilder.append("\n  Effect on Hazards: ").append(isPlugged ? "Pass through" : "Plugs the hole");
        stringBuilder.append("\n  Behavior: ");
        if (isPlugged) {
            stringBuilder.append("Hole is plugged, objects can pass safely");
        } else {
            stringBuilder.append("Active hole, penguins fall and removed, hazards plug it");
        }
        return stringBuilder.toString();
    }

    /**
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
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (isPlugged ? 1 : 0);
        return result;
    }

    /**
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
     * @return "PLUGGED" or "ACTIVE"
     */
    public String getStatusCategory() {
        return isPlugged ? "PLUGGED" : "ACTIVE";
    }

    /**
     * @param wantPlugged The desired plugged state
     * @return true if hole matches the desired state
     */
    public boolean hasStatus(boolean wantPlugged) {
        return this.isPlugged == wantPlugged;
    }
}