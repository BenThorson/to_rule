package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.ai.pathing.AStarPathTo;
import com.bthorson.torule.entity.ai.pathing.PathTo;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.World;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Stack;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 6:04 PM
 */
public class MoveToAI extends CreatureAI {

    private Point point;
    private CreatureAI previous;
    private Stack<Point> path = new Stack<Point>();
    private PathTo pathTo = new AStarPathTo();

    public MoveToAI(Point point, CreatureAI previous) {
        super(previous.self);
        this.point = point;
        this.previous = previous;
        path = pathTo.buildPath(World.getInstance(), self.position(), point);
    }

    @Override
    public CreatureAI execute() {

        if (self.position().equals(point)) {
            return previous;
        }

        if (path.empty()) {
            path = pathTo.buildPath(World.getInstance(), self.position(), point);
        }

        Point nextMove = path.peek();
        if (nextMove != null) {
            Point delta = nextMove.subtract(self.position());
            if (World.getInstance().isTravelable(nextMove) || PointUtil.getDiagDist(self.position(), point) != 1) {
                self.move(delta);
                path.pop();
            } else {
                Direction[] alternates = Direction.directionOf(delta).neighboringDirections();
                if (!World.getInstance().isOccupied(alternates[0].point())) {
                    self.move(alternates[0].point());
                } else if (!World.getInstance().isOccupied(alternates[1].point())) {
                    self.move(alternates[1].point());
                }
            }
        } else {
            return previous;
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
        obj.add("point", gson.toJsonTree(point));
        obj.add("previous", previous.serialize());
        return obj;
    }
}
