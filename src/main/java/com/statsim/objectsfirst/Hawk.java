package com.statsim.objectsfirst;

import com.statsim.predatorprey.DEBUG;

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
public class Hawk extends Animal {
    // Characteristics shared by all hawkes (class variables).

    // The age at which a hawk can start to breed.
    private static final int BREEDING_AGE = 1;
    // The age to which a hawk can live.
    private static final int MAX_AGE = 11;
    // The age to which the hawk can die from
    private static final int MIN_DIE_AGE = 9;

    private static final double DIE_BEFORE_MAX_AGE_PROBABILITY = 0.05;

    // The likelihood of a hawk breeding.
    private static final double BREEDING_PROBABILITY = 0.012;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single squirrel. In effect, this is the
    // number of steps a hawk can go before it has to eat again.
    private static final int SQUIRREL_FOOD_VALUE = 11;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Tick at which the hawk can breed at
    private static final int BREED_PERIOD_START = 90;

    //Tick at which the hack can no longer breed
    private static final int BREED_PERIOD_END = 140;

    // The hawk's food level, which is increased by eating squirrels.
    private int foodLevel;


    /**
     * Create a hawk. A hawk can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the hawk will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Hawk(boolean randomAge, Field field, Location location, int tickBorn) {
        super(field, location, tickBorn);
        this.setAnimalType(AnimalType.HAWK);
        if (randomAge) {
            this.setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(SQUIRREL_FOOD_VALUE);
        } else {
            this.setAge(0);
            foodLevel = SQUIRREL_FOOD_VALUE;
        }
        DEBUG.HW_TOTALSPAWNED++;
    }

    @Override
    public void newYear() {
        this.resetBreedCount();
    }

    /**
     * This is what the hawk does most of the time: it hunts for
     * squirrels. In the process, it might breed, die of hunger,
     * or die of old age.
     *
     * @param field    The field currently occupied.
     * @param newHawks A list to return newly born hawkes.
     */
    public void act(List<Animal> newHawks) {
        this.haveBirthday();
        this.incrementHunger();
        this.isTooOld();
        if (isAlive()) {
            giveBirth(newHawks);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if (newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().doubleFreeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
                this.setDeathCause(DeathCause.OVERCROWD);
                DEBUG.HW_DIE_LOC++;

            }
        }
    }

    /**
     * Increase the age. This could result in the hawk's death.
     */
    private void isTooOld() {
        int age = this.getAge();
        if (age > MAX_AGE) {
            setDead();
            this.setDeathCause(DeathCause.AGE);
            DEBUG.HW_DIE_AGE++;
        } else if (age >= MIN_DIE_AGE) {
            if (Math.random() <= DIE_BEFORE_MAX_AGE_PROBABILITY) {
                setDead();
                this.setDeathCause(DeathCause.AGE);
                DEBUG.HW_DIE_AGE++;
            }
        }
    }

    /**
     * Make this hawk more hungry. This could result in the hawk's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
            this.setDeathCause(DeathCause.HUNGER);
            DEBUG.HW_STARVATION++;
        }
    }

    /**
     * Look for squirrels adjacent to the current location.
     * Only the first live squirrel is eaten.
     *
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.doubleAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Squirrel) {
                Squirrel squirrel = (Squirrel) animal;
                if (squirrel.isAlive()) {
                    squirrel.setDead(true);
                    foodLevel = SQUIRREL_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this hawk is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param newHawks A list to return newly born hawkes.
     */
    private void giveBirth(List<Animal> newHawks) {
        boolean canBreed = this.getCanBreed();
        if (canBreed && isOnBreedingPeriod() && this.getBreedCount() == 0) {
            // New hawkes are born into adjacent locations.
            // Get a list of adjacent free locations.
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            for (int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Hawk young = new Hawk(false, field, loc, this.getCurrentTick());
                newHawks.add(young);
            }
        } else if (!canBreed) {
            isOldEnoughToBreed();
        }
    }

    private boolean isOnBreedingPeriod() {
        int day = this.getCurrentTick() % 365;
        return (day >= BREED_PERIOD_START && day <= BREED_PERIOD_END);
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    private int breed() {
        int births = 0;
        if (this.getCanBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE);
            if (births == 0) {
                births = Math.round((float)Math.random());
            } else {
                births += 1;
            }
        }
        return births;
    }

    @Override
    protected void isOldEnoughToBreed() {
        boolean canBreed = this.getAge() >= BREEDING_AGE;
        if (canBreed) {
            this.setCanBreed(true);
        }
    }

}
