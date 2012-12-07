package com.bthorson.torule.quest;

import com.bthorson.torule.entity.Creature;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 7:44 PM
 */
public interface ActiveQuest {
    public Creature getQuestGiver();
    public boolean isComplete();
    public QuestReward getReward();
}
