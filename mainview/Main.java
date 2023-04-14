package mainview;

import Constants.Constants;
import Entities.*;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {

    private static final int NS_TO_MS = 1000000;

    private static int mult(int num) {
        return Constants.CELL_RADIUS * num;
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        int[][] foodList = new int[][]{
                {mult(16), mult(-8), 500},
                {mult(19), mult(-5), 500}
        };

        List<int[]> foodArrayList = new ArrayList<>();
        foodArrayList.addAll(Arrays.asList(foodList));

        Point2 startingLoc = new Point2(mult(10), mult(-10));
        int startingEnergy = 2100;
        float exploration = (float) 0.09;

        SlimeMoldModel model = new SlimeMoldModel(startingLoc, startingEnergy, foodArrayList, exploration);
        ViewController vc = new ViewController(model);

        vc.startSimulation();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            Mat vismap = new Mat(Constants.VIEW_HEIGHT, Constants.VIEW_WIDTH, CvType.CV_8UC3, new Scalar(0));

            vc.model.tick();

            for (Food food : vc.model.food) {
                int y = (int) (-food.location.y + Constants.MAX_Y);
                int x = (int) (food.location.x + Constants.MAX_X);
                vismap.put(y, x, new double[]{0, 255, 0});
            }

            for (SlimeMoldCenter center : vc.model.slimeMoldCenters) {
                for (Cell cell : center.population) {
                    int y = (int) (-cell.location.y + Constants.MAX_Y);
                    int x = (int) (cell.location.x + Constants.MAX_X);
                    vismap.put(y, x, new double[]{0, 0, 255});
                }
            }

            Imgproc.resize(vismap, vismap, new Size(Constants.VIEW_WIDTH * 3, Constants.VIEW_HEIGHT * 3));
            HighGui.imshow("e", vismap);
            vismap.release();

            if (HighGui.waitKey(5) == 27) {
                executor.shutdown();
                HighGui.destroyAllWindows();
            }
        }, 0, 30, TimeUnit.MILLISECONDS);
    }


        /*
        while (true) {
            Mat vismap = new Mat(Constants.VIEW_HEIGHT, Constants.VIEW_WIDTH, CvType.CV_8UC3, new Scalar(0));

            long start_time = System.nanoTime();
            vc.model.tick();
            long end_time = System.nanoTime();

            for (Food food : vc.model.food) {
                int y = (int) (-food.location.y + Constants.MAX_Y);
                int x = (int) (food.location.x + Constants.MAX_X);
                vismap.put(y, x, new double[]{0, 255, 0});
            }

            for (SlimeMoldCenter center : vc.model.slimeMoldCenters) {
                for (Cell cell : center.population) {
                    int y = (int) (-cell.location.y + Constants.MAX_Y);
                    int x = (int) (cell.location.x + Constants.MAX_X);
                    vismap.put(y, x, new double[]{0, 0, 255});
                }
            }

            System.out.println("Check 1");

            if (vc.model.isComplete()) {
                System.exit(0);
                System.out.println("Check 1.5");
            } else {
                end_time = System.nanoTime() / NS_TO_MS;

                int next_tick = 30 - (int) (end_time - (start_time) / NS_TO_MS);
                if (next_tick < 0) {
                    next_tick = 0;
                }
                System.out.println(next_tick);

                try {
                    TimeUnit.MILLISECONDS.sleep(next_tick/NS_TO_MS);
                    System.out.println("Check 1.9");
                } catch (InterruptedException e) {

                }

            }
            System.out.println("Check 2");

            Imgproc.resize(vismap, vismap, new Size(Constants.VIEW_WIDTH * 3, Constants.VIEW_HEIGHT * 3));
            HighGui.imshow("e", vismap);
            vismap.release();

            System.out.println("Check 3");
            end_time = System.nanoTime() / NS_TO_MS;
            double timediff = end_time - start_time / NS_TO_MS;

            try {
                System.out.println("FRAME RATE:" + (1000 / timediff));
            } catch (Exception e) {
                // Do nothing if there is an exception
            }

            if (HighGui.waitKey(5) == 27) {
                break;
            }

            System.out.println("Check 4");
        }
        HighGui.destroyAllWindows();
        System.out.println("Check 5");
    }

         */
}
