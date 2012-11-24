package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 11/23/12
 * Time: 11:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class TownAI implements AiControllable{


    @Override
    public boolean move(Point point) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Faction> getFactionEnemies() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Creature> getVisibleCreatures() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean canSee(Point position) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public World getWorld() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attack(Creature entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Point position() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Creature getLeader() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Point getTarget() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Faction getFaction() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Direction getHeading() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isEnemy(Creature facing) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
