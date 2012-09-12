package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.Message;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.security.PublicKey;
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
        List<Message> msg = world.getPlayer().getMessages();
        String blankLine = ScreenUtil.blankString(SCREEN_WIDTH);
        String border = ScreenUtil.solidLine(SCREEN_WIDTH, Tile.WALL_HORIZ.glyph());
        terminal.write(border, 0, yOffset, Color.WHITE);
        for (int i = yOffset + 1; i < SCREEN_HEIGHT ; i++){
            terminal.write(blankLine, 0, i, Color.WHITE);
        }
        for (int i = 0; i < msg.size(); i++){
            terminal.write(msg.get(i).getMessage(), 0, yOffset+ 1 +i);
        }
        msg.clear();
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}