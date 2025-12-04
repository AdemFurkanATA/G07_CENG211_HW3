package com.enums;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction getOpposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return null;
        }
    }

    public static Direction fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        switch (input.trim().toUpperCase()) {
            case "U":
                return UP;
            case "D":
                return DOWN;
            case "L":
                return LEFT;
            case "R":
                return RIGHT;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case UP:
                return "UPWARDS";
            case DOWN:
                return "DOWNWARDS";
            case LEFT:
                return "to the LEFT";
            case RIGHT:
                return "to the RIGHT";
            default:
                return this.name();
        }
    }
}