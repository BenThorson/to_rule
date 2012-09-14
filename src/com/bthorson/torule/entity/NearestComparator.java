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

    private Creature thisCreature;

    public NearestComparator(Creature thisCreature){
        this.thisCreature = thisCreature;
    }

    @Override
    public int compare(Creature o1, Creature o2) {
        Point thisCreat = new Point(thisCreature.x, thisCreature.y, 0);
        Point other1 = new Point(o1.x, o1.y);
        Point other2 = new Point(o2.x, o2.y);
        int diff = PointUtil.getDiagDist(thisCreat, other1) -
                PointUtil.getDiagDist(thisCreat, other2);
        if (diff == 0){
            return PointUtil.diagMoves(thisCreat, other1) - PointUtil.diagMoves(thisCreat, other2);
        } else {
            return diff;
        }
    }
}
