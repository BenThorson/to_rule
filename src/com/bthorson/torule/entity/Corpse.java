package com.bthorson.torule.entity;


import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 9/12/12
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Corpse extends PhysicalEntity {
    public Corpse(Creature creature) {
        super(creature.position(), creature.glyph(), creature.color(), creature.getName());
    }

    @Override
    public List<String> getDetailedInfo() {
        return Arrays.asList("This is " + getName() + "'s corpse.");
    }
}
