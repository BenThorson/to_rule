package com.bthorson.torule.screens;

import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.screens.component.Menu;

import java.awt.event.KeyEvent;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:51 AM
 */
public class StartScreen implements Screen {

    Menu startMenu;

    public StartScreen(){
        startMenu = new Menu("Welcome to my game!", (String)null, new String[]{"Start new game",
                                                                 "Load saved game",
                                                                 "Exit to OS"});
    }


    @Override
    public void displayOutput(AsciiPanel terminal) {
        startMenu.displayOutput(terminal, (SCREEN_WIDTH - startMenu.getWidth()) / 2,
                                (SCREEN_HEIGHT - startMenu.getHeight()) / 2);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (startMenu.respondToUserInput(key)){
            case 0:
                return new WorldParamScreen();
            case 1:
                return new LoadScreen();
            case 2:
                System.exit(0);
                break;
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
