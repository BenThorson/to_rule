package com.bthorson.torule.entity;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 9/12/12
 * Time: 9:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class NearestComparator implements Comparator<Creature> {

    private Point point;

    public NearestComparator(Point point){
        this.point = point;
    }

    @Override
    public int compare(Creature o1, Creature o2) {
        int diff = PointUtil.getDiagDist(point, o1.position()) -
                PointUtil.getDiagDist(point, o2.position());
        if (diff == 0) {
            return PointUtil.diagMoves(point, o1.position()) - PointUtil.diagMoves(point, o2.position());
        } else {
            return diff;
        }
    }
}
