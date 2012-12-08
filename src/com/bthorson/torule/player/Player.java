package com.bthorson.torule.player;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.ai.PlayerAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.persist.SerializeUtils;
import com.bthorson.torule.quest.ActiveQuest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

    private int fame = 10;

    private int commandableRange;

    private ExploredMap explored = new ExploredMap();

    private List<Creature> followers = new ArrayList<Creature>();
    private List<ActiveQuest> quests = new ArrayList<ActiveQuest>();

    public Player(CreatureBuilder builder) {
        super(builder);
        setAi(new PlayerAI(this));
        commandableRange = visionRadius();
    }

    public int getFame() {
        return fame;
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
        if (followers == null){
            followers = new ArrayList<Creature>();
        }
        return new ArrayList<Creature>(followers);

    }

    public void addQuest(ActiveQuest quest) {
        if (quests == null){
            quests = new ArrayList<ActiveQuest>();
        }
        quests.add(quest);
    }

    public void removeQuest(ActiveQuest quest){
        if (quests == null){
            quests = new ArrayList<ActiveQuest>();
        }
        quests.remove(quest);
    }

    public List<ActiveQuest> getQuests() {
        if (quests == null){
            quests = new ArrayList<ActiveQuest>();
        }
        return quests;
    }

    public void addFame(int fame) {
        this.fame += fame;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject object = super.serialize().getAsJsonObject();
        object.addProperty("fame", fame);
        object.add("explored", gson.toJsonTree(explored));
        object.addProperty("commandableRange", commandableRange);
        SerializeUtils.serializeRefCollection(followers, object, "followers");
        SerializeUtils.serializeRefCollection(quests, object, "quests");
        return object;
    }

    public List<Creature> getFollowersInCommandableRange() {
        List<Creature> commandable = new ArrayList<Creature>(followers.size());
        for (Creature follower : followers){
            if (isWithinRange(follower.position(), commandableRange)){
                commandable.add(follower);
            }
        }
        return commandable;
    }
}
