package com.enums;

public enum FoodType {
    KRILL("Kr"),
    CRUSTACEAN("Cr"),
    ANCHOVY("An"),
    SQUID("Sq"),
    MACKEREL("Ma");

    private final String shorthand;
    private static final FoodType[] VALUES = values();

    FoodType(String shorthand) {
        this.shorthand = shorthand;
    }

    public String getShorthand() {
        return shorthand;
    }

    public static FoodType getRandomType() {
        int randomIndex = (int) (Math.random() * VALUES.length);
        return VALUES[randomIndex];
    }

    @Override
    public String toString() {
        switch (this) {
            case KRILL:
                return "Krill";
            case CRUSTACEAN:
                return "Crustacean";
            case ANCHOVY:
                return "Anchovy";
            case SQUID:
                return "Squid";
            case MACKEREL:
                return "Mackerel";
            default:
                return this.name();
        }
    }
}