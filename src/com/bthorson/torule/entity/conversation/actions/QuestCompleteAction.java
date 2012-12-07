package com.bthorson.torule.entity.conversation.actions;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.quest.ActiveQuest;
import com.bthorson.torule.quest.QuestReward;
import com.bthorson.torule.screens.ConversationScreen;

import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 8:44 PM
 */
public class QuestCompleteAction implements ConversationAction {

    @Override
    public void doAction(ConversationScreen screen, Creature creature) {
        List<ActiveQuest> quests = EntityManager.getInstance().getPlayer().getQuests();
        ActiveQuest toRemove = null;
        for (ActiveQuest quest : quests){
            if (quest.getQuestGiver().equals(creature)){
                QuestReward reward = quest.getReward();
                EntityManager.getInstance().getPlayer().addGold(reward.getGold());
                EntityManager.getInstance().getPlayer().addFame(reward.getFame());
                toRemove = quest;
            }
        }
        EntityManager.getInstance().getPlayer().removeQuest(toRemove);
    }
}
