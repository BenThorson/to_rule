package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.EntityManager;
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
    private int height;
    private final int maxStringLength;

    public StatusScreen(int xOffset, int height) {
        this.xOffset = xOffset;
        this.height = height;
        maxStringLength = SCREEN_WIDTH - (xOffset + 2);
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        Player player = EntityManager.getInstance().getPlayer();
        String blank = ScreenUtil.blankString(SCREEN_WIDTH - xOffset);
        for (int i = 0; i < height; i++){
            terminal.write(Tile.WALL_VERT.glyph(), new Point(xOffset, i), Color.WHITE, Color.blue);
        }
        int row = 0;
        terminal.writeText("Turn " + World.getInstance().getTurnCounter(), new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
        terminal.writeText(String.format("%d/%d HP", player.getHitpoints(),
                                         player.getMaxHitpoints()), new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
        terminal.writeText(String.format("%d gold", player.getGold()), new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
        row++;
        terminal.writeText("Equipped Items", new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
        for (String key : player.getEquipmentSlots().keySet()){
            terminal.writeText(key + ":", new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
            Item item = player.getEquipmentSlots().get(key).getItem();
            String itemName = (item == null) ? "<none>" : item.getName();
            if (itemName.length() > maxStringLength){
                itemName = itemName.substring(0, maxStringLength);
            }

            terminal.writeText(itemName, new Point(xOffset + 1, row++), Color.WHITE, Color.BLACK);
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
