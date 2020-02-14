package com.statsim.objectsfirst;

import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a hawk.
 * Hawks age, move, eat squirrels, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Hawk extends Animal
{
    // Characteristics shared by all hawkes (class variables).

    // The age at which a hawk can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a hawk can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a hawk breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single squirrel. In effect, this is the
    // number of steps a hawk can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 9;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    // The hawk's age.
    private int age;
    // The hawk's food level, which is increased by eating squirrels.
    private int foodLevel;

    /**
     * Create a hawk. A hawk can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the hawk will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hawk(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    /**
     * This is what the hawk does most of the time: it hunts for
     * squirrels. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newHawks A list to return newly born hawkes.
     */
    public void act(List<Animal> newHawks)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newHawks);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age. This could result in the hawk's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this hawk more hungry. This could result in the hawk's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for squirrels adjacent to the current location.
     * Only the first live squirrel is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Squirrel) {
                Squirrel squirrel = (Squirrel) animal;
                if(squirrel.isAlive()) {
                    squirrel.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this hawk is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHawks A list to return newly born hawkes.
     */
    private void giveBirth(List<Animal> newHawks)
    {
        // New hawkes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Hawk young = new Hawk(false, field, loc);
            newHawks.add(young);
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A hawk can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
