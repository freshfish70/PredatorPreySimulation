package com.statsim.predatorprey;

import com.statsim.objectsfirst.Simulator;

/**
 * Application entry point
 */
public class Main {
    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.simulate(500);
    }
}
