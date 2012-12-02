package com.bthorson.torule.player;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.ai.PlayerAI;
import com.bthorson.torule.geom.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 1:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class Player {

    private Creature creature;

    private int fame;

    private ExploredMap explored = new ExploredMap();

    private List<Creature> followers = new ArrayList<Creature>();

    public void purchase(int price){}

    public void receive(int amount){}

    public int getFame() {
        return 1;
    }

    public Player(Creature creature){
        this.creature = creature;
        creature.setAi(new PlayerAI(creature));
    }

    public Creature getCreature(){
        return creature;
    }

    public boolean hasExplored(Point point){
        if (explored == null){
            return false;
        }
        return explored.hasExplored(point);
    }

    public void explore(Point point) {
        explored.explore(point);
    }


    public void addFollower(Creature creature) {
        followers.add(creature);
    }

    public void removeFollower(Creature creature){
        followers.remove(creature);
    }

}
