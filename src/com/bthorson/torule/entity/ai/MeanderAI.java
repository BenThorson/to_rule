package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Entity;

import java.util.Random;

/**
 * The desired behavior of this AI is to wander, but in a manner that encourages it to go in one direction for
 * a while.
 * User: Ben Thorson
 * Date: 12/10/12
 * Time: 2:40 PM
 */
public class MeanderAI extends CreatureAI {

    public MeanderAI(AiControllable self, CreatureAI previous) {
        super(self, previous);
    }

    @Override
    public CreatureAI execute() {

        Random random = new Random();
        if (random.nextInt(10) > 8){
            if (random.nextInt(10) > 7){
                self.move(self.getHeading().point());
            } else {
                new WanderAI(self, this, false).execute();
            }
        }
        return this;

    }

    @Override
    public boolean interact(Entity entity) {
        return false;
    }
}
