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

    public static Creature buildVillager(World world, Point pos){
        Creature creature = new Creature(world, pos, CreatureImage.H_PEASANT.num(), 15, 40);
        creature.setAi(new WanderAI(creature));
        creature.setName("villie");
        return creature;
    }

    public static Creature buildPlayer(World world, Point pos){
        Creature creature = new Creature(world, pos, CreatureImage.H_KNIGHT.num(), 30, 60);
        creature.setAi(new PlayerAI(creature));
        creature.setExplored(new ExploredMap());
        creature.setName("player");
        return creature;
    }

    public static Creature buildGoblin(World world, Point pos){
        Creature creature = new Creature(world, pos, CreatureImage.G_KNIGHT.num(), 15, 40);
        creature.setAi(new WanderAI(creature));
        creature.setName("gobbo");
        return creature;
    }
}
