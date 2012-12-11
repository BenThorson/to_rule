package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonElement;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 9:17 PM
 */
public class AggroableAI extends CreatureAI {

    private int radius;

    public AggroableAI(AiControllable self, CreatureAI previous) {
        super(self, previous);
        this.radius = Integer.parseInt(((Creature) self).getAggressionLevel().get("range"));
    }

    @Override
    public CreatureAI execute() {
        Creature toAggro = getTarget();
        if(toAggro != null && self.isWithinRange(toAggro.position(), radius)) {
            AggroAI aggroAI = new AggroAI(self, toAggro, this);
            aggroAI.execute();
            return aggroAI;
        }
        return previous;
    }

    @Override
    public boolean interact(Entity entity) {
        return false;
    }

    protected Creature getTarget() {
        return self.closestVisibleHostile();
    }


}
