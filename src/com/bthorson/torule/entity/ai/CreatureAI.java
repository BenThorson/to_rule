package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.NearestComparator;
import com.google.gson.JsonElement;

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

    protected Creature getTarget(){
        return self.closestVisibleHostile();
    }

    public abstract CreatureAI execute();

    public abstract boolean interact(Entity entity);

    public abstract JsonElement serialize();
}
