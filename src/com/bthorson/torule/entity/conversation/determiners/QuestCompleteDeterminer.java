package com.bthorson.torule.entity.conversation.determiners;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.quest.ActiveQuest;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 6:10 PM
 */
public class QuestCompleteDeterminer implements Determiner {

    @Override
    public int determine(Creature creature) {
        List<ActiveQuest> quests = EntityManager.getInstance().getPlayer().getQuests();
        for (ActiveQuest quest : quests){
            if (quest.getQuestGiver().equals(creature) && quest.isComplete()){
                return 0;
            }
        }
        return 1;
    }
}
