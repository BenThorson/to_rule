package com.bthorson.torule.screens;

import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.screens.component.Menu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/3/12
 * Time: 4:46 PM
 */
public class LootScreen implements Screen {

    private Menu itemsToLoot;
    private Screen previous;
    List<Item> items;

    public LootScreen(Screen previous, List<Item> items){
        this.previous = previous;
        this.items = items;
        itemsToLoot = new Menu("Pickup Items", (String)null, makeChoiceArray(items));
    }

    private String[] makeChoiceArray(List<Item> items) {
        List<String> choices = new ArrayList<String>();
        for (Item item : items){
            choices.add(item.getName());
        }
        choices.add("All");
        return choices.toArray(new String[choices.size()]);
    }


    @Override
    public void displayOutput(AsciiPanel terminal) {
        previous.displayOutput(terminal);
        itemsToLoot.displayOutput(terminal, 15, 15);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        int val = itemsToLoot.respondToUserInput(key);
        switch (val){
            case -2:
                return previous;
            case -1:
                return this;
            default:
                if (val == items.size()){
                    EntityManager.getInstance().getPlayer().addItems(items);
                    for (Item item: items){
                        EntityManager.getInstance().removeFreeItem(item);
                    }
                } else {
                    EntityManager.getInstance().getPlayer().addItem(items.get(val));
                    EntityManager.getInstance().removeFreeItem(items.get(val));
                }
                return previous;
        }
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
