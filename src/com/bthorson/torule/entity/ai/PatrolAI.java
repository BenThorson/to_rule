package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Point;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 9:15 PM
 */
public class PatrolAI extends AggroableAI {

    private List<Point> patrolPath;
    private int current = 0;

    public PatrolAI(Creature self, List<Point> patrolPath, CreatureAI previous){
        super(self, previous);
        this.patrolPath = patrolPath;
    }

    public PatrolAI(Creature self, List<Point> patrolPath, int start, CreatureAI previous){
        super(self, previous);
        this.patrolPath = patrolPath;
        current = start;
    }

    @Override
    public CreatureAI execute() {
        CreatureAI ai = super.execute();
        if (ai instanceof AggroAI){
            return ai;
        }

        if (patrolPath.get(current).equals(self.position())){
            current = (current + 1) % patrolPath.size();
        }
        return new MoveToAI(patrolPath.get(current), this);

    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.addProperty("current", current);
        obj.add("patrolPath", gson.toJsonTree(patrolPath));
        return obj;
    }
}
