package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 10:51 PM
 */
public class AggroAI extends SeekAI {

    public AggroAI(AiControllable self, Creature target, CreatureAI previous) {

        super(self, target, previous);
    }

    @Override
    public void interact(Entity entity) {
        if (entity == target){
            self.attack((Creature)entity);
        }
    }

}
