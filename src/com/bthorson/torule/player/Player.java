package com.bthorson.torule.player;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.ai.PlayerAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.persist.SerializeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 1:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class Player extends Creature {

    private int fame;

    private ExploredMap explored = new ExploredMap();

    private List<Creature> followers = new ArrayList<Creature>();

    public Player(CreatureBuilder builder) {
        super(builder);
        setAi(new PlayerAI(this));
    }

    public int getFame() {
        return 1;
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
        if (followers == null){
            followers = new ArrayList<Creature>();
        }
        followers.add(creature);
    }

    public void removeFollower(Creature creature){
        if (followers != null){
            followers.remove(creature);
        }
    }

    public List<Creature> getFollowers() {
        if (followers != null){
            return new ArrayList<Creature>(followers);
        } else {
            return null;
        }

    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject object = super.serialize().getAsJsonObject();
        object.addProperty("fame", fame);
        object.add("explored", gson.toJsonTree(explored));
        SerializeUtils.serializeRefCollection(followers, object, "followers");
        return object;
    }
}
