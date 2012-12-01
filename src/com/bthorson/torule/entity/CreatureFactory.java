package com.bthorson.torule.entity;

import com.bthorson.torule.entity.ai.PlayerAI;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.ExploredMap;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:07 PM
 */
public class CreatureFactory {

    public static Creature buildSoldier(Point pos){
        Creature creature = new Creature(pos, CreatureImage.H_SWORDMAN.num(), 30, 80, Profession.SOLDIER);
        creature.setAi(new WanderAI(creature, World.NW_CORNER, World.getInstance().seCorner()));
        creature.setName("Swordsman");
        EntityManager.getInstance().addCreature(creature);
        return creature;

    }

    public static Creature buildVillager(Point pos){
        Creature creature = new Creature(pos, CreatureImage.H_PEASANT.num(), 30, 40, Profession.VILLAGER);
        creature.setAi(new WanderAI(creature, World.NW_CORNER, World.getInstance().seCorner()));
        creature.setFaction(Faction.TEST);
        EntityManager.getInstance().addCreature(creature);
        return creature;
    }

    public static Creature buildMerchant(Point pos, Building shop){
        Creature creature = new Creature(pos, CreatureImage.H_MERCHANT.num(), 30, 40, Profession.MERCHANT);
        creature.setAi(new WanderAI(creature, shop.getNwCorner().add(new Point(1,1)), shop.getSeCorner().subtract(new Point(1,1))));
        EntityManager.getInstance().addCreature(creature);
        creature.setFaction(Faction.TEST);
        return creature;
    }

    public static Player buildPlayer(Point pos){
        Creature creature = new Creature(pos, CreatureImage.H_KNIGHT.num(), 30, 80, Profession.LEADER);
        creature.setAi(new PlayerAI(creature));
        creature.setExplored(new ExploredMap());
        creature.setName("player");
        EntityManager.getInstance().addCreature(creature);
        Player player = new Player(creature);

        return player;
    }

    public static Creature buildGoblin(Point pos){
        Creature creature = new Creature(pos, CreatureImage.G_SWORDMAN.num(), 30, 40, Profession.SOLDIER);
        creature.setAi(new WanderAI(creature, World.NW_CORNER, World.getInstance().seCorner()));
        creature.setName("gobbo");
        return creature;
    }

    public static Creature buildGoblinLeader(Point pos) {
        Creature creature = new Creature(pos, CreatureImage.G_KNIGHT.num(), 30, 60, Profession.LEADER);
        creature.setAi(new WanderAI(creature, World.NW_CORNER, World.getInstance().seCorner()));
        creature.setName("Gobbo Leader");
        return creature;
    }
}
