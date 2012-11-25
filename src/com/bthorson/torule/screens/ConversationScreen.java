package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

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

    public ConversationScreen(PlayScreen playScreen, Point position) {
        this.previous = playScreen;
        selectScreen = new SelectScreen(playScreen, position, this);
    }

    @Override
    public void positionSelected(Point point) {
        attemptedSelection = true;
        Creature conversant = World.getInstance().creature(point.add(previous.getOffset()));
        if (conversant != null){
            convoDialog = new Menu(conversant.getName(), "Hello", new String[]{"Hi"}, AsciiPanel.yellow, AsciiPanel.black );
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
        if (convoDialog == null){
            return attemptedSelection ? previous : selectScreen;
        } else {
            switch (convoDialog.respondToUserInput(key)){
                case 0:
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
