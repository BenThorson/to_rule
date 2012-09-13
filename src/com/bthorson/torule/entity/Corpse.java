package com.bthorson.torule.entity;

import com.bthorson.torule.map.World;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 9/12/12
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Corpse extends Entity {
    public Corpse(World world, int glyph, int x, int y, Color color) {
        super(world, x, y, glyph, color);
    }
}
