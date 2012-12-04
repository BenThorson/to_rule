package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonObject;

/**
 * User: ben
 * Date: 9/11/12
 * Time: 9:36 PM
 */
public class DeadAi extends CreatureAI {

    public DeadAi() {
        super(null);
    }

    @Override
    public CreatureAI execute() {
        return this;
    }

    @Override
    public void interact(Entity entity) {
    }

    @Override
    public JsonObject serialize() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", getClass().getSimpleName());
        return obj;
    }
}
