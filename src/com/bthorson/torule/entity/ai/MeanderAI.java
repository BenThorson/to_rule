package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Direction;

import java.util.Random;

/**
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
        if (random.nextInt(10) == 0){
            new WanderAI(self, this, false).execute();

        } else if (random.nextInt(10) > 8){
            self.move(self.getHeading().point());
        }
        return this;

    }

    @Override
    public boolean interact(Entity entity) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
