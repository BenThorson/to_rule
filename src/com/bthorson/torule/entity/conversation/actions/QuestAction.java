package com.bthorson.torule.entity.conversation.actions;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.quest.Quest;
import com.bthorson.torule.quest.QuestFactory;
import com.bthorson.torule.screens.ConversationScreen;
import com.bthorson.torule.screens.QuestOfferScreen;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 2:12 PM
 */
public class QuestAction implements ConversationAction {

    @Override
    public void doAction(ConversationScreen screen, Creature creature) {
        Quest quest = QuestFactory.INSTANCE.getQuest();
        QuestOfferScreen questScreen = new QuestOfferScreen(screen, creature, quest);
        screen.setNewScreen(questScreen);
    }
}
