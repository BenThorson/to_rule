package com.bthorson.torule.entity.conversation.actions;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.screens.ConversationScreen;
import com.bthorson.torule.screens.PlayerBuyScreen;
import com.bthorson.torule.screens.PlayerSellScreen;
import com.bthorson.torule.screens.Screen;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 2:36 PM
 */
public class PlayerSellAction implements ConversationAction {

    @Override
    public void doAction(ConversationScreen parent, Creature creature) {
        Screen screen = new PlayerSellScreen(parent, creature.getOwnedProperties("shop"));
        parent.setNewScreen(screen);
    }
}
