package com.bthorson.torule.entity;

import com.bthorson.torule.entity.group.Group;
import com.bthorson.torule.geom.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ben
 * Date: 9/20/12
 * Time: 10:12 AM
 */
public class EntityManager {

    private List<Creature> nonGroupedCreatures;
    private List<Group> groups;
    private List<Group> groupToRemove;
    private Group playerGroup;
    private Creature player;

    private static final EntityManager INSTANCE = new EntityManager();

    private EntityManager(){
        groupToRemove = new ArrayList<Group>();
        nonGroupedCreatures = new ArrayList<Creature>();
        groups = new ArrayList<Group>();
    }

    public static EntityManager getInstance(){
        return INSTANCE;
    }

    public void setPlayer(Creature creature){
        this.player = creature;
    }

    public void addCreature(Creature creature){
        nonGroupedCreatures.add(creature);
    }

    public void addGroup(Group group){
        groups.add(group);
    }

    public void creatureToGroup(Creature creature, Group group){
        nonGroupedCreatures.remove(creature);
        group.addCreature(creature);
    }

    public void update(){
        for (Group toRemove: groupToRemove){
            groups.remove(toRemove);
        }
        groupToRemove.clear();
        for (Group group : groups){
            group.update();
        }

        for (Creature creature : nonGroupedCreatures){
            creature.update();
        }
    }

    public Creature creatureAt(Point position){
        for (Group group : groups){
            for (Creature creature : group.getMemList()){
                if (creature.position().equals(position)){
                    return creature;
                }
            }
        }

        for (Creature creature : nonGroupedCreatures){
            if (creature.position().equals(position)){
                return creature;
            }
        }
        return null;
    }

    public void remove(Creature dead) {
        for (Group group : groups){
            group.remove(dead);
        }
        nonGroupedCreatures.remove(dead);
    }

    public List<Creature> getCreaturesInRange(Point p1, Point p2) {
        List<Creature> retList = new ArrayList<Creature>();
        for (Group group : groups){
            for (Creature creature : group.getMemList()){
                if (creature.position().withinRect(p1, p2)){
                    retList.add(creature);
                }
            }
        }
        for (Creature c: nonGroupedCreatures){
            if (c.position().withinRect(p1, p2)){
                retList.add(c);
            }
        }
        return retList;
    }

    public void removeGroup(Group group) {

        groupToRemove.add(group);
    }

    public boolean isPlayer(Creature creature) {
        return creature.equals(player);
    }
}