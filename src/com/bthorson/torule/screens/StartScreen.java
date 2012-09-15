package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.awt.event.KeyEvent;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:51 AM
 */
public class StartScreen implements Screen {

    Menu startMenu;

    public StartScreen(){
        startMenu = new Menu("Welcome to my game!", new String[]{"Start new game",
                                                                 "Load saved game",
                                                                 "Exit to OS"}, AsciiPanel.yellow, AsciiPanel.black);
    }


    @Override
    public void displayOutput(AsciiPanel terminal) {
        startMenu.displayOutput(terminal, (SCREEN_WIDTH - startMenu.getWidth()) / 2,
                                (SCREEN_HEIGHT - startMenu.getHeight()) / 2);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (startMenu.respondToUserInput(key) == 0){
            return new PlayScreen(new World());
        }
        return this;
    }

    @Override
    public Screen respondToMouseInput(Point key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
