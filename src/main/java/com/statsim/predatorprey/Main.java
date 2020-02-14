package com.statsim.predatorprey;

import com.statsim.objectsfirst.Simulator;

import java.io.IOException;

/**
 * Application entry point
 */
public class Main {

    public static void main(String[] args) {
Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("SQ START: " + DEBUG.SQ_START);
        System.out.println("SQ TOTAL: " + DEBUG.SQ_TOTALSPAWNED);
        System.out.println("SQ STARVATION: " + DEBUG.SQ_STARVATION);
        System.out.println("SQ DIE AGE: " + DEBUG.SQ_DIE_AGE);
        System.out.println("SQ DIE LOC: " + DEBUG.SQ_DIE_LOC);
        System.out.println("---------------------------------------");
        System.out.println("HW START: " + DEBUG.HW_START);
        System.out.println("HW TOTAL: " + DEBUG.HW_TOTALSPAWNED);
        System.out.println("HW STARVATION: " + DEBUG.HW_STARVATION);
        System.out.println("HW DIE AGE: " + DEBUG.HW_DIE_AGE);
        System.out.println("HW DIE LOC: " + DEBUG.HW_DIE_LOC);
    }
}));
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
    simulator.simulate(7500);



//        System.exit(1);
    }
}
