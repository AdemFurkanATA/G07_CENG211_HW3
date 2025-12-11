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
    private final FoodType type;
    private final int weight;

    /**
     * @param position The position of the food on the terrain (must not be null)
     * @param type The type of food (must not be null)
     * @param weight The weight of the food (must be 1-5 units)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Food(Position position, FoodType type, int weight) {
        if (position == null) {
            throw new IllegalArgumentException("Food position cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Food type cannot be null");
        }
        if (weight < 1 || weight > 5) {
            throw new IllegalArgumentException("Food weight must be between 1 and 5, got: " + weight);
        }

        this.position = new Position(position);  // 🔒 DEFENSIVE COPY!
        this.type = type;
        this.weight = weight;
    }

    /**
     * @param position The position where the food will be placed (must not be null)
     * @return A new Food object with random type and weight
     * @throws IllegalArgumentException if position is null
     */
    public static Food createRandom(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Cannot create food at null position");
        }

        FoodType randomType = FoodType.getRandomType();
        int randomWeight = (int) (Math.random() * 5) + 1; // Random weight from 1 to 5

        return new Food(position, randomType, randomWeight);
    }

    /**
     * Creates a food item with specified parameters.
     * @param position The position for the food (must not be null)
     * @param type The food type (must not be null)
     * @param weight The weight (must be 1-5)
     * @return A new Food object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static Food create(Position position, FoodType type, int weight) {
        // All validation happens in constructor
        return new Food(position, type, weight);
    }

    /**
     * Gets the position of the food.
     * @return A new Position object (defensive copy)
     */
    @Override
    public Position getPosition() {
        if (position == null) {
            return null;  // Safety check (should never happen)
        }
        return new Position(position);  // 🔒 DEFENSIVE COPY!
    }

    /**
     * Sets the position of the food.
     * @param position The new Position (must not be null)
     * @throws IllegalArgumentException if position is null
     */
    @Override
    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position = new Position(position);  // 🔒 DEFENSIVE COPY!
    }

    /**
     * Gets the shorthand notation for displaying on the grid.
     * @return The two-letter shorthand (e.g., "Kr" for Krill)
     */
    @Override
    public String getShorthand() {
        return type.getShorthand();
    }

    /**
     * Gets the display name of the food.
     * @return The full name of the food type
     */
    @Override
    public String getDisplayName() {
        return type.toString();
    }

    /**
     * Gets the type of the food.
     * @return The FoodType
     */
    public FoodType getType() {
        return type;
    }

    /**
     * Gets the weight of the food.
     * @return The weight in units (1-5)
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param position The position to check (must not be null)
     * @return true if food is at this position, false otherwise
     * @throws IllegalArgumentException if position is null
     */
    public boolean isAtPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        return this.position.equals(position);
    }

    /**
     * @param type The FoodType to check
     * @return true if this food is of the specified type
     */
    public boolean isType(FoodType type) {
        if (type == null) {
            return false;
        }
        return this.type == type;
    }

    /**
     * @param minWeight Minimum weight (inclusive)
     * @param maxWeight Maximum weight (inclusive)
     * @return true if weight is within range
     */
    public boolean isWeightInRange(int minWeight, int maxWeight) {
        return weight >= minWeight && weight <= maxWeight;
    }

    /**
     * @return true if weight is 1 or 2
     */
    public boolean isLightweight() {
        return weight <= 2;
    }

    /**
     * @return true if weight is 4 or 5
     */
    public boolean isHeavyweight() {
        return weight >= 4;
    }

    /**
     * @return Detailed string with food information
     */
    public String getDetailedDescription() {
        return String.format("%s weighing %d units at position %s",
                type.toString(), weight, position.toString());
    }

    /**
     * Returns a string representation of the food for display purposes.
     * @return String representation of the food
     */
    @Override
    public String toString() {
        return type.toString() + " (" + weight + " units)";
    }

    /**
     * @return true if food state is valid, false if corrupted
     */
    public boolean validateState() {
        if (position == null) {
            return false;
        }
        if (type == null) {
            return false;
        }
        if (weight < 1 || weight > 5) {
            return false;
        }
        return true;
    }

    /**
     * Compares this food with another object for equality.
     * @param obj The object to compare with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Food other = (Food) obj;

        if (weight != other.weight) {
            return false;
        }
        if (type != other.type) {
            return false;
        }

        // Use equals for position comparison (defensive)
        return position != null ? position.equals(other.position) : other.position == null;
    }

    /**
     * Returns a hash code for this food.
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + weight;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }

    /**
     * @param newPosition The position for the copy (must not be null)
     * @return A new Food object with same type and weight, different position
     * @throws IllegalArgumentException if newPosition is null
     */
    public Food copyAtPosition(Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("New position cannot be null");
        }
        return new Food(newPosition, this.type, this.weight);
    }

    /**
     * @param other The other food to compare with
     * @return Negative if this is lighter, positive if heavier, 0 if equal
     */
    public int compareByWeight(Food other) {
        if (other == null) {
            return 1;  // This food is "greater" than null
        }
        return Integer.compare(this.weight, other.weight);
    }

    /**
     * Returns formatted information without exposing mutable objects.
     * @return Formatted string with food details
     */
    public String getStateSummary() {
        return String.format("Food[type=%s, weight=%d, position=%s, valid=%b]",
                type, weight, position, validateState());
    }
}