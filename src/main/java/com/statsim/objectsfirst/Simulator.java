package com.statsim.objectsfirst;

import com.statsim.predatorprey.CsvLogger;
import com.statsim.predatorprey.DEBUG;

import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing squirrels and hawk.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator {
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 200;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 200;
    // The probability that a hawk will be created in any given grid position.
    //0.001
    private static final double HAWK_CREATION_PROBABILITY = 0.0015;
    // The probability that a squirrel will be created in any given grid position.
    //0.2
    private static final double SQUIRREL_CREATION_PROBABILITY = 0.35;

    // Time before animal resets
    public static final int CYCLE_LENGTH = 365;

    // List of animals in the field.
    private List<Animal> animals;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // The current step of the simulation.
    private int localStep;
    // A graphical view of the simulation.
    private SimulatorView view;

    CsvLogger logger;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        String[] header = {"Animal", "Age", "Time of death", "Death cause", "Total population"};
        try {
            logger = new CsvLogger("./log-" + LocalDateTime.now().toString() + ".csv", header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Squirrel.class, Color.GREEN);
        view.setColor(Hawk.class, Color.RED);

        // Setup a valid starting point.

        reset();

    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation() {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     *
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
//            delay(60);   // uncomment this to run more slowly
        }
        try {
            logger.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * hawk and squirrel.
     */
    public void simulateOneStep() {
        step++;
        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();
        // Let all squirrels act.
        for (Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.setTick(step);
            if (this.localStep % CYCLE_LENGTH == 0) animal.newYear();
            animal.act(newAnimals);
            if (!animal.isAlive()) {
                String[] log = {animal.getAnimalType().toString(), "" + animal.getAge(), "" + this.step, animal.getDeathCouse().toString(), "" + animals.size()};
                logger.log(log);
                it.remove();
            }
        }

        // Add the newly born hawks and squirrels to the main lists.
        animals.addAll(newAnimals);

        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        animals.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    /**
     * Randomly populate the field with hawk and squirrels.
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= HAWK_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Hawk hawk = new Hawk(true, field, location, this.step);
                    animals.add(hawk);
                    DEBUG.HW_START++;
                } else if (rand.nextDouble() <= SQUIRREL_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Squirrel squirrel = new Squirrel(true, field, location, this.step);
                    animals.add(squirrel);
                    DEBUG.SQ_START++;
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Pause for a given time.
     *
     * @param millisec The time to pause for, in milliseconds
     */
    private void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ie) {
            // wake up
        }
    }
}
