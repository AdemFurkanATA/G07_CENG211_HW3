package com.enums;

/**
 * Enum representing the five types of food items available in the game.
 * Each food type has a specific shorthand notation and serves as a collectible item.
 * Utilizes caching for efficient random selection.
 */
public enum FoodType {
    KRILL("Kr"),           // Small crustaceans
    CRUSTACEAN("Cr"),      // Shell creatures
    ANCHOVY("An"),         // Small fish
    SQUID("Sq"),           // Cephalopods
    MACKEREL("Ma");        // Larger fish

    private final String shorthand;

    // Cache values to avoid array cloning overhead on frequent random calls
    private static final FoodType[] VALUES = values();

    /**
     * Constructor for FoodType enum.
     *
     * @param shorthand The two-letter abbreviation for display on the grid
     */
    FoodType(String shorthand) {
        this.shorthand = shorthand;
    }

    /**
     * Gets the shorthand notation for the food type.
     * Used for rendering the map state.
     *
     * @return The two-letter shorthand (e.g., "Kr" for Krill)
     */
    public String getShorthand() {
        return shorthand;
    }

    /**
     * Returns a random FoodType with equal probability for each type.
     * Optimized to use cached values for better performance during generation.
     *
     * @return A randomly selected FoodType
     */
    public static FoodType getRandomType() {
        int randomIndex = (int) (Math.random() * VALUES.length);
        return VALUES[randomIndex];
    }

    /**
     * Returns the formatted full name of the food type.
     *
     * @return The food type name (e.g., "Krill")
     */
    @Override
    public String toString() {
        String name = this.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}