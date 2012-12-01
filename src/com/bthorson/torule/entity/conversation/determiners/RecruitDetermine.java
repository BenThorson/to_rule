package com.bthorson.torule.entity.conversation.determiners;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecruitDetermine implements Determiner {



    @Override
    public int determine(Creature creature) {
        Random random = new Random();
        int value = random.nextInt(10) + World.getInstance().getPlayer().getFame();
        if (value > 7){
            return 0;
        } else {
            return 2;
        }
    }
}
