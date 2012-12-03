package com.bthorson.torule.entity;

import com.bthorson.torule.item.Item;
import com.bthorson.torule.item.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/2/12
 * Time: 3:39 PM
 */
public class CreatureUtils {

    private CreatureUtils(){}

    public static void optimizeEquipedItems(Creature creature) {
        Map<String, EquipmentSlot> slots = creature.getEquipmentSlots();
        List<Item> items = creature.getInventory();

        for (String key : slots.keySet()){
            EquipmentSlot slot = slots.get(key);
            Item toEquip = findBestEquipmentForSlot(slot, items);
            slots.get(key).setItem(toEquip);
        }

    }

    private static Item findBestEquipmentForSlot(EquipmentSlot slot, List<Item> items) {
        Item toEquip = null;
        for (Item item : items){
            if (item.getSlotType().equals(slot.getName())){
                if (toEquip == null ||
                        ItemType.INSTANCE.getItemRelativeWorth(item) > ItemType.INSTANCE.getItemRelativeWorth(toEquip)){
                    toEquip = item;
                }
            }
        }
        return toEquip;
    }
}
