package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Point;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * User: Ben Thorson
 * Date: 12/7/12
 * Time: 10:30 AM
 */
public class AttackAI extends CreatureAI {

    private Creature creature;
    private AggroAI aggro;

    public AttackAI(AiControllable self, Creature creature, CreatureAI previous) {
        super(self, previous);
        aggro = new AggroAI(self, creature, this);
        this.creature = creature;
    }

    @Override
    public CreatureAI execute() {
        if (aggro != null){
            aggro.execute();
        }
        if (creature.dead()){
            creature = self.closestVisibleHostile();
            if (creature == null){
                return previous;
            }
        }
        return this;
    }

    @Override
    public boolean interact(Entity entity) {
        if (aggro != null){
            return aggro.interact(entity);
        }
        return false;
    }

    @Override
    public JsonElement serialize() {
        JsonObject object = super.serialize().getAsJsonObject();
        object.addProperty("creature", creature != null ? creature.id : 0);
        return object;
    }
}
