package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.map.World;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 8:48 PM
 */
public abstract class CreatureAI {

    protected Creature self;

    public CreatureAI(Creature self){

        this.self = self;
    }

    public abstract void execute();

    public abstract void interact(Entity entity);

    protected boolean shouldHostile(Creature other) {
        return self.getFactionEnemies().contains(other.getFaction()) || other.getFactionEnemies().contains(self.getFactionEnemies());
    }

}
