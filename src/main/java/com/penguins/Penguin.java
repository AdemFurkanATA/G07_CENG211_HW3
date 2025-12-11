package com.penguins;

import com.enums.Direction;
import com.enums.PenguinType;
import com.food.Food;
import com.interfaces.ITerrainObject;
import com.interfaces.ISlidable;
import com.utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for all penguin types.
 * Implements ITerrainObject and ISlidable interfaces.
 */
public abstract class Penguin implements ITerrainObject, ISlidable {
    protected String name;                      // Penguin identifier (P1, P2, P3)
    protected Position position;                 // Current position on terrain
    protected PenguinType type;                  // Type of penguin
    protected List<Food> collectedFood;          // Food items collected
    protected boolean hasUsedSpecialAction;      // Whether special action has been used
    protected boolean isStunned;                 // Whether penguin is stunned (skips turn)
    protected boolean isRemoved;                 // Whether penguin has been removed from game
    protected boolean isSliding;                 // Whether penguin is currently sliding
    protected Direction slidingDirection;        // Current sliding direction
    protected boolean isPlayerPenguin;           // Whether this is the player's penguin

    /**
     * Constructor for Penguin.
     * @param name The name/identifier of the penguin (P1, P2, P3)
     * @param position The starting position (must not be null)
     * @param type The type of penguin (must not be null)
     * @throws IllegalArgumentException if any parameter is null or name is empty
     */
    public Penguin(String name, Position position, PenguinType type) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Penguin name cannot be null or empty");
        }
        if (position == null) {
            throw new IllegalArgumentException("Penguin position cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Penguin type cannot be null");
        }

        this.name = name;
        this.position = new Position(position);  // 🔒 DEFENSIVE COPY!
        this.type = type;
        this.collectedFood = new ArrayList<>();
        this.hasUsedSpecialAction = false;
        this.isStunned = false;
        this.isRemoved = false;
        this.isSliding = false;
        this.slidingDirection = null;
        this.isPlayerPenguin = false;
    }


    /**
     * Gets the position of the penguin.
     * @return A new Position object (defensive copy)
     */
    @Override
    public Position getPosition() {
        if (position == null) {
            return null;  // Safety check
        }
        return new Position(position);  // 🔒 DEFENSIVE COPY!
    }

    /**
     * Sets the position of the penguin.
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
     * @return The penguin name (P1, P2, P3)
     */
    @Override
    public String getShorthand() {
        return name;
    }

    /**
     * Gets the display name of the penguin.
     * @return The full name with type (e.g., "P1 (King Penguin)")
     */
    @Override
    public String getDisplayName() {
        return name + " (" + type.getDisplayName() + ")";
    }

    /**
     * Initiates sliding in a direction.
     * @param direction The direction to slide
     */
    @Override
    public void slide(Direction direction) {
        this.isSliding = true;
        this.slidingDirection = direction;
    }

    /**
     * Checks if the penguin is currently sliding.
     * @return true if currently sliding, false otherwise
     */
    @Override
    public boolean isSliding() {
        return isSliding;
    }

    /**
     * Sets the sliding state.
     * @param sliding true to mark as sliding
     */
    @Override
    public void setSliding(boolean sliding) {
        this.isSliding = sliding;
    }

    /**
     * Gets the current sliding direction.
     * @return The Direction of current sliding, or null if not sliding
     */
    @Override
    public Direction getSlidingDirection() {
        return slidingDirection;
    }

    /**
     * Sets the sliding direction.
     * @param direction The Direction to slide
     */
    @Override
    public void setSlidingDirection(Direction direction) {
        this.slidingDirection = direction;
    }


    /**
     * Abstract method for using the penguin's special action.
     * Each penguin type implements this differently.
     * @return true if action was successfully used, false otherwise
     */
    public abstract boolean useSpecialAction();

    /**
     * Gets the name of the penguin.
     * @return The penguin name (P1, P2, P3)
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the penguin.
     * @return The PenguinType
     */
    public PenguinType getType() {
        return type;
    }

    /**
     * Adds a food item to the penguin's collection.
     * @param food The Food to add (must not be null)
     * @throws IllegalArgumentException if food is null
     */
    public void collectFood(Food food) {
        if (food == null) {
            throw new IllegalArgumentException("Cannot collect null food");
        }
        collectedFood.add(food);
    }

    /**
     * Gets all collected food items.
     * @return Unmodifiable list of collected Food (defensive copy)
     */
    public List<Food> getCollectedFood() {
        // Create a new ArrayList (defensive copy of the list structure)
        // Food objects themselves are same references (intentional for game state)
        // but the List cannot be modified externally
        return Collections.unmodifiableList(new ArrayList<>(collectedFood));  // 🔒 DEFENSIVE COPY + UNMODIFIABLE!
    }

    /**
     * Calculates the total weight of collected food.
     * @return Total weight in units
     */
    public int getTotalFoodWeight() {
        int total = 0;
        for (Food food : collectedFood) {
            if (food != null) {  // Null safety
                total += food.getWeight();
            }
        }
        return total;
    }

    /**
     * Removes the lightest food item from the penguin's collection.
     * Used as penalty when hitting HeavyIceBlock.
     * @return The removed Food, or null if no food to remove
     */
    public Food removeLightestFood() {
        if (collectedFood.isEmpty()) {
            return null;
        }

        Food lightest = collectedFood.get(0);
        for (Food food : collectedFood) {
            if (food != null && lightest != null && food.getWeight() < lightest.getWeight()) {
                lightest = food;
            }
        }

        collectedFood.remove(lightest);
        return lightest;
    }

    /**
     * Checks if the penguin has used their special action.
     * @return true if action has been used, false otherwise
     */
    public boolean hasUsedSpecialAction() {
        return hasUsedSpecialAction;
    }

    /**
     * Sets whether the penguin has used their special action.
     * @param used true if action has been used
     */
    public void setHasUsedSpecialAction(boolean used) {
        this.hasUsedSpecialAction = used;
    }

    /**
     * Checks if the penguin is stunned.
     * @return true if stunned, false otherwise
     */
    public boolean isStunned() {
        return isStunned;
    }

    /**
     * @param stunned true to stun the penguin
     */
    public void setStunned(boolean stunned) {
        this.isStunned = stunned;
    }

    /**
     * @return true if removed, false otherwise
     */
    public boolean isRemoved() {
        return isRemoved;
    }

    /**
     * @param removed true to mark as removed
     */
    public void setRemoved(boolean removed) {
        this.isRemoved = removed;
    }

    /**
     * Checks if this is the player's penguin.
     * @return true if player's penguin, false otherwise
     */
    public boolean isPlayerPenguin() {
        return isPlayerPenguin;
    }

    /**
     * @param isPlayer true if this is the player's penguin
     */
    public void setPlayerPenguin(boolean isPlayer) {
        this.isPlayerPenguin = isPlayer;
    }

    /**
     * @return The number of food items collected
     */
    public int getFoodCount() {
        return collectedFood.size();
    }

    /**
     * @return true if penguin has at least one food item
     */
    public boolean hasFood() {
        return !collectedFood.isEmpty();
    }

    /**
     * @param index The index of the food item
     * @return The Food at that index, or null if index is invalid
     */
    public Food getFoodAt(int index) {
        if (index < 0 || index >= collectedFood.size()) {
            return null;
        }
        return collectedFood.get(index);
    }

    /**
     * Returns a string representation of the penguin for debugging.
     * @return String representation of the penguin
     */
    @Override
    public String toString() {
        return "Penguin{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", position=" + (position != null ? position.toString() : "null") +
                ", foodCount=" + collectedFood.size() +
                ", totalWeight=" + getTotalFoodWeight() +
                ", isRemoved=" + isRemoved +
                ", isStunned=" + isStunned +
                ", isPlayer=" + isPlayerPenguin +
                '}';
    }

    /**
     * @return true if penguin state is valid, false if corrupted
     */
    public boolean validateState() {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (position == null) {
            return false;
        }
        if (type == null) {
            return false;
        }
        if (collectedFood == null) {
            return false;
        }
        return true;
    }

    /**
     * @return A formatted string with penguin statistics
     */
    public String getStateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(type.getDisplayName()).append(")");
        sb.append("\n  Position: ").append(position);
        sb.append("\n  Food Items: ").append(collectedFood.size());
        sb.append("\n  Total Weight: ").append(getTotalFoodWeight()).append(" units");
        sb.append("\n  Status: ");

        if (isRemoved) {
            sb.append("REMOVED");
        } else if (isStunned) {
            sb.append("STUNNED");
        } else {
            sb.append("Active");
        }

        if (isPlayerPenguin) {
            sb.append(" (YOUR PENGUIN)");
        }

        return sb.toString();
    }
}