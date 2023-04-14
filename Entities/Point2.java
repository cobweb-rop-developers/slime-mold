package Entities;

import java.awt.geom.Point2D;

public class Point2 extends Point2D.Double {


    public float x;
    public float y;

    public Point2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Point2 add(Point2 other){
        float x = this.x + other.x;
        float y = this.y + other.y;
        return new Point2(x, y);
    }

    public boolean same(Point2 other){
        return (this.x == other.x) && (this.y == other.y);
    }

    public float hash() {
        return this.x * 10000 + this.y;
    }
}
