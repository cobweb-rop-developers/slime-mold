package Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlimeMoldModel {
    public List<SlimeMoldCenter> slimeMoldCenters;
    List<Point2> slimeMoldCentersLoc;
    public List<Food> food;
    Map<Float, Integer> foodDict;
    int time;

    public SlimeMoldModel(Point2 startingLoc, int initialEnergy, List<int[]> foodList, float exploration) {
        slimeMoldCenters = new ArrayList<>();
        slimeMoldCentersLoc = new ArrayList<>();
        food = new ArrayList<>();
        foodDict = new HashMap<>();
        time = 0;
        // initialize fruiting body center
        SlimeMoldCenter newCenter = new SlimeMoldCenter(startingLoc, initialEnergy, exploration);
        slimeMoldCenters.add(newCenter);
        slimeMoldCentersLoc.add(startingLoc);

        // initialize food
        for (int[] f : foodList) {
            Point2 foodLoc = new Point2(f[0], f[1]);
            food.add(new Food(foodLoc, f[2]));
        }
    }

    public void tick() {
        // update the simulation by one time step
        time++;

        // update each slime mold center
        List<SlimeMoldCenter> newCenters = new ArrayList<>();
        for (SlimeMoldCenter center : slimeMoldCenters) {
            Point2 result = center.updateOnTick(this.foodDict);

            if (result != null) {
                // create a new slime mold center
                Point2 newLoc = result;
                if (!slimeMoldCentersLoc.contains(newLoc)) {
                    int foodEnergy = findFoodEnergy(newLoc);
                    int energy = center.fruitingBody.get(0).totalEnergy;
                    float exploration = center.fruitingBody.get(0).exploration;
                    int newEnergy = findNextEnergy(energy, foodEnergy);
                    float newExploration = findNextExploration(exploration, foodEnergy);

                    SlimeMoldCenter newCenter = new SlimeMoldCenter(newLoc, newEnergy, newExploration);
                    newCenters.add(newCenter);
                    slimeMoldCentersLoc.add(newLoc);
                }
            }
        }

        // add new centers into slimeMoldCenters
        slimeMoldCenters.addAll(newCenters);
    }

    // uses hashmap
    public int findFoodEnergy(Point2 location) {
        Point2 l = new Point2((int) location.x, (int) location.y);
        int food_energy = 0;
        try {
            food_energy = this.foodDict.get(l);
        } catch (NullPointerException e) {
            // handle the exception or do nothing
        }

        for (Food food : this.food) {
            Point2 food_loc = new Point2(food.location.x, food.location.y);
            if (food_loc.same(location)) {
                food_energy = food.energy;
            }
        }

        return food_energy;
    }

    public int findNextEnergy(int curEnergy, int foodEnergy) {
        return (curEnergy + foodEnergy) / 2;
    }

    public float findNextExploration(float curExploration, int foodEnergy) {
        // the more energy a food has, the more exploratory the new slime mold center will be
        return curExploration + (float) foodEnergy / 10000;
    }

    public boolean isComplete() {
        // happens when all food locations have been found
        return false;
    }
}
