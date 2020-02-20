package com.statsim.objectsfirst;

public enum AnimalType {
    UNDEFINED("UNDEFINED"),
    HAWK("HAWK"),
    SQUIRREL("SQUIRREL");

    private String name;
    AnimalType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
