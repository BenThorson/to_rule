package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
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

    private Screen previous;
    ScrollList list;
    List<Item> items;
    String title;

    public ItemDetailScreen(Screen previous, List<Item> items, String title){
        this.previous = previous;
        List<String> extractStrings = getItemNames(items);
        this.items = items;
        list = new ScrollList(extractStrings, 15, Screen.SCREEN_HEIGHT-1, Color.YELLOW, Color.BLACK);
        this.title = title;
    }

    private List<String> getItemNames(List<Item> inventory) {
        List<String> list = new ArrayList<String>();
        for (Item item: inventory){
            list.add(item.getName());
        }
        return list;
    }

    public void displayOutput(AsciiPanel terminal) {

        list.displayOutput(terminal, 0, 0);
        Point detailsStart = new Point(17, 2);
        terminal.writePopup(title, new Point(20, 0), Color.WHITE, Color.BLACK);
        int detailsRow = 1;
        Item item = items.get(list.getCurrentChoice());

        terminal.writePopup("Item:    " + item.getName(), detailsStart.add(new Point(0,detailsRow++)), Color.WHITE, Color.BLACK);
        terminal.writePopup("Price:   " + item.getPrice(), detailsStart.add(new Point(0,detailsRow++)), Color.WHITE, Color.BLACK);
        terminal.writePopup("Weight:  " + item.getWeight(), detailsStart.add(new Point(0, detailsRow++)), Color.WHITE, Color.BLACK);
        for (String key : item.getAttributes().keySet()){
            terminal.writePopup(key + ":  " + item.getAttributes().get(key), detailsStart.add(new Point(0, detailsRow++)), Color.WHITE, Color.BLACK);
        }

    }

    public int respondToUserInput(KeyEvent key) {
        return list.respondToUserInput(key);
    }
}
