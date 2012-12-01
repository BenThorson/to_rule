package com.bthorson.torule.entity.conversation.actions;

import com.bthorson.torule.entity.Creature;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 1:59 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ConversationAction {

    void doAction(Creature creature);
}
