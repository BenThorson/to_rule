package com.bthorson.torule.entity;

import com.bthorson.torule.map.World;

import java.awt.Color;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:39 PM
 */
public class Creature extends Entity {



    public Creature(World world, int x, int y, char glyph, Color color) {
        super(world, x, y, glyph, color);
    }

    public void move(int dx, int dy){
        x += dx;
        y += dy;
    }
}
