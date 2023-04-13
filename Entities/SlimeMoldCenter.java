package Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SlimeMoldCenter {
    List<Cell> fruitingBody;
    List<Cell> leadingEdge;
    public List<Cell> population;
    List<Point> board;
    Point startingLoc;
    int initialEnergy;
    float exploration;
    int CELL_RADIUS = 1;

    public SlimeMoldCenter(Point startingLoc, int initialEnergy, float exploration) {

        this.population = new ArrayList<Cell>();
        this.fruitingBody = new ArrayList<Cell>();
        this.startingLoc = startingLoc;
        this.initialEnergy = initialEnergy;
        this.exploration = exploration;

        int[][] dirs = {{0, 0}, {CELL_RADIUS / 2, 0}, {0, CELL_RADIUS / 2},
                {CELL_RADIUS / 2, CELL_RADIUS / 2},
                {-CELL_RADIUS / 2, 0}, {0, -CELL_RADIUS / 2}, {-CELL_RADIUS / 2, CELL_RADIUS / 2},
                {CELL_RADIUS / 2, -CELL_RADIUS / 2}, {-CELL_RADIUS / 2, -CELL_RADIUS / 2}};

        for (int[] dir : dirs) {
            Point new_loc = new Point(startingLoc.x + dir[0], startingLoc.y + dir[1]);
            if (dir[0] == 0 && dir[1] == 0) {
                Cell new_cell = new Cell(new_loc, new Point(dir[0], dir[1]), initialEnergy, exploration);
                new_cell.maxChildren = 0;
                fruitingBody.add(new_cell);
                population.add(new_cell);
            } else if (new_loc.x == 0 || new_loc.y == 0) {
                StraightCell new_cell = new StraightCell(new_loc, new Point(dir[0], dir[1]), initialEnergy, exploration);
                fruitingBody.add(new_cell);
                population.add(new_cell);
            } else {
                DiagonalCell new_cell = new DiagonalCell(new_loc, new Point(dir[0], dir[1]), initialEnergy, exploration);
                fruitingBody.add(new_cell);
                population.add(new_cell);
            }
        }

        this.leadingEdge = new ArrayList<Cell>(this.fruitingBody);
        this.board = new ArrayList<Point>();

        for (Cell cell : population) {
            board.add(cell.location);
        }
    }

    public void die(Cell cell) {
        // remove cell from population
        population.remove(cell);
        if (leadingEdge.contains(cell)) {
            leadingEdge.remove(cell);
        }
    }

    public Point updateOnTick(List<Food> foodList) {
        Point newCellLoc = null;
        // update each cell in population
        for (Cell cell : population) {
            boolean alive = cell.updateOnTick();
            if (!alive && !fruitingBody.contains(cell)) {
                die(cell);
            }
        }

        // create new children
        List<Cell> new_leading_edge = new ArrayList<Cell>();
        for (Cell cell : leadingEdge) {
            // check if cell has found food
            if (!cell.hasFoundFood((HashMap<String, Integer>) foodList)) {
                List<Cell> new_children = cell.create_child();
                for (Cell child : new_children) {
                    if (!board.contains(child.location)) {
                        population.add(child);
                        new_leading_edge.add(child);
                        board.add(child.location);
                    }
                }
                if (cell.maxChildren > 0) {
                    new_leading_edge.add(cell);
                }
            } else {
                // we have found food, need to create a new center
                newCellLoc = cell.location;
                // now we decrease the exploration not in that direction
            }
        }
        this.leadingEdge = new ArrayList<>(new_leading_edge);
        return newCellLoc;
    }
}

