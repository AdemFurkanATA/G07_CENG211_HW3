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
 * Demonstrates inheritance, polymorphism, and abstract classes.
 *
 * SECURITY ENHANCED VERSION:
 * - All position parameters and returns use defensive copying
 * - Food list is never directly exposed - always returns defensive copies
 * - All getters return safe copies or unmodifiable collections
 * - Comprehensive null safety checks
 * - Protected internal state from external manipulation
 * - Validation on all setter methods
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
     * SECURITY: Position parameter is defensively copied.
     *
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

    // ===== ITerrainObject Methods =====

    /**
     * Gets the position of the penguin.
     * SECURITY: Returns a defensive copy to prevent external modification.
     *
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
     * SECURITY: Position parameter is defensively copied.
     *
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
     * Safe to return String as it's immutable.
     *
     * @return The penguin name (P1, P2, P3)
     */
    @Override
    public String getShorthand() {
        return name;
    }

    /**
     * Gets the display name of the penguin.
     * Safe to return String as it's immutable.
     *
     * @return The full name with type (e.g., "P1 (King Penguin)")
     */
    @Override
    public String getDisplayName() {
        return name + " (" + type.getDisplayName() + ")";
    }

    // ===== ISlidable Methods =====

    /**
     * Initiates sliding in a direction.
     * SECURITY: Direction is an enum (immutable), so safe to use directly.
     *
     * @param direction The direction to slide
     */
    @Override
    public void slide(Direction direction) {
        this.isSliding = true;
        this.slidingDirection = direction;
    }

    /**
     * Checks if the penguin is currently sliding.
     * Safe to return primitive boolean.
     *
     * @return true if currently sliding, false otherwise
     */
    @Override
    public boolean isSliding() {
        return isSliding;
    }

    /**
     * Sets the sliding state.
     * Safe method - only modifies internal boolean.
     *
     * @param sliding true to mark as sliding
     */
    @Override
    public void setSliding(boolean sliding) {
        this.isSliding = sliding;
    }

    /**
     * Gets the current sliding direction.
     * Safe to return Direction enum (immutable).
     *
     * @return The Direction of current sliding, or null if not sliding
     */
    @Override
    public Direction getSlidingDirection() {
        return slidingDirection;
    }

    /**
     * Sets the sliding direction.
     * Safe method - Direction is an enum (immutable).
     *
     * @param direction The Direction to slide
     */
    @Override
    public void setSlidingDirection(Direction direction) {
        this.slidingDirection = direction;
    }

    // ===== Penguin-specific Methods =====

    /**
     * Abstract method for using the penguin's special action.
     * Each penguin type implements this differently.
     *
     * @return true if action was successfully used, false otherwise
     */
    public abstract boolean useSpecialAction();

    /**
     * Gets the name of the penguin.
     * Safe to return String (immutable).
     *
     * @return The penguin name (P1, P2, P3)
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the penguin.
     * Safe to return PenguinType enum (immutable).
     *
     * @return The PenguinType
     */
    public PenguinType getType() {
        return type;
    }

    /**
     * Adds a food item to the penguin's collection.
     * SECURITY: Food is added directly but list itself is never exposed.
     * Note: Food objects are meant to be shared references for game state tracking.
     *
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
     * SECURITY CRITICAL: Returns an unmodifiable defensive copy of the list.
     * This prevents external code from modifying the penguin's food collection.
     *
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
     * Safe method - only reads internal state and returns primitive int.
     *
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
     * SECURITY: Safe method - modifies internal state only.
     *
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
     * Safe to return primitive boolean.
     *
     * @return true if action has been used, false otherwise
     */
    public boolean hasUsedSpecialAction() {
        return hasUsedSpecialAction;
    }

    /**
     * Sets whether the penguin has used their special action.
     * Safe method - only modifies internal boolean.
     *
     * @param used true if action has been used
     */
    public void setHasUsedSpecialAction(boolean used) {
        this.hasUsedSpecialAction = used;
    }

    /**
     * Checks if the penguin is stunned.
     * Safe to return primitive boolean.
     *
     * @return true if stunned, false otherwise
     */
    public boolean isStunned() {
        return isStunned;
    }

    /**
     * Sets the stunned status of the penguin.
     * Safe method - only modifies internal boolean.
     *
     * @param stunned true to stun the penguin
     */
    public void setStunned(boolean stunned) {
        this.isStunned = stunned;
    }

    /**
     * Checks if the penguin has been removed from the game.
     * Safe to return primitive boolean.
     *
     * @return true if removed, false otherwise
     */
    public boolean isRemoved() {
        return isRemoved;
    }

    /**
     * Sets the removed status of the penguin.
     * Safe method - only modifies internal boolean.
     *
     * @param removed true to mark as removed
     */
    public void setRemoved(boolean removed) {
        this.isRemoved = removed;
    }

    /**
     * Checks if this is the player's penguin.
     * Safe to return primitive boolean.
     *
     * @return true if player's penguin, false otherwise
     */
    public boolean isPlayerPenguin() {
        return isPlayerPenguin;
    }

    /**
     * Sets whether this is the player's penguin.
     * Safe method - only modifies internal boolean.
     *
     * @param isPlayer true if this is the player's penguin
     */
    public void setPlayerPenguin(boolean isPlayer) {
        this.isPlayerPenguin = isPlayer;
    }

    /**
     * SECURITY: Gets the number of food items collected.
     * Safe alternative to exposing the entire food list.
     *
     * @return The number of food items collected
     */
    public int getFoodCount() {
        return collectedFood.size();
    }

    /**
     * SECURITY: Checks if the penguin has any food.
     * Safe query method that doesn't expose internal state.
     *
     * @return true if penguin has at least one food item
     */
    public boolean hasFood() {
        return !collectedFood.isEmpty();
    }

    /**
     * SECURITY: Gets a specific food item by index.
     * Returns null if index is out of bounds (safe behavior).
     * This allows controlled access to individual food items without exposing the entire list.
     *
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
     * Safe method - only returns formatted string with safe information.
     *
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
     * SECURITY: Validates the penguin's state integrity.
     * Useful for debugging and ensuring the penguin is in a valid state.
     *
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
     * SECURITY: Creates a summary of the penguin's current state.
     * Safe method that returns only safe, formatted information.
     *
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