package com.bthorson.torule.entity;

import com.bthorson.torule.map.World;


/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 9/12/12
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Corpse extends Entity {
    public Corpse(World world, Creature creature) {
        super(world,  creature.position(), creature.glyph(), creature.color());
    }
}
