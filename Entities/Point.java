package Entities;

import java.awt.geom.Point2D;

public class Point extends Point2D.Double {


    public float x;
    public float y;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Point add(Point other){
        float x = this.x + other.x;
        float y = this.y + other.y;
        return new Point(x, y);
    }

    public boolean same(Point other){
        return (this.x == other.x) && (this.y == other.y);
    }

    public float hash() {
        return this.x * 10000 + this.y;
    }
}
