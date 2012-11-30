package com.bthorson.torule.entity.conversation.model;

import java.util.List;

/**
 * User: Ben Thorson
 * Date: 11/30/12
 * Time: 12:56 PM
 */
public class Conversations {

    private List<ConversationScript> professions;

    public List<ConversationScript> getProfessions() {
        return professions;
    }

    public void setProfessions(List<ConversationScript> professions) {
        this.professions = professions;
    }
}
