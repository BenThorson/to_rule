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
        targetPosition = new Point(target.position());
    }

    @Override
    public void execute() {
        target = getTarget();

        if (target == null || target.dead()){
            WanderAI ai = new WanderAI(self);
            self.setAi(ai);
            return;
        }


        if (!self.canSee(target.position())){
            return;
        }
        if(target.equals(targetPosition)){
            if (path.empty()){
                path = pathTo.buildPath(self.getWorld(), self.position(), targetPosition);
            }
            Point nextMove = path.peek();
            if (nextMove != null && self.getWorld().isTravelable(nextMove) || nextMove.equals(targetPosition)){
                self.move(nextMove.subtract(self.position()));
                if (!nextMove.equals(targetPosition)){
                    path.pop();
                }
            } else {
                targetPosition = new Point(target.position());
                path = pathTo.buildPath(self.getWorld(), self.position(), targetPosition);
                nextMove = path.pop();
                self.move(nextMove.subtract(self.position()));
            }
        } else {
            targetPosition = new Point(target.position());
            path = pathTo.buildPath(self.getWorld(), self.position(), targetPosition);
            Point nextMove = path.pop();
            self.move(nextMove.subtract(self.position()));
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
