package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.ai.pathing.AStarPathTo;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public CreatureAI execute() {
        if (self.position().equals(self.getTarget()) || self.getTarget() == null){
            checkToInteract();
            return this;
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
        } else {
            checkToInteract();
        }
        return this;
    }

    private void checkToInteract() {
        Creature adjacentHostile = getAdjacentToHostile();
        if (adjacentHostile != null){
            interact(adjacentHostile);
        }
    }

    private Creature getAdjacentToHostile() {
        List<Creature> creatures = new ArrayList<Creature>();
        Creature facing = self.getWorld().creature(self.position().add(self.getHeading().point()));
        if (facing != null  && self.isEnemy(facing)){
            return facing;
        }
        for (Direction d : Direction.values()){
            if (d.equals(self.getHeading())){
                continue;
            }
            Creature candidate = self.getWorld().creature(self.position().add(d.point()));
            if (candidate != null && self.isEnemy(candidate)){
                creatures.add(candidate);
            }
        }
        if (creatures.isEmpty()){
            return null;
        }
        Collections.shuffle(creatures);
        return creatures.get(0);
    }

    @Override
    public void interact(Entity entity) {
        if (entity instanceof Creature){
            Creature creat = (Creature)entity;
            if (self.isEnemy(creat)){
                self.attack(creat);
            }
        }

    }
}
