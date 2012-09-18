package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;

/**
 * User: ben
 * Date: 9/17/12
 * Time: 7:34 PM
 */
public class GroupLeadAI extends CreatureAI{

    CreatureAI subAI;

    public GroupLeadAI(Creature self) {
        super(self);
        subAI = new WanderAI(self);
    }

    @Override
    public CreatureAI execute() {
        if (self.getGroup().inFormation()){
            subAI = subAI.execute();
        }
        return this;
    }

    @Override
    public void interact(Entity entity) {
        subAI.interact(entity);
    }
}
