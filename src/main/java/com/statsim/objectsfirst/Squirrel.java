package com.statsim.objectsfirst;

import com.statsim.predatorprey.DEBUG;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A simple model of a squirrel.
 * squirrel age, move, breed, and die.
 *
 */
public class Squirrel extends Animal
{
    // Characteristics shared by all squirrels (class variables).

    // The age at which a squirrel can start to breed.
    private static final int BREEDING_AGE = 1;
    // The age to which a squirrel can live.
    private static final int MAX_AGE = 3;
    // The likelihood of a squirrel breeding.
    private static final double BREEDING_PROBABILITY = 0.13;

    private static final int MIN_DIE_AGE = 2;

    private static final double DIE_BEFORE_MAX_AGE_PROBABILITY = 0.01;

    private static final double FIND_PINECONE_PROBABILITY = 0.8;

    private static final int PINECONE_FOOD_VALUE = 5;

    private static final int BREED_PERIOD_TIME = 7;

    private static final int MAX_BREED_COUNT = 2;

    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    private int foodLevel;

    private int breedPeriodOffset;

    /**
     * Create a new squirrel. A squirrel may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the squirrel will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Squirrel(boolean randomAge, Field field, Location location, int tickBorn)
    {
        super(field, location, tickBorn);
        this.setAnimalType(AnimalType.SQUIRREL);
        if(randomAge) {
            this.setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(PINECONE_FOOD_VALUE)+1;
        }
        else {
            this.setAge(0);
            foodLevel = PINECONE_FOOD_VALUE;
        }
        DEBUG.SQ_TOTALSPAWNED++;
    }

    @Override
    public void newYear() {
        generateBreedOffset();
        this.resetBreedCount();
    }

    private void generateBreedOffset(){
        this.breedPeriodOffset = (int)Math.floor(Math.random() * 100);
    }

    /**
     * This is what the squirrel does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param newSquirrel A list to return newly born squirrels.
     */
    public void act(List<Animal> newSquirrel)
    {
        this.haveBirthday();
        this.incrementHunger();
        this.isTooOld();
        findFood();
        if(isAlive()) {
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
                giveBirth(newSquirrel);
            }
            else {
                setDead();
                this.setDeathCause(DeathCause.OVERCROWD);
                DEBUG.SQ_DIE_LOC++;

            }
        }
    }

    /**
     * Increase the age. This could result in the squirrel's death.
     */
    private void isTooOld()
    {
        int age = this.getAge();
        if(age > MAX_AGE) {
            setDead();
            this.setDeathCause(DeathCause.AGE);
            DEBUG.SQ_DIE_AGE++;
        }else if (age >= MIN_DIE_AGE){
            if (Math.random() <= DIE_BEFORE_MAX_AGE_PROBABILITY) {
                setDead();
                this.setDeathCause(DeathCause.AGE);
                DEBUG.SQ_DIE_AGE++;
            }
        }
    }

    /**
     * Make this squirrel more hungry. This could result in the squirrel's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
            this.setDeathCause(DeathCause.HUNGER);
            DEBUG.SQ_STARVATION++;

        }
    }

    /**
     * Look for squirrels adjacent to the current location.
     * Only the first live squirrel is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private void findFood()
    {
        if (Math.random() <= FIND_PINECONE_PROBABILITY) foodLevel = PINECONE_FOOD_VALUE;
    }

    /**
     * Check whether or not this squirrel is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param newSquirrels A list to return newly born squirreles.
     */
    private void giveBirth(List<Animal> newSquirrels) {
        boolean canBreed = this.getCanBreed();
        if (canBreed && isOnBreedingPeriod()) {
            // New squirreles are born into adjacent locations.
            // Get a list of adjacent free locations.
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            for (int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Squirrel young = new Squirrel(false, field, loc, this.getCurrentTick());
                newSquirrels.add(young);
            }

        } else if (!canBreed) {
            isOldEnoughToBreed();
        }
    }

    private boolean isOnBreedingPeriod() {
        return (this.getCurrentTick() + this.breedPeriodOffset) % Math.ceil(Simulator.CYCLE_LENGTH / MAX_BREED_COUNT) < BREED_PERIOD_TIME;
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
