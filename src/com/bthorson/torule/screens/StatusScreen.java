package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;

import java.awt.Color;
import java.awt.event.KeyEvent;

/**
 * User: ben
 * Date: 9/9/12
 * Time: 10:39 PM
 */
public class StatusScreen implements Screen {

    private World world;
    private final int xOffset;

    public StatusScreen(World world, int xOffset) {
        this.world = world;
        this.xOffset = xOffset;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        String blank = ScreenUtil.blankString(SCREEN_WIDTH - xOffset);
        for (int i = 0; i < SCREEN_HEIGHT; i++){
            terminal.write(Tile.WALL_VERT.glyph(), new Point(xOffset, i), Color.WHITE, Color.blue);
        }
        terminal.write(String.format("%d/%d HP", world.getPlayer().getHitpoints(), world.getPlayer().getMaxHitpoints()), new Point(xOffset + 1, 0), Color.WHITE, Color.BLACK);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Screen respondToMouseInput(Point key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
