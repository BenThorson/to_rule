package com.bthorson.torule.town;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.entity.NameGenerator;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.Local;
import com.bthorson.torule.persist.SerializeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 11/24/12
 * Time: 11:14 AM
 */
public class Town extends Entity {

    private List<Building> buildings = new ArrayList<Building>();
    private List<Creature> citizens = new ArrayList<Creature>();
    private WealthLevel wealthLevel;
    private Point regionalPosition;
    private Faction faction;
    private Local local;

    public Town(){
        super(NameGenerator.getInstance().genTownName());
        faction = new Faction(getName());
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


    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public WealthLevel getWealthLevel() {
        return wealthLevel;
    }

    public void setWealthLevel(WealthLevel wealthLevel) {
        this.wealthLevel = wealthLevel;
    }

    public List<Building> getBuildings() {
        return new ArrayList<Building>(buildings);
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        SerializeUtils.serializeRefCollection(buildings, obj, "buildings");
        SerializeUtils.serializeRefCollection(citizens, obj, "citizens");
        obj.add("wealthLevel", gson.toJsonTree(wealthLevel));
        obj.add("regionalPosition", gson.toJsonTree(regionalPosition));
        obj.addProperty("faction", faction.id);
        return obj;
    }
}
