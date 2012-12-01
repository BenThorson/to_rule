package com.bthorson.torule.entity.conversation.actions;

import com.bthorson.torule.entity.conversation.determiners.*;
import com.bthorson.torule.entity.conversation.determiners.Determiner;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 2:00 AM
 * To change this template use File | Settings | File Templates.
 */
public enum ActionMap {
    INSTANCE;

    private HashMap<String, ConversationAction> actions;

    private ActionMap(){
        actions = new HashMap<String, ConversationAction>();
        actions.put("joinPlayer", new JoinPlayer());
        actions.put("showInventory", new ShowInventoryAction());
    }

    public ConversationAction get(String key){
        return actions.get(key);
    }

}
