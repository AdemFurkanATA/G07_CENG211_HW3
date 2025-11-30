package com.penguins;

import com.enums.Direction;
import com.enums.PenguinType;
import com.food.Food;
import com.interfaces.ITerrainObject;
import com.interfaces.ISlidable;
import com.utils.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all penguin types.
 * Implements ITerrainObject and ISlidable interfaces.
 * Demonstrates inheritance, polymorphism, and abstract classes.
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
     *
     * @param name The name/identifier of the penguin (P1, P2, P3)
     * @param position The starting position
     * @param type The type of penguin
     */
    public Penguin(String name, Position position, PenguinType type) {
        this.name = name;
        this.position = position;
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

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String getShorthand() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return name + " (" + type.getDisplayName() + ")";
    }

    // ===== ISlidable Methods =====

    @Override
    public void slide(Direction direction) {
        this.isSliding = true;
        this.slidingDirection = direction;
    }

    @Override
    public boolean isSliding() {
        return isSliding;
    }

    @Override
    public void setSliding(boolean sliding) {
        this.isSliding = sliding;
    }

    @Override
    public Direction getSlidingDirection() {
        return slidingDirection;
    }

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
     *
     * @return The penguin name (P1, P2, P3)
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the penguin.
     *
     * @return The PenguinType
     */
    public PenguinType getType() {
        return type;
    }

    /**
     * Adds a food item to the penguin's collection.
     *
     * @param food The Food to add
     */
    public void collectFood(Food food) {
        collectedFood.add(food);
    }

    /**
     * Gets all collected food items.
     *
     * @return List of collected Food
     */
    public List<Food> getCollectedFood() {
        return collectedFood;
    }

    /**
     * Calculates the total weight of collected food.
     *
     * @return Total weight in units
     */
    public int getTotalFoodWeight() {
        int total = 0;
        for (Food food : collectedFood) {
            total += food.getWeight();
        }
        return total;
    }

    /**
     * Removes the lightest food item from the penguin's collection.
     * Used as penalty when hitting HeavyIceBlock.
     *
     * @return The removed Food, or null if no food to remove
     */
    public Food removeLightestFood() {
        if (collectedFood.isEmpty()) {
            return null;
        }

        Food lightest = collectedFood.get(0);
        for (Food food : collectedFood) {
            if (food.getWeight() < lightest.getWeight()) {
                lightest = food;
            }
        }

        collectedFood.remove(lightest);
        return lightest;
    }

    /**
     * Checks if the penguin has used their special action.
     *
     * @return true if action has been used, false otherwise
     */
    public boolean hasUsedSpecialAction() {
        return hasUsedSpecialAction;
    }

    /**
     * Sets whether the penguin has used their special action.
     *
     * @param used true if action has been used
     */
    public void setHasUsedSpecialAction(boolean used) {
        this.hasUsedSpecialAction = used;
    }

    /**
     * Checks if the penguin is stunned.
     *
     * @return true if stunned, false otherwise
     */
    public boolean isStunned() {
        return isStunned;
    }

    /**
     * Sets the stunned status of the penguin.
     *
     * @param stunned true to stun the penguin
     */
    public void setStunned(boolean stunned) {
        this.isStunned = stunned;
    }

    /**
     * Checks if the penguin has been removed from the game.
     *
     * @return true if removed, false otherwise
     */
    public boolean isRemoved() {
        return isRemoved;
    }

    /**
     * Sets the removed status of the penguin.
     *
     * @param removed true to mark as removed
     */
    public void setRemoved(boolean removed) {
        this.isRemoved = removed;
    }

    /**
     * Checks if this is the player's penguin.
     *
     * @return true if player's penguin, false otherwise
     */
    public boolean isPlayerPenguin() {
        return isPlayerPenguin;
    }

    /**
     * Sets whether this is the player's penguin.
     *
     * @param isPlayer true if this is the player's penguin
     */
    public void setPlayerPenguin(boolean isPlayer) {
        this.isPlayerPenguin = isPlayer;
    }
}