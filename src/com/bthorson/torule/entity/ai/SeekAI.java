package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.ai.pathing.AStarPathTo;
import com.bthorson.torule.entity.ai.pathing.PathTo;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.World;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Stack;

/**
 * User: ben
 * Date: 9/14/12
 * Time: 11:29 PM
 */
public class SeekAI extends CreatureAI{
    protected Creature target;
    private Point targetPosition;
    private Stack<Point> path = new Stack<Point>();
    private PathTo pathTo = new AStarPathTo();
    private int stuckCount;
    private int stuckCountMax = 5;

    private CreatureAI previous;


    public SeekAI(AiControllable self, Creature target, CreatureAI previous){
        super(self);
        this.target = target;
        this.previous = previous;
        targetPosition = new Point(target.position());
    }

    @Override
    public CreatureAI execute() {
        if (target == null || target.dead()){
            return previous;
        }

        targetPosition = target.position();

        if (!self.canSee(target.position())){
            if (!path.empty()){
                self.move(path.pop());
            }
        }
        if (path.empty()){
            path = pathTo.buildPath(World.getInstance(), self.position(), targetPosition);
        }

        Point nextMove = path.peek();
        if (nextMove != null && PointUtil.getDiagDist(nextMove, targetPosition) <
                                PointUtil.getDiagDist(self.position(), targetPosition) &&
                (World.getInstance().isTravelable(nextMove) ||
                        nextMove.equals(targetPosition))){
            if (self.move(nextMove.subtract(self.position()))){
                path.pop();
            } else if (!nextMove.equals(targetPosition)) {
                if (++stuckCount % stuckCountMax == 0){
                    calcAndExecutePath();
                }
            }
        } else {
            calcAndExecutePath();
        }
        return this;
    }

    private void calcAndExecutePath() {
        targetPosition = new Point(target.position());
        path = pathTo.buildPath(World.getInstance(), self.position(), targetPosition);
        Point nextMove = path.peek();
        if (self.move(nextMove.subtract(self.position()))){
            path.pop();
        }
    }

    @Override
    public void interact(Entity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JsonElement serialize() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", getClass().getSimpleName());
        obj.addProperty("self", ((Entity)self).id);
        obj.addProperty("target", target.id);
        obj.add("previous", previous.serialize());
        return obj;
    }
}
