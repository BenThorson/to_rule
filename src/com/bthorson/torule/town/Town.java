package com.bthorson.torule.town;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.entity.NameGenerator;
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
    private Faction faction;
    private String name;

    public Town(){
        name = NameGenerator.getInstance().genName();
        faction = new Faction(name);
    }

    public String getName() {
        return name;
    }

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

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }


}
