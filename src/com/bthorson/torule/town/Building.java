package com.bthorson.torule.town;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 11/24/12
 * Time: 11:17 AM
 */
public class Building extends Entity {

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
}
