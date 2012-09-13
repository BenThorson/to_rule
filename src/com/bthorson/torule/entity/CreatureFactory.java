package com.bthorson.torule.entity;

import com.bthorson.torule.entity.ai.AggroAI;
import com.bthorson.torule.entity.ai.PlayerAI;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.map.World;

import java.awt.Color;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:07 PM
 */
public class CreatureFactory {

    public static Creature buildVillager(World world, int x, int y){
        Creature creature = new Creature(world, x, y, CreatureImage.H_PEASANT.num(), 10, 10);
        creature.setAi(new WanderAI(creature));
        creature.setName("villie");
        return creature;
    }

    public static Creature buildPlayer(World world, int x, int y){
        Creature creature = new Creature(world, x, y, CreatureImage.H_KNIGHT.num(), 10, 20);
        creature.setAi(new PlayerAI(creature));
        creature.setName("player");
        return creature;
    }

    public static Creature buildGoblin(World world, int x, int y){
        Creature creature = new Creature(world, x, y, CreatureImage.G_KNIGHT.num(), 10, 10);
        creature.setAi(new WanderAI(creature));
        creature.setName("gobbo");
        return creature;
    }
}
