package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.persist.SaveAction;
import com.bthorson.torule.screens.component.Menu;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * User: Ben Thorson
 * Date: 12/4/12
 * Time: 10:16 AM
 */
public class SaveScreen implements Screen {

    private Screen previous;
    private Menu confirm = new Menu("Quit and Save?", (String)null, new String[]{"Yes", "No"}, Color.YELLOW, Color.BLACK, Color.WHITE);
    private InputDialog saveName;
    private StartScreen startScreen;

    @Override
    public void displayOutput(AsciiPanel terminal) {
        confirm.displayOutput(terminal, 10,10);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (saveName != null){
            String name = saveName.respondToUserInput(key);
            if (name != null){
                new SaveAction().save(name);
            }
        }
        return this;
    }

    @Override
    public Screen respondToMouseInput(Point translatedPoint) {
        return this;
    }

    @Override
    public Screen respondToMouseClick(Point translatedPoint, int mouseButton) {
        return this;
    }
}
