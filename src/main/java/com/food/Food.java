package com.food;

import com.enums.FoodType;
import com.interfaces.ITerrainObject;
import com.utils.Position;

/**
 * Represents a food item on the icy terrain.
 * Food items have a type and weight, and can be collected by penguins.
 * Implements ITerrainObject to be placed on the terrain grid.
 */
public class Food implements ITerrainObject {
    private Position position;
    private FoodType type;
    private int weight;

    /**
     * Constructor for Food.
     *
     * @param position The position of the food on the terrain
     * @param type The type of food
     * @param weight The weight of the food (1-5 units)
     */
    public Food(Position position, FoodType type, int weight) {
        this.position = position;
        this.type = type;
        this.weight = weight;
    }

    /**
     * Creates a random food item at the specified position.
     * Both type and weight are randomly assigned with equal probability.
     *
     * @param position The position where the food will be placed
     * @return A new Food object with random type and weight
     */
    public static Food createRandom(Position position) {
        FoodType randomType = FoodType.getRandomType();
        int randomWeight = (int) (Math.random() * 5) + 1; // Random weight from 1 to 5
        return new Food(position, randomType, randomWeight);
    }

    /**
     * Gets the position of the food.
     *
     * @return The Position of the food
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of the food.
     *
     * @param position The new Position
     */
    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the shorthand notation for displaying on the grid.
     *
     * @return The two-letter shorthand (e.g., "Kr" for Krill)
     */
    @Override
    public String getShorthand() {
        return type.getShorthand();
    }

    /**
     * Gets the display name of the food.
     *
     * @return The full name of the food type
     */
    @Override
    public String getDisplayName() {
        return type.toString();
    }

    /**
     * Gets the type of the food.
     *
     * @return The FoodType
     */
    public FoodType getType() {
        return type;
    }

    /**
     * Gets the weight of the food.
     *
     * @return The weight in units
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Returns a string representation of the food for display purposes.
     * Format: "Type (Weight units)"
     *
     * @return String representation of the food
     */
    @Override
    public String toString() {
        return type.toString() + " (" + weight + " units)";
    }
}