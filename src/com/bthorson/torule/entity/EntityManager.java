package com.bthorson.torule.entity;

import com.bthorson.torule.entity.group.Group;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintWriter;
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

    private Map<Integer, Entity> fullCatalog = new HashMap<Integer, Entity>();
    private List<Creature> nonGroupedCreatures;
    private List<PhysicalEntity> freeItems;
    private Map<Point, Town> towns;
    private List<Faction> factions;

    private List<Group> groups;
    private List<Group> groupToRemove;
    List<Creature> toRemove;
    private Group playerGroup;

    private Faction aggressiveAnimalFaction;
    private Faction passiveAnimalFaction;
    private Faction goblinFaction;


    private Creature player;
    private static EntityManager INSTANCE = new EntityManager();

    public static void destroy(){
        INSTANCE = null;
    }

    private EntityManager(){
        groupToRemove = new ArrayList<Group>();
        nonGroupedCreatures = new ArrayList<Creature>();
        groups = new ArrayList<Group>();
        freeItems = new ArrayList<PhysicalEntity>();
        toRemove = new ArrayList<Creature>();
        towns = new HashMap<Point, Town>();
        factions = new ArrayList<Faction>();
        aggressiveAnimalFaction = new Faction("aggressiveAnimal");
        passiveAnimalFaction = new Faction("passiveAnimal");
        goblinFaction = new Faction("goblin");
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
        fullCatalog.put(creature.id, creature);
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
        fullCatalog.remove(dead.id);
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

    public List<PhysicalEntity> item(Point itemPos) {
        List<PhysicalEntity> items = new ArrayList<PhysicalEntity>();
        for (PhysicalEntity item : freeItems){
            if (item.position().equals(itemPos)){
                items.add(item);
            }
        }
        return items;
    }

    public void addFreeItem(PhysicalEntity item) {
        freeItems.add(item);
    }

    public void removeFreeItem(Item item) {
        freeItems.remove(item);
    }

    public void addItem(Item item){
        fullCatalog.put(item.id, item);
    }

    public void removeItem(Item item){
        fullCatalog.remove(item.id);
    }

    public List<PhysicalEntity> getAllEntites(Point point) {
        List<PhysicalEntity> entities = new ArrayList<PhysicalEntity>();
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
        for (Building building : town.getBuildings()){
            fullCatalog.put(building.id, building);
        }
        fullCatalog.put(town.id, town);
    }

    public Town town(Point pointInLocal){
        return towns.get(pointInLocal);
    }

    public List<Town> getTowns() {
        return new ArrayList<Town>(towns.values());
    }

    private Entity getById(int id){
        return fullCatalog.get(id);
    }

    public Map<String, JsonArray> serialize(){
        Map<String, JsonArray> itemMap = new HashMap<String, JsonArray>();

        for (Integer id : fullCatalog.keySet()){
            Entity entity = fullCatalog.get(id);
            String type = entity.getClass().getSimpleName();
            if (!itemMap.containsKey(type)){
                itemMap.put(type, new JsonArray());
            }
            itemMap.get(type).add(entity.serialize());
        }
        return itemMap;
    }

    public void setupFactions() {
        factions.add(aggressiveAnimalFaction);
        fullCatalog.put(aggressiveAnimalFaction.id, aggressiveAnimalFaction);
        factions.add(passiveAnimalFaction);
        fullCatalog.put(passiveAnimalFaction.id, passiveAnimalFaction);
        factions.add(goblinFaction);
        fullCatalog.put(goblinFaction.id, goblinFaction);

        for (Town town : getTowns()){
            Faction faction = town.getFaction();
            aggressiveAnimalFaction.addEnemyFaction(town.getFaction());
            town.getFaction().addEnemyFaction(aggressiveAnimalFaction);
            town.getFaction().addEnemyFaction(goblinFaction);
            goblinFaction.addEnemyFaction(town.getFaction());
            fullCatalog.put(faction.id, faction);
            factions.add(faction);
        }
            aggressiveAnimalFaction.addEnemyFaction(goblinFaction);
            goblinFaction.addEnemyFaction(aggressiveAnimalFaction);

        for (Faction faction : factions){
        }


    }

    public Faction getAggressiveAnimalFaction() {
        return aggressiveAnimalFaction;
    }
}
