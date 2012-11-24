package com.bthorson.torule.town;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;

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

    public Town(){}

    public void registerBuilding(Building building){
        buildings.add(building);
    }

    public void registerCitizen(Creature citizen){
        citizens.add(citizen);
    }


}
