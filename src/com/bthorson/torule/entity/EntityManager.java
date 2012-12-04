package com.bthorson.torule.entity;

import com.bthorson.torule.entity.group.Group;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ben
 * Date: 9/20/12
 * Time: 10:12 AM
 */
public class EntityManager {

    private List<Creature> nonGroupedCreatures;
    private List<Entity> freeItems;
    private Map<Point, Town> towns;
    private List<Group> groups;
    private List<Group> groupToRemove;
    List<Creature> toRemove;
    private Group playerGroup;
    private Creature player;

    private static EntityManager INSTANCE = new EntityManager();

    public static void destroy(){
        INSTANCE = null;
    }

    private EntityManager(){
        groupToRemove = new ArrayList<Group>();
        nonGroupedCreatures = new ArrayList<Creature>();
        groups = new ArrayList<Group>();
        freeItems = new ArrayList<Entity>();
        toRemove = new ArrayList<Creature>();
        towns = new HashMap<Point, Town>();
    }

    public static EntityManager getInstance(){
        if (INSTANCE == null){
            INSTANCE = new EntityManager();
        }
        return INSTANCE;
    }

    public void setPlayer(Creature creature){
        this.player = creature;
    }

    public void addCreature(Creature creature){
        nonGroupedCreatures.add(creature);
    }

    public void addGroup(Group group){
        groups.add(group);
    }

    public void creatureToGroup(Creature creature, Group group){
        nonGroupedCreatures.remove(creature);
        group.addCreature(creature);
    }

    public void update(){

        for (Creature dead : toRemove){
            remove(dead);
        }
        toRemove.clear();

        for (Group toRemove: groupToRemove){
            groups.remove(toRemove);
        }
        groupToRemove.clear();
        for (Group group : groups){
            group.update();
        }

        for (Creature creature : nonGroupedCreatures){
            creature.update();
        }
    }

    public Creature creatureAt(Point position){
        for (Group group : groups){
            for (Creature creature : group.getMemList()){
                if (creature.position().equals(position)){
                    return creature;
                }
            }
        }

        for (Creature creature : nonGroupedCreatures){
            if (creature.position().equals(position)){
                return creature;
            }
        }
        return null;
    }

    public void remove(Creature dead) {
        for (Group group : groups){
            group.remove(dead);
        }
        nonGroupedCreatures.remove(dead);
    }

    public List<Creature> getCreaturesInRange(Point p1, Point p2) {
        List<Creature> retList = new ArrayList<Creature>();
        for (Group group : groups){
            for (Creature creature : group.getMemList()){
                if (creature.position().withinRect(p1, p2)){
                    retList.add(creature);
                }
            }
        }
        for (Creature c: nonGroupedCreatures){
            if (c.position().withinRect(p1, p2)){
                retList.add(c);
            }
        }
        return retList;
    }

    public void removeGroup(Group group) {

        groupToRemove.add(group);
    }

    public void creatureDead(Creature creature){

        toRemove.add(creature);
        EntityManager.getInstance().addFreeItem(new Corpse(creature));
    }

    public boolean isPlayer(Creature creature) {
        return creature.equals(player);
    }

    public List<Entity> item(Point itemPos) {
        List<Entity> items = new ArrayList<Entity>();
        for (Entity item : freeItems){
            if (item.position().equals(itemPos)){
                items.add(item);
            }
        }
        return items;
    }

    public void addFreeItem(Entity item) {
        freeItems.add(item);
    }

    public void removeItem(Item item) {
        freeItems.remove(item);
    }

    public List<Entity> getAllEntites(Point point) {
        List<Entity> entities = new ArrayList<Entity>();
        Creature creature = creatureAt(point);
        if (creature != null){
            entities.add(creatureAt(point));
        }
        entities.addAll(item(point));
        for (Building building : town(point.divide(MapConstants.LOCAL_SIZE_POINT)).getBuildings()){
            if (point.withinRect(building.getNwCorner(), building.getSeCorner().add(new Point(1,1)))){
                entities.add(building);
                break;
            }
        }
        return entities;

    }

    public void addTown(Point localPosition, Town town) {
        towns.put(localPosition, town);
    }

    public Town town(Point pointInLocal){
        return towns.get(pointInLocal);
    }

    public List<Town> getTowns() {
        return new ArrayList<Town>(towns.values());
    }
}
