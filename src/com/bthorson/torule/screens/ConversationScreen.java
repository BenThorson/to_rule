package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.conversation.SampleConversation;
import com.bthorson.torule.entity.conversation.model.ConversationNode;
import com.bthorson.torule.entity.conversation.model.ConversationTextAndOptions;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * User: Ben Thorson
 * Date: 11/25/12
 * Time: 8:26 AM
 */
public class ConversationScreen implements ControlCallbackScreen {

    private SelectScreen selectScreen;
    private boolean attemptedSelection;
    private PlayScreen previous;
    private Menu convoDialog;
    private SampleConversation conversation;
    private Creature conversant;

    public ConversationScreen(PlayScreen playScreen, Point position) {
        this.previous = playScreen;
        selectScreen = new SelectScreen(playScreen, position, this);
    }

    @Override
    public void positionSelected(Point point) {
        attemptedSelection = true;
        conversant = World.getInstance().creature(point.add(previous.getOffset()));
        if (conversant != null){
            conversation = new SampleConversation(conversant);
            ConversationTextAndOptions convoTexts = conversation.startConversation();
            convoDialog = new Menu(conversant.getName(), convoTexts.getText(), convoTexts.getOptions(), AsciiPanel.yellow, AsciiPanel.black );
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        if (convoDialog == null){
            selectScreen.displayOutput(terminal);
        } else {
            previous.displayOutput(terminal);
            convoDialog.displayOutput(terminal, 5,5);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        int response = -1;
        if (convoDialog == null){
            return attemptedSelection ? previous : selectScreen;
        } else if ((response = convoDialog.respondToUserInput(key)) != -1) {
            ConversationTextAndOptions convs = conversation.continueConversation(response);
            if (convs != null){
                convoDialog = new Menu(conversant.getName(), convs.getText(), convs.getOptions(), AsciiPanel.yellow, AsciiPanel.black );
            } else {
                return previous;
            }
        }
        return this;
    }

    @Override
    public Screen respondToMouseInput(Point key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
