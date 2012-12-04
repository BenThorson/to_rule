package com.bthorson.torule.entity;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.List;


/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:21 PM
 */
public abstract class Entity {

    private static int idGen;

    public static void resetIdGen(){
        idGen = 0;
    }
    private String name;

    public final int id;

    private static int getId() {
        return idGen++;
    }

    public Entity(){
        this.id = getId();
        this.name = "";
    }


    public Entity(String name) {
        this.id = getId();
        this.name = name;
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

    public JsonElement serialize(){
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("name", getName());
        return obj;
    }
}
