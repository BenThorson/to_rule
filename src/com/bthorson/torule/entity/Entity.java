package com.bthorson.torule.entity;

import com.bthorson.torule.map.World;

import java.awt.*;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:21 PM
 */
public class Entity {

    private static int idGen;
    private static int getId() {
        return idGen++;
    }

    private final int id;

    private World world;
    public World getWorld() {
        return world;
    }

    public int x;
    public int y;

    private char glyph;
    public char glyph(){
        return glyph;
    }

    private Color color;
    public Color color(){
        return color;
    }

    public Entity(World world, int x, int y, char glyph, Color color) {
        this.id = getId();
        this.world = world;
        this.x = x;
        this.y = y;
        this.glyph = glyph;
        this.color = color;
    }
}
