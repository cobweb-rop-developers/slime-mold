package Entities;

import java.util.ArrayList;
import java.util.List;

public class StraightCell extends Cell {
    private int maxChildren;

    public StraightCell(Point location, Point direction, int energy, float exploration) {
        super(location, direction, energy, exploration);
        this.maxChildren = 1;
    }

    @Override
    public List<Cell> create_child() {
        List<Cell> newChildren = new ArrayList<>();
        double createChild = Math.random();
        if (createChild < this.exploration) {
            Point newLoc = this.location.add(this.direction);
            StraightCell newCell = new StraightCell(newLoc, this.direction, this.totalEnergy - 10, this.exploration);
            newCell.set_parent(this);
            this.children.add(newCell);
            newChildren.add(newCell);
        }
        this.maxChildren -= newChildren.size();
        return newChildren;
    }

}