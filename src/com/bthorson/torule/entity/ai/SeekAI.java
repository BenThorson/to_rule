package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.ai.pathing.AStarPathTo;
import com.bthorson.torule.entity.ai.pathing.PathTo;
import com.bthorson.torule.geom.Point;

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


    public SeekAI(AiControllable self, Creature target){
        super(self);
        this.target = target;
        targetPosition = new Point(target.position());
    }

    @Override
    public CreatureAI execute() {
        target = getTarget();
        if (target == null || target.dead()){
            WanderAI ai = new WanderAI(self);
            return ai;
        }

        targetPosition = target.position().add(target.getHeading().point());

        if (!self.canSee(target.position())){
            return this;
        }
        if(target.equals(targetPosition)){
            if (path.empty()){
                path = pathTo.buildPath(self.getWorld(), self.position(), targetPosition);
            }
            Point nextMove = path.peek();
            if (nextMove != null && self.getWorld().isTravelable(nextMove) || nextMove.equals(targetPosition)){
                if (self.move(nextMove.subtract(self.position()))){
                    path.pop();
                } else if (!nextMove.equals(targetPosition)) {
                    if (++stuckCount % stuckCountMax == 0){
                        calcAndExecutePath();
                    }
                }
            }
        } else {           //todo optimize by only modifying the end of the path instead of recalcing
            calcAndExecutePath();
        }
        return this;
    }

    private void calcAndExecutePath() {
        targetPosition = new Point(target.position());
        path = pathTo.buildPath(self.getWorld(), self.position(), targetPosition);
        Point nextMove = path.peek();
        if (self.move(nextMove.subtract(self.position()))){
            path.pop();
        }
    }

    @Override
    public void interact(Entity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
