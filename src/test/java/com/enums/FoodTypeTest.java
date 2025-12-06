package com.enums;

/**
 * Enum representing the five types of food items available in the game.
 * Each food type can have a weight between 1-5 units.
 */
public enum FoodType {
    KRILL("Kr"),           // Small crustaceans
    CRUSTACEAN("Cr"),      // Shell creatures
    ANCHOVY("An"),         // Small fish
    SQUID("Sq"),           // Cephalopods
    MACKEREL("Ma");        // Larger fish

    private final String shorthand;

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
     * Used for displaying food on the terrain grid.
     *
     * @return The two-letter shorthand (e.g., "Kr" for Krill)
     */
    public String getShorthand() {
        return shorthand;
    }

    /**
     * Returns a random FoodType with equal probability for each type.
     *
     * @return A randomly selected FoodType
     */
    public static FoodType getRandomType() {
        FoodType[] types = FoodType.values();
        int randomIndex = (int) (Math.random() * types.length);
        return types[randomIndex];
    }

    /**
     * Returns the full name of the food type.
     *
     * @return The food type name (e.g., "Krill")
     */
    @Override
    public String toString() {
        // Capitalize first letter, lowercase the rest
        String name = this.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}