package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/8/12
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class LimitedRadiusAggroAI extends AggroableAI {

    private int radius = 5;
    private CreatureAI defaultAI;

    public LimitedRadiusAggroAI(AiControllable self, CreatureAI previous, int radius, CreatureAI defaultAI) {
        super(self, previous);
        this.radius = radius;
        this.defaultAI = defaultAI;
    }

    @Override
    public CreatureAI execute() {
        CreatureAI aggro = super.execute();
        if (aggro instanceof AggroAI){
            return aggro;
        } else {
            defaultAI.execute();
            return this;
        }
    }

    @Override
    public boolean interact(Entity entity) {
        return false;  
    }

    @Override
    protected Creature getTarget() {
        Creature creature = super.getTarget();

        if(creature != null && self.isWithinRange(creature.position(), radius)) {
            return creature;

        }

        return null;
    }

    @Override
    public JsonElement serialize() {
        JsonObject object = super.serialize().getAsJsonObject();
        object.addProperty("radius", radius);
        object.add("defaultAI", defaultAI.serialize());
        return object;
    }
}
