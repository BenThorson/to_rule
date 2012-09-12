package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;

import java.util.List;
import java.util.Random;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 8:51 PM
 */
public class WanderAI implements CreatureAI {

    private Creature self;
    public WanderAI(Creature self){
        this.self = self;
    }
    @Override
    public void execute() {

        List<Creature> visibleCreatures = self.getVisibleCreatures();
        for (Creature other: visibleCreatures){
        //todo add hostility logic
        }
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
                self.move(1, 0);
                break;
            case 7:
                self.move(-1, 0);
                break;
            case 8:
                self.move(0, 1);
                break;
            case 9:
                self.move(0, -1);
                break;
        }
    }

    @Override
    public void interact(Entity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
