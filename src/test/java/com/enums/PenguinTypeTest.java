package com.enums;

/**
 * Enum representing the four types of penguins in the game.
 * Each penguin type has unique abilities and characteristics.
 */
public enum PenguinType {
    KING("King Penguin"),                    // Can stop at 5th square
    EMPEROR("Emperor Penguin"),              // Can stop at 3rd square
    ROYAL("Royal Penguin"),                  // Can move one square safely
    ROCKHOPPER("Rockhopper Penguin");        // Can jump over one hazard

    private final String displayName;

    /**
     * Constructor for PenguinType enum.
     *
     * @param displayName The full display name of the penguin type
     */
    PenguinType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the penguin type.
     *
     * @return The full name (e.g., "King Penguin")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns a random PenguinType with equal probability for each type.
     * Used when generating penguins at the start of the game.
     *
     * @return A randomly selected PenguinType
     */
    public static PenguinType getRandomType() {
        PenguinType[] types = PenguinType.values();
        int randomIndex = (int) (Math.random() * types.length);
        return types[randomIndex];
    }

    /**
     * Returns the display name of the penguin type.
     *
     * @return The full penguin type name
     */
    @Override
    public String toString() {
        return displayName;
    }
}