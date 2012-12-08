package com.bthorson.torule.screens;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.StringUtil;
import com.bthorson.torule.exception.CannotEquipException;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.screens.component.ItemNameRenderer;
import com.bthorson.torule.screens.component.Menu;

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
    private static final String DROP = "Drop item";
    private Screen previous;
    private EntityDetailScreen<Item> itemDetailScreen;
    private Item selectedItem;
    private List<Item> items;
    private Creature creature;
    private Menu itemActionMenu;
    private List<String> choices;

    public InventoryManagementScreen(Screen previous, Creature creature) {
        itemDetailScreen = new EntityDetailScreen<Item>(creature.getInventory(), String.format("%s's Inventory Screen", creature.getName()), new ItemNameRenderer());
        items = creature.getInventory();
        this.creature = creature;
        this.previous = previous;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        itemDetailScreen.displayOutput(terminal);
        if (itemActionMenu != null) {
            itemActionMenu.displayOutput(terminal, 22,10);
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
            }

        } else {
            int num = itemDetailScreen.respondToUserInput(key);

            if (num == -2) {
                return previous;
            } else if (num != -1) {
                selectedItem = creature.getInventory().get(num);
                itemActionMenu = new Menu(selectedItem.getName(), "What to do with this?", getChoices(selectedItem));
            }
        }
        return this;
    }

    private void doAction(String value) {
        if (EQUIP.equalsIgnoreCase(value)){
            try {
                creature.equip(creature.getInventory().indexOf(selectedItem));
            } catch (CannotEquipException e) {
                //todo make dialog to disallow equipping of item
            }
        } else if (UNEQUIP.equalsIgnoreCase(value)){
            creature.unEquip(creature.getInventory().indexOf(selectedItem));
        } else if (CONSUME.equalsIgnoreCase(value)){
            //todo make consume action
        } else if (DROP.equalsIgnoreCase(value)){
            creature.dropItem(selectedItem);
        }
        itemDetailScreen.updateList(creature.getInventory());
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
