package mainview;

import Entities.SlimeMoldModel;

public class ViewController {
    /**
     * This class is responsible for controlling the simulation and visualizing it.
     */
    public Object screen;
    public SlimeMoldModel model;
    private static final long ticknum = 0;
    private static final long NS_TO_MS = 1000000;

    public ViewController(SlimeMoldModel model) {
        /**
         * Initialize the VC.
         */
        this.model = model;
        /*
        try {
            FileWriter timefile = new FileWriter("agent_simulation_time.txt");
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
         */
    }

    public void startSimulation() {
        /**
         * Call the first tick of the simulation and begin visualization.
         */
        System.out.println("Starting");

        /*
        try {
            timefile.write("TickNum, SimTime (ms)\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        long start = System.nanoTime();
        model.run();
        long end = System.nanoTime();
        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(end - start);

        System.out.printf("Simulation finished in %d ms\n", elapsedMs);
        try {
            timefile.write(String.format("%d,%d\n", ticknum, elapsedMs));
            timefile.flush();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


        public void closeFile() {
            /**
             * Closes the timefile when the simulation is over.

            try {
                timefile.close();
            } catch (IOException e) {
                System.err.println("Error closing file: " + e.getMessage());
            }
        }
        */

    }
}
