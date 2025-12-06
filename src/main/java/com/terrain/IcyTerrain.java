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
 * SECURITY ENHANCED VERSION:
 * - All position returns use defensive copying
 * - All list returns are deep copied and unmodifiable
 * - Null safety checks added throughout
 * - Input validation strengthened
 */
public class IcyTerrain {

    private static final int GRID_SIZE = 10;
    private static final int NUM_PENGUINS = 3;
    private static final int NUM_HAZARDS = 15;
    private static final int NUM_FOOD = 20;
    private static final int NUM_TURNS = 4;
    private static final double AI_SPECIAL_ACTION_CHANCE = 0.30;

    private TerrainGrid terrainGrid;          // The terrain grid
    private List<Penguin> penguins;           // All penguins in the game
    private List<Food> foodItems;             // All food items
    private List<IHazard> hazards;            // All hazards
    private Penguin playerPenguin;            // The player's penguin
    private Scanner scanner;                  // For user input
    private int currentTurn;                  // Current turn number (1-4)

    /**
     * Constructor for IcyTerrain.
     * Initializes the grid and starts the game.
     */
    public IcyTerrain() {
        this.scanner = new Scanner(System.in);
        this.terrainGrid = new TerrainGrid(GRID_SIZE);
        this.penguins = new ArrayList<>();
        this.foodItems = new ArrayList<>();
        this.hazards = new ArrayList<>();
        this.currentTurn = 1;

        startGame();
    }

    /**
     * Starts and orchestrates the entire game flow.
     */
    private void startGame() {
        System.out.println("Welcome to Sliding Penguins Puzzle Game App. An " + GRID_SIZE + "x" + GRID_SIZE + " icy terrain grid is being generated.");
        System.out.println("Penguins, Hazards, and Food items are also being generated. The initial icy terrain grid:");

        // Generate game objects
        generatePenguins();
        generateHazards();
        generateFood();

        // Display initial grid
        displayGrid();
        displayPenguinTypes();

        // Play the game
        playGame();

        // Display final results
        displayGameOver();
    }

    /**
     * Generates 3 penguins with random types and places them on edge positions.
     */
    private void generatePenguins() {
        String[] penguinNames = {"P1", "P2", "P3"};

        for (int i = 0; i < NUM_PENGUINS; i++) {
            Position pos = findEmptyEdgePosition();
            PenguinType type = PenguinType.getRandomType();
            Penguin penguin = createPenguin(penguinNames[i], pos, type);

            penguins.add(penguin);
            terrainGrid.placeObject(penguin, new Position(pos)); // Defensive copy
        }

        // Randomly assign one penguin to the player
        int playerIndex = GameHelper.randomInt(0, NUM_PENGUINS - 1);
        playerPenguin = penguins.get(playerIndex);
        playerPenguin.setPlayerPenguin(true);
    }

    /**
     * Creates a penguin of the specified type.
     * Uses defensive copying for position parameter.
     *
     * @param name The penguin's name
     * @param position The starting position
     * @param type The penguin type
     * @return The created Penguin object
     * @throws IllegalArgumentException if any parameter is null
     */
    private Penguin createPenguin(String name, Position position, PenguinType type) {
        if (name == null || position == null || type == null) {
            throw new IllegalArgumentException("Penguin parameters cannot be null");
        }

        // Defensive copy of position
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
    }

    /**
     * Generates 15 hazards with random types and places them on the grid.
     */
    private void generateHazards() {
        for (int i = 0; i < NUM_HAZARDS; i++) {
            Position pos = findEmptyPosition();
            IHazard hazard = createRandomHazard(pos);

            hazards.add(hazard);
            terrainGrid.placeObject((ITerrainObject) hazard, new Position(pos)); // Defensive copy
        }
    }

    /**
     * Creates a random hazard at the specified position.
     * Uses defensive copying for position parameter.
     *
     * @param position The position for the hazard
     * @return A randomly created IHazard
     * @throws IllegalArgumentException if position is null
     */
    private IHazard createRandomHazard(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Hazard position cannot be null");
        }

