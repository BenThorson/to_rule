package com.bthorson.torule.entity.conversation.determiners;

import com.bthorson.torule.entity.Creature;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 3:09 PM
 */
public class ShopHasInventory implements Determiner {

    @Override
    public int determine(Creature creature) {
        if (creature.getOwnedProperties("shop") == null || creature.getOwnedProperties("shop").getInventory().isEmpty()){
            return 1;
        } else {
            return 0;
        }
    }
}
