package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.conversation.SampleConversation;
import com.bthorson.torule.conversation.model.ConversationTextAndOptions;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;
import com.bthorson.torule.screens.component.Menu;

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
    private Screen newScreen;
    private boolean goBack = false;

    public ConversationScreen(PlayScreen playScreen, Point position) {
        this.previous = playScreen;
        selectScreen = new SelectScreen(playScreen, position, this);
    }

    @Override
    public void positionSelected(Point point) {
        if (point == null){
            goBack = true;
            return;
        }
        attemptedSelection = true;
        conversant = World.getInstance().creature(point.add(previous.getOffset()));
        if (conversant != null){
            startConversation();
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
        if (goBack){
            return previous;
        }
        newScreen = null;
        int response = -1;
        if (convoDialog == null){
            return attemptedSelection ? previous : selectScreen.respondToUserInput(key);
        } else if ((response = convoDialog.respondToUserInput(key)) != -1) {
            if (response == -2){
                return previous;
            }
            ConversationTextAndOptions convs = conversation.continueConversation(this, response);
            if (convs != null){
                convoDialog = new Menu(conversant.getName(), convs.getText(), convs.getOptions());
                if (newScreen != null){
                    return newScreen;
                }
            } else {
                return previous;
            }
        }
        return this;
    }

    private void startConversation() {
        conversation = new SampleConversation(conversant);
        ConversationTextAndOptions convoTexts = conversation.startConversation();
        convoDialog = new Menu(conversant.getName(), convoTexts.getText(), convoTexts.getOptions());
    }

    @Override
    public Screen respondToMouseInput(Point translatedPoint) {
        if (convoDialog == null){
            return attemptedSelection ? previous : selectScreen.respondToMouseInput(translatedPoint);
        }
        return null;
    }

    @Override
    public Screen respondToMouseClick(Point translatedPoint, int mouseButton) {
        return attemptedSelection ? previous : selectScreen.respondToMouseClick(translatedPoint, mouseButton);
    }

    public Creature getConversant() {
        return conversant;
    }

    public void setNewScreen(Screen newScreen) {
        this.newScreen = newScreen;
    }
}
