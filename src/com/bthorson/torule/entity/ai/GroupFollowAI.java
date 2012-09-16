package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.ai.pathing.AStarPathTo;
import com.bthorson.torule.geom.Point;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 9/15/12
 * Time: 10:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupFollowAI extends CreatureAI {

    private Point lastTarget;
    private Stack<Point> path;


    public GroupFollowAI(Creature self) {
        super(self);
    }

    @Override
    public void execute() {
        if (self.position().equals(self.getTarget())){
            return;
        }
        if (!self.getTarget().equals(lastTarget) || path == null || path.isEmpty()){
            path = new AStarPathTo().buildPath(self.getWorld(), self.position(), self.getTarget());
            lastTarget = self.getTarget();
        }
        Point next = path.peek();
        Creature creat = self.getWorld().creature(next);
        if (creat != null && creat.getFaction().equals(self.getFaction())){
            path = new AStarPathTo().buildPath(self.getWorld(), self.position(), self.getTarget());
            next = path.peek();
        }
        if (self.move(next.subtract(self.position()))){
            path.pop();
        }
    }

    @Override
    public void interact(Entity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
