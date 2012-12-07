package com.bthorson.torule.conversation.model;

import com.bthorson.torule.conversation.actions.ConversationAction;

/**
 * User: Ben Thorson
 * Date: 11/30/12
 * Time: 1:59 PM
 */
public class ConversationTextAndOptions {

    private String text;
    private String[] options;

    private ConversationAction action;

    public ConversationTextAndOptions(String text, String[] options) {
        this.text = text;
        this.options = options;
    }

    public ConversationTextAndOptions(String text, String[] options, ConversationAction action) {
        this.text = text;
        this.options = options;
        this.action = action;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public ConversationAction getAction() {
        return action;
    }
}
