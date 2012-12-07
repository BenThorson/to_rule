package com.bthorson.torule.quest;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.persist.SerializeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 7:44 PM
 */
public class ActiveQuest extends Entity {

    private String description;
    private Creature questGiver;
    private List<Creature> creatures;
    private QuestReward questReward;

    public ActiveQuest(String name, String description, Creature questGiver, List<Creature> creatures, QuestReward questReward) {
        super(name);
        this.description = description;
        this.questGiver = questGiver;
        this.creatures = creatures;
        this.questReward = questReward;
    }

    public Creature getQuestGiver(){
        return questGiver;
    }


    public void setQuestGiver(Creature questGiver) {
        this.questGiver = questGiver;
    }

    public boolean isComplete(){
        for (Creature creature : creatures){
            if (!creature.dead()){
                return false;
            }
        }
        return true;
    }

    public QuestReward getReward() {
        return questReward;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public void setCreatures(List<Creature> creatures) {
        this.creatures = creatures;
    }

    public QuestReward getQuestReward() {
        return questReward;
    }

    public void setQuestReward(QuestReward questReward) {
        this.questReward = questReward;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject object = super.serialize().getAsJsonObject();
        object.addProperty("description", description);
        object.addProperty("questGiver", questGiver.id);
        SerializeUtils.serializeRefCollection(creatures, object, "creatures");
        object.add("questReward", gson.toJsonTree(questReward));
        return object;
    }
}
