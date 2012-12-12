package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.Herd;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.World;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Used by a member of a Herd, which differs from a group in that there is no formation, and the looseness of the herd
 * allows for non synchronous movement.
 * User: Ben Thorson
 * Date: 12/10/12
 * Time: 1:41 PM
 */
public class HerdAI extends CreatureAI {

    private Herd herd;

    public HerdAI(AiControllable self,
                  CreatureAI previous,
                  Herd herd) {
        super(self, previous);
        this.herd = herd;

    }

    @Override
    public CreatureAI execute() {
        if (self.position().withinRect(herd.getNwPoint(), herd.getSePoint())){
            new WanderAI(self, herd.getNwPoint(), herd.getSePoint(), null, true).execute();
        } else {
            return new MoveToAI(getSuitablePointInHerd(), this);
        }
        return this;
    }

    private Point getSuitablePointInHerd() {
        for (int i = 0; i < 1000; i++){
            Point point = PointUtil.randomPoint(herd.getNwPoint(), herd.getSePoint());
            if (World.getInstance().isTravelable(point)){
                return point;
            }
        }
        throw new RuntimeException("couldn't find point for herd" );
    }

    @Override
    public boolean interact(Entity entity) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JsonElement serialize() {
        JsonObject object = super.serialize().getAsJsonObject();
        object.addProperty("herd", herd.id);
        return object;
    }
}
