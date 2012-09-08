package com.bthorson.torule.entity;

import com.bthorson.torule.map.World;

import java.awt.Color;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:07 PM
 */
public class CreatureFactory {

    public static Creature buildVillager(World world, int x, int y){
        Creature creature = new Creature(world, x, y, (char)1, Color.CYAN);
        creature.setAi(new WanderAI(creature));
        return creature;
    }

    public static Creature buildPlayer(World world, int x, int y){
        Creature creature = new Creature(world, x, y, '@', Color.YELLOW);
        creature.setAi(new PlayerAI());
        return creature;
    }
}
