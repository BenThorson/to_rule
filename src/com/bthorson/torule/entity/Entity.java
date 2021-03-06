package com.bthorson.torule.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:21 PM
 */
public abstract class Entity {

    private static int idGen;

    public static void resetIdGen(){
        idGen = 1;
    }
    private String name;

    public final int id;

    private static int getId() {
        return idGen++;
    }

    public static int getCurrentId() {
        return idGen;
    }

    public static void setCurrentId(int idGen){
        Entity.idGen = idGen;
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
