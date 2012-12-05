package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.screens.component.ScrollList;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 10:04 AM
 */
public class ItemDetailScreen {

    ScrollList list;
    List<Item> items;
    String title;

    public ItemDetailScreen(List<Item> items, String title){
        List<String> extractStrings = getItemNames(items);
        this.items = items;
        list = new ScrollList(extractStrings, 20, Screen.SCREEN_HEIGHT-1, Color.YELLOW, Color.BLACK, Color.WHITE);
        this.title = title;
    }

    private List<String> getItemNames(List<Item> inventory) {
        List<String> list = new ArrayList<String>();
        for (Item item: inventory){
            String prefix = item.isEquipped() ? "*" : "";
            list.add(prefix + item.getName());
        }
        return list;
    }

    public void displayOutput(AsciiPanel terminal) {

        list.displayOutput(terminal, 0, 0);
        Point detailsStart = new Point(22, 2);
        terminal.writePopupText(title, new Point(22, 0), Color.WHITE, Color.BLACK);
        int detailsRow = 1;
        Item item = items.get(list.getCurrentChoice());
        List<String> details = item.getDetailedInfo();
        for (String detail : details){
            terminal.writePopupText(detail, detailsStart.add(new Point(0, detailsRow++)), Color.WHITE, Color.BLACK);
        }
    }

    public int respondToUserInput(KeyEvent key) {
        return list.respondToUserInput(key);
    }

    public void updateList(List<Item> items) {
        this.items = items;
        list.updateList(getItemNames(items));
    }
}
