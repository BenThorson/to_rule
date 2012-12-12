package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.World;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Random;

/**
 * Randomly and aimlessly moves around
 * User: ben
 * Date: 9/7/12
 * Time: 8:51 PM
 */
public class WanderAI extends CreatureAI {

    private Point nwBound;
    private Point seBound;
    private boolean isAggroable;

    public WanderAI(AiControllable creature, CreatureAI previous, boolean isAggroable) {
        this(creature, PointUtil.POINT_ORIGIN, World.getInstance().seCorner(), previous, isAggroable);
    }

    public WanderAI(AiControllable self, Point nwBound, Point seBound, CreatureAI previous, boolean isAggroable) {
        super(self, previous);
        this.nwBound = nwBound;
        this.seBound = seBound;
        this.isAggroable = isAggroable;
    }

    @Override
    public CreatureAI execute() {

        if (isAggroable){
            CreatureAI ai = new AggroableAI(self, this).execute();
            if (ai instanceof AggroAI){
                return ai;
            }
        }


        int check = new Random().nextInt(20);

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
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                break;
            case 16:
                if(self.position().add(Direction.NORTH_WEST.point()).withinRect(nwBound, seBound))
                    self.move(Direction.NORTH_WEST.point());
                break;
            case 17:
                if(self.position().add(Direction.SOUTH_EAST.point()).withinRect(nwBound, seBound))
                    self.move(Direction.SOUTH_EAST.point());
                break;
            case 18:
                if(self.position().add(Direction.NORTH_EAST.point()).withinRect(nwBound, seBound))
                    self.move(Direction.NORTH_EAST.point());
                break;
            case 19:
                if(self.position().add(Direction.WEST.point()).withinRect(nwBound, seBound))
                    self.move(Direction.SOUTH_WEST.point());
                break;
        }
        return this;
    }

    @Override
    public boolean interact(Entity entity) {
        return false;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.add("nwBound", gson.toJsonTree(nwBound));
        obj.add("seBound", gson.toJsonTree(seBound));
        obj.addProperty("isAggroable", isAggroable);
        return obj;
    }
}
