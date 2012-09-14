package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.ai.pathing.AStarPathTo;
import com.bthorson.torule.entity.ai.pathing.PathTo;
import com.bthorson.torule.geom.Point;

import java.util.Stack;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 10:51 PM
 */
public class AggroAI extends CreatureAI {

    private Creature target;
    private Point targetPosition;
    private Stack<Point> path = new Stack<Point>();
    private PathTo pathTo = new AStarPathTo();


    public AggroAI(Creature self, Creature target){
        super(self);
        this.target = target;
        targetPosition = new Point(target.x, target.y);
    }

    @Override
    public void execute() {
        target = getTarget();

        if (target == null || target.dead()){
            WanderAI ai = new WanderAI(self);
            self.setAi(ai);
            return;
        }


        if (!self.canSee(target.x, target.y)){
            return;
        }
        if(target.x == targetPosition.x() && target.y == targetPosition.y()){
            if (path.empty()){
                path = pathTo.buildPath(self.getWorld(), new Point(self.x, self.y), targetPosition);
                path.pop();
            }
            Point nextMove = path.peek();
            if (nextMove != null && self.getWorld().isTravelable(nextMove.x(), nextMove.y()) || nextMove.equals(targetPosition)){
                self.move(nextMove.x() - self.x, nextMove.y() - self.y);
                if (!nextMove.equals(targetPosition)){
                    path.pop();
                }
            } else {
                targetPosition = new Point(target.x, target.y);
                path = pathTo.buildPath(self.getWorld(), new Point(self.x, self.y), targetPosition);
                path.pop();
                nextMove = path.pop();
                self.move(nextMove.x() - self.x, nextMove.y() - self.y);
            }
        } else {
            targetPosition = new Point(target.x, target.y);
            path = pathTo.buildPath(self.getWorld(), new Point(self.x, self.y), targetPosition);
            path.pop();
            Point nextMove = path.pop();
            self.move(nextMove.x() - self.x, nextMove.y() - self.y);
        }
    }

    @Override
    public void interact(Entity entity) {
        if (entity == target){
            self.attack((Creature)entity);
            if(((Creature) entity).dead()){
                self.setAi(new WanderAI(self));
            }
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
