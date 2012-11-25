package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 10:51 PM
 */
public class AggroAI extends SeekAI {


    public AggroAI(AiControllable self, Creature target){
        super(self, target);
    }

    @Override
    public void interact(Entity entity) {
        if (entity == target){
            self.attack((Creature)entity);
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