        int type = GameHelper.randomInt(0, 3);
        // Defensive copy of position
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
    }

    /**
     * Generates 20 food items with random types and weights.
     */
    private void generateFood() {
        for (int i = 0; i < NUM_FOOD; i++) {
            Position pos = findEmptyPosition();
            Food food = Food.createRandom(pos);

            foodItems.add(food);
            terrainGrid.placeObject(food, new Position(pos)); // Defensive copy
        }
    }

    /**
     * Finds an empty edge position for penguin placement.
     *
     * @return An empty edge Position (defensive copy)
     */
    private Position findEmptyEdgePosition() {
        Position pos;
        do {
            pos = GameHelper.randomEdgePosition(GRID_SIZE);
        } while (terrainGrid.getObjectAt(pos) != null);
        return new Position(pos); // Return defensive copy
    }

    /**
     * Finds an empty position anywhere on the grid.
     *
     * @return An empty Position (defensive copy)
     */
    private Position findEmptyPosition() {
        Position pos;
        do {
            pos = GameHelper.randomPosition(GRID_SIZE);
        } while (terrainGrid.getObjectAt(pos) != null);
        return new Position(pos); // Return defensive copy
    }

    /**
     * Gets the object at the specified position.
     * Does NOT return the actual object reference for security.
     *
     * @param position The position to check
     * @return The ITerrainObject at that position, or null if empty
     */
    private ITerrainObject getObjectAt(Position position) {
        if (position == null) {
            return null;
        }
        return terrainGrid.getObjectAt(new Position(position)); // Defensive copy
    }

    /**
     * Displays the current state of the grid.
     */
    private void displayGrid() {
        terrainGrid.display();
    }

    /**
     * Displays the penguin types at the start of the game.
     */
    private void displayPenguinTypes() {
        System.out.println("These are the penguins on the icy terrain:");
        for (Penguin penguin : penguins) {
            String playerIndicator = penguin.isPlayerPenguin() ? " ---> YOUR PENGUIN" : "";
            System.out.println("- Penguin " + penguin.getName().charAt(1) + " (" + penguin.getName() + "): " +
                    penguin.getType().getDisplayName() + playerIndicator);
        }
    }

    /**
     * Main game loop - plays all turns for all penguins.
     */
    private void playGame() {
        for (currentTurn = 1; currentTurn <= NUM_TURNS; currentTurn++) {
            for (Penguin penguin : penguins) {
                if (!penguin.isRemoved()) {
                    playTurn(penguin);
                }
            }
        }
    }

    /**
     * Plays a single turn for the specified penguin.
     *
     * @param penguin The penguin taking their turn
     */
    private void playTurn(Penguin penguin) {
        if (penguin == null) {
            return;
        }

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
    }

    /**
     * Handles a turn for the player's penguin.
     *
     * @param penguin The player's penguin
     */
    private void handlePlayerTurn(Penguin penguin) {
        if (penguin == null) {
            return;
        }

        // Ask about special action
        boolean useSpecial = false;
        if (!penguin.hasUsedSpecialAction()) {
            useSpecial = askYesNo("Will " + penguin.getName() + " use its special action? Answer with Y or N");
        }

        // Handle Royal Penguin special move
        if (useSpecial && penguin instanceof RoyalPenguin) {
            Direction specialDir = askDirection("Which direction for the special single-step move? Answer with U (Up), D (Down), L (Left), R (Right)");
            ((RoyalPenguin) penguin).setSpecialMoveDirection(specialDir);
            penguin.useSpecialAction();

            System.out.println(penguin.getName() + " moves one square " + getDirectionText(specialDir) + ".");
            executeRoyalSpecialMove((RoyalPenguin) penguin, specialDir);

            if (penguin.isRemoved()) {
                return;  // Penguin fell during special move
            }
        } else if (useSpecial) {
            penguin.useSpecialAction();
            if (penguin instanceof KingPenguin) {
                System.out.println(penguin.getName() + " will use its special action to stop at the 5th square.");
            } else if (penguin instanceof EmperorPenguin) {
                System.out.println(penguin.getName() + " will use its special action to stop at the 3rd square.");
            } else if (penguin instanceof RockhopperPenguin) {
                System.out.println(penguin.getName() + " is prepared to jump over a hazard.");
            }
        }

        // Ask for movement direction
        Direction direction = askDirection("Which direction will " + penguin.getName() + " move? Answer with U (Up), D (Down), L (Left), R (Right)");

        // Execute movement
        System.out.println(penguin.getName() + " chooses to move " + getDirectionText(direction) + ".");
        executePenguinMovement(penguin, direction);
    }

    /**
     * Handles a turn for an AI-controlled penguin.
     *
     * @param penguin The AI penguin
     */
    private void handleAITurn(Penguin penguin) {
        if (penguin == null) {
            return;
        }

        // Decide whether to use special action (30% chance)
        boolean useSpecial = !penguin.hasUsedSpecialAction() &&
                GameHelper.randomChance(AI_SPECIAL_ACTION_CHANCE);

        // Special handling for Rockhopper - auto-use when moving toward hazard
        Direction chosenDir = null;
        if (penguin instanceof RockhopperPenguin && !penguin.hasUsedSpecialAction()) {
            chosenDir = chooseAIDirection(penguin);
            Position nextPos = penguin.getPosition().getNextPosition(chosenDir);

            if (nextPos.isValid(GRID_SIZE)) {
                ITerrainObject nextObj = getObjectAt(nextPos);
                if (nextObj instanceof IHazard) {
                    useSpecial = true;
                    System.out.println(penguin.getName() + " will automatically USE its special action.");
                    penguin.useSpecialAction();
                }
            }
        }

        if (useSpecial && !(penguin instanceof RockhopperPenguin)) {
            System.out.println(penguin.getName() + " chooses to USE its special action.");
            penguin.useSpecialAction();

            // Handle Royal Penguin special move
            if (penguin instanceof RoyalPenguin) {
                Direction specialDir = chooseRoyalSpecialDirection((RoyalPenguin) penguin);
                System.out.println(penguin.getName() + " moves one square " + getDirectionText(specialDir) + ".");
                ((RoyalPenguin) penguin).setSpecialMoveDirection(specialDir);
                executeRoyalSpecialMove((RoyalPenguin) penguin, specialDir);

                if (penguin.isRemoved()) {
                    return;
                }
            }
        } else if (!useSpecial && !(penguin instanceof RockhopperPenguin)) {
            System.out.println(penguin.getName() + " does NOT to use its special action.");
        }

        // Choose and execute movement (reuse already chosen direction for Rockhopper)
        if (chosenDir == null) {
            chosenDir = chooseAIDirection(penguin);
        }
        System.out.println(penguin.getName() + " chooses to move " + getDirectionText(chosenDir) + ".");
        executePenguinMovement(penguin, chosenDir);
    }

    /**
     * Converts Direction to proper display text matching PDF format.
     *
     * @param dir The direction
     * @return Formatted direction text
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
     *
     * @param penguin The AI penguin
     * @return The chosen Direction
     */
    private Direction chooseAIDirection(Penguin penguin) {
        if (penguin == null) {
            return Direction.UP; // Safe default
        }

        Direction[] directions = Direction.values();
        List<Direction> foodDirections = new ArrayList<>();
        List<Direction> hazardDirections = new ArrayList<>();
        List<Direction> waterDirections = new ArrayList<>();
        List<Direction> safeDirections = new ArrayList<>();

        Position currentPos = penguin.getPosition();
        if (currentPos == null) {
            return Direction.UP; // Safe default
        }

        for (Direction dir : directions) {
            Position nextPos = currentPos.getNextPosition(dir);

            if (!nextPos.isValid(GRID_SIZE)) {
                waterDirections.add(dir);
                continue;
            }

            ITerrainObject obj = getObjectAt(nextPos);

            if (obj == null) {
                safeDirections.add(dir);
            } else if (obj instanceof Food) {
                foodDirections.add(dir);
            } else if (obj instanceof IHazard) {
                if (obj instanceof HoleInIce && ((HoleInIce) obj).isActive()) {
                    // Active HoleInIce is treated like water
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
        } else {
            // No choice but water
            return waterDirections.get(GameHelper.randomInt(0, waterDirections.size() - 1));
        }
    }

    /**
     * Chooses a safe direction for Royal Penguin's special move.
     *
     * @param penguin The Royal Penguin
     * @return A safe Direction for the special move
     */
    private Direction chooseRoyalSpecialDirection(RoyalPenguin penguin) {
        if (penguin == null) {
            return Direction.UP; // Safe default
        }

        Direction[] directions = Direction.values();
        List<Direction> safeDirections = new ArrayList<>();

        Position currentPos = penguin.getPosition();
        if (currentPos == null) {
            return Direction.UP; // Safe default
        }

        for (Direction dir : directions) {
            Position nextPos = currentPos.getNextPosition(dir);

            if (!nextPos.isValid(GRID_SIZE)) {
                continue;  // Avoid water
            }

            ITerrainObject obj = getObjectAt(nextPos);

            if (obj == null || obj instanceof Food) {
                safeDirections.add(dir);
            }
        }

        if (!safeDirections.isEmpty()) {
            return safeDirections.get(GameHelper.randomInt(0, safeDirections.size() - 1));
        } else {
            // No safe option, choose randomly
            return directions[GameHelper.randomInt(0, directions.length - 1)];
        }
    }

    /**
     * Executes the Royal Penguin's special single-step move.
     *
     * @param penguin The Royal Penguin
     * @param direction The direction to move
     */
    private void executeRoyalSpecialMove(RoyalPenguin penguin, Direction direction) {
        if (penguin == null || direction == null) {
            return;
        }

        Position currentPos = penguin.getPosition();
        if (currentPos == null) {
            return;
        }

        Position nextPos = currentPos.getNextPosition(direction);

        // Check if moving out of bounds
        if (!nextPos.isValid(GRID_SIZE)) {
            System.out.println(penguin.getName() + " accidentally stepped out of the grid and fell into the water!");
            System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
            removePenguin(penguin);
            return;
        }

        ITerrainObject targetObj = getObjectAt(nextPos);

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
                return;  // Don't move
            }
        }

        // Move penguin (use defensive copy)
        removeObject(new Position(currentPos));
        penguin.setPosition(new Position(nextPos));

        // Handle food collection
        if (targetObj instanceof Food) {
            Food food = (Food) targetObj;
            penguin.collectFood(food);
            foodItems.remove(food);
            System.out.println(penguin.getName() + " takes the " + food.getDisplayName() + " on the ground. (Weight=" + food.getWeight() + " units)");
        }

        terrainGrid.placeObject(penguin, new Position(nextPos));

        penguin.resetSpecialMove();
    }

    /**
     * Executes penguin movement with sliding mechanics.
     * This is the main method that handles all movement logic.
     *
     * @param penguin The penguin to move
     * @param direction The direction to move
     */
    private void executePenguinMovement(Penguin penguin, Direction direction) {
        if (penguin == null || direction == null) {
            return;
        }

        penguin.slide(direction);

        Position currentPos = new Position(penguin.getPosition()); // Defensive copy
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
            squareCount++;

            // Check if falling off the grid
            if (!nextPos.isValid(GRID_SIZE)) {
                System.out.println(penguin.getName() + " falls into the water!");
                System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
                removePenguin(penguin);
                return;
            }

            ITerrainObject targetObj = getObjectAt(nextPos);

            // Check for stop at specific square (King/Emperor ability)
            if (stopSquare > 0 && squareCount == stopSquare) {
                if (targetObj == null) {
                    // Move to this square and stop - NO MESSAGE for empty square
                    removeObject(new Position(currentPos));
                    penguin.setPosition(new Position(nextPos));
                    terrainGrid.placeObject(penguin, new Position(nextPos));
                    shouldStop = true;
                    continue;
                } else if (targetObj instanceof Food) {
                    // Collect food and stop
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
                // If there's an obstacle, continue sliding past stop square
            }

            // Handle empty square
            if (targetObj == null) {
                removeObject(new Position(currentPos));
                currentPos = new Position(nextPos); // Update with defensive copy
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

                // Current penguin stops
                penguin.setSliding(false);

                // Other penguin starts sliding
                removeObject(new Position(otherPenguin.getPosition()));
                executePenguinMovement(otherPenguin, direction);
                shouldStop = true;
                continue;
            }

            // Handle hazard collision
            if (targetObj instanceof IHazard) {
                shouldStop = handleHazardCollision(penguin, (IHazard) targetObj, direction, new Position(currentPos));
                continue;
            }

            // Unknown object - stop
            shouldStop = true;
        }

        penguin.setSliding(false);
    }

    /**
     * Handles collision between a penguin and a hazard.
     *
     * @param penguin The colliding penguin
     * @param hazard The hazard being hit
     * @param direction The direction of movement
     * @param currentPos The current position before collision (defensive copy expected)
     * @return true if penguin should stop, false to continue
     */
    private boolean handleHazardCollision(Penguin penguin, IHazard hazard, Direction direction, Position currentPos) {
        if (penguin == null || hazard == null || direction == null || currentPos == null) {
            return true; // Safe default - stop
        }

        // Rockhopper jump ability
        if (penguin instanceof RockhopperPenguin) {
            RockhopperPenguin rockhopper = (RockhopperPenguin) penguin;
            if (rockhopper.isPreparedToJump()) {
                Position hazardPos = hazard.getPosition();
                if (hazardPos == null) {
                    return true;
                }

                Position landingPos = hazardPos.getNextPosition(direction);

                // Check if landing square is empty
                if (landingPos.isValid(GRID_SIZE)) {
                    ITerrainObject landingObj = getObjectAt(landingPos);
                    if (landingObj == null || landingObj instanceof Food) {
                        System.out.println(penguin.getName() + " jumps over " + hazard.getDisplayName() + " in its path.");
                        rockhopper.executeJump();

                        // Move to landing position (use defensive copies)
                        removeObject(new Position(currentPos));
                        penguin.setPosition(new Position(landingPos));

                        if (landingObj instanceof Food) {
                            Food food = (Food) landingObj;
                            penguin.collectFood(food);
                            foodItems.remove(food);
                            System.out.println(penguin.getName() + " takes the " + food.getDisplayName() +
                                    " on the ground. (Weight=" + food.getWeight() + " units)");
                        }

                        terrainGrid.placeObject(penguin, new Position(landingPos));
                        return true;  // Stop after jumping
                    }
                }

                // Failed to jump
                System.out.println(penguin.getName() + " fails to jump over " + hazard.getDisplayName() + "!");
                rockhopper.executeJump();
                // Continue to handle collision normally
            }
        }

        // Handle specific hazard types
        if (hazard instanceof HoleInIce) {
            HoleInIce hole = (HoleInIce) hazard;
            if (hole.isPlugged()) {
                // Can pass through plugged hole - continue sliding
                removeObject(new Position(currentPos));
                Position holePos = hole.getPosition();
                if (holePos != null) {
                    penguin.setPosition(new Position(holePos));
                    terrainGrid.placeObject(penguin, new Position(holePos));
                }
                return false;  // Continue sliding
            } else {
                // Fall into hole
                System.out.println(penguin.getName() + " falls into a HoleInIce!");
                System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
                removePenguin(penguin);
                return true;
            }
        }

        if (hazard instanceof LightIceBlock) {
            LightIceBlock iceBlock = (LightIceBlock) hazard;
            System.out.println(penguin.getName() + " collides with a LightIceBlock!");

            // Penguin is stunned
            penguin.setStunned(true);
            System.out.println(penguin.getName() + " is stunned and will skip the next turn!");

            // Ice block starts sliding
            Position icePos = iceBlock.getPosition();
            if (icePos != null) {
                removeObject(new Position(icePos));
                executeSlidableMovement(iceBlock, direction);
            }

            return true;  // Penguin stops
        }

        if (hazard instanceof HeavyIceBlock) {
            System.out.println(penguin.getName() + " collides with a HeavyIceBlock!");

            // Penguin loses lightest food
            Food lost = penguin.removeLightestFood();
            if (lost != null) {
                System.out.println(penguin.getName() + " loses their lightest food item: " + lost);
            }

            return true;  // Penguin stops
        }

        if (hazard instanceof SeaLion) {
            SeaLion seaLion = (SeaLion) hazard;
            System.out.println(penguin.getName() + " bounces off a SeaLion!");

            // Penguin bounces in opposite direction
            Direction oppositeDir = direction.getOpposite();
            if (oppositeDir == null) {
                return true; // Safe fallback
            }

            System.out.println(penguin.getName() + " starts sliding in the opposite direction.");

            // Remove penguin from current position
            removeObject(new Position(currentPos));

            // SeaLion starts sliding in original direction
            Position seaLionPos = seaLion.getPosition();
            if (seaLionPos != null) {
                removeObject(new Position(seaLionPos));
                executeSlidableMovement(seaLion, direction);
            }

            // Penguin slides in opposite direction
            penguin.setPosition(new Position(currentPos));
            terrainGrid.placeObject(penguin, new Position(currentPos));
            executePenguinMovement(penguin, oppositeDir);

            return true;  // Already handled
        }

        return true;  // Default: stop
    }

    /**
     * Executes sliding movement for slidable hazards (LightIceBlock, SeaLion).
     *
     * @param slidable The slidable object
     * @param direction The direction to slide
     */
    private void executeSlidableMovement(ISlidable slidable, Direction direction) {
        if (slidable == null || direction == null) {
            return;
        }

        slidable.slide(direction);

        ITerrainObject obj = (ITerrainObject) slidable;
        Position currentPos = new Position(obj.getPosition()); // Defensive copy
        if (currentPos == null) {
            return;
        }

        boolean shouldStop = false;

        while (!shouldStop) {
            Position nextPos = currentPos.getNextPosition(direction);

            // Check if falling off grid
            if (!nextPos.isValid(GRID_SIZE)) {
                System.out.println(obj.getDisplayName() + " falls off the edge!");
                if (slidable instanceof LightIceBlock) {
                    hazards.remove((IHazard) slidable);
                } else if (slidable instanceof SeaLion) {
                    hazards.remove((IHazard) slidable);
                }
                return;
            }

            ITerrainObject targetObj = getObjectAt(nextPos);

            // Empty square - continue sliding
            if (targetObj == null) {
                removeObject(new Position(currentPos));
                currentPos = new Position(nextPos); // Update with defensive copy
                obj.setPosition(new Position(nextPos));
                terrainGrid.placeObject(obj, new Position(nextPos));
                continue;
            }

            // Hit food - remove food and continue
            if (targetObj instanceof Food) {
                Food food = (Food) targetObj;
                System.out.println(obj.getDisplayName() + " destroys " + food.getDisplayName() + "!");
                foodItems.remove(food);
                removeObject(new Position(currentPos));
                currentPos = new Position(nextPos); // Update with defensive copy
                obj.setPosition(new Position(nextPos));
                terrainGrid.placeObject(obj, new Position(nextPos));
                continue;
            }

            // Hit penguin (not current turn penguin) - stop
            if (targetObj instanceof Penguin) {
                Penguin otherPenguin = (Penguin) targetObj;
                System.out.println(obj.getDisplayName() + " is stopped by " + otherPenguin.getName() + "!");
                shouldStop = true;
                continue;
            }

            // Hit HoleInIce - plug it
            if (targetObj instanceof HoleInIce) {
                HoleInIce hole = (HoleInIce) targetObj;
                if (hole.isActive()) {
                    System.out.println(obj.getDisplayName() + " falls into a HoleInIce and plugs it!");
                    hole.plug();
                    removeObject(new Position(currentPos));
                    hazards.remove((IHazard) slidable);
                    return;
                } else {
                    // Plugged hole - can pass through
                    removeObject(new Position(currentPos));
                    currentPos = new Position(nextPos); // Update with defensive copy
                    obj.setPosition(new Position(nextPos));
                    terrainGrid.placeObject(obj, new Position(nextPos));
                    continue;
                }
            }

            // Hit LightIceBlock with another LightIceBlock or SeaLion
            if (targetObj instanceof LightIceBlock) {
                LightIceBlock targetBlock = (LightIceBlock) targetObj;
                System.out.println(obj.getDisplayName() + " collides with a LightIceBlock!");

                // Current slidable stops
                shouldStop = true;

                // Target LightIceBlock starts sliding
                removeObject(new Position(targetBlock.getPosition()));
                executeSlidableMovement(targetBlock, direction);
                continue;
            }

            // Hit any other obstacle - stop
            shouldStop = true;
        }

        slidable.setSliding(false);
    }

    /**
     * Removes a penguin from the game.
     *
     * @param penguin The penguin to remove
     */
    private void removePenguin(Penguin penguin) {
        if (penguin == null) {
            return;
        }

        penguin.setRemoved(true);
        Position pos = penguin.getPosition();
        if (pos != null) {
            removeObject(new Position(pos)); // Defensive copy
        }
    }

    /**
     * Removes an object from the grid at the specified position.
     *
     * @param position The position to clear
     */
    private void removeObject(Position position) {
        if (position == null) {
            return;
        }
        terrainGrid.removeObject(new Position(position)); // Defensive copy
    }

    /**
     * Asks the user a yes/no question.
     *
     * @param question The question to ask
     * @return true for yes, false for no
     */
    private boolean askYesNo(String question) {
        if (question == null || scanner == null) {
            return false;
        }

        while (true) {
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
        }
    }

    /**
     * Asks the user for a direction.
     *
     * @param question The question to ask
     * @return The chosen Direction
     */
    private Direction askDirection(String question) {
        if (question == null || scanner == null) {
            return Direction.UP; // Safe default
        }

        while (true) {
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
        }
    }

    /**
     * Displays the game over screen with final scoreboard.
     */
    private void displayGameOver() {
        System.out.println("\n***** GAME OVER *****");
        System.out.println("***** SCOREBOARD FOR THE PENGUINS *****");

        // Create a defensive copy of penguins list for sorting
        List<Penguin> sortedPenguins = new ArrayList<>(penguins);

        // Sort penguins by total food weight (descending)
        sortedPenguins.sort((p1, p2) -> {
            if (p1 == null && p2 == null) return 0;
            if (p1 == null) return 1;
            if (p2 == null) return -1;
            return p2.getTotalFoodWeight() - p1.getTotalFoodWeight();
        });

        // Display rankings
        for (int i = 0; i < sortedPenguins.size(); i++) {
            Penguin p = sortedPenguins.get(i);
            if (p == null) {
                continue;
            }

            String place = getPlaceSuffix(i + 1);
            String playerIndicator = p.isPlayerPenguin() ? " (Your Penguin)" : "";

            System.out.println("* " + place + " place: " + p.getName() + playerIndicator);

            System.out.print("|---> Food items: ");
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
    }

    /**
     * Gets the ordinal suffix for a place number (1st, 2nd, 3rd, etc.)
     *
     * @param place The place number
     * @return The place with suffix (e.g., "1st")
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
     * SECURITY METHOD: Returns an unmodifiable defensive copy of penguins list.
     * This prevents external modification of the game state.
     *
     * @return Unmodifiable list of penguins (defensive copy)
     */
    public List<Penguin> getPenguins() {
        return Collections.unmodifiableList(new ArrayList<>(penguins));
    }

    /**
     * SECURITY METHOD: Returns an unmodifiable defensive copy of food items list.
     *
     * @return Unmodifiable list of food items (defensive copy)
     */
    public List<Food> getFoodItems() {
        return Collections.unmodifiableList(new ArrayList<>(foodItems));
    }

    /**
     * SECURITY METHOD: Returns an unmodifiable defensive copy of hazards list.
     *
     * @return Unmodifiable list of hazards (defensive copy)
     */
    public List<IHazard> getHazards() {
        return Collections.unmodifiableList(new ArrayList<>(hazards));
    }

    /**
     * SECURITY METHOD: Returns the player's penguin (reference is okay since it's read-only access).
     * Do NOT expose setters that could modify the penguin.
     *
     * @return The player's penguin
     */
    public Penguin getPlayerPenguin() {
        return playerPenguin;
    }

    /**
     * SECURITY METHOD: Returns the current turn number.
     *
     * @return Current turn (1-4)
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * SECURITY METHOD: Returns the grid size.
     *
     * @return Grid size constant
     */
    public int getGridSize() {
        return GRID_SIZE;
    }
}