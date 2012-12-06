package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonElement;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 9:17 PM
 */
public abstract class AggroableAI extends CreatureAI {

    public AggroableAI(AiControllable self) {
        super(self);
    }

    @Override
    public CreatureAI execute() {
        Creature toAggro = getTarget();
        if (toAggro != null){
            AggroAI aggroAI = new AggroAI(self, toAggro, this);
            aggroAI.execute();
            return aggroAI;
        }
        return this;
    }

    @Override
    public boolean interact(Entity entity) {
        return false;
    }

    @Override
    public JsonElement serialize() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
