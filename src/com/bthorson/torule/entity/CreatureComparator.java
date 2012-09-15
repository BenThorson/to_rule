package com.bthorson.torule.entity;

import java.util.Comparator;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 3:46 AM
 */
public class CreatureComparator implements Comparator<Entity> {

    @Override
    public int compare(Entity o1, Entity o2) {
        return o1.id - o2.id;
    }
}
