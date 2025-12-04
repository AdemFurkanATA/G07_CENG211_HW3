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
import java.util.List;
import java.util.Scanner;

/**
 * Main game class that manages the icy terrain grid and game logic.
 * Contains the terrain grid, game objects, and orchestrates the game flow.
 * Demonstrates the use of Lists, ArrayLists, and object-oriented design.
 */
public class IcyTerrain {

    private static final int GRID_SIZE = 10;
    private static final int NUM_PENGUINS = 3;
    private static final int NUM_HAZARDS = 15;
    private static final int NUM_FOOD = 20;
    private static final int NUM_TURNS = 4;
    private static final double AI_SPECIAL_ACTION_CHANCE = 0.30;

    private TerrainGrid terrainGrid;
    private List<Penguin> penguins;
    private List<Food> foodItems;
    private List<IHazard> hazards;
    private Penguin playerPenguin;
    private Scanner scanner;
    private int currentTurn;

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

        generatePenguins();
        generateHazards();
        generateFood();

        displayGrid();
        displayPenguinTypes();

        playGame();

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
            terrainGrid.placeObject(penguin, pos);
        }

        int playerIndex = GameHelper.randomInt(0, NUM_PENGUINS - 1);
        playerPenguin = penguins.get(playerIndex);
        playerPenguin.setPlayerPenguin(true);
    }

    /**
     * Creates a penguin of the specified type.
     */
    private Penguin createPenguin(String name, Position position, PenguinType type) {
        switch (type) {
            case KING:
                return new KingPenguin(name, position);
            case EMPEROR:
                return new EmperorPenguin(name, position);
            case ROYAL:
                return new RoyalPenguin(name, position);
            case ROCKHOPPER:
                return new RockhopperPenguin(name, position);
            default:
                return new KingPenguin(name, position);
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
            terrainGrid.placeObject((ITerrainObject) hazard, pos);
        }
    }

    /**
     * Creates a random hazard at the specified position.
     */
    private IHazard createRandomHazard(Position position) {
        int type = GameHelper.randomInt(0, 3);

        switch (type) {
            case 0:
                return new LightIceBlock(position);
            case 1:
                return new HeavyIceBlock(position);
            case 2:
                return new SeaLion(position);
            case 3:
                return new HoleInIce(position);
            default:
                return new HeavyIceBlock(position);
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
            terrainGrid.placeObject(food, pos);
        }
    }

    /**
     * Finds an empty edge position for penguin placement.
     */
    private Position findEmptyEdgePosition() {
        Position pos;
        do {
            pos = GameHelper.randomEdgePosition(GRID_SIZE);
        } while (terrainGrid.getObjectAt(pos) != null);
        return pos;
    }

    /**
     * Finds an empty position anywhere on the grid.
     */
    private Position findEmptyPosition() {
        Position pos;
        do {
            pos = GameHelper.randomPosition(GRID_SIZE);
        } while (terrainGrid.getObjectAt(pos) != null);
        return pos;
    }

    /**
     * Gets the object at the specified position.
     */
    private ITerrainObject getObjectAt(Position position) {
        return terrainGrid.getObjectAt(position);
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
     */
    private void playTurn(Penguin penguin) {
        System.out.println("\n*** Turn " + currentTurn + " – " + penguin.getName() +
                (penguin.isPlayerPenguin() ? " (Your Penguin):" : ":"));

        if (penguin.isStunned()) {
            System.out.println(penguin.getName() + " is stunned and skips this turn!");
            penguin.setStunned(false);
            return;
        }

        if (penguin.isPlayerPenguin()) {
            handlePlayerTurn(penguin);
        } else {
            handleAITurn(penguin);
        }

        System.out.println("New state of the grid:");
        displayGrid();
    }

    /**
     * Handles a turn for the player's penguin.
     */
    private void handlePlayerTurn(Penguin penguin) {
        boolean useSpecial = false;
        if (!penguin.hasUsedSpecialAction()) {
            useSpecial = askYesNo("Will " + penguin.getName() + " use its special action? Answer with Y or N");
        }

        if (useSpecial && penguin instanceof RoyalPenguin) {
            penguin.useSpecialAction();
            Direction specialDir = askDirection("Which direction for the special single-step move? Answer with U (Up), D (Down), L (Left), R (Right)");
            ((RoyalPenguin) penguin).setSpecialMoveDirection(specialDir);
            System.out.println(penguin.getName() + " moves one square to the " + specialDir + ".");
            executeRoyalSpecialMove((RoyalPenguin) penguin, specialDir);

            if (penguin.isRemoved()) {
                return;
            }
        } else if (useSpecial) {
            penguin.useSpecialAction();
        }

        Direction direction = askDirection("Which direction will " + penguin.getName() + " move? Answer with U (Up), D (Down), L (Left), R (Right)");

        if (penguin instanceof RockhopperPenguin && !penguin.hasUsedSpecialAction()) {
            if (shouldRockhopperAutoUse((RockhopperPenguin) penguin, direction)) {
                System.out.println(penguin.getName() + " will automatically USE its special action.");
                penguin.useSpecialAction();
            }
        }

        executePenguinMovement(penguin, direction);
    }

    /**
     * Checks if Rockhopper should automatically use its ability.
     */
    private boolean shouldRockhopperAutoUse(RockhopperPenguin penguin, Direction direction) {
        Position checkPos = new Position(penguin.getPosition());

        for (int i = 0; i < GRID_SIZE; i++) {
            checkPos = checkPos.getNextPosition(direction);

            if (!checkPos.isValid(GRID_SIZE)) {
                return false;
            }

            ITerrainObject obj = getObjectAt(checkPos);

            if (obj instanceof IHazard) {
                return true;
            }

            if (obj instanceof Penguin) {
                return false;
            }
        }

        return false;
    }

    /**
     * Handles a turn for an AI-controlled penguin.
     */
    private void handleAITurn(Penguin penguin) {
        boolean useSpecial = !penguin.hasUsedSpecialAction() &&
                GameHelper.randomChance(AI_SPECIAL_ACTION_CHANCE);

        Direction chosenDir = chooseAIDirection(penguin);

        if (penguin instanceof RockhopperPenguin && !penguin.hasUsedSpecialAction()) {
            if (shouldRockhopperAutoUse((RockhopperPenguin) penguin, chosenDir)) {
                useSpecial = true;
            }
        }

        if (useSpecial) {
            System.out.println(penguin.getName() + " chooses to USE its special action.");
            penguin.useSpecialAction();

            if (penguin instanceof RoyalPenguin) {
                Direction specialDir = chooseRoyalSpecialDirection((RoyalPenguin) penguin);
                System.out.println(penguin.getName() + " moves one square to the " + specialDir + ".");
                ((RoyalPenguin) penguin).setSpecialMoveDirection(specialDir);
                executeRoyalSpecialMove((RoyalPenguin) penguin, specialDir);

                if (penguin.isRemoved()) {
                    return;
                }

                chosenDir = chooseAIDirection(penguin);
            }
        } else {
            System.out.println(penguin.getName() + " does NOT to use its special action.");
        }

        System.out.println(penguin.getName() + " chooses to move " + chosenDir + ".");
        executePenguinMovement(penguin, chosenDir);
    }

    /**
     * Chooses a direction for AI penguin based on priorities.
     */
    private Direction chooseAIDirection(Penguin penguin) {
        Direction[] directions = Direction.values();
        List<Direction> foodDirections = new ArrayList<>();
        List<Direction> hazardDirections = new ArrayList<>();
        List<Direction> waterDirections = new ArrayList<>();
        List<Direction> safeDirections = new ArrayList<>();

        for (Direction dir : directions) {
            Position nextPos = penguin.getPosition().getNextPosition(dir);

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
                if (!(obj instanceof HoleInIce && ((HoleInIce) obj).isActive())) {
                    hazardDirections.add(dir);
                } else {
                    waterDirections.add(dir);
                }
            } else {
                safeDirections.add(dir);
            }
        }

        if (!foodDirections.isEmpty()) {
            return foodDirections.get(GameHelper.randomInt(0, foodDirections.size() - 1));
        } else if (!hazardDirections.isEmpty()) {
            return hazardDirections.get(GameHelper.randomInt(0, hazardDirections.size() - 1));
        } else if (!safeDirections.isEmpty()) {
            return safeDirections.get(GameHelper.randomInt(0, safeDirections.size() - 1));
        } else {
            return waterDirections.get(GameHelper.randomInt(0, waterDirections.size() - 1));
        }
    }

    /**
     * Chooses a safe direction for Royal Penguin's special move.
     */
    private Direction chooseRoyalSpecialDirection(RoyalPenguin penguin) {
        Direction[] directions = Direction.values();
        List<Direction> safeDirections = new ArrayList<>();

        for (Direction dir : directions) {
            Position nextPos = penguin.getPosition().getNextPosition(dir);

            if (!nextPos.isValid(GRID_SIZE)) {
                continue;
            }

            ITerrainObject obj = getObjectAt(nextPos);

            if (obj == null || obj instanceof Food) {
                safeDirections.add(dir);
            }
        }

        if (!safeDirections.isEmpty()) {
            return safeDirections.get(GameHelper.randomInt(0, safeDirections.size() - 1));
        } else {
            return directions[GameHelper.randomInt(0, directions.length - 1)];
        }
    }

    /**
     * Executes the Royal Penguin's special single-step move.
     */
    private void executeRoyalSpecialMove(RoyalPenguin penguin, Direction direction) {
        Position currentPos = penguin.getPosition();
        Position nextPos = currentPos.getNextPosition(direction);

        if (!nextPos.isValid(GRID_SIZE)) {
            System.out.println(penguin.getName() + " accidentally stepped out of the grid and fell into the water!");
            System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
            removePenguin(penguin);
            return;
        }

        ITerrainObject targetObj = getObjectAt(nextPos);

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

        removeObject(currentPos);
        penguin.setPosition(nextPos);

        if (targetObj instanceof Food) {
            Food food = (Food) targetObj;
            penguin.collectFood(food);
            foodItems.remove(food);
            System.out.println(penguin.getName() + " takes the " + food.getDisplayName() + " on the ground. (Weight=" + food.getWeight() + " units)");
        }

        terrainGrid.placeObject(penguin, nextPos);

        penguin.resetSpecialMove();
    }

    /**
     * Executes penguin movement with sliding mechanics.
     */
    private void executePenguinMovement(Penguin penguin, Direction direction) {
        penguin.slide(direction);

        Position currentPos = new Position(penguin.getPosition());
        int squareCount = 0;
        boolean shouldStop = false;

        int stopSquare = -1;
        if (penguin.hasUsedSpecialAction()) {
            if (penguin instanceof KingPenguin) {
                stopSquare = ((KingPenguin) penguin).getStopSquare();
            } else if (penguin instanceof EmperorPenguin) {
                stopSquare = ((EmperorPenguin) penguin).getStopSquare();
            }
        }

        while (!shouldStop) {
            Position nextPos = currentPos.getNextPosition(direction);
            squareCount++;

            if (!nextPos.isValid(GRID_SIZE)) {
                System.out.println(penguin.getName() + " falls into the water!");
                System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
                removePenguin(penguin);
                return;
            }

            ITerrainObject targetObj = getObjectAt(nextPos);

            if (stopSquare > 0 && squareCount == stopSquare) {
                if (targetObj == null) {
                    removeObject(currentPos);
                    penguin.setPosition(nextPos);
                    terrainGrid.placeObject(penguin, nextPos);
                    System.out.println(penguin.getName() + " stops at an empty square using its special action.");
                    shouldStop = true;
                    continue;
                } else if (targetObj instanceof Food) {
                    removeObject(currentPos);
                    penguin.setPosition(nextPos);
                    Food food = (Food) targetObj;
                    penguin.collectFood(food);
                    foodItems.remove(food);
                    terrainGrid.placeObject(penguin, nextPos);
                    System.out.println(penguin.getName() + " takes the " + food.getDisplayName() +
                            " on the ground. (Weight=" + food.getWeight() + " units)");
                    System.out.println(penguin.getName() + " stops using its special action.");
                    shouldStop = true;
                    continue;
                }
            }

            if (targetObj == null) {
                removeObject(currentPos);
                currentPos = nextPos;
                penguin.setPosition(nextPos);
                terrainGrid.placeObject(penguin, nextPos);
                continue;
            }

            if (targetObj instanceof Food) {
                removeObject(currentPos);
                Food food = (Food) targetObj;
                penguin.collectFood(food);
                foodItems.remove(food);
                penguin.setPosition(nextPos);
                terrainGrid.placeObject(penguin, nextPos);
                System.out.println(penguin.getName() + " takes the " + food.getDisplayName() +
                        " on the ground. (Weight=" + food.getWeight() + " units)");
                shouldStop = true;
                continue;
            }

            if (targetObj instanceof Penguin) {
                Penguin otherPenguin = (Penguin) targetObj;
                System.out.println(penguin.getName() + " collides with " + otherPenguin.getName() + "!");

                penguin.setSliding(false);

                removeObject(otherPenguin.getPosition());
                executePenguinMovement(otherPenguin, direction);
                shouldStop = true;
                continue;
            }

            if (targetObj instanceof IHazard) {
                shouldStop = handleHazardCollision(penguin, (IHazard) targetObj, direction, currentPos);
                continue;
            }

            shouldStop = true;
        }

        penguin.setSliding(false);
    }

    /**
     * Handles collision between a penguin and a hazard.
     */
    private boolean handleHazardCollision(Penguin penguin, IHazard hazard, Direction direction, Position currentPos) {
        if (penguin instanceof RockhopperPenguin) {
            RockhopperPenguin rockhopper = (RockhopperPenguin) penguin;
            if (rockhopper.isPreparedToJump()) {
                Position hazardPos = hazard.getPosition();
                Position landingPos = hazardPos.getNextPosition(direction);

                if (landingPos.isValid(GRID_SIZE)) {
                    ITerrainObject landingObj = getObjectAt(landingPos);
                    if (landingObj == null || landingObj instanceof Food) {
                        System.out.println(penguin.getName() + " jumps over " + hazard.getShorthand() + " in its path.");
                        rockhopper.executeJump();

                        removeObject(currentPos);
                        penguin.setPosition(landingPos);

                        if (landingObj instanceof Food) {
                            Food food = (Food) landingObj;
                            penguin.collectFood(food);
                            foodItems.remove(food);
                            System.out.println(penguin.getName() + " takes the " + food.getDisplayName() +
                                    " on the ground. (Weight=" + food.getWeight() + " units)");
                            terrainGrid.placeObject(penguin, landingPos);
                            return true;
                        }

                        terrainGrid.placeObject(penguin, landingPos);

                        return false;
                    }
                }

                System.out.println(penguin.getName() + " fails to jump and collides with the hazard!");
                rockhopper.executeJump();
            }
        }

        if (hazard instanceof HoleInIce) {
            HoleInIce hole = (HoleInIce) hazard;
            if (hole.isPlugged()) {
                removeObject(currentPos);
                penguin.setPosition(hole.getPosition());
                terrainGrid.placeObject(penguin, hole.getPosition());
                return false;
            } else {
                System.out.println(penguin.getName() + " falls into the water due to " + hazard.getShorthand() + " in its path.");
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

            removeObject(iceBlock.getPosition());
            executeSlidableMovement(iceBlock, direction);

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
            System.out.println(penguin.getName() + " starts sliding in the opposite direction.");

            removeObject(currentPos);

            removeObject(seaLion.getPosition());
            executeSlidableMovement(seaLion, direction);

            penguin.setPosition(currentPos);
            terrainGrid.placeObject(penguin, currentPos);
            executePenguinMovement(penguin, oppositeDir);

            return true;
        }

        return true;
    }

    /**
     * Executes sliding movement for slidable hazards.
     */
    private void executeSlidableMovement(ISlidable slidable, Direction direction) {
        slidable.slide(direction);

        ITerrainObject obj = (ITerrainObject) slidable;
        Position currentPos = new Position(obj.getPosition());
        boolean shouldStop = false;

        while (!shouldStop) {
            Position nextPos = currentPos.getNextPosition(direction);

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

            if (targetObj == null) {
                removeObject(currentPos);
                currentPos = nextPos;
                obj.setPosition(nextPos);
                terrainGrid.placeObject(obj, nextPos);
                continue;
            }

            if (targetObj instanceof Food) {
                Food food = (Food) targetObj;
                System.out.println(obj.getDisplayName() + " destroys " + food.getDisplayName() + "!");
                foodItems.remove(food);
                removeObject(currentPos);
                currentPos = nextPos;
                obj.setPosition(nextPos);
                terrainGrid.placeObject(obj, nextPos);
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
                    removeObject(currentPos);
                    hazards.remove((IHazard) slidable);
                    return;
                }
            }

            shouldStop = true;
        }

        slidable.setSliding(false);
    }

    /**
     * Removes a penguin from the game.
     */
    private void removePenguin(Penguin penguin) {
        penguin.setRemoved(true);
        removeObject(penguin.getPosition());
    }

    /**
     * Removes an object from the grid at the specified position.
     */
    private void removeObject(Position position) {
        terrainGrid.removeObject(position);
    }

    /**
     * Asks the user a yes/no question.
     */
    private boolean askYesNo(String question) {
        while (true) {
            System.out.print(question + " --> ");
            String input = scanner.nextLine().trim().toUpperCase();

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
     */
    private Direction askDirection(String question) {
        while (true) {
            System.out.print(question + " --> ");
            String input = scanner.nextLine().trim();
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

        penguins.sort((p1, p2) -> p2.getTotalFoodWeight() - p1.getTotalFoodWeight());

        for (int i = 0; i < penguins.size(); i++) {
            Penguin p = penguins.get(i);
            String place = getPlaceSuffix(i + 1);
            String playerIndicator = p.isPlayerPenguin() ? " (Your Penguin)" : "";

            System.out.println("* " + place + " place: " + p.getName() + playerIndicator);

            System.out.print("|---> Food items: ");
            List<Food> foods = p.getCollectedFood();
            for (int j = 0; j < foods.size(); j++) {
                Food food = foods.get(j);
                System.out.print(food.getType().toString() + " (" + food.getWeight() + " units)");
                if (j < foods.size() - 1) {
                    System.out.print(", ");
                }
            }
            if (foods.isEmpty()) {
                System.out.print("None");
            }
            System.out.println();

            System.out.println("|---> Total weight: " + p.getTotalFoodWeight() + " units");
        }
    }

    /**
     * Gets the ordinal suffix for a place number.
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
}