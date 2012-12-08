package com.bthorson.torule.screens;

import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.screens.component.ItemNameRenderer;
import com.bthorson.torule.screens.component.Menu;
import com.bthorson.torule.town.Building;

import java.awt.event.KeyEvent;

/**
 * User: Ben Thorson
 * Date: 12/3/12
 * Time: 8:27 AM
 */
public class PlayerSellScreen implements Screen {
    private EntityDetailScreen<Item> itemDetailScreen;
    private Building shop;
    private Screen previous;
    private Menu confirmation;
    private Item selectedItem;
    private int sellPrice;
    private Creature player = EntityManager.getInstance().getPlayer();

    public PlayerSellScreen(ConversationScreen previous, Building shop) {
        this.previous = previous;
        this.shop = shop;

        itemDetailScreen = new EntityDetailScreen<Item>(player.getInventory(),
                                                        shop.getOwner().getName() + "s " +
                                                                shop.getBuildingType().prettyName(),
                                                        new ItemNameRenderer());
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
            if (option == 0){
                completeTransaction();
            }
                selectedItem = null;
                confirmation = null;
        } else {
            int itemNum = itemDetailScreen.respondToUserInput(key);
            if (itemNum == -2){
                return previous;
            }
            if (itemNum != -1){
                prepareTransaction(itemNum);
            }
        }

        return this;
    }

    private void completeTransaction() {
        EntityManager.getInstance().getPlayer().sellItem(selectedItem, sellPrice);
        selectedItem.setOwnedBy(shop.getOwner());
        shop.addItem(selectedItem);
        itemDetailScreen.updateList(player.getInventory());
    }

    private void prepareTransaction(int itemNum) {
        selectedItem = player.getInventory().get(itemNum);
        sellPrice = selectedItem.getPrice() / 3;
        confirmation = new Menu("Confirm Sale", "Sell " + selectedItem.getName() + " for " + sellPrice + " gold?",
                                Menu.YES_NO);
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
