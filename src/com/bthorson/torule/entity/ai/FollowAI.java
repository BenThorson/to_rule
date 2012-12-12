package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;

/**
 * Uses delegated seeking behavior to follow an object that is its leader
 * User: ben
 * Date: 9/14/12
 * Time: 10:53 PM
 */
public class FollowAI extends SeekAI {

    public FollowAI(AiControllable self, CreatureAI previous) {
        super(self, self.getLeader(), previous);
    }

    @Override
    public CreatureAI execute() {
        CreatureAI ai = new AggroableAI(self, this).execute();
        if (ai instanceof AggroAI){
            return ai;
        }
        return super.execute();
    }

    @Override
    protected Creature getTarget() {
        return self.getLeader();
    }

}
