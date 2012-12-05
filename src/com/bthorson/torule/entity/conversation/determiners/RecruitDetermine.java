package com.bthorson.torule.entity.conversation.determiners;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecruitDetermine implements Determiner {

    private static final int REASK_TIME = 1000;

    private final Map<Creature, Integer> haveAsked = new HashMap<Creature, Integer>();

    @Override
    public int determine(Creature creature) {
        if (haveAsked.containsKey(creature) && haveAsked.get(creature) + REASK_TIME > World.getInstance().getTurnCounter()){
            return 1;
        }
        if (EntityManager.getInstance().getPlayer().getFollowers().contains(creature)){
            return 3;
        }
        Random random = new Random();
        int value = random.nextInt(10) + EntityManager.getInstance().getPlayer().getFame();
        if (value > 7){
            value = 0;
        } else {
            value = 2;
        }
        haveAsked.put(creature, REASK_TIME);
        return value;
    }
}
