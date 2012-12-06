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
public class MoveToAI extends AggroableAI {

    private Point point;
    private CreatureAI previous;
    private Stack<Point> path = new Stack<Point>();
    private PathTo pathTo;
    private boolean shouldAggro;
    private int stuckCount = 0;
    private int stuckCountMax = 5;

    public MoveToAI(Point point, CreatureAI previous) {
        this(point, previous, false);
    }

    public MoveToAI(Point point, CreatureAI previous, boolean shouldAggro) {
        super(previous.self);
        this.point = point;
        this.previous = previous;
        pathTo = new AStarPathTo();
        this.shouldAggro = shouldAggro;
    }

    @Override
    public CreatureAI execute() {

        if (shouldAggro) {

            CreatureAI ai = super.execute();
            if (ai instanceof AggroAI) {
                return ai;
            }
        }

        if (self.position().equals(point)) {
            return previous;
        }

        if (path == null || path.empty()) {
            path = pathTo.buildPath(World.getInstance(), self.position(), point);
        }

        Point nextMove = path.peek();
        if (nextMove != null) {
            Point delta = nextMove.subtract(self.position());
            if (World.getInstance().isTravelable(nextMove) || PointUtil.getDiagDist(self.position(), point) != 1) {
                self.move(delta.normalize());
                if (self.position().equals(nextMove)) {
                    path.pop();
                } else if (!nextMove.equals(point)) {
                    if (++stuckCount % stuckCountMax == 0) {
                        path = pathTo.buildPath(World.getInstance(), self.position(), point);
                        return this;
                    }
                }
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
    public boolean interact(Entity entity) {
        return false;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        obj.addProperty("name", getClass().getSimpleName());
        obj.add("point", gson.toJsonTree(point));
        obj.add("previous", previous.serialize());
        return obj;
    }
}
