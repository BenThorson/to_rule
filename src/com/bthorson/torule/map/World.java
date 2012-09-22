package com.bthorson.torule.map;

import com.bthorson.torule.entity.*;
import com.bthorson.torule.entity.ai.GroupFollowAI;
import com.bthorson.torule.entity.group.Group;
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
        return EntityManager.getInstance().creatureAt(position);
    }

    public void populateSomeCreatures(){
        Faction human = new Faction("Human");
        Faction goblin = new Faction("Goblin");
        human.addEnemyFaction(goblin);
        goblin.addEnemyFaction(human);
        player = CreatureFactory.buildPlayer(this, new Point(40, 19));
        player.setFaction(human);
        EntityManager.getInstance().setPlayer(player);
        EntityManager.getInstance().addCreature(player);

        Creature gobLeader = CreatureFactory.buildGoblinLeader(this, new Point(30, 32));
        gobLeader.setFaction(goblin);
        gobLeader.setAi(new GroupFollowAI(gobLeader));
        List<Creature> group = new ArrayList<Creature>();
        List<Creature> gobGroup = new ArrayList<Creature>();
        gobGroup.add(gobLeader);

        for (int i = 0; i < 14; i++) {
            Creature villy = CreatureFactory.buildSoldier(this, new Point(30 + i, 21));
            villy.setFaction(human);
            villy.setLeader(player);
            group.add(villy);
            villy.setAi(new GroupFollowAI(villy));
            Creature gobby = CreatureFactory.buildGoblin(this, new Point(30 + i, 31));
            gobby.setFaction(goblin);
            gobby.setAi(new GroupFollowAI(gobby));
            gobGroup.add(gobby);
        }
        Group grp = new Group(group, player);
        player.setGroup(grp);
        EntityManager.getInstance().addGroup(grp);

        Group gobGrp = new Group(gobGroup, gobLeader);
        EntityManager.getInstance().addGroup(gobGrp);
    }

    public void update() {
        EntityManager.getInstance().update();
        for (Creature dead : toRemove){
            EntityManager.getInstance().remove(dead);
        }
        toRemove.clear();
    }

    public Creature getPlayer() {
        return player;
    }

    public List<Creature> getCreaturesInRange(Point p1, Point p2) {
        return EntityManager.getInstance().getCreaturesInRange(p1, p2);
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
                && tile(point).passable();
    }

    public Point topLeft() {
        return new Point(0,0);
    }

    public Point bottomRight() {
        return new Point(width(), height());
    }
}
