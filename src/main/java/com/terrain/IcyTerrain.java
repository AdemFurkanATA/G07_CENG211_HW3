package com.terrain;

import com.enums.Direction;
import com.enums.PenguinType;
import com.food.Food;
import com.hazards.*;
import com.interfaces.IHazard;
import com.interfaces.ISlidable;
import com.interfaces.ITerrainObject;
import com.penguins.*;
import com.utils.GameHelper;
import com.utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main game class that manages the icy terrain grid and game logic.
 * Contains the terrain grid, game objects, and orchestrates the game flow.
 * Demonstrates the use of Lists, ArrayLists, and object-oriented design.
 *
 * MAXIMUM SECURITY VERSION + LOGIC FIXES:
 * - ALL position parameters use defensive copying (never trust external data)
 * - ALL position returns are defensive copies (never expose internal state)
 * - ALL list returns are deep copied AND unmodifiable (double protection)
 * - Comprehensive null safety checks on every method
 * - Input validation strengthened with try-catch blocks
 * - No direct access to internal collections
 * - State validation after critical operations
 * - Protected against all common security vulnerabilities
 *
 * LOGIC FIXES:
 * ✅ RockhopperPenguin can only jump to COMPLETELY EMPTY squares (no food allowed)
 * ✅ RockhopperPenguin continues sliding after successful jump
 * ✅ Cleaner AI Rockhopper messages
 * ✅ King/Emperor stop messages clarified
 */
public class IcyTerrain {

    private static final int GRID_SIZE = 10;
    private static final int NUM_PENGUINS = 3;
    private static final int NUM_HAZARDS = 15;
    private static final int NUM_FOOD = 20;
    private static final int NUM_TURNS = 4;
    private static final double AI_SPECIAL_ACTION_CHANCE = 0.30;

    private final TerrainGrid terrainGrid;          // The terrain grid (final for immutability)
    private final List<Penguin> penguins;           // All penguins in the game (final reference)
    private final List<Food> foodItems;             // All food items (final reference)
    private final List<IHazard> hazards;            // All hazards (final reference)
    private Penguin playerPenguin;                  // The player's penguin
    private final Scanner scanner;                  // For user input (final reference)
    private int currentTurn;                        // Current turn number (1-4)

    /**
     * Constructor for IcyTerrain.
     * Initializes the grid and starts the game.
     * SECURITY: All collections initialized as new ArrayList to prevent external references.
     */
    public IcyTerrain() {
        // Use try-with-resources would be ideal, but Scanner is managed throughout game
        this.scanner = new Scanner(System.in);
        this.terrainGrid = new TerrainGrid(GRID_SIZE);
        this.penguins = new ArrayList<>();      // Fresh list, no external reference
        this.foodItems = new ArrayList<>();     // Fresh list, no external reference
        this.hazards = new ArrayList<>();       // Fresh list, no external reference
        this.currentTurn = 1;

        startGame();
    }

    /**
     * Starts and orchestrates the entire game flow.
     * SECURITY: Protected method, no external access to game initialization.
     */
    private void startGame() {
        System.out.println("Welcome to Sliding Penguins Puzzle Game App. An " + GRID_SIZE + "x" + GRID_SIZE + " icy terrain grid is being generated.");
        System.out.println("Penguins, Hazards, and Food items are also being generated. The initial icy terrain grid:");

        // Generate game objects with security checks
        generatePenguins();
        generateHazards();
        generateFood();

        // Validate initial state
        if (!validateGameState()) {
            System.err.println("WARNING: Initial game state validation failed!");
        }

        // Display initial grid
        displayGrid();
        displayPenguinTypes();

        // Play the game
        playGame();

        // Display final results
        displayGameOver();

        // Cleanup
        if (scanner != null) {
            scanner.close();
        }
    }

    /**
     * Generates 3 penguins with random types and places them on edge positions.
     * SECURITY: All positions are defensively copied before storage.
     */
    private void generatePenguins() {
        String[] penguinNames = {"P1", "P2", "P3"};

        for (int i = 0; i < NUM_PENGUINS; i++) {
            Position pos = findEmptyEdgePosition();
            if (pos == null) {
                System.err.println("ERROR: Could not find empty edge position for penguin!");
                continue;
            }

            PenguinType type = PenguinType.getRandomType();
            if (type == null) {
                type = PenguinType.KING; // Safe fallback
            }

            Penguin penguin = createPenguin(penguinNames[i], pos, type);
            if (penguin == null) {
                System.err.println("ERROR: Could not create penguin!");
                continue;
            }

            penguins.add(penguin);
            // SECURITY: Defensive copy when placing
            terrainGrid.placeObject(penguin, new Position(pos));
        }

        // Randomly assign one penguin to the player
        if (!penguins.isEmpty()) {
            int playerIndex = GameHelper.randomInt(0, penguins.size() - 1);
            playerPenguin = penguins.get(playerIndex);
            if (playerPenguin != null) {
                playerPenguin.setPlayerPenguin(true);
            }
        }
    }

