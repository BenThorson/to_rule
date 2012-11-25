package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;

/**
 * User: ben
 * Date: 9/14/12
 * Time: 10:53 PM
 */
public class FollowAI extends SeekAI {


    public FollowAI(AiControllable self) {
        super(self, self.getLeader());
    }

    @Override
    protected Creature getTarget() {
        return self.getLeader();
    }
}
