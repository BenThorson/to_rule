package com.bthorson.torule.entity.group;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.entity.ai.AiControllable;
import com.bthorson.torule.entity.group.ai.GroupAI;
import com.bthorson.torule.entity.group.ai.PlayerGroupAI;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 1:31 AM
 */
public class Group extends Entity implements AiControllable{


    private Map<Point, Creature> members = new HashMap<Point, Creature>();
    private List<Creature> memList;
    private Creature squadCommander;
    private Point commanderPoint;

    private Formation formation;
    private MovementCommander commander;
    private GroupAI groupAI;

    private boolean reformOnUpdate;

    public Group(List<Creature> memList, Creature squadCommander){
        this.squadCommander = squadCommander;
        this.memList = memList;
        formation = new RectangleFormation();
        formation.buildPositions(memList, squadCommander);
        if (EntityManager.getInstance().isPlayer(squadCommander)){
            groupAI = new PlayerGroupAI(this);
        } else {
            groupAI = new GroupAI(this);
        }
    }

    public void rotateTest(){
        formation.wheelCW();
        members = null;
    }


    public boolean inFormation(){
        if (getCommanderPoint() == null || members == null){
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
        members.get(point).update(World.getInstance().getTurnCounter());
    }

    public void update(){
        if (memList.isEmpty()){
            return;
        }
        if (reformOnUpdate){
            formation.buildPositions(memList, squadCommander);
            reformOnUpdate = false;
        }

        groupAI.execute();


    }

    public boolean move(Point point){

        if (!inFormation() || members.size() == 0){
            commander = new FormUpCommander();
        } else {
            commander = new InPlaceCommander();
        }

        commander.move(this, point);
        return true;
    }

    @Override
    public Set<Faction> getFactionEnemies() {
        return squadCommander.getFactionEnemies();
    }

    @Override
    public List<Creature> getVisibleCreatures() {
        return squadCommander.getVisibleCreatures();
    }

    @Override
    public boolean canSee(Point position) {
        return squadCommander.canSee(position);
    }

    @Override
    public void attack(Creature entity) { }

    @Override
    public Point position() {
        return squadCommander.position();
    }

    @Override
    public Creature getLeader() {
        return squadCommander.getLeader();
    }

    @Override
    public Point getTarget() {
        return squadCommander.getTarget();
    }

    @Override
    public Faction getFaction() {
        return squadCommander.getFaction();
    }

    @Override
    public Direction getHeading() {
        return squadCommander.getHeading();
    }

    @Override
    public boolean isEnemy(Creature facing) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
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

    public void addCreature(Creature creature) {
        memList.add(creature);
        reformOnUpdate = true;
    }

    @Override
    public Creature closestVisibleHostile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isHostile(Creature other) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void remove(Creature dead) {
//        if (memList.remove(dead)){
//            reformOnUpdate = true;
//            if(!memList.isEmpty()){
//                if (dead.equals(squadCommander)){
//                    squadCommander = memList.get(0);
//                }
//            } else {
//                EntityManager.getInstance().removeGroup(this);
//            }
//        }
    }

    @Override
    public JsonElement serialize() {
        return super.serialize();
    }
}
