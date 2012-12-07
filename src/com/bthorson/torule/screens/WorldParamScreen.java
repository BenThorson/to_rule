package com.bthorson.torule.screens;

import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;
import com.bthorson.torule.worldgen.WorldGenParams;
import com.bthorson.torule.worldgen.WorldGenerator;

import java.awt.event.KeyEvent;

/**
 * User: Ben Thorson
 * Date: 11/25/12
 * Time: 1:13 PM
 */
public class WorldParamScreen implements Screen {

    private com.bthorson.torule.screens.component.Menu worldSize = new com.bthorson.torule.screens.component.Menu("World Size", (String)null, new String[]{"1000 by 1000"});
    private com.bthorson.torule.screens.component.Menu numCities = new com.bthorson.torule.screens.component.Menu("Number of Towns", (String)null, new String[]{"5", "10", "15"});
    private InputDialog playerName = new InputDialog("Enter Player Name");

    private enum MenuType {WORLD_SIZE, NUM_CITIES, PLAYER_NAME}

    private MenuType activeMenu;

    private WorldGenParams params = new WorldGenParams();

    public WorldParamScreen() {
        activeMenu = MenuType.WORLD_SIZE;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        switch (activeMenu){
            case WORLD_SIZE:
                worldSize.displayOutput(terminal, (SCREEN_WIDTH - worldSize.getWidth()) / 2,
                                                (SCREEN_HEIGHT - worldSize.getHeight()) / 2);
                break;
            case NUM_CITIES:
                numCities.displayOutput(terminal,  (SCREEN_WIDTH - numCities.getWidth()) / 2,
                                                                (SCREEN_HEIGHT - numCities.getHeight()) / 2 );
                break;
            case PLAYER_NAME:
                playerName.displayOutput(terminal, (SCREEN_WIDTH - playerName.getWidth()) / 2,
                                                    (SCREEN_HEIGHT - playerName.getHeight()) / 2 );
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (activeMenu){
            case WORLD_SIZE:
                switch (worldSize.respondToUserInput(key)){
                    case 0:
                        params.setWorldSize(new Point(1000,1000));
                        break;
                    default:
                        return this;
                }
                activeMenu = MenuType.NUM_CITIES;
                return this;
            case NUM_CITIES:
                switch (numCities.respondToUserInput(key)){
                    case 0:
                        params.setNumCities(5);
                        break;
                    case 1:
                        params.setNumCities(10);
                        break;
                    case 2:
                        params.setNumCities(15);
                        break;
                    default:
                        return this;
                }
                activeMenu = MenuType.PLAYER_NAME;
                return this;
            case PLAYER_NAME:
                String s = playerName.respondToUserInput(key);
                if (s != null){
                    params.setPlayerName(s);
                    World.destroy();
                    new WorldGenerator().generateWorld(params);
                    return new PlayScreen();
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
