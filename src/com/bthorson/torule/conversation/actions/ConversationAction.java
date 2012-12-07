package com.bthorson.torule.conversation.actions;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.screens.ConversationScreen;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 1:59 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ConversationAction {

    void doAction(ConversationScreen screen, Creature creature);
}
