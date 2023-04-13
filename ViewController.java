import Constants.Constants;
import Entities.Cell;
import Entities.Food;
import Entities.SlimeMoldCenter;
import Entities.SlimeMoldModel;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

    public class ViewController extends JFrame {
        private static final int VIEW_WIDTH = Constants.VIEW_WIDTH;
        private static final int VIEW_HEIGHT = Constants.VIEW_HEIGHT;
        private static final int CELL_RADIUS = Constants.CELL_RADIUS;
        private static final int TICK_TIME_MS = 30;

        public SlimeMoldModel model;
        private JPanel canvas;

        public ViewController(SlimeMoldModel model) {
            super("Slime Mold Model");
            this.model = model;

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(VIEW_WIDTH, VIEW_HEIGHT);

            this.canvas = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;

                    for (SlimeMoldCenter center : model.slimeMoldCenters) {
                        for (Cell cell : center.population) {
                            g2d.setColor(cell.color());
                            Point2D.Double loc = cell.location;
                            Ellipse2D.Double circle = new Ellipse2D.Double(loc.x, loc.y, CELL_RADIUS, CELL_RADIUS);
                            g2d.fill(circle);
                        }
                    }

                    for (Food food : model.food) {
                        g2d.setColor(Color.GREEN);
                        Point2D.Double loc = food.location;
                        Ellipse2D.Double circle = new Ellipse2D.Double(loc.x, loc.y, CELL_RADIUS, CELL_RADIUS);
                        g2d.fill(circle);
                    }
                }
            };
            this.add(canvas);

            this.setVisible(true);
        }

        public void startSimulation() {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    tick();
                }
            }, 0, TICK_TIME_MS);
        }

        private void tick() {
            long startTime = System.nanoTime() / 1000000;
            model.tick();
            canvas.repaint();

            if (model.isComplete()) {
                System.exit(0);
            } else {
                long endTime = System.nanoTime() / 1000000;
                long nextTick = TICK_TIME_MS - (endTime - startTime);
                if (nextTick < 0) {
                    nextTick = 0;
                }
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        tick();
                    }
                }, nextTick);
            }
        }
    }