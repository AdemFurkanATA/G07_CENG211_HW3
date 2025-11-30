package com.interfaces;

/**
 * Interface for all hazards on the icy terrain.
 * This includes LightIceBlock, HeavyIceBlock, SeaLion, and HoleInIce.
 * Extends ITerrainObject to inherit basic terrain object properties.
 */
public interface IHazard extends ITerrainObject {

    /**
     * Checks if this hazard can be moved or slid.
     * LightIceBlock and SeaLion can slide, others cannot.
     *
     * @return true if the hazard can slide, false otherwise
     */
    boolean canSlide();

    /**
     * Handles the interaction when a penguin collides with this hazard.
     * Different hazards have different collision effects:
     * - LightIceBlock: Starts sliding, penguin is stunned
     * - HeavyIceBlock: Stops penguin, penguin loses lightest food
     * - SeaLion: Bounces penguin back, SeaLion starts sliding
     * - HoleInIce: Penguin falls into the hole
     *
     * @param penguinName The name of the colliding penguin (for messages)
     * @return A message describing the collision result
     */
    String handleCollision(String penguinName);

    /**
     * Checks if this hazard is currently active/dangerous.
     * For example, a plugged HoleInIce is not dangerous.
     *
     * @return true if hazard is active, false otherwise
     */
    boolean isActive();
}