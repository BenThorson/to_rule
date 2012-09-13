package com.bthorson.torule.entity;

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
        return getDiagDist(o1) - getDiagDist(o2);
    }

    private int getDiagDist(Creature o1) {
        int x1Dist = Math.abs(o1.x - thisCreature.x);
        int y1Dist = Math.abs(o1.y - thisCreature.y);
        return Math.max(x1Dist, y1Dist);
    }
}
