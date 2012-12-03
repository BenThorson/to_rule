package com.bthorson.torule.entity;

import com.bthorson.torule.item.Item;

/**
 * User: Ben Thorson
 * Date: 12/2/12
 * Time: 11:34 AM
 */
public class EquipmentSlot {

    private String slotName;
    private String itemType;
    private String itemPurpose;
    private Item item;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemPurpose() {
        return itemPurpose;
    }

    public void setItemPurpose(String itemPurpose) {
        this.itemPurpose = itemPurpose;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }
}
