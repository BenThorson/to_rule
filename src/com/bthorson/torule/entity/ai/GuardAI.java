package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Point;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 7:15 PM
 */
public class GuardAI extends AggroableAI {

    private Point guardPoint;

    public GuardAI(Creature self, Point point) {
        super(self);
        guardPoint = point;
    }

    @Override
    public CreatureAI execute() {
        CreatureAI ai = super.execute();
        if (ai instanceof AggroAI){
            return ai;
        }

        if (!self.position().equals(guardPoint)){
            return new MoveToAI(guardPoint, this);
        }
        return ai;
    }

    @Override
    public boolean interact(Entity entity) {
        return true;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        obj.addProperty("name", getClass().getSimpleName());
        obj.addProperty("self", ((Entity)self).id);
        obj.add("guardPoint", gson.toJsonTree(guardPoint));
        return obj;
    }
}
