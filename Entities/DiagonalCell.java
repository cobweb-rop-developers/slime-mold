package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiagonalCell extends Cell {
    int max_children;
    List<Point> children_dirs;

    public DiagonalCell(Point location, Point direction, int energy, float exploration) {
        super(location, direction, energy, exploration);
        this.max_children = 3;
        this.children_dirs = new ArrayList<Point>();
        this.children_dirs.add(direction);
        this.children_dirs.add(new Point(0, direction.y));
        this.children_dirs.add(new Point(direction.x, 0));
    }

    @Override
    public List<Cell> create_child() {
        // diagonal cell creates 3 cell children
        List<Cell> new_children = new ArrayList<Cell>();
        List<Point> created_dirs = new ArrayList<Point>();

        for (Point dir : this.children_dirs) {
            float create_child = new Random().nextFloat();
            if (create_child < this.exploration) {
                Point new_loc = this.location.add(dir);

                Cell new_cell;
                if (dir.x == 0) {
                    new_cell = new StraightCell(new_loc, dir, this.totalEnergy - 10, this.exploration);
                    created_dirs.add(dir);
                } else if (dir.y == 0) {
                    new_cell = new StraightCell(new_loc, dir, this.totalEnergy - 10, this.exploration);
                    created_dirs.add(dir);
                } else {
                    new_cell = new DiagonalCell(new_loc, dir, this.totalEnergy - 10, this.exploration);
                    created_dirs.add(dir);
                }

                new_cell.set_parent(this);
                this.children.add(new_cell);
                new_children.add(new_cell);
            }
        }

        for (Point dir : created_dirs) {
            this.children_dirs.remove(dir);
        }

        this.max_children -= new_children.size();
        return new_children;
    }
}