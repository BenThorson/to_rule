package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.awt.event.KeyEvent;

/**
 * User: ben
 * Date: 9/12/12
 * Time: 1:51 PM
 */
public class DeadScreen implements Screen {
    private Screen screen;

    public DeadScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        screen.displayOutput(terminal);
        terminal.writeCenter("YOU HAVE DIED", SCREEN_HEIGHT / 2);
        terminal.writeCenter("Press Enter to restart", SCREEN_HEIGHT / 2 + 1);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER){
            World.destroy();
            return new StartScreen();
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
