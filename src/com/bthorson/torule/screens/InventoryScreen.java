package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.screens.component.ScrollList;
import com.bthorson.torule.town.Building;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 10:04 AM
 */
public class InventoryScreen implements Screen {

    private Screen previous;
    ScrollList list;
    List<Item> items;

    public InventoryScreen(Screen previous, Creature creature){

    }

    public InventoryScreen(Screen previous, Building building){
        this.previous = previous;
        List<String> extractStrings = getItemNames(building.getInventory());
        items = building.getInventory();
        list = new ScrollList(extractStrings, 15, SCREEN_HEIGHT-1, Color.YELLOW, Color.BLACK);
    }

    private List<String> getItemNames(List<Item> inventory) {
        List<String> list = new ArrayList<String>();
        for (Item item: inventory){
            list.add(item.getName());
        }
        return list;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        list.displayOutput(terminal, 0, 0);
        Point detailsStart = new Point(25, 5);
        int detailsRow = 1;
        Item item = items.get(list.getCurrentChoice());
        terminal.writePopup("Item:  " + item.getName(), detailsStart.add(new Point(0,detailsRow++)), Color.WHITE, Color.BLACK);
        terminal.writePopup("Price:  " + item.getPrice(), detailsStart.add(new Point(0,detailsRow++)), Color.WHITE, Color.BLACK);
        terminal.writePopup("Weight:  " + item.getWeight(), detailsStart.add(new Point(0, detailsRow++)), Color.WHITE, Color.BLACK);
        for (String key : item.getAttributes().keySet()){
            terminal.writePopup(key + ":  " + item.getAttributes().get(key), detailsStart.add(new Point(0, detailsRow++)), Color.WHITE, Color.BLACK);
        }

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        int item = list.respondToUserInput(key);
        if (item == -1){
            return this;
        }
        return previous;
    }

    @Override
    public Screen respondToMouseInput(Point key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
