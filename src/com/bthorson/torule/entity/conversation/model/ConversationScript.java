package com.bthorson.torule.entity.conversation.model;

/**
 * User: Ben Thorson
 * Date: 11/30/12
 * Time: 12:46 PM
 */
public class ConversationScript {
    private String profession;
    private ConversationNode introduction;

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public ConversationNode getIntroduction() {
        return introduction;
    }

    public void setIntroduction(ConversationNode introduction) {
        this.introduction = introduction;
    }
}
