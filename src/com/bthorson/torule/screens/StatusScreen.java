package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.EquipmentSlot;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * User: ben
 * Date: 9/9/12
 * Time: 10:39 PM
 */
public class StatusScreen implements Screen {

    private final int xOffset;
    private final int maxStringLength;

    public StatusScreen(int xOffset) {
        this.xOffset = xOffset;
        maxStringLength = SCREEN_WIDTH - (xOffset + 2);
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        Player player = World.getInstance().getPlayer();
        String blank = ScreenUtil.blankString(SCREEN_WIDTH - xOffset);
        for (int i = 0; i < SCREEN_HEIGHT; i++){
            terminal.write(Tile.WALL_VERT.glyph(), new Point(xOffset, i), Color.WHITE, Color.blue);
        }
        int row = 0;
        terminal.write("Turn " + World.getInstance().getTurnCounter(), new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
        terminal.write(String.format("%d/%d HP", player.getCreature().getHitpoints(),
                                     player.getCreature().getMaxHitpoints()), new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
        terminal.write(String.format("%d gold",  player.getCreature().getGold()), new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
        row++;
        terminal.write("Equipped Items", new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
        for (String key : player.getCreature().getEquipmentSlots().keySet()){
            terminal.write(key + ":", new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
            Item item = player.getCreature().getEquipmentSlots().get(key).getItem();
            String itemName = (item == null) ? "<none>" : item.getName();
            if (itemName.length() > maxStringLength){
                itemName = itemName.substring(0, maxStringLength);
            }

            terminal.write(itemName, new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
            row++;
        }

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
