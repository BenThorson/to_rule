package com.bthorson.torule.conversation.actions;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.screens.ConversationScreen;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 2:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class JoinPlayer implements ConversationAction {
    @Override
    public void doAction(ConversationScreen screen, Creature creature) {
        EntityManager.getInstance().getPlayer().addFollower(creature);
        creature.follow(EntityManager.getInstance().getPlayer());
    }
}
