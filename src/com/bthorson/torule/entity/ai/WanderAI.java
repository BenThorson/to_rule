package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.World;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Random;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 8:51 PM
 */
public class WanderAI extends AggroableAI {

    private Point nwBound;
    private Point seBound;
    public WanderAI(Creature self, Point nwBound, Point seBound){
        super(self);
        this.nwBound = nwBound;
        this.seBound = seBound;
    }

    @Override
    public CreatureAI execute() {

        CreatureAI ai = super.execute();
        if (ai instanceof AggroAI){
            return ai;
        }

        int check = new Random().nextInt(10);

        switch (check){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                break;
            case 6:
                if(self.position().add(Direction.NORTH.point()).withinRect(nwBound, seBound))
                    self.move(Direction.NORTH.point());
                break;
            case 7:
                if(self.position().add(Direction.SOUTH.point()).withinRect(nwBound, seBound))
                    self.move(Direction.SOUTH.point());
                break;
            case 8:
                if(self.position().add(Direction.EAST.point()).withinRect(nwBound, seBound))
                    self.move(Direction.EAST.point());
                break;
            case 9:
                if(self.position().add(Direction.WEST.point()).withinRect(nwBound, seBound))
                    self.move(Direction.WEST.point());
                break;
        }
        return this;
    }

    @Override
    public void interact(Entity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        obj.addProperty("name", getClass().getSimpleName());
        obj.addProperty("self", ((Entity)self).id);
        obj.add("nwBound", gson.toJsonTree(nwBound));
        obj.add("seBound", gson.toJsonTree(seBound));
        return obj;
    }
}
