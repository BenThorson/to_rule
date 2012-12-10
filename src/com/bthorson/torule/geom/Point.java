package com.bthorson.torule.geom;

import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.World;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 11:49 PM
 */
public class Point {

    private int x;
    private int y;
    private int z;

    public static Point BLANK = new Point(0,0,0);

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

    public Point add(int x, int y){
        return new Point(this.x + x, this.y + y);
    }

    public Point add(int d){
        return new Point(x + d, y + d);
    }

    public Point subtract(Point other){
        return new Point(x-other.x, y() -other.y);
    }

    public Point subtract(int x, int y){
        return new Point(this.x - x, this.y - y);
    }

    public Point subtract(int d){
        return new Point(x - d, y - d);
    }

    public boolean withinRect(Point bottomRight){
        return withinRect(PointUtil.POINT_ORIGIN, bottomRight);
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

    public Point multiply(int x, int y){
        return new Point(this.x * x, this.y * y);
    }

    public Point multiply(int d){
        return new Point(x * d, y * d);
    }

    public Point divide(Point other) {
        return new Point(x/other.x, y/other.y);
    }

    public Point divide(int x, int y){
        return new Point(this.x / x, this.y / y);
    }

    public Point divide(int d){
        return new Point(x / d, y / d);
    }

    public Point invertY() {
        return new Point(x, -y);
    }

    public Point cloneDeep() {
        return new Point(x,y);
    }

    public Point normalize() {
        int retx = x;
        int rety = y;
        if (x > 1){
            retx = 1;
        }
        if (x < -1){
            retx = -1;
        }
        if (y > 1){
            rety = 1;
        }
        if (y < -1){
            rety = -1;
        }
        return new Point(retx, rety);
    }

    public int absSumOfValues() {
        return Math.abs(x) + Math.abs(y);
    }

    public boolean isOutOfBounds() {
        return x < 0 || y < 0 || x > World.getInstance().seCorner().x() || y > World.getInstance().seCorner().y();
    }
}
