package com.statsim.predatorprey;

import com.statsim.objectsfirst.Simulator;

import java.io.IOException;

/**
 * Application entry point
 */
public class Main {
    public static void main(String[] args) {
        String[] header = {"test", "test2"};
        try {
            CsvLogger e = new CsvLogger("./file.csv", header);
            String[] aw = {"ewer", "ewrwer", "ewrwer"};
            String[] aw2 = {"ewer", "ewrwer"};
            e.log(aw);
            e.log(aw2);
            e.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Simulator simulator = new Simulator();
        simulator.simulate(500);
    }
}
