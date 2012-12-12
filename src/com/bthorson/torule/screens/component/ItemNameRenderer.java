package com.bthorson.torule.screens.component;

import com.bthorson.torule.item.Item;
import com.bthorson.torule.screens.EntityRenderer;

/**
 * User: Ben Thorson
 * Date: 12/7/12
 * Time: 2:59 PM
 */
public class ItemNameRenderer implements EntityRenderer<Item> {

    @Override
    public String render(Item input) {
        return (input.isEquipped() ? "*" : "") + input.getName();
    }
}
