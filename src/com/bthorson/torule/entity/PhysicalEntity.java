package com.bthorson.torule.entity;

import com.bthorson.torule.geom.Point;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.*;

public abstract class PhysicalEntity extends Entity implements Describable {
    public String templateName;
    protected Point position;
    private int glyph;
    private Color color;

    public PhysicalEntity(Point position, int glyph, Color color, String name) {
        super(name);
        this.position = position;
        this.glyph = glyph;
        this.color = color;
    }

    public Point position() {
        return position;
    }

    public int glyph() {
        return glyph;
    }

    public Color color() {
        return color;
    }

    public void setGlyph(int glyph) {
        this.glyph = glyph;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.add("position", gson.toJsonTree(position()));
        obj.addProperty("glyph", glyph());
        obj.addProperty("color", color().getRGB());
        obj.addProperty("templateName", templateName);
        return obj;
    }
}