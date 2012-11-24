package com.bthorson.torule.entity;

import com.bthorson.torule.entity.ai.PlayerAI;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.ExploredMap;

import javax.swing.text.Position;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:07 PM
 */
public class CreatureFactory {

    public static Creature buildSoldier(World world, Point pos){
        Creature creature = new Creature(world, pos, CreatureImage.H_SWORDMAN.num(), 30, 80);
        creature.setAi(new WanderAI(creature, World.NW_CORNER, world.seCorner()));
        creature.setName("Swordsman");
        EntityManager.getInstance().addCreature(creature);
        return creature;

    }

    public static Creature buildVillager(World world, Point pos){
        Creature creature = new Creature(world, pos, CreatureImage.H_PEASANT.num(), 30, 40);
        creature.setAi(new WanderAI(creature, World.NW_CORNER, world.seCorner()));
        creature.setName("villie");
        creature.setFaction(Faction.TEST);
        EntityManager.getInstance().addCreature(creature);
        return creature;
    }

    public static Creature buildPlayer(World world, Point pos){
        Creature creature = new Creature(world, pos, CreatureImage.H_KNIGHT.num(), 30, 80);
        creature.setAi(new PlayerAI(creature));
        creature.setExplored(new ExploredMap());
        creature.setName("player");
        EntityManager.getInstance().addCreature(creature);
        return creature;
    }

    public static Creature buildGoblin(World world, Point pos){
        Creature creature = new Creature(world, pos, CreatureImage.G_SWORDMAN.num(), 30, 40);
        creature.setAi(new WanderAI(creature, World.NW_CORNER, world.seCorner()));
        creature.setName("gobbo");
        return creature;
    }

    public static Creature buildGoblinLeader(World world, Point pos) {
        Creature creature = new Creature(world, pos, CreatureImage.G_KNIGHT.num(), 30, 60);
        creature.setAi(new WanderAI(creature, World.NW_CORNER, world.seCorner()));
        creature.setName("Gobbo Leader");
        return creature;
    }
}
