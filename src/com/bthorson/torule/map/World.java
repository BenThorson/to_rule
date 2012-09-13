package com.bthorson.torule.map;

import com.bthorson.torule.entity.*;

import java.awt.Color;
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


    public Tile tile(int x, int y){
        return regions[x/1000][y/1000].tile(x % 1000, y % 1000);
    }

    public Creature creature(int x, int y){
        for (Creature creature : creatures){
            if (creature.x == x && creature.y == y){
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
        player = CreatureFactory.buildPlayer(this, 29, 20);
        player.setFaction(human);
        addCreature(player);

        for (int i = 0; i < 20; i++){
            Creature villy = CreatureFactory.buildVillager(this, 30 + i, 20);
            addCreature(villy);
            villy.setFaction(human);
            Creature gobby = CreatureFactory.buildGoblin(this, 30 + i, 25);
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
    }

    public Creature getPlayer() {
        return player;
    }

    public List<Creature> getCreaturesInRange(int x, int y, int x2, int y2) {
        List<Creature> retList = new ArrayList<Creature>();
        for (Creature c: getCreatures()){
            if (c.x >= x && c.x <= x2 && c.y >= y && c.y <= y2){
                retList.add(c);
            }
        }
        return retList;
    }

    public void creatureDead(Creature creature){
        creatures.remove(creature);
        items.add(new Corpse(this, creature.glyph(), creature.x, creature.y, creature.color()));
    }

    public Entity item(int x, int y) {
        for (Entity item : items){
            if (item.x == x && item.y == y){
                return item;
            }
        }
        return null;
    }
}
