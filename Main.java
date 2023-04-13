import Constants.Constants;
import Entities.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static Constants.Constants.CELL_RADIUS;

public class Main {

    public static int mult(int num) {
        return CELL_RADIUS * num;
    }

    public static void main(String[] args) throws IOException {
        int[][] foodList = new int[][]{
                {mult(16), mult(-8), 500},
                {mult(19), mult(-5), 500},
                {mult(23), mult(-5), 500},
                {mult(26), mult(-6), 500},
                {mult(29), mult(-2), 100},
                {mult(2), mult(-10), 700},
                {mult(6), mult(-14), 700},
                {mult(-7), mult(-8), 700},
                {mult(-17), mult(-6), 700},
                {mult(-28), mult(-3), 500},
                {mult(-35), mult(1), 500},
                {mult(-26), mult(0), 500},
                {mult(-42), mult(0), 500},
                {mult(-33), mult(3), 50},
                {mult(-38), mult(7), 50},
                {mult(-41), mult(11), 50},
                {mult(-36), mult(13), 50},
                {mult(-37), mult(16), 20},
                {mult(-39), mult(19), 20},
                {mult(-38), mult(23), 20},
                {mult(-32), mult(13), 20},
                {mult(-28), mult(15), 20}
        };

        Point startingLoc = new Point(mult(10), mult(-10));
        int initialEnergy = 2100;
        float exploration = 0.09f;

        List<int[]> foodArrayList = new ArrayList<>();
        for (int i = 0; i < foodList.length; i++) {
            foodArrayList.add(foodList[i]);
        }

        SlimeMoldModel model = new SlimeMoldModel(startingLoc, initialEnergy, foodArrayList, exploration);
        ViewController vc = new ViewController(model);
        vc.startSimulation();


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        int VIEW_WIDTH = Constants.VIEW_WIDTH;
        int VIEW_HEIGHT = Constants.VIEW_HEIGHT;
        int MAX_X = (int) Constants.MAX_X;
        int MAX_Y = (int) Constants.MAX_Y;

        Mat vismap = new Mat(VIEW_HEIGHT, VIEW_WIDTH, CvType.CV_8UC3);
        vismap.setTo(new Scalar(0, 0, 0));

        while (true) {

            vismap.setTo(new Scalar(0, 0, 0));

            long start_time = System.nanoTime();
            vc.model.tick();
            long end_time = System.nanoTime();

            List<Food> foodList2 = vc.model.food;
            for (Food food : foodList2) {
                int x = (int) food.location.x + MAX_X;
                int y = (int) (-food.location.y + MAX_Y);
                vismap.put(y, x, 0, 255, 0);
            }

            List<SlimeMoldCenter> slimeMoldCenters = vc.model.slimeMoldCenters;
            for (SlimeMoldCenter center : slimeMoldCenters) {
                List<Cell> population = center.population;
                for (Cell cell : population) {
                    int x = (int) cell.location.x + MAX_X;
                    int y = (int) (-cell.location.y + MAX_Y);
                    if (cell instanceof StraightCell) {
                        vismap.put(y, x, 0, 0, 255);
                    } else if (cell instanceof DiagonalCell) {
                        vismap.put(y, x, 255, 0, 0);
                    } else {
                        vismap.put(y, x, 255, 255, 255);
                    }
                }
            }

            if (vc.model.isComplete()) {
                break;
            } else {
                end_time = TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);

                long next_tick = 30 - (end_time - start_time) / 1000000;
                if (next_tick < 0) {
                    next_tick = 0;
                }
                try {
                    Thread.sleep(next_tick);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            HighGui.imshow("e", vismap);
            long end_time2 = TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
            long timediff = end_time2 - start_time;
            try {
                System.out.println("FRAME RATE: " + (1000 / timediff));
            } catch (Exception e) {
            }

            if (HighGui.waitKey(5) == 27) {
                break;
            }
        }

        HighGui.destroyAllWindows();
    }
}


