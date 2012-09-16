package com.bthorson.torule.entity.group;

import com.bthorson.torule.geom.Point;

import java.util.Comparator;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 9:18 PM
 */
public class SortByX implements Comparator<Point> {

    @Override
    public int compare(Point o1, Point o2) {
        return o1.x() - o2.x();
    }
}
