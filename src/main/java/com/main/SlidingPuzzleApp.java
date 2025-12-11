package com.main;

import com.terrain.IcyTerrain;

/**
 * Main application class for the Sliding Penguins Puzzle Game.
 * Entry point for the program - initializes the game by creating an IcyTerrain object.
 */
public class SlidingPuzzleApp {

    /**
     * Main method - entry point of the application.
     * Creates an IcyTerrain object which initializes and runs the game.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize the icy terrain and start the game
        new IcyTerrain();
    }
}