package com.bthorson.torule.entity.group;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Point;

import java.util.*;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 1:31 AM
 */
public class Group {


    private Map<Point, Creature> members = new HashMap<Point, Creature>();
    private List<Creature> memList;
    private Creature squadCommander;
    private Point commanderPoint;

    private Formation formation;
    private MovementCommander commander;

    public Group(List<Creature> memList, Creature squadCommander){
        this.squadCommander = squadCommander;
        this.memList = memList;
        formation = new RectangleFormation();
        formation.buildPositions(memList, squadCommander);
    }


    private boolean inFormation(){
        if (getCommanderPoint() == null){
            return false;
        }
        Point offset = getSquadCommander().position().subtract(getCommanderPoint());
        for (Point p : members.keySet()){
            if (members.get(p) != null){
                if (!members.get(p).position().equals(p.add(offset))){
                    return false;
                }
            }
        }
        return true;
    }

    public void updateForPoint(Point point, Point offset){
        if (point == null || members.get(point) == null){
            return;
        }
        members.get(point).setTarget(point.add(offset));
        members.get(point).update();
    }

    public void move(Point point){

        if (!inFormation() || members.size() == 0){
            commander = new FormUpCommander();
        } else {
            commander = new InPlaceCommander();
        }

        commander.move(this, point);
    }

    public Formation getFormation() {
        return formation;
    }

    public void setMembers(Map<Point, Creature> members) {
        this.members = members;
    }

    public Creature getSquadCommander() {
        return squadCommander;
    }

    public Point getCommanderPoint() {
        return commanderPoint;
    }

    public void setCommanderPoint(Point commanderPoint) {
        this.commanderPoint = commanderPoint;
    }

    public List<Creature> getMemList() {
        return memList;
    }
}
