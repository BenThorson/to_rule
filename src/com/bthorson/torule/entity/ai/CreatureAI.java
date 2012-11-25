package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.NearestComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 8:48 PM
 */
public abstract class CreatureAI {

    protected AiControllable self;

    public CreatureAI(AiControllable self){

        this.self = self;
    }

    public abstract CreatureAI execute();

    public abstract void interact(Entity entity);

    protected boolean shouldHostile(Creature other) {
        return self.getFactionEnemies().contains(other.getFaction()) || other.getFactionEnemies().contains(self.getFactionEnemies());
    }

    protected Creature getTarget() {
        List<Creature> visibleCreatures = self.getVisibleCreatures();
        List<Creature> hostilable = new ArrayList<Creature>();
        for (Creature other: visibleCreatures){
            if (shouldHostile(other)){
                hostilable.add(other);
            }
        }
        if (hostilable.size() > 0){
            Collections.sort(hostilable, new NearestComparator(self.position()));
            return hostilable.get(0);
        }
        return null;
    }

}
