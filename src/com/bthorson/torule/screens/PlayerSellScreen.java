package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.World;
import com.bthorson.torule.screens.component.Menu;
import com.bthorson.torule.town.Building;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * User: Ben Thorson
 * Date: 12/3/12
 * Time: 8:27 AM
 */
public class PlayerSellScreen implements Screen {
    private ItemDetailScreen itemDetailScreen;
    private Building shop;
    private Screen previous;
    private Menu confirmation;
    private Item selectedItem;
    private int sellPrice;
    private Creature player = World.getInstance().getPlayer().getCreature();

    public PlayerSellScreen(ConversationScreen previous, Building shop) {
        this.previous = previous;
        this.shop = shop;

        itemDetailScreen = new ItemDetailScreen(player.getInventory(), shop.getOwner().getName() + "s " + shop.getBuildingType().prettyName());

    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        itemDetailScreen.displayOutput(terminal);

        if (confirmation != null){
            confirmation.displayOutput(terminal, 20, 20);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        if (confirmation != null){
            int option = confirmation.respondToUserInput(key);
            if (option == -1){
                return this;
            } else if (option == 0){
                completeTransaction();
            } else if (option == 1){
                selectedItem = null;
                confirmation = null;
                return this;
            }
        } else {
            int itemNum = itemDetailScreen.respondToUserInput(key);
            if (itemNum == -2){
                return previous;
            }
            if (itemNum != -1){
                prepareTransaction(itemNum);
            }
            return this;
        }

        return previous;
    }

    private void completeTransaction() {
        World.getInstance().getPlayer().getCreature().sellItem(selectedItem, sellPrice);
        selectedItem.setOwnedBy(shop.getOwner());
        shop.addItem(selectedItem);
    }

    private void prepareTransaction(int itemNum) {
        selectedItem = player.getInventory().get(itemNum);
        sellPrice = selectedItem.getPrice() / 3;
        confirmation = new com.bthorson.torule.screens.component.Menu("Confirm Sale", "Sell " + selectedItem.getName() + " for " + sellPrice + " gold?",
                                new String[]{"Yes", "No"}, Color.YELLOW, Color.BLACK, Color.WHITE);
    }

    @Override
    public Screen respondToMouseInput(com.bthorson.torule.geom.Point translatedPoint) {
        return this;
    }

    @Override
    public Screen respondToMouseClick(com.bthorson.torule.geom.Point translatedPoint, int mouseButton) {
        return this;
    }

}
