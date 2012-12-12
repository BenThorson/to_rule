package com.bthorson.torule.screens;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.screens.component.Confirmation;
import com.bthorson.torule.screens.component.ItemNameRenderer;
import com.bthorson.torule.screens.component.ScrollList;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * User: Ben Thorson
 * Date: 12/7/12
 * Time: 10:04 PM
 */
public class ItemTransferScreen implements Screen {

    private Screen previous;
    private Creature transferFrom;
    private Creature transferTo;
    private Item toTransfer;
    private Confirmation confirmation = new Confirmation("Are you sure?");
    private boolean confirmationActive = false;

    private ScrollList<Item> fromInventory;
    private ScrollList<Item> toInventory;

    public ItemTransferScreen(Screen previous, Creature transferFrom, Creature transferTo) {
        this.previous = previous;
        this.transferFrom = transferFrom;
        this.transferTo = transferTo;
        this.fromInventory = new ScrollList<Item>(transferFrom.getInventory(), 20, 15, new ItemNameRenderer());
        this.toInventory = new ScrollList<Item>(transferTo.getInventory(), 20, 15, new ItemNameRenderer());
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        fromInventory.displayOutput(terminal, 0, 0);
        toInventory.displayOutput(terminal,0,16);
        Point start = new Point(22,0);
        int row = 0;
        terminal.writeText(String.format("Item transfer from %s", transferFrom.getName()), start.add(0,row++), Color.WHITE, Color.BLACK);
        terminal.writeText(String.format("to %s", transferTo.getName()), start.add(0, row++), Color.WHITE, Color.BLACK);

        if (confirmationActive){
            confirmation.displayOutput(terminal, 20,10);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (confirmationActive){
            int num = confirmation.respondToUserInput(key);
            switch (num){
                case -2:
                case 1:
                    confirmationActive = false;
                    return this;
                case 0:
                    doTransfer();
                    confirmationActive = false;
                    return this;
            }
        } else {
            int num = fromInventory.respondToUserInput(key);
            switch (num){
                case -2:
                    return previous;
                case -1:
                    return this;
                default:
                    toTransfer = transferFrom.getInventory().get(num);
                    confirmationActive = true;
                    return this;
            }
        }
        return this;
    }

    private void doTransfer() {
        transferFrom.sellItem(toTransfer, 0);
        transferTo.purchaseItem(toTransfer, 0);
        if (!transferTo.equals(EntityManager.getInstance().getPlayer())){
            transferTo.optimizeEquippedItems();
        } else {
            transferFrom.optimizeEquippedItems();
        }
        fromInventory.updateList(transferFrom.getInventory());
        toInventory.updateList(transferTo.getInventory());
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
