package com.bthorson.torule.town;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.PhysicalEntity;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.persist.SerializeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 11/24/12
 * Time: 11:17 AM
 */
public class Building extends PhysicalEntity {

    private final Point nwCorner;
    private final Point seCorner;

    private BuildingType buildingType;

    private Creature owner;

    private List<Item> inventory = new ArrayList<Item>();

    public Building(Point nwCorner, Point seCorner, BuildingType buildingType) {
        super(nwCorner, 0, Color.WHITE, buildingType.prettyName());
        this.nwCorner = nwCorner;
        this.seCorner = seCorner;
        this.buildingType = buildingType;
    }

    public Point getNwCorner() {
        return nwCorner;
    }

    public Point getSeCorner() {
        return seCorner;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public Creature getOwner() {
        return owner;
    }

    public void setOwner(Creature owner) {
        this.owner = owner;
    }

    public void addItem(Item item){
        if (inventory == null){
            inventory = new ArrayList<Item>();
        }
        if (owner != null){
            item.setOwnedBy(owner);
        }
        inventory.add(item);
    }

    public void removeItem(Item item){
        inventory.remove(item);
    }

    public List<Item> getInventory() {
        return new ArrayList<Item>(inventory);
    }

    @Override
    public List<String> getDetailedInfo() {
        List<String> toRet = new ArrayList<String>();
        toRet.add("This is a " + buildingType.prettyName());
        if (owner != null){
            toRet.add("It is owned by " + owner.getName());
        } else {
            toRet.add("It is vacant");
        }
        return toRet;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.add("nwCorner", gson.toJsonTree(nwCorner));
        obj.add("seCorner", gson.toJsonTree(seCorner));
        obj.addProperty("buildingType", buildingType.name());
        if (owner != null){
            obj.addProperty("owner", owner.id);
        }
        SerializeUtils.serializeRefCollection(inventory, obj, "inventory");
        return obj;
    }
}
