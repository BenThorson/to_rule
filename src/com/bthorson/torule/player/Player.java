package com.bthorson.torule.player;

import com.bthorson.torule.entity.Creature;

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

    private int gold;

    private List<Creature> followers = new ArrayList<Creature>();

    public void purchase(int price){}

    public void receive(int amount){}

    public int getFame() {
        return 1;
    }

    public Player(Creature creature){
        this.creature = creature;
    }

    public Creature getCreature(){
        return creature;
    }


    public void addFollower(Creature creature) {
        followers.add(creature);
    }

    public void clear(){
        creature = null;
        fame = 0;
        gold = 0;
        followers = new ArrayList<Creature>();
    }
}
