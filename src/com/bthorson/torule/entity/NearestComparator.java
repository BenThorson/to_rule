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
        int diff = PointUtil.getDiagDist(thisCreature.position(), o1.position()) -
                PointUtil.getDiagDist(thisCreature.position(), o2.position());
        if (diff == 0) {
            return PointUtil.diagMoves(thisCreature.position(), o1.position()) - PointUtil.diagMoves(thisCreature.position(), o2.position());
        } else {
            return diff;
        }
    }
}
