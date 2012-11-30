package com.bthorson.torule.town;

import java.util.Arrays;

/**
 * User: Ben Thorson
 * Date: 11/24/12
 * Time: 11:21 AM
 */
public enum BuildingType {

    HOUSE,
    FARM,
    WEAPON_SHOP,
    ARMOR_SHOP,
    INN,
    FOOD_SHOP,
    GENERAL_SHOP,
    KEEP,
    BARRACKS;

    public static boolean isShop(BuildingType bType){
        return Arrays.asList(WEAPON_SHOP, ARMOR_SHOP, INN, FOOD_SHOP, GENERAL_SHOP).contains(bType);
    }

}
