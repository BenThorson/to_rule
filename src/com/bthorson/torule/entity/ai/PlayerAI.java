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
        super(self, null);
    }

    @Override
    public CreatureAI execute() {
//        self.move(self.getTarget().subtract(self.position()));
        return this;
    }

    @Override
    public boolean interact(Entity entity) {
        if (entity instanceof Creature) {
            Creature other = (Creature)entity;
            if (self.isHostile(other)){
                self.attack(other);
                return true;
            }
            else if (other.getLeader() != null && other.getLeader().equals(self)){
                            ((Creature)self).swapPlaces(other);
            }
        }
        return false;
    }

}
