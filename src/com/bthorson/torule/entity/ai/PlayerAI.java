package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:11 PM
 */
public class PlayerAI extends CreatureAI {

    public PlayerAI(AiControllable self) {
        super(self);
    }

    @Override
    public CreatureAI execute() {
//        self.move(self.getTarget().subtract(self.position()));
        return this;
    }

    @Override
    public void interact(Entity entity) {
        if (entity instanceof Creature) {
            Creature other = (Creature)entity;
            if (shouldHostile(other)){
                self.attack(other);
            }
//            else if (other.getLeader().equals(self)){
//                            ((Creature)self).swapPlaces(other);
//                        }
        }

    }

    @Override
    public JsonElement serialize() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", getClass().getSimpleName());
        obj.addProperty("self", ((Entity)self).id);
        return obj;
    }
}
