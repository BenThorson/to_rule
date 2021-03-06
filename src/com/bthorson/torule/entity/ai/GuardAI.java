package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Point;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This AI stands in one place.  If an enemy approaches, it will attack until the target or it is dead.  If it survives
 * it will return to the passed in position
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 7:15 PM
 */
public class GuardAI extends AggroableAI {

    private Point guardPoint;

    public GuardAI(AiControllable self, Point point, CreatureAI previous) {
        super(self, previous);
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
        return this;
    }

    @Override
    public boolean interact(Entity entity) {
        return true;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.add("guardPoint", gson.toJsonTree(guardPoint));
        return obj;
    }
}
