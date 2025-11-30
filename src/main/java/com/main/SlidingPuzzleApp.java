package com.main;

import com.terrain.IcyTerrain;

/**
 * Main application class for the Sliding Penguins Puzzle Game.
 * Entry point for the program - initializes the game by creating an IcyTerrain object.
 *
 * CENG211 - Programming Fundamentals
 * Homework #3
 *
 * This application demonstrates:
 * - Lists and ArrayLists
 * - Interfaces (ITerrainObject, ISlidable, IHazard)
 * - Abstract Classes (Penguin, Hazard)
 * - Inheritance (Penguin types, Hazard types)
 * - Polymorphism (Using interfaces and base classes)
 * - Enumerations (Direction, FoodType, PenguinType)
 */
public class SlidingPuzzleApp {

    /**
     * Main method - entry point of the application.
     * Creates an IcyTerrain object which initializes and runs the game.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize the icy terrain and start the game
        new IcyTerrain();
    }
}