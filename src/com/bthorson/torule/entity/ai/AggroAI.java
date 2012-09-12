package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 10:51 PM
 */
public class AggroAI implements CreatureAI {
    private Creature self;

    private Creature target;

    public AggroAI(Creature self, Creature target){
        this.target = target;
        this.self = self;
    }

    @Override
    public void execute() {

        if (target.dead()){
            self.setAi(new WanderAI(self));
        }
        if (target != null){
            if (self.canSee(target.x, target.y)){
                self.goToTarget(target.x, target.y);
            }
        }
    }

    @Override
    public void interact(Entity entity) {
        if (entity == target){
            self.attack((Creature)entity);
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
