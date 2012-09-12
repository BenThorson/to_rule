package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.ai.CreatureAI;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:11 PM
 */
public class PlayerAI implements CreatureAI {

    private Creature self;

    public PlayerAI(Creature self) {
        this.self = self;
    }

    @Override
    public void execute() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void interact(Entity entity) {
        if (entity instanceof Creature){
            self.attack((Creature)entity);
        }
    }
}