package com.bthorson.torule.entity;

import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.quest.ActiveQuest;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;
import com.google.gson.JsonArray;

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
    private List<Creature> creatures;
    private List<PhysicalEntity> freeItems;
    private Map<Point, Town> towns;
    private List<Faction> factions;

    List<Creature> toRemove;

    private Faction aggressiveAnimalFaction;
    private Faction passiveAnimalFaction;
    private Faction goblinFaction;

    private List<Creature> locallyUpdate;
    private boolean nextReady;
    private List<Creature> nextLocalUpdate;
    private Point lastCheckedPosition;

    private Player player;
    private static EntityManager INSTANCE = new EntityManager();

    public static void destroy(){
        INSTANCE = null;
    }

    private EntityManager(){
        creatures = new ArrayList<Creature>();
        freeItems = new ArrayList<PhysicalEntity>();
        toRemove = new ArrayList<Creature>();
        towns = new HashMap<Point, Town>();
        factions = new ArrayList<Faction>();
    }


    public static EntityManager getInstance(){
        if (INSTANCE == null){
            INSTANCE = new EntityManager();
        }
        return INSTANCE;
    }

    public void setPlayer(Player creature){
        this.player = creature;
    }


    public Player getPlayer() {
        return player;
    }

    public void addCreature(Creature creature){
        creatures.add(creature);
        fullCatalog.put(creature.id, creature);
    }

    public void update(){

        if (!player.position().divide(MapConstants.LOCAL_SIZE_POINT).equals(lastCheckedPosition.divide(MapConstants.LOCAL_SIZE_POINT))){
            nextReady = false;
            System.out.println("starting a new thread to load new chunks in");
            new NewLocalUpdateAction();
        }

        if (nextReady){
            locallyUpdate = nextLocalUpdate;
        }

//        System.out.println("in main, updating at " + World.getInstance().getTurnCounter());

        for (Creature creature : locallyUpdate){
            creature.update(World.getInstance().getTurnCounter());
        }

        for (Creature dead : toRemove){
            remove(dead);
        }
        toRemove.clear();
    }

    public Creature creatureAt(Point position){
        for (Creature creature : creatures){
            if (creature == null){
                System.out.println("huh?");
                continue;
            }
            if (creature.position().equals(position)){
                return creature;
            }
        }
        return null;
    }

    public void remove(Creature dead) {
        creatures.remove(dead);
        fullCatalog.remove(dead.id);
        locallyUpdate.remove(dead);
    }

    public List<Creature> getActiveCreaturesInRange(Point p1, Point p2) {
        List<Creature> retList = new ArrayList<Creature>();
        for (Creature c: locallyUpdate){
            if (c.position().withinRect(p1, p2)){
                retList.add(c);
            }
        }
        return retList;
    }

    private List<Creature> getAllCreaturesInRange(Point p1, Point p2) {
        List<Creature> retList = new ArrayList<Creature>();
        for (Creature c: creatures){
            if (c.position().withinRect(p1, p2)){
                retList.add(c);
            }
        }
        return retList;
    }

    public void creatureDead(Creature creature){

        toRemove.add(creature);
        Corpse corpse = new Corpse(creature);
        addFreeItem(corpse);
        fullCatalog.put(corpse.id, corpse);
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

    public void destroyItem(Item item){
        freeItems.remove(item);
        fullCatalog.remove(item.id);
    }

    public List<PhysicalEntity> getAllEntities(Point point) {
        List<PhysicalEntity> entities = new ArrayList<PhysicalEntity>();
        Creature creature = creatureAt(point);
        if (creature != null){
            entities.add(creatureAt(point));
        }
        entities.addAll(item(point));
        if (town(point.divide(MapConstants.LOCAL_SIZE_POINT)) != null){
            for (Building building : town(point.divide(MapConstants.LOCAL_SIZE_POINT)).getBuildings()){
                if (point.withinRect(building.getNwCorner(), building.getSeCorner().add(new Point(1,1)))){
                    entities.add(building);
                    break;
                }
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

    public Entity getById(int id){
        return fullCatalog.get(id);
    }

    public Map<String, JsonArray> serialize(){
        Map<String, JsonArray> itemMap = new HashMap<String, JsonArray>();
        JsonArray qArr = new JsonArray();
        for (ActiveQuest quest : player.getQuests()){
            qArr.add(quest.serialize());
        }
        itemMap.put(ActiveQuest.class.getSimpleName(), qArr);
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

    public Faction getAggressiveAnimalFaction() {
        return aggressiveAnimalFaction;
    }

    public void setFactions(List<Faction> factns) {
        this.factions = factns;
        for (Faction faction : factns){
            if (faction.getName().equals("aggressiveAnimal")){
                aggressiveAnimalFaction = faction;
            } else if (faction.getName().equals("passiveAnimal")) {
                passiveAnimalFaction = faction;
            } else if (faction.getName().equals("goblin")){
                goblinFaction = faction;
            }
            fullCatalog.put(faction.id, faction);
        }
    }

    public void start() {

        new NewLocalUpdateAction();


        while (!nextReady){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        locallyUpdate = nextLocalUpdate;
        lastCheckedPosition = player.position();
    }

    public Faction getPassiveAnimalFaction() {
        return passiveAnimalFaction;
    }

    public Faction getGoblinFaction() {
        return goblinFaction;
    }


    private class NewLocalUpdateAction implements Runnable {

        public NewLocalUpdateAction(){
            Thread thread = new Thread(this);
            thread.start();
        }


        @Override
        public void run() {
            Point origin = player.position();
            Point toRegion = origin.divide(MapConstants.LOCAL_SIZE_POINT);
            Point nwRegion = correctNwCorner(toRegion).multiply(MapConstants.LOCAL_SIZE_POINT);
            Point seRegion = correctSeCorner(toRegion).multiply(MapConstants.LOCAL_SIZE_POINT);
            List<Creature> newUpdate = getAllCreaturesInRange(nwRegion, seRegion);

            List<Creature> doStuff = new ArrayList<Creature>();

            long startTime = Long.MAX_VALUE;
            for (Creature creature : newUpdate){
                if (locallyUpdate != null && !locallyUpdate.contains(creature)){
                    doStuff.add(creature);
                    if (creature.getLastUpdate() < startTime){
                        startTime = creature.getLastUpdate();
                    }
                }
            }

            for (;startTime < World.getInstance().getTurnCounter(); startTime++){
//                System.out.println("in thread, updating at " + startTime);
                for (Creature creature : doStuff){
                    if (creature.getLastUpdate() < startTime){
                        creature.update(startTime);
                    }
                }
            }

            nextReady = true;
            lastCheckedPosition = player.position();
            nextLocalUpdate = newUpdate;
        }

        private Point correctSeCorner(Point toRegion) {
            Point seRegion = toRegion.add(Direction.SOUTH_EAST.point().add(Direction.SOUTH_EAST.point()));
            if (seRegion.x() > World.getInstance().bottomRight().divide(MapConstants.LOCAL_SIZE_POINT).x()){
                seRegion.add(Direction.WEST.point());
            }
            if (seRegion.y() > World.getInstance().bottomRight().divide(MapConstants.LOCAL_SIZE_POINT).y()){
                seRegion.add(Direction.NORTH.point());
            }
            return seRegion;
        }

        private Point correctNwCorner(Point toRegion) {
            Point nwRegion = toRegion.add(Direction.NORTH_WEST.point());
            if (nwRegion.x() < 0){
                nwRegion.add(Direction.EAST.point());
            }
            if (nwRegion.y() < 0){
                nwRegion.add(Direction.SOUTH.point());
            }
            return nwRegion;
        }

    }
}
