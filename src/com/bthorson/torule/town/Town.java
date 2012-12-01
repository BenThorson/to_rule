package com.bthorson.torule.town;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 11/24/12
 * Time: 11:14 AM
 */
public class Town {

    private List<Building> buildings = new ArrayList<Building>();
    private List<Creature> citizens = new ArrayList<Creature>();
    private Point regionalPosition;

    public Town(){}

    public void registerBuilding(Building building){
        buildings.add(building);
    }

    public void registerCitizen(Creature citizen){
        citizens.add(citizen);
    }


    public void setRegionalPosition(Point regionalPosition) {
        this.regionalPosition = regionalPosition;
    }

    public Point getRegionalPosition() {
        return regionalPosition;
    }
}
