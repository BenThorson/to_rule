package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.ai.CreatureAI;

import java.util.Random;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 8:51 PM
 */
public class WanderAI implements CreatureAI {

    private Creature creature;
    public WanderAI(Creature creature){
        this.creature = creature;
    }
    @Override
    public void execute() {
        int check = new Random().nextInt(10);

        switch (check){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                break;
            case 6:
                creature.move(1, 0);
                break;
            case 7:
                creature.move(-1, 0);
                break;
            case 8:
                creature.move(0,1);
                break;
            case 9:
                creature.move(0,-1);
                break;
        }
    }

    @Override
    public void interact(Entity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
