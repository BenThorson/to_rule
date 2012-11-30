package com.bthorson.torule.entity.conversation.model;

import com.bthorson.torule.screens.Screen;

/**
 * User: Ben Thorson
 * Date: 11/30/12
 * Time: 1:59 PM
 */
public class ConversationTextAndOptions {

    private String text;
    private String[] options;

    private Screen newScreen;

    public ConversationTextAndOptions(String text, String[] options) {
        this.text = text;
        this.options = options;
    }

    public ConversationTextAndOptions(String text, String[] options, Screen newScreen) {
        this.text = text;
        this.options = options;
        this.newScreen = newScreen;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }
}
