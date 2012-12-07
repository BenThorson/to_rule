package com.bthorson.torule.quest;

import com.bthorson.torule.entity.Creature;

import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 6:04 PM
 */
public class KillQuest implements ActiveQuest {


    private String name;
    private String objective;
    private Creature questGiver;
    private List<Creature> creatures;
    private QuestReward questReward;

    public KillQuest(String name, String objective, Creature questGiver, List<Creature> creatures, QuestReward questReward) {
        this.name = name;
        this.objective = objective;
        this.questGiver = questGiver;
        this.creatures = creatures;
        this.questReward = questReward;
    }

    public Creature getQuestGiver(){
        return questGiver;
    }

    public boolean isComplete(){
        for (Creature creature : creatures){
            if (!creature.dead()){
                return false;
            }
        }
        return true;
    }

    @Override
    public QuestReward getReward() {
        return questReward;
    }
}
