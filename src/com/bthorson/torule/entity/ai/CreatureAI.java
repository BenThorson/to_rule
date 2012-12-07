package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.NearestComparator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 8:48 PM
 */
public abstract class CreatureAI {

    protected CreatureAI previous;
    protected AiControllable self;

    public CreatureAI(AiControllable self, CreatureAI previous){
        this.previous = previous;
        this.self = self;
    }

    protected Creature getTarget(){
        return self.closestVisibleHostile();
    }

    public CreatureAI getPrevious(){
        return previous;
    }

    public abstract CreatureAI execute();

    public abstract boolean interact(Entity entity);

    public JsonElement serialize(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", getClass().getSimpleName());
        jsonObject.addProperty("self", ((Entity)self).id);
        if (previous != null){
            jsonObject.add("previous", previous.serialize());
        } else {
            jsonObject.addProperty("previous", 0);
        }
        return jsonObject;
    }
}
