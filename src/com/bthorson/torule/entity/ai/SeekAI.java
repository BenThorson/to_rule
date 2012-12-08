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

    public SeekAI(AiControllable self, Creature target, CreatureAI previous){
        super(self, previous);
        this.target = target;
        this.previous = previous;
        targetPosition = new Point(target.position());
    }

    @Override
    public CreatureAI execute() {
        if (target == null || target.dead()){
            return previous;
        }

        if (PointUtil.manhattanDist(self.position(), target.position()) > 100){
            System.out.println("Too large to calculate, aborting seek ai");
            return previous;
        }



        if (self.canSee(target.position())){
            targetPosition = target.position();
        }

        if (path.empty()){
            if (targetPosition.equals(self.position())){
                return this;
            }
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
    public boolean interact(Entity entity) {
        return false;
    }

    @Override
    public JsonElement serialize() {
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.addProperty("target", target.id);
        return obj;
    }
}
