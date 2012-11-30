package com.bthorson.torule.entity;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.awt.*;


/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:21 PM
 */
public class Entity {

    private static int idGen;
    private String name;

    public final int id;
    private static int getId() {
        return idGen++;
    }

    private World world = World.getInstance();
    public World getWorld() {
        return world;
    }

    protected Point position;
    public Point position() {
        return position;
    }

    private int glyph;
    public int glyph(){
        return glyph;
    }

    private Color color;
    public Color color(){
        return color;
    }

    public Entity(Point pos, int glyph, Color color) {
        this.id = getId();
        this.position = pos;
        this.glyph = glyph;
        this.color = color;
    }

    public void setGlyph(int glyph) {
        this.glyph = glyph;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