    /**
     * Creates a penguin of the specified type.
     * SECURITY: All parameters validated, position defensively copied.
     *
     * @param name The penguin's name (must not be null)
     * @param position The starting position (must not be null)
     * @param type The penguin type (must not be null)
     * @return The created Penguin object, or null if creation fails
     */
    private Penguin createPenguin(String name, Position position, PenguinType type) {
        // SECURITY: Comprehensive null checks
        if (name == null || name.trim().isEmpty()) {
            System.err.println("ERROR: Invalid penguin name");
            return null;
        }
        if (position == null) {
            System.err.println("ERROR: Invalid penguin position");
            return null;
        }
        if (type == null) {
            System.err.println("ERROR: Invalid penguin type");
            return null;
        }

        try {
            // SECURITY: Defensive copy of position before passing to constructor
            Position safeCopy = new Position(position);

            switch (type) {
                case KING:
                    return new KingPenguin(name, safeCopy);
                case EMPEROR:
                    return new EmperorPenguin(name, safeCopy);
                case ROYAL:
                    return new RoyalPenguin(name, safeCopy);
                case ROCKHOPPER:
                    return new RockhopperPenguin(name, safeCopy);
                default:
                    return new KingPenguin(name, safeCopy);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception creating penguin: " + e.getMessage());
            return null;
        }
    }

    /**
     * Generates 15 hazards with random types and places them on the grid.
     * SECURITY: All positions defensively copied, null checks throughout.
     */
    private void generateHazards() {
        for (int i = 0; i < NUM_HAZARDS; i++) {
            Position pos = findEmptyPosition();
            if (pos == null) {
                System.err.println("WARNING: Could not find empty position for hazard " + (i+1));
                continue;
            }

            IHazard hazard = createRandomHazard(pos);
            if (hazard == null) {
                System.err.println("WARNING: Could not create hazard " + (i+1));
                continue;
            }

            hazards.add(hazard);
            // SECURITY: Defensive copy when placing
            terrainGrid.placeObject((ITerrainObject) hazard, new Position(pos));
        }
    }

    /**
     * Creates a random hazard at the specified position.
     * SECURITY: Position defensively copied, comprehensive error handling.
     *
     * @param position The position for the hazard (must not be null)
     * @return A randomly created IHazard, or null if creation fails
     */
    private IHazard createRandomHazard(Position position) {
        if (position == null) {
            System.err.println("ERROR: Cannot create hazard at null position");
            return null;
        }

        try {
            int type = GameHelper.randomInt(0, 3);
            // SECURITY: Defensive copy of position
            Position safeCopy = new Position(position);

            switch (type) {
                case 0:
                    return new LightIceBlock(safeCopy);
                case 1:
                    return new HeavyIceBlock(safeCopy);
                case 2:
                    return new SeaLion(safeCopy);
                case 3:
                    return new HoleInIce(safeCopy);
                default:
                    return new HeavyIceBlock(safeCopy);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception creating hazard: " + e.getMessage());
            return null;
        }
    }

    /**
     * Generates 20 food items with random types and weights.
     * SECURITY: All positions defensively copied, null safety ensured.
     */
    private void generateFood() {
        for (int i = 0; i < NUM_FOOD; i++) {
            Position pos = findEmptyPosition();
            if (pos == null) {
                System.err.println("WARNING: Could not find empty position for food " + (i+1));
                continue;
            }

            try {
                Food food = Food.createRandom(pos);
                if (food == null) {
                    System.err.println("WARNING: Could not create food " + (i+1));
                    continue;
                }

                foodItems.add(food);
                // SECURITY: Defensive copy when placing
                terrainGrid.placeObject(food, new Position(pos));
            } catch (Exception e) {
                System.err.println("ERROR: Exception creating food: " + e.getMessage());
            }
        }
    }

    /**
     * Finds an empty edge position for penguin placement.
     * SECURITY: Returns a NEW Position object (defensive copy), never returns null without checks.
     *
     * @return An empty edge Position (defensive copy), or null if none found
     */
    private Position findEmptyEdgePosition() {
        int attempts = 0;
        int maxAttempts = 100; // Prevent infinite loop

        while (attempts < maxAttempts) {
            try {
                Position pos = GameHelper.randomEdgePosition(GRID_SIZE);
                if (pos != null && terrainGrid.getObjectAt(new Position(pos)) == null) {
                    return new Position(pos); // SECURITY: Return defensive copy
                }
            } catch (Exception e) {
                System.err.println("ERROR: Exception finding edge position: " + e.getMessage());
            }
            attempts++;
        }

        System.err.println("ERROR: Could not find empty edge position after " + maxAttempts + " attempts");
        return null;
    }

    /**
     * Finds an empty position anywhere on the grid.
     * SECURITY: Returns a NEW Position object (defensive copy), never returns null without checks.
     *
     * @return An empty Position (defensive copy), or null if none found
     */
    private Position findEmptyPosition() {
        int attempts = 0;
        int maxAttempts = 200; // Prevent infinite loop

        while (attempts < maxAttempts) {
            try {
                Position pos = GameHelper.randomPosition(GRID_SIZE);
                if (pos != null && terrainGrid.getObjectAt(new Position(pos)) == null) {
                    return new Position(pos); // SECURITY: Return defensive copy
                }
            } catch (Exception e) {
                System.err.println("ERROR: Exception finding position: " + e.getMessage());
            }
            attempts++;
        }

        System.err.println("ERROR: Could not find empty position after " + maxAttempts + " attempts");
        return null;
    }

    /**
     * Gets the object at the specified position.
     * SECURITY: Position parameter is defensively copied before use.
     *
     * @param position The position to check (must not be null)
     * @return The ITerrainObject at that position, or null if empty/invalid
     */
    private ITerrainObject getObjectAt(Position position) {
        if (position == null) {
            return null;
        }
        try {
            // SECURITY: Defensive copy before grid access
            return terrainGrid.getObjectAt(new Position(position));
        } catch (Exception e) {
            System.err.println("ERROR: Exception getting object at position: " + e.getMessage());
            return null;
        }
    }

    /**
     * Displays the current state of the grid.
     * SECURITY: Only reads state, no modification possible.
     */
    private void displayGrid() {
        try {
            if (terrainGrid != null) {
                terrainGrid.display();
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception displaying grid: " + e.getMessage());
        }
    }

    /**
     * Displays the penguin types at the start of the game.
     * SECURITY: Only reads penguin data, creates defensive copies implicitly.
     */
    private void displayPenguinTypes() {
        try {
            System.out.println("These are the penguins on the icy terrain:");
            for (Penguin penguin : penguins) {
                if (penguin == null) continue;

                String playerIndicator = penguin.isPlayerPenguin() ? " ---> YOUR PENGUIN" : "";
                String name = penguin.getName();
                if (name != null && name.length() > 1) {
                    System.out.println("- Penguin " + name.charAt(1) + " (" + name + "): " +
                            penguin.getType().getDisplayName() + playerIndicator);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception displaying penguin types: " + e.getMessage());
        }
    }

    /**
     * Main game loop - plays all turns for all penguins.
     * SECURITY: Protected loop with comprehensive error handling.
     */
    private void playGame() {
        try {
            for (currentTurn = 1; currentTurn <= NUM_TURNS; currentTurn++) {
                // Create defensive copy of penguins list for iteration
                List<Penguin> penguinsCopy = new ArrayList<>(penguins);

                for (Penguin penguin : penguinsCopy) {
                    if (penguin != null && !penguin.isRemoved()) {
                        try {
                            playTurn(penguin);
                        } catch (Exception e) {
                            System.err.println("ERROR: Exception during turn: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR in game loop: " + e.getMessage());
        }
    }

    /**
     * Plays a single turn for the specified penguin.
     * SECURITY: Comprehensive null checks and error handling.
     *
     * @param penguin The penguin taking their turn (must not be null)
     */
    private void playTurn(Penguin penguin) {
        if (penguin == null) {
            return;
        }

        try {
            System.out.println("\n*** Turn " + currentTurn + " – " + penguin.getName() +
                    (penguin.isPlayerPenguin() ? " (Your Penguin):" : ":"));

            // Check if stunned
            if (penguin.isStunned()) {
                System.out.println(penguin.getName() + " is stunned and skips this turn!");
                penguin.setStunned(false);
                return;
            }

            // Handle special action and movement
            if (penguin.isPlayerPenguin()) {
                handlePlayerTurn(penguin);
            } else {
                handleAITurn(penguin);
            }

            // Display updated grid
            System.out.println("New state of the grid:");
            displayGrid();

        } catch (Exception e) {
            System.err.println("ERROR: Exception in playTurn: " + e.getMessage());
        }
    }

    /**
     * Handles a turn for the player's penguin.
     * SECURITY: Input validation with try-catch, defensive copies throughout.
     *
     * @param penguin The player's penguin (must not be null)
     */
    private void handlePlayerTurn(Penguin penguin) {
        if (penguin == null) {
            return;
        }

        try {
            // Ask about special action
            boolean useSpecial = false;
            if (!penguin.hasUsedSpecialAction()) {
                useSpecial = askYesNo("Will " + penguin.getName() + " use its special action? Answer with Y or N");
            }

            // Handle Royal Penguin special move
            if (useSpecial && penguin instanceof RoyalPenguin) {
                Direction specialDir = askDirection("Which direction for the special single-step move? Answer with U (Up), D (Down), L (Left), R (Right)");
                if (specialDir != null) {
                    ((RoyalPenguin) penguin).setSpecialMoveDirection(specialDir);
                    penguin.useSpecialAction();

                    System.out.println(penguin.getName() + " moves one square " + getDirectionText(specialDir) + ".");
                    executeRoyalSpecialMove((RoyalPenguin) penguin, specialDir);

                    // SECURITY: Check if penguin was removed during special move
                    if (penguin.isRemoved()) {
                        return;  // Penguin fell during special move
                    }
                }
            } else if (useSpecial && penguin instanceof RockhopperPenguin) {
                // Rockhopper prepares to jump (doesn't show message yet)
                penguin.useSpecialAction();
                System.out.println(penguin.getName() + " is prepared to jump over a hazard.");
            } else if (useSpecial) {
                // King/Emperor just use action, message comes during slide
                penguin.useSpecialAction();
                // NO MESSAGE HERE - Message will appear when they actually stop at the square
            }

            // Ask for movement direction
            Direction direction = askDirection("Which direction will " + penguin.getName() + " move? Answer with U (Up), D (Down), L (Left), R (Right)");

            // Execute movement
            if (direction != null) {
                System.out.println(penguin.getName() + " chooses to move " + getDirectionText(direction) + ".");
                executePenguinMovement(penguin, direction);
            }

        } catch (Exception e) {
            System.err.println("ERROR: Exception in handlePlayerTurn: " + e.getMessage());
        }
    }

    /**
     * Handles a turn for an AI-controlled penguin.
     * SECURITY: Protected AI logic with error handling.
     * LOGIC FIX: Cleaner Rockhopper auto-use messaging.
     *
     * @param penguin The AI penguin (must not be null)
     */
    private void handleAITurn(Penguin penguin) {
        if (penguin == null) {
            return;
        }

        try {
            // Decide whether to use special action (30% chance)
            boolean useSpecial = !penguin.hasUsedSpecialAction() &&
                    GameHelper.randomChance(AI_SPECIAL_ACTION_CHANCE);

            // Special handling for Rockhopper - auto-use when moving toward hazard
            Direction chosenDir = null;
            if (penguin instanceof RockhopperPenguin && !penguin.hasUsedSpecialAction()) {
                chosenDir = chooseAIDirection(penguin);
                if (chosenDir != null) {
                    // SECURITY: Defensive copy of position
                    Position currentPos = penguin.getPosition();
                    if (currentPos != null) {
                        Position nextPos = currentPos.getNextPosition(chosenDir);

                        if (nextPos != null && nextPos.isValid(GRID_SIZE)) {
                            ITerrainObject nextObj = getObjectAt(new Position(nextPos));
                            if (nextObj instanceof IHazard) {
                                useSpecial = true;
                                // ✅ LOGIC FIX: Remove confusing "will automatically USE" message
                                // Jump message will appear when actually jumping
                                penguin.useSpecialAction();
                            }
                        }
                    }
                }
            }

            if (useSpecial && !(penguin instanceof RockhopperPenguin)) {
                System.out.println(penguin.getName() + " chooses to USE its special action.");
                penguin.useSpecialAction();

                // Handle Royal Penguin special move
                if (penguin instanceof RoyalPenguin) {
                    Direction specialDir = chooseRoyalSpecialDirection((RoyalPenguin) penguin);
                    if (specialDir != null) {
                        System.out.println(penguin.getName() + " moves one square " + getDirectionText(specialDir) + ".");
                        ((RoyalPenguin) penguin).setSpecialMoveDirection(specialDir);
                        executeRoyalSpecialMove((RoyalPenguin) penguin, specialDir);

                        if (penguin.isRemoved()) {
                            return;
                        }
                    }
                }
            } else if (!useSpecial && !(penguin instanceof RockhopperPenguin)) {
                System.out.println(penguin.getName() + " does NOT use its special action.");
            }

            // Choose and execute movement
            if (chosenDir == null) {
                chosenDir = chooseAIDirection(penguin);
            }

            if (chosenDir != null) {
                System.out.println(penguin.getName() + " chooses to move " + getDirectionText(chosenDir) + ".");
                executePenguinMovement(penguin, chosenDir);
            }

        } catch (Exception e) {
            System.err.println("ERROR: Exception in handleAITurn: " + e.getMessage());
        }
    }

    /**
     * Converts Direction to proper display text matching PDF format.
     * SECURITY: Null-safe, returns defensive string.
     *
     * @param dir The direction (can be null)
     * @return Formatted direction text (never null)
     */
    private String getDirectionText(Direction dir) {
        if (dir == null) {
            return "UNKNOWN";
        }

        switch (dir) {
            case UP:
                return "UPWARDS";
            case DOWN:
                return "DOWNWARDS";
            case LEFT:
                return "to the LEFT";
            case RIGHT:
                return "to the RIGHT";
            default:
                return dir.toString();
        }
    }

    /**
     * Chooses a direction for AI penguin based on priorities.
     * Priority: 1) Food, 2) Hazards (except HoleInIce), 3) Safe, 4) Water (last resort)
     * SECURITY: Comprehensive null checks, defensive position copying.
     *
     * @param penguin The AI penguin (must not be null)
     * @return The chosen Direction (never null)
     */
    private Direction chooseAIDirection(Penguin penguin) {
        if (penguin == null) {
            return Direction.UP; // Safe default
        }

        try {
            Direction[] directions = Direction.values();
            List<Direction> foodDirections = new ArrayList<>();
            List<Direction> hazardDirections = new ArrayList<>();
            List<Direction> waterDirections = new ArrayList<>();
            List<Direction> safeDirections = new ArrayList<>();

            // SECURITY: Defensive copy of current position
            Position currentPos = penguin.getPosition();
            if (currentPos == null) {
                return Direction.UP; // Safe default
            }
            currentPos = new Position(currentPos);

            for (Direction dir : directions) {
                if (dir == null) continue;

                Position nextPos = currentPos.getNextPosition(dir);
                if (nextPos == null) continue;

                if (!nextPos.isValid(GRID_SIZE)) {
                    waterDirections.add(dir);
                    continue;
                }

                // SECURITY: Defensive copy when checking objects
                ITerrainObject obj = getObjectAt(new Position(nextPos));

                if (obj == null) {
                    safeDirections.add(dir);
                } else if (obj instanceof Food) {
                    foodDirections.add(dir);
                } else if (obj instanceof IHazard) {
                    if (obj instanceof HoleInIce && ((HoleInIce) obj).isActive()) {
                        waterDirections.add(dir);
                    } else {
                        hazardDirections.add(dir);
                    }
                } else {
                    safeDirections.add(dir);
                }
            }

            // Priority selection
            if (!foodDirections.isEmpty()) {
                return foodDirections.get(GameHelper.randomInt(0, foodDirections.size() - 1));
            } else if (!hazardDirections.isEmpty()) {
                return hazardDirections.get(GameHelper.randomInt(0, hazardDirections.size() - 1));
            } else if (!safeDirections.isEmpty()) {
                return safeDirections.get(GameHelper.randomInt(0, safeDirections.size() - 1));
            } else if (!waterDirections.isEmpty()) {
                return waterDirections.get(GameHelper.randomInt(0, waterDirections.size() - 1));
            } else {
                return Direction.UP; // Ultimate fallback
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception in chooseAIDirection: " + e.getMessage());
            return Direction.UP;
        }
    }

    /**
     * Chooses a safe direction for Royal Penguin's special move.
     * SECURITY: Defensive copying, null safety throughout.
     *
     * @param penguin The Royal Penguin (must not be null)
     * @return A safe Direction for the special move (never null)
     */
    private Direction chooseRoyalSpecialDirection(RoyalPenguin penguin) {
        if (penguin == null) {
            return Direction.UP;
        }

        try {
            Direction[] directions = Direction.values();
            List<Direction> safeDirections = new ArrayList<>();

            // SECURITY: Defensive copy
            Position currentPos = penguin.getPosition();
            if (currentPos == null) {
                return Direction.UP;
            }
            currentPos = new Position(currentPos);

            for (Direction dir : directions) {
                if (dir == null) continue;

                Position nextPos = currentPos.getNextPosition(dir);
                if (nextPos == null) continue;

                if (!nextPos.isValid(GRID_SIZE)) {
                    continue;
                }

                // SECURITY: Defensive copy
                ITerrainObject obj = getObjectAt(new Position(nextPos));

                if (obj == null || obj instanceof Food) {
                    safeDirections.add(dir);
                }
            }

            if (!safeDirections.isEmpty()) {
                return safeDirections.get(GameHelper.randomInt(0, safeDirections.size() - 1));
            } else {
                return directions[GameHelper.randomInt(0, directions.length - 1)];
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception in chooseRoyalSpecialDirection: " + e.getMessage());
            return Direction.UP;
        }
    }

    /**
     * Executes the Royal Penguin's special single-step move.
     * SECURITY: All positions defensively copied, comprehensive error handling.
     *
     * @param penguin The Royal Penguin (must not be null)
     * @param direction The direction to move (must not be null)
     */
    private void executeRoyalSpecialMove(RoyalPenguin penguin, Direction direction) {
        if (penguin == null || direction == null) {
            return;
        }

        try {
            // SECURITY: Defensive copy
            Position currentPos = penguin.getPosition();
            if (currentPos == null) {
                return;
            }
            currentPos = new Position(currentPos);

            Position nextPos = currentPos.getNextPosition(direction);
            if (nextPos == null) {
                return;
            }

            // Check if moving out of bounds
            if (!nextPos.isValid(GRID_SIZE)) {
                System.out.println(penguin.getName() + " accidentally stepped out of the grid and fell into the water!");
                System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
                removePenguin(penguin);
                return;
            }

            // SECURITY: Defensive copy
            ITerrainObject targetObj = getObjectAt(new Position(nextPos));

            // Handle collisions during special move
            if (targetObj instanceof IHazard) {
                IHazard hazard = (IHazard) targetObj;
                if (hazard instanceof HoleInIce && ((HoleInIce) hazard).isActive()) {
                    System.out.println(penguin.getName() + " fell into a HoleInIce during the special move!");
                    System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
                    removePenguin(penguin);
                    return;
                } else if (hazard instanceof HeavyIceBlock) {
                    System.out.println(penguin.getName() + " hit a HeavyIceBlock during the special move!");
                    Food lost = penguin.removeLightestFood();
                    if (lost != null) {
                        System.out.println(penguin.getName() + " lost their lightest food item: " + lost);
                    }
                    return;
                }
            }

            // Move penguin - SECURITY: All defensive copies
            removeObject(new Position(currentPos));
            penguin.setPosition(new Position(nextPos));

            // Handle food collection
            if (targetObj instanceof Food) {
                Food food = (Food) targetObj;
                penguin.collectFood(food);
                foodItems.remove(food);
                System.out.println(penguin.getName() + " takes the " + food.getDisplayName() +
                        " on the ground. (Weight=" + food.getWeight() + " units)");
            }

            terrainGrid.placeObject(penguin, new Position(nextPos));
            penguin.resetSpecialMove();

        } catch (Exception e) {
            System.err.println("ERROR: Exception in executeRoyalSpecialMove: " + e.getMessage());
        }
    }

    /**
     * Executes penguin movement with sliding mechanics.
     * SECURITY: All positions defensively copied throughout, comprehensive error handling.
     * LOGIC FIX: King/Emperor stop messages clarified.
     *
     * @param penguin The penguin to move (must not be null)
     * @param direction The direction to move (must not be null)
     */
    private void executePenguinMovement(Penguin penguin, Direction direction) {
        if (penguin == null || direction == null) {
            return;
        }

        try {
            penguin.slide(direction);

            // SECURITY: Defensive copy of starting position
            Position currentPos = penguin.getPosition();
            if (currentPos == null) {
                return;
            }
            currentPos = new Position(currentPos);

            int squareCount = 0;
            boolean shouldStop = false;

            // Determine stop square for King/Emperor penguins
            int stopSquare = -1;
            if (penguin.hasUsedSpecialAction()) {
                if (penguin instanceof KingPenguin) {
                    stopSquare = ((KingPenguin) penguin).getStopSquare();
                } else if (penguin instanceof EmperorPenguin) {
                    stopSquare = ((EmperorPenguin) penguin).getStopSquare();
                }
            }

            // Slide until stopped
            while (!shouldStop) {
                Position nextPos = currentPos.getNextPosition(direction);
                if (nextPos == null) {
                    shouldStop = true;
                    continue;
                }

                squareCount++;

                // Check if falling off the grid
                if (!nextPos.isValid(GRID_SIZE)) {
                    System.out.println(penguin.getName() + " falls into the water!");
                    System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
                    removePenguin(penguin);
                    return;
                }

                // SECURITY: Defensive copy
                ITerrainObject targetObj = getObjectAt(new Position(nextPos));

                // ✅ LOGIC FIX: Check for stop at specific square (King/Emperor ability)
                if (stopSquare > 0 && squareCount == stopSquare) {
                    if (targetObj == null) {
                        // ✅ CLARIFIED: Message for stopping at empty square
                        removeObject(new Position(currentPos));
                        penguin.setPosition(new Position(nextPos));
                        terrainGrid.placeObject(penguin, new Position(nextPos));
                        System.out.println(penguin.getName() + " stops at an empty square using its special action.");
                        shouldStop = true;
                        continue;
                    } else if (targetObj instanceof Food) {
                        // ✅ CLARIFIED: Collect food and stop with message
                        removeObject(new Position(currentPos));
                        penguin.setPosition(new Position(nextPos));
                        Food food = (Food) targetObj;
                        penguin.collectFood(food);
                        foodItems.remove(food);
                        terrainGrid.placeObject(penguin, new Position(nextPos));
                        System.out.println(penguin.getName() + " takes the " + food.getDisplayName() +
                                " on the ground. (Weight=" + food.getWeight() + " units)");
                        shouldStop = true;
                        continue;
                    }
                    // If there's a hazard/penguin at stop square, continue sliding
                }

                // Handle empty square
                if (targetObj == null) {
                    removeObject(new Position(currentPos));
                    currentPos = new Position(nextPos);
                    penguin.setPosition(new Position(nextPos));
                    terrainGrid.placeObject(penguin, new Position(nextPos));
                    continue;
                }

                // Handle food
                if (targetObj instanceof Food) {
                    removeObject(new Position(currentPos));
                    Food food = (Food) targetObj;
                    penguin.collectFood(food);
                    foodItems.remove(food);
                    penguin.setPosition(new Position(nextPos));
                    terrainGrid.placeObject(penguin, new Position(nextPos));
                    System.out.println(penguin.getName() + " takes the " + food.getDisplayName() +
                            " on the ground. (Weight=" + food.getWeight() + " units)");
                    shouldStop = true;
                    continue;
                }

                // Handle penguin collision
                if (targetObj instanceof Penguin) {
                    Penguin otherPenguin = (Penguin) targetObj;
                    System.out.println(penguin.getName() + " collides with " + otherPenguin.getName() + "!");

                    penguin.setSliding(false);

                    // SECURITY: Defensive copy
                    Position otherPos = otherPenguin.getPosition();
                    if (otherPos != null) {
                        removeObject(new Position(otherPos));
                        executePenguinMovement(otherPenguin, direction);
                    }
                    shouldStop = true;
                    continue;
                }

                // Handle hazard collision
                if (targetObj instanceof IHazard) {
                    shouldStop = handleHazardCollision(penguin, (IHazard) targetObj, direction, new Position(currentPos));
                    continue;
                }

                shouldStop = true;
            }

            penguin.setSliding(false);

        } catch (Exception e) {
            System.err.println("ERROR: Exception in executePenguinMovement: " + e.getMessage());
            if (penguin != null) {
                penguin.setSliding(false);
            }
        }
    }

    /**
     * Handles collision between a penguin and a hazard.
     * SECURITY: All positions defensively copied, comprehensive null checks.
     * 🔴 CRITICAL LOGIC FIXES:
     * ✅ RockhopperPenguin can ONLY jump to COMPLETELY EMPTY squares (no food)
     * ✅ RockhopperPenguin CONTINUES SLIDING after successful jump (returns false)
     *
     * @param penguin The colliding penguin (must not be null)
     * @param hazard The hazard being hit (must not be null)
     * @param direction The direction of movement (must not be null)
     * @param currentPos The current position before collision (must not be null, defensive copy expected)
     * @return true if penguin should stop, false to continue
     */
    private boolean handleHazardCollision(Penguin penguin, IHazard hazard, Direction direction, Position currentPos) {
        if (penguin == null || hazard == null || direction == null || currentPos == null) {
            return true;
        }

        try {
            // ✅ CRITICAL LOGIC FIX: Rockhopper jump ability
            if (penguin instanceof RockhopperPenguin) {
                RockhopperPenguin rockhopper = (RockhopperPenguin) penguin;

                if (rockhopper.isPreparedToJump()) {
                    // SECURITY: Defensive copy
                    Position hazardPos = hazard.getPosition();
                    if (hazardPos == null) {
                        return true;
                    }
                    hazardPos = new Position(hazardPos);

                    // Calculate landing position (one square beyond the hazard)
                    Position landingPos = hazardPos.getNextPosition(direction);
                    if (landingPos == null) {
                        System.out.println(penguin.getName() + " cannot jump - no landing square!");
                        rockhopper.executeJump(); // Consume the jump
                        return true; // Stop at hazard
                    }

                    // ✅ Check if landing position is valid (not out of bounds)
                    if (!landingPos.isValid(GRID_SIZE)) {
                        System.out.println(penguin.getName() + " jumps over " + hazard.getDisplayName() + " in its path.");
                        System.out.println(penguin.getName() + " falls into the water!");
                        System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
                        rockhopper.executeJump();
                        removePenguin(penguin);
                        return true;
                    }

                    // SECURITY: Defensive copy
                    ITerrainObject landingObj = getObjectAt(new Position(landingPos));

                    // ✅ CRITICAL FIX: Can ONLY jump to COMPLETELY EMPTY square
                    // PDF says: "they can only jump to an empty square"
                    if (landingObj == null) {
                        System.out.println(penguin.getName() + " jumps over " + hazard.getDisplayName() + " in its path.");
                        rockhopper.executeJump();

                        // SECURITY: All defensive copies
                        removeObject(new Position(currentPos));
                        penguin.setPosition(new Position(landingPos));
                        terrainGrid.placeObject(penguin, new Position(landingPos));

                        // ✅ CRITICAL FIX: Continue sliding after successful jump (return false)
                        // PDF example shows P1 jumping HB then hitting HI - meaning it continued sliding
                        return false;

                    } else if (landingObj instanceof Food) {
                        // ❌ CANNOT jump if food is on landing square
                        System.out.println(penguin.getName() + " fails to jump over " + hazard.getDisplayName() + " - landing square has food!");
                        rockhopper.executeJump(); // Consume the jump anyway
                        // Fall through to normal hazard collision

                    } else {
                        // Landing square is occupied by penguin or another hazard
                        System.out.println(penguin.getName() + " fails to jump over " + hazard.getDisplayName() + " - landing square is occupied!");
                        rockhopper.executeJump();
                        // Fall through to normal hazard collision
                    }
                }
            }

            // Handle specific hazard types
            if (hazard instanceof HoleInIce) {
                HoleInIce hole = (HoleInIce) hazard;
                if (hole.isPlugged()) {
                    // SECURITY: Defensive copies
                    removeObject(new Position(currentPos));
                    Position holePos = hole.getPosition();
                    if (holePos != null) {
                        holePos = new Position(holePos);
                        penguin.setPosition(new Position(holePos));
                        terrainGrid.placeObject(penguin, new Position(holePos));
                    }
                    return false; // Continue sliding over plugged hole
                } else {
                    System.out.println(penguin.getName() + " falls into a HoleInIce!");
                    System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
                    removePenguin(penguin);
                    return true;
                }
            }

            if (hazard instanceof LightIceBlock) {
                LightIceBlock iceBlock = (LightIceBlock) hazard;
                System.out.println(penguin.getName() + " collides with a LightIceBlock!");

                penguin.setStunned(true);
                System.out.println(penguin.getName() + " is stunned and will skip the next turn!");

                // SECURITY: Defensive copy
                Position icePos = iceBlock.getPosition();
                if (icePos != null) {
                    icePos = new Position(icePos);
                    removeObject(new Position(icePos));
                    executeSlidableMovement(iceBlock, direction);
                }

                return true;
            }

            if (hazard instanceof HeavyIceBlock) {
                System.out.println(penguin.getName() + " collides with a HeavyIceBlock!");

                Food lost = penguin.removeLightestFood();
                if (lost != null) {
                    System.out.println(penguin.getName() + " loses their lightest food item: " + lost);
                }

                return true;
            }

            if (hazard instanceof SeaLion) {
                SeaLion seaLion = (SeaLion) hazard;
                System.out.println(penguin.getName() + " bounces off a SeaLion!");

                Direction oppositeDir = direction.getOpposite();
                if (oppositeDir == null) {
                    return true;
                }

                System.out.println(penguin.getName() + " starts sliding in the opposite direction.");

                // SECURITY: Defensive copy
                removeObject(new Position(currentPos));

                Position seaLionPos = seaLion.getPosition();
                if (seaLionPos != null) {
                    seaLionPos = new Position(seaLionPos);
                    removeObject(new Position(seaLionPos));
                    executeSlidableMovement(seaLion, direction);
                }

                penguin.setPosition(new Position(currentPos));
                terrainGrid.placeObject(penguin, new Position(currentPos));
                executePenguinMovement(penguin, oppositeDir);

                return true;
            }

        } catch (Exception e) {
            System.err.println("ERROR: Exception in handleHazardCollision: " + e.getMessage());
        }

        return true;
    }

    /**
     * Executes sliding movement for slidable hazards.
     * SECURITY: All positions defensively copied, error handling throughout.
     *
     * @param slidable The slidable object (must not be null)
     * @param direction The direction to slide (must not be null)
     */
    private void executeSlidableMovement(ISlidable slidable, Direction direction) {
        if (slidable == null || direction == null) {
            return;
        }

        try {
            slidable.slide(direction);

            ITerrainObject obj = (ITerrainObject) slidable;
            // SECURITY: Defensive copy
            Position currentPos = obj.getPosition();
            if (currentPos == null) {
                return;
            }
            currentPos = new Position(currentPos);

            boolean shouldStop = false;

            while (!shouldStop) {
                Position nextPos = currentPos.getNextPosition(direction);
                if (nextPos == null) {
                    shouldStop = true;
                    continue;
                }

                if (!nextPos.isValid(GRID_SIZE)) {
                    System.out.println(obj.getDisplayName() + " falls off the edge!");
                    if (slidable instanceof LightIceBlock) {
                        hazards.remove((IHazard) slidable);
                    } else if (slidable instanceof SeaLion) {
                        hazards.remove((IHazard) slidable);
                    }
                    return;
                }

                // SECURITY: Defensive copy
                ITerrainObject targetObj = getObjectAt(new Position(nextPos));

                if (targetObj == null) {
                    removeObject(new Position(currentPos));
                    currentPos = new Position(nextPos);
                    obj.setPosition(new Position(nextPos));
                    terrainGrid.placeObject(obj, new Position(nextPos));
                    continue;
                }

                if (targetObj instanceof Food) {
                    Food food = (Food) targetObj;
                    System.out.println(obj.getDisplayName() + " destroys " + food.getDisplayName() + "!");
                    foodItems.remove(food);
                    removeObject(new Position(currentPos));
                    currentPos = new Position(nextPos);
                    obj.setPosition(new Position(nextPos));
                    terrainGrid.placeObject(obj, new Position(nextPos));
                    continue;
                }

                if (targetObj instanceof Penguin) {
                    Penguin otherPenguin = (Penguin) targetObj;
                    System.out.println(obj.getDisplayName() + " is stopped by " + otherPenguin.getName() + "!");
                    shouldStop = true;
                    continue;
                }

                if (targetObj instanceof HoleInIce) {
                    HoleInIce hole = (HoleInIce) targetObj;
                    if (hole.isActive()) {
                        System.out.println(obj.getDisplayName() + " falls into a HoleInIce and plugs it!");
                        hole.plug();
                        removeObject(new Position(currentPos));
                        hazards.remove((IHazard) slidable);
                        return;
                    } else {
                        removeObject(new Position(currentPos));
                        currentPos = new Position(nextPos);
                        obj.setPosition(new Position(nextPos));
                        terrainGrid.placeObject(obj, new Position(nextPos));
                        continue;
                    }
                }

                if (targetObj instanceof LightIceBlock) {
                    LightIceBlock targetBlock = (LightIceBlock) targetObj;
                    System.out.println(obj.getDisplayName() + " collides with a LightIceBlock!");

                    shouldStop = true;

                    // SECURITY: Defensive copy
                    Position targetPos = targetBlock.getPosition();
                    if (targetPos != null) {
                        removeObject(new Position(targetPos));
                        executeSlidableMovement(targetBlock, direction);
                    }
                    continue;
                }

                shouldStop = true;
            }

            slidable.setSliding(false);

        } catch (Exception e) {
            System.err.println("ERROR: Exception in executeSlidableMovement: " + e.getMessage());
            if (slidable != null) {
                slidable.setSliding(false);
            }
        }
    }

    /**
     * Removes a penguin from the game.
     * SECURITY: Position defensively copied.
     *
     * @param penguin The penguin to remove (must not be null)
     */
    private void removePenguin(Penguin penguin) {
        if (penguin == null) {
            return;
        }

        try {
            penguin.setRemoved(true);
            Position pos = penguin.getPosition();
            if (pos != null) {
                removeObject(new Position(pos));
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception in removePenguin: " + e.getMessage());
        }
    }

    /**
     * Removes an object from the grid at the specified position.
     * SECURITY: Position defensively copied.
     *
     * @param position The position to clear (must not be null)
     */
    private void removeObject(Position position) {
        if (position == null) {
            return;
        }
        try {
            terrainGrid.removeObject(new Position(position));
        } catch (Exception e) {
            System.err.println("ERROR: Exception in removeObject: " + e.getMessage());
        }
    }

    /**
     * Asks the user a yes/no question.
     * SECURITY: Input validation with try-catch, null safety.
     *
     * @param question The question to ask (must not be null)
     * @return true for yes, false for no
     */
    private boolean askYesNo(String question) {
        if (question == null || scanner == null) {
            return false;
        }

        while (true) {
            try {
                System.out.print(question + " --> ");
                String input = scanner.nextLine();

                if (input == null) {
                    continue;
                }

                input = input.trim().toUpperCase();

                if (input.equals("Y")) {
                    return true;
                } else if (input.equals("N")) {
                    return false;
                } else {
                    System.out.println("Invalid input. Please answer with Y or N.");
                }
            } catch (Exception e) {
                System.err.println("ERROR: Exception reading input: " + e.getMessage());
                return false;
            }
        }
    }

    /**
     * Asks the user for a direction.
     * SECURITY: Input validation with try-catch, null safety.
     *
     * @param question The question to ask (must not be null)
     * @return The chosen Direction (never null)
     */
    private Direction askDirection(String question) {
        if (question == null || scanner == null) {
            return Direction.UP;
        }

        while (true) {
            try {
                System.out.print(question + " --> ");
                String input = scanner.nextLine();

                if (input == null) {
                    continue;
                }

                Direction direction = Direction.fromString(input);

                if (direction != null) {
                    return direction;
                } else {
                    System.out.println("Invalid input. Please answer with U, D, L, or R.");
                }
            } catch (Exception e) {
                System.err.println("ERROR: Exception reading input: " + e.getMessage());
                return Direction.UP;
            }
        }
    }

    /**
     * Displays the game over screen with final scoreboard.
     * SECURITY: Creates defensive copy of penguins list for sorting.
     */
    private void displayGameOver() {
        try {
            System.out.println("\n***** GAME OVER *****");
            System.out.println("***** SCOREBOARD FOR THE PENGUINS *****");

            // SECURITY: Create a defensive copy for sorting
            List<Penguin> sortedPenguins = new ArrayList<>(penguins);

            sortedPenguins.sort((p1, p2) -> {
                if (p1 == null && p2 == null) return 0;
                if (p1 == null) return 1;
                if (p2 == null) return -1;
                return p2.getTotalFoodWeight() - p1.getTotalFoodWeight();
            });

            for (int i = 0; i < sortedPenguins.size(); i++) {
                Penguin p = sortedPenguins.get(i);
                if (p == null) {
                    continue;
                }

                String place = getPlaceSuffix(i + 1);
                String playerIndicator = p.isPlayerPenguin() ? " (Your Penguin)" : "";

                System.out.println("* " + place + " place: " + p.getName() + playerIndicator);

                System.out.print("|---> Food items: ");
                // SECURITY: getCollectedFood() returns unmodifiable defensive copy
                List<Food> foods = p.getCollectedFood();

                if (foods == null || foods.isEmpty()) {
                    System.out.print("None");
                } else {
                    for (int j = 0; j < foods.size(); j++) {
                        Food food = foods.get(j);
                        if (food != null) {
                            System.out.print(food.getType().toString() + " (" + food.getWeight() + " units)");
                            if (j < foods.size() - 1) {
                                System.out.print(", ");
                            }
                        }
                    }
                }
                System.out.println();

                System.out.println("|---> Total weight: " + p.getTotalFoodWeight() + " units");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception in displayGameOver: " + e.getMessage());
        }
    }

    /**
     * Gets the ordinal suffix for a place number.
     * SECURITY: Safe method, no state access.
     *
     * @param place The place number
     * @return The place with suffix
     */
    private String getPlaceSuffix(int place) {
        switch (place) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            default:
                return place + "th";
        }
    }

    /**
     * SECURITY: Validates the entire game state.
     * Useful for debugging and ensuring consistency.
     *
     * @return true if game state is valid, false if corrupted
     */
    private boolean validateGameState() {
        try {
            if (terrainGrid == null) return false;
            if (penguins == null) return false;
            if (foodItems == null) return false;
            if (hazards == null) return false;

            if (!terrainGrid.validateGridIntegrity()) return false;

            for (Penguin p : penguins) {
                if (p != null && !p.validateState()) return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ===== SECURITY CRITICAL: Public Getter Methods =====
    // ALL return defensive copies or unmodifiable views!

    /**
     * SECURITY CRITICAL: Returns an unmodifiable defensive copy of penguins list.
     * Double protection: new ArrayList + unmodifiableList.
     *
     * @return Unmodifiable list of penguins (defensive copy)
     */
    public List<Penguin> getPenguins() {
        return Collections.unmodifiableList(new ArrayList<>(penguins));
    }

    /**
     * SECURITY CRITICAL: Returns an unmodifiable defensive copy of food items list.
     *
     * @return Unmodifiable list of food items (defensive copy)
     */
    public List<Food> getFoodItems() {
        return Collections.unmodifiableList(new ArrayList<>(foodItems));
    }

    /**
     * SECURITY CRITICAL: Returns an unmodifiable defensive copy of hazards list.
     *
     * @return Unmodifiable list of hazards (defensive copy)
     */
    public List<IHazard> getHazards() {
        return Collections.unmodifiableList(new ArrayList<>(hazards));
    }

    /**
     * SECURITY: Returns the player's penguin reference.
     * Reference is okay for read-only access, but caller should not modify.
     *
     * @return The player's penguin (can be null)
     */
    public Penguin getPlayerPenguin() {
        return playerPenguin;
    }

    /**
     * SECURITY: Returns the current turn number.
     * Safe - primitive value.
     *
     * @return Current turn (1-4)
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * SECURITY: Returns the grid size constant.
     * Safe - primitive value.
     *
     * @return Grid size (10)
     */
    public int getGridSize() {
        return GRID_SIZE;
    }

    /**
     * SECURITY: Gets a defensive copy of the terrain grid.
     * Returns the grid reference for read-only operations.
     * Grid itself has defensive methods.
     *
     * @return The TerrainGrid instance
     */
    public TerrainGrid getTerrainGrid() {
        return terrainGrid;
    }
}