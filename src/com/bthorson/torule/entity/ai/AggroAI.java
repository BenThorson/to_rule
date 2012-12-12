package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;

/**
 * A hostile state that will attempt to enter melee range and attack the target
 * User: ben
 * Date: 9/7/12
 * Time: 10:51 PM
 */
public class AggroAI extends SeekAI {

    public AggroAI(AiControllable self, Creature target, CreatureAI previous) {

        super(self, target, previous);
    }


    @Override
    public boolean interact(Entity entity) {
        if (entity == target){
            self.attack((Creature)entity);
            return true;
        }
        return false;
    }

}
