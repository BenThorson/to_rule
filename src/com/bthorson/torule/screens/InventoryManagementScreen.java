package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.StringUtil;
import com.bthorson.torule.exception.CannotEquipException;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.screens.component.Menu;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/2/12
 * Time: 11:28 PM
 */
public class InventoryManagementScreen implements Screen {
    private static final String CONSUMABLE = "consumable";
    private static final String CONSUME = "Consume item";
    private static final String EQUIP = "Equip item";
    private static final String UNEQUIP = "Unequip item";
    private static final String GIVE = "Give to follower";
    private static final String DROP = "Drop item";
    private Screen previous;
    private ItemDetailScreen itemDetailScreen;
    private Item selectedItem;
    private Player player = World.getInstance().getPlayer();
    private Menu itemActionMenu;
    private List<String> choices;

    public InventoryManagementScreen(Screen previous) {
        itemDetailScreen = new ItemDetailScreen(player.getCreature().getInventory(), "Inventory Screen");
        this.previous = previous;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        itemDetailScreen.displayOutput(terminal);
        if (itemActionMenu != null) {
            itemActionMenu.displayOutput(terminal, 10,10);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        if (itemActionMenu != null) {
            int num = itemActionMenu.respondToUserInput(key);
            switch (num){
                case -2:
                    itemActionMenu = null;
                    choices = null;
                case -1:
                    return this;
                default:
                    doAction(choices.get(num));
                    itemActionMenu = null;
                    itemDetailScreen = new ItemDetailScreen(player.getCreature().getInventory(), "Inventory Screen");
            }

        } else {
            int num = itemDetailScreen.respondToUserInput(key);

            if (num == -2) {
                return previous;
            } else if (num != -1) {
                selectedItem = player.getCreature().getInventory().get(num);
                itemActionMenu = new Menu(selectedItem.getName(), "What to do with this?", getChoices(selectedItem), Color.YELLOW, Color.BLACK, Color.WHITE);
            }
        }
        return this;
    }

    private void doAction(String value) {
        if (EQUIP.equalsIgnoreCase(value)){
            try {
                player.getCreature().equip(player.getCreature().getInventory().indexOf(selectedItem));
            } catch (CannotEquipException e) {
                //todo make dialog to disallow equipping of item
            }
        } else if (UNEQUIP.equalsIgnoreCase(value)){
            player.getCreature().unEquip(player.getCreature().getInventory().indexOf(selectedItem));
        } else if (CONSUME.equalsIgnoreCase(value)){
            //todo make consume action
        } else if (GIVE.equalsIgnoreCase(value)){
            //todo make give action
        } else if (DROP.equalsIgnoreCase(value)){
            player.getCreature().removeItem(selectedItem);
        }
    }

    private String[] getChoices(Item selectedItem) {
        List<String> choices = new ArrayList<String>();
        if (selectedItem.getAttributes().containsKey(CONSUMABLE)){
            choices.add(CONSUME);
        }
        if (!StringUtil.isNullOrEmpty(selectedItem.getSlotType()) && !selectedItem.isEquipped()){
            choices.add(EQUIP);
        }
        if (selectedItem.isEquipped()){
            choices.add(UNEQUIP);
        }
        if (!player.getFollowers().isEmpty()){
            choices.add(GIVE);
        }
        choices.add(DROP);
        this.choices = choices;
        return choices.toArray(new String[choices.size()]);
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
