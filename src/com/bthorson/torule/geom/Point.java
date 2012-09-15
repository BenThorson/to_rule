package com.bthorson.torule.geom;

import com.bthorson.torule.entity.Creature;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 11:49 PM
 */
public class Point {

    private int x;
    private int y;
    private int z;

    public Point(int x, int y){
        this(x,y,0);
    }

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(Point copy) {
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        if (y != point.y) return false;
        if (z != point.z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    public Point add(Point other){
        return new Point(x + other.x, y + other.y, other.z);
    }

    public Point subtract(Point other){
        return new Point(x-other.x, y() -other.y);
    }

    public boolean withinRect(Point topLeft, Point bottomRight){
        return x() >= topLeft.x() &&
                x() < bottomRight.x() &&
                y() >= topLeft.y() &&
                y() < bottomRight.y();
    }

    public Point squared() {
        return new Point(x * x, y * y);
    }

    public Point absoluteDifference(Point p0) {
        return new Point(Math.abs(x - p0.x()), Math.abs(y - p0.y()));
    }

    public Point getDeltaOne(Point point) {
        return new Point(getDelta(x, point.x), getDelta(y, point.y));
    }

    private int getDelta(int x1, int x2) {
        int dx;

        if (x1 > x2){
            dx = -1;
        } else if (x1 < x2){
            dx = 1;
        } else {
            dx = 0;
        }
        return dx;
    }

    public Point multiply(Point other) {
        return new Point(x*other.x, y*other.y);
    }

    public Point divide(Point other) {
        return new Point(x/other.x, y/other.y);
    }
}
