package com.statsim.objectsfirst;

import java.util.List;

/**
 * A class representing shared characteristics of animals.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal {
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // Counter of how many times an animals has bred
    private int breedCount = 0;

    private int tickBorn = 0;

    private int ticks = 1;

    private boolean isBreedable = false;

    private int age = 0;

    private AnimalType animalType = AnimalType.UNDEFINED;

    private DeathCause causeOfDeath = DeathCause.UNDEFINED;

    /**
     * Create a new animal at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location, int tickBorn) {
        alive = true;
        this.field = field;
        setLocation(location);
        this.tickBorn = tickBorn % Simulator.CYCLE_LENGTH;
    }

    protected void setAnimalType(AnimalType type) {
        this.animalType = type;
    }

    public AnimalType getAnimalType() {
        return this.animalType;
    }

    protected void setDeathCause(DeathCause cause) {
        this.causeOfDeath = cause;
    }

    public DeathCause getDeathCouse() {
        return this.causeOfDeath;
    }

    /**
     * Increment the bred counter to zero
     */
    protected void incrementBreedCount() {
        this.breedCount++;
    }

    /**
     * Resets the bred counter for an animal
     */
    protected void resetBreedCount() {
        this.breedCount = 0;
    }

    /**
     * Returns how many times an animal has bred
     *
     * @return times an animal has bred
     */
    protected int getBreedCount() {
        return this.breedCount;
    }

    /**
     * Sets the can breed flag for the animal
     *
     * @param breed true if can breed else false
     */
    protected void setCanBreed(boolean breed) {
        this.isBreedable = breed;
    }

    /**
     * Returns true of the animal ca breed else false
     *
     * @return true if can breed else false
     */
    protected boolean getCanBreed() {
        return this.isBreedable;
    }

    /**
     * Returns the tick when the animal was born
     *
     * @return tick the animal was born
     */
    protected int getTickBorn() {
        return this.tickBorn;
    }

    /**
     * Returns number of ticks alive
     *
     * @return number of ticks alive
     */
    protected int getTickAge() {
        return this.ticks - this.tickBorn;
    }


    /**
     * Sets the current simulator tick value
     *
     * @param tick the current simulator tick
     */
    public void setTick(int tick) {
        this.ticks = tick;
    }

    /**
     * Returns the current tick
     *
     * @return current tick
     */
    protected int getCurrentTick() {
        return this.ticks;
    }

    protected void setAge(int age) {
        this.age = age;
    }

    protected void incrementAge() {
        this.age++;
    }

    protected void haveBirthday() {
        if (tickBorn == this.ticks % Simulator.CYCLE_LENGTH) this.incrementAge();
    }

    protected int getAge() {
        return this.age;
    }

    public abstract void newYear();

    /**
     * Check if the animal is old enough to breed
     */
    protected abstract void isOldEnoughToBreed();

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     *
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     *
     * @return true if the animal is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead(boolean eaten) {
        if (eaten) {
            this.setDeathCause(DeathCause.EATEN);
        }
        this.setDead();
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     *
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the animal at the new location in the given field.
     *
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     *
     * @return The animal's field.
     */
    protected Field getField() {
        return field;
    }
}
