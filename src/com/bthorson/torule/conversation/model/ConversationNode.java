package com.bthorson.torule.conversation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 11/30/12
 * Time: 12:49 PM
 */
public class ConversationNode {

    private String id;
    private String text;
    private String check;
    private String action;
    private String newScreen;
    private List<ConversationNode> responses = new ArrayList<ConversationNode>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNewScreen() {
        return newScreen;
    }

    public void setNewScreen(String newScreen) {
        this.newScreen = newScreen;
    }

    public List<ConversationNode> getResponses() {
        return responses;
    }

    public void setResponses(List<ConversationNode> responses) {
        this.responses = responses;
    }

}
