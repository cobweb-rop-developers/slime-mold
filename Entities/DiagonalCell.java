package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiagonalCell extends Cell {
    int max_children;
    List<Point2> children_dirs;

    public DiagonalCell(Point2 location, Point2 direction, int energy, float exploration) {
        super(location, direction, energy, exploration);
        this.max_children = 3;
        this.children_dirs = new ArrayList<Point2>();
        this.children_dirs.add(direction);
        this.children_dirs.add(new Point2(0, direction.y));
        this.children_dirs.add(new Point2(direction.x, 0));
    }

    @Override
    public List<Cell> create_child() {
        // diagonal cell creates 3 cell children
        List<Cell> new_children = new ArrayList<Cell>();
        List<Point2> created_dirs = new ArrayList<Point2>();

        for (Point2 dir : this.children_dirs) {
            float create_child = new Random().nextFloat();
            if (create_child < this.exploration) {
                Point2 new_loc = this.location.add(dir);

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

        for (Point2 dir : created_dirs) {
            this.children_dirs.remove(dir);
        }

        this.max_children -= new_children.size();
        return new_children;
    }
}