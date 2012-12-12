package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.Herd;
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
    private PathTo pathTo;
    private int stuckCount = 0;
    private int stuckCountMax = 5;


    public MoveToAI(Point point, CreatureAI previous) {
        super(previous.self, previous);
        this.point = point;
        this.previous = previous;
        pathTo = new AStarPathTo();
    }

    @Override
    public CreatureAI execute() {

        CreatureAI ai = new AggroableAI(self, this).execute();
        if (ai instanceof AggroAI){
            return ai;
        }

        if (self.position().equals(point)) {
            return previous;
        }

        if (path == null || path.empty()) {
            path = pathTo.buildPath(World.getInstance(), self.position(), point, self instanceof Herd);
            if (path == null || path.empty()){
                return previous;
            }
        }

        Point nextMove = path.peek();
        if (nextMove != null) {
            Point delta = nextMove.subtract(self.position());
            if (World.getInstance().isTravelable(nextMove) || PointUtil.getDiagDist(self.position(), point) != 1) {

                if (!delta.equals(delta.normalize())){
                    repairPath(nextMove);
                    delta = path.peek().subtract(self.position());
                }

                self.move(delta);
                if (self.position().equals(nextMove)) {
                    path.pop();
                } else if (!nextMove.equals(point)) {
                    if (++stuckCount % stuckCountMax == 0) {
                        path = pathTo.buildPath(World.getInstance(), self.position(), point, self instanceof Herd);
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

    private void repairPath(Point nextMove) {
        path.pop();
        Stack<Point> repair = pathTo.buildPath(World.getInstance(), self.position(), nextMove, self instanceof Herd);
        System.out.println("repairing path");
        Stack<Point> reverse = new Stack<Point>();
        while (!repair.empty()){
            reverse.push(repair.pop());
        }
        while (!reverse.empty()){
            path.push(reverse.pop());
        }
    }


    @Override
    public boolean interact(Entity entity) {
        return false;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.add("point", gson.toJsonTree(point));
        return obj;
    }
}
