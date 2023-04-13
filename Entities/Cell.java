package Entities;

import Constants.Constants;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class Cell {
    // An individual subject in the simulation.
    public Point location;
    Point direction;
    int totalEnergy;
    List<Cell> children;
    Cell parent;
    boolean foundFood;
    float exploration;
    int maxChildren;

    public Cell(Point location, Point direction, int energy, float exploration) {
        // Construct a cell with its location and direction.
        this.location = location;
        this.direction = direction;
        this.totalEnergy = energy;
        this.children = new ArrayList<Cell>();
        this.foundFood = false;
        this.parent = null; // consider this
        this.exploration = exploration;
    }

    public boolean updateOnTick() {
        // check if child has found food
        if (this.children.size() > 0) {
            for (Cell child : this.children) {
                if (child.foundFood) {
                    this.foundFood = true;
                }
            }
        }

        // decrements the energy level of a given cell
        if (!this.foundFood) {
            this.totalEnergy -= Constants.ENERGY_DECREASE;
            if (this.totalEnergy < 0) {
                // cell death
                return false;
            }
        }
        return true;
    }

    public boolean hasFoundFood(HashMap<String, Integer> foodDict) {
        try {
            int f = foodDict.get(this.location.hash());
            if (f > 0) {
                this.foundFood = true;
                return true;
            }
        } catch (NullPointerException e) {
            // handle the exception or do nothing
        }
        return false;
    }


    public List<Cell> create_child() {
        return new ArrayList<Cell>();
    }

    public void set_parent(Cell other) {
        this.parent = other;
    }

    public Color color() {
        // Return the color representation of a cell.
        return Color.decode("gold");
    }
}