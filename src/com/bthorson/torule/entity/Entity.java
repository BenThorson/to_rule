package com.bthorson.torule.entity;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.World;

import java.awt.*;
import java.util.List;


/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:21 PM
 */
public abstract class Entity {

    private static int idGen;
    private String name;

    public final int id;
    protected String templateName;

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

    protected Color color;
    public Color color(){
        return color;
    }

    public Entity(){
        this.id = getId();
        this.position = PointUtil.POINT_ORIGIN;
        this.glyph = 0;
        this.color = Color.WHITE;
        this.name = "";
    }


    public Entity(Point pos, int glyph, Color color, String name) {
        this.id = getId();
        this.position = pos;
        this.glyph = glyph;
        this.color = color;
        this.name = name;
    }

    public abstract List<String> getDetailedInfo();

    public void setGlyph(int glyph) {
        this.glyph = glyph;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Entity && ((Entity) obj).id == id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
