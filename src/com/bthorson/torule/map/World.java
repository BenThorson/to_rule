package com.bthorson.torule.map;

import com.bthorson.torule.entity.*;
import com.bthorson.torule.geom.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:07 PM
 */
public class World {

    private Region[][] regions;

    List<Entity> items = new ArrayList<Entity>();
    List<Creature> creatures = new ArrayList<Creature>();
    List<Creature> toRemove = new ArrayList<Creature>();
    private Creature player;

    public World(){
        regions = new Region[1][1];
        for (int x = 0; x < 1; x++){
            for (int y = 0; y < 1; y++){
                regions[x][y] = new Region(x,y);
            }
        }
        populateSomeCreatures();
    }



    public int width() {
        return 1000;
    }

    public int height() {
        return 1000;
    }


    public Tile tile(Point tilePoint){
        return regions[tilePoint.x()/1000][tilePoint.y()/1000].tile(tilePoint.x() % 1000, tilePoint.y() % 1000);
    }

    public Creature creature(Point position){
        for (Creature creature : creatures){
            if (creature.position().equals(position)){
                return creature;
            }
        }
        return null;
    }

    public void addCreature(Creature creature){
        creatures.add(creature);
    }

    public void populateSomeCreatures(){
        Faction human = new Faction("Human");
        Faction goblin = new Faction("Goblin");
        human.addEnemyFaction(goblin);
        goblin.addEnemyFaction(human);
        player = CreatureFactory.buildPlayer(this, new Point(40, 19));
        player.setFaction(human);
        addCreature(player);

        for (int i = 0; i < 20; i++) {
            Creature villy = CreatureFactory.buildVillager(this, new Point(30 + i, 20));
            addCreature(villy);
            villy.setFaction(human);
            Creature gobby = CreatureFactory.buildGoblin(this, new Point(30 + i, 25));
            gobby.setFaction(goblin);
            addCreature(gobby);
        }
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public void update() {
        for (Creature creature: getCreatures()){
            creature.update();
        }
        for (Creature dead : toRemove){
            creatures.remove(dead);
        }
        toRemove.clear();
    }

    public Creature getPlayer() {
        return player;
    }

    public List<Creature> getCreaturesInRange(Point p1, Point p2) {
        List<Creature> retList = new ArrayList<Creature>();
        for (Creature c: getCreatures()){
            if (c.position().withinRect(p1, p2)){
                retList.add(c);
            }
        }
        return retList;
    }

    public void creatureDead(Creature creature){

        toRemove.add(creature);
        items.add(new Corpse(this, creature));
    }

    public Entity item(Point itemPos) {
        for (Entity item : items){
            if (item.position().equals(itemPos)){
                return item;
            }
        }
        return null;
    }

    public boolean isTravelable(Point point) {
        return point.x() >= 0 && point.x() < width() && point.y() >= 0
                && point.y() < height()
                && creature(point) == null && tile(point).passable();
    }

    public Point topLeft() {
        return new Point(0,0);
    }

    public Point bottomRight() {
        return new Point(width(), height());
    }
}
