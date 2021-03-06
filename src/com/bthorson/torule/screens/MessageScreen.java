package com.bthorson.torule.screens;

import com.bthorson.torule.Message;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * User: ben
 * Date: 9/11/12
 * Time: 9:53 PM
 */
public class MessageScreen implements Screen{

    private final World world;
    private final int yOffset;
    List<Message> messageCache;

    public MessageScreen(World world, int yOffset){
        this.world = world;
        this.yOffset = yOffset;

    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        List<Message> msg = EntityManager.getInstance().getPlayer().getMessages();
        String border = ScreenUtil.solidLine(SCREEN_WIDTH, Tile.WALL_HORIZ.glyph());
        terminal.write(border, new Point(0, yOffset), Color.WHITE, Color.BLACK);

        int size = Math.min(3, msg.size());
        for (int i = 0; i < size; i++){
            terminal.writeText(msg.get(i).getMessage(), new Point(0, yOffset+ 1 +i), Color.WHITE, Color.BLACK);
        }
        msg.clear();
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
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
