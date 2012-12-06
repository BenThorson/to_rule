package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * User: ben
 * Date: 9/14/12
 * Time: 10:53 PM
 */
public class FollowAI extends SeekAI {


    public FollowAI(AiControllable self, CreatureAI previous) {
        super(self, self.getLeader(), previous);
    }

    @Override
    protected Creature getTarget() {
        return self.getLeader();
    }

}
