package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;
import com.bthorson.torule.worldgen.WorldGenParams;

import java.awt.event.KeyEvent;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:51 AM
 */
public class StartScreen implements Screen {

    Menu startMenu;

    public StartScreen(){
        startMenu = new Menu("Welcome to my game!", null, new String[]{"Start new game",
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
        switch (startMenu.respondToUserInput(key)){
            case 0:
                WorldGenParams params = new WorldGenParams();
                params.setWorldSize(new Point(1000,1000));
                params.setNumCities(5);
                World.getInstance().loadWorld(params);
                return new PlayScreen(World.getInstance());
            case 2:
                System.exit(0);
                break;
        }
        return this;
    }

    @Override
    public Screen respondToMouseInput(Point key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
