package com.enums;

/**
 * Enum representing the four distinct types of penguins in the game.
 * Each type corresponds to specific game mechanics and abilities.
 * Utilizes caching for efficient random generation.
 */
public enum PenguinType {
    KING("King Penguin"),                    // Can stop at 5th square
    EMPEROR("Emperor Penguin"),              // Can stop at 3rd square
    ROYAL("Royal Penguin"),                  // Can move one square safely
    ROCKHOPPER("Rockhopper Penguin");        // Can jump over one hazard

    private final String displayName;

    // Cache values to avoid array cloning overhead
    private static final PenguinType[] VALUES = values();

    /**
     * Constructor for PenguinType enum.
     *
     * @param displayName The full display name of the penguin type
     */
    PenguinType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the user-friendly display name of the penguin type.
     *
     * @return The full name (e.g., "King Penguin")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns a random PenguinType with equal probability for each type.
     * Used for initializing the game state.
     *
     * @return A randomly selected PenguinType
     */
    public static PenguinType getRandomType() {
        int randomIndex = (int) (Math.random() * VALUES.length);
        return VALUES[randomIndex];
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