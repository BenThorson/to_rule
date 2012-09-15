package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.ai.CreatureAI;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:11 PM
 */
public class PlayerAI extends CreatureAI {

    public PlayerAI(Creature self) {
        super(self);
    }

    @Override
    public void execute() {
        self.move(self.getTarget().subtract(self.position()));
    }

    @Override
    public void interact(Entity entity) {
        if (entity instanceof Creature) {
            Creature other = (Creature)entity;
            if (shouldHostile(other)){
                self.attack(other);
            } else if (other.getLeader().equals(self)){
                self.swapPlaces(other);
            }
        }

    }
}
