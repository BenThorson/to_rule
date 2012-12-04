package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.World;
import com.bthorson.torule.screens.component.Menu;
import com.bthorson.torule.town.Building;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * User: Ben Thorson
 * Date: 12/2/12
 * Time: 9:14 PM
 */
public class PlayerBuyScreen implements Screen {

    private ItemDetailScreen itemDetailScreen;
    private Building shop;
    private Screen previous;
    private Menu confirmation;
    private Item selectedItem;
    private Menu noAfford = new Menu("Cannot afford", (String)null, new String[]{"Okay"}, Color.YELLOW, Color.BLACK, Color.WHITE);
    private boolean showNoAfford = false;

    public PlayerBuyScreen(ConversationScreen previous, Building shop) {
        this.previous = previous;
        this.shop = shop;
        itemDetailScreen = new ItemDetailScreen(shop.getInventory(), shop.getOwner().getName() + "s " + shop.getBuildingType().prettyName());

    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        itemDetailScreen.displayOutput(terminal);
        if (showNoAfford){
            noAfford.displayOutput(terminal, 20,20);
        }
        if (confirmation != null){
            confirmation.displayOutput(terminal, 15, 15);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        if (showNoAfford){
            if (noAfford.respondToUserInput(key) == 0){
                showNoAfford = false;
                return this;
            }
        }

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
        World.getInstance().getPlayer().getCreature().purchaseItem(selectedItem, selectedItem.getPrice());
        shop.removeItem(selectedItem);
    }

    private void prepareTransaction(int itemNum) {
        selectedItem = shop.getInventory().get(itemNum);
        if (World.getInstance().getPlayer().getCreature().getGold() >= selectedItem.getPrice()){
            confirmation = new Menu("Confirm Purchase", "Purchase " + selectedItem.getName() + " for " + selectedItem.getPrice() + " gold?",
                                    new String[]{"Yes", "No"}, Color.YELLOW, Color.BLACK, Color.WHITE);

        } else {
            showNoAfford = true;
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
