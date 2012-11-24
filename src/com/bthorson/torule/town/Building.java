package com.bthorson.torule.town;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Point;

/**
 * User: Ben Thorson
 * Date: 11/24/12
 * Time: 11:17 AM
 */
public class Building {

    private final Point nwCorner;
    private final Point seCorner;

    private BuildingType buildingType;

    private Creature owner;

    public Building(Point nwCorner, Point seCorner, BuildingType buildingType) {
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
}
