package com.bthorson.torule.town;

import java.util.Arrays;

/**
 * User: Ben Thorson
 * Date: 11/24/12
 * Time: 11:21 AM
 */
public enum BuildingType {

    HOUSE("house"),
    FARM("farm"),
    WEAPON_SHOP("weapon shop"),
    ARMOR_SHOP("armor shop"),
    INN("inn"),
    FOOD_SHOP("food shop"),
    GENERAL_SHOP("general shop"),
    KEEP("keep"),
    BARRACKS("barracks");

    

    public static boolean isShop(BuildingType bType){
        return Arrays.asList(WEAPON_SHOP, ARMOR_SHOP, INN, FOOD_SHOP, GENERAL_SHOP).contains(bType);
    }
    
    private String prettyName;
    
    private BuildingType(String prettyName){
        this.prettyName = prettyName;
    }

    public String prettyName() {
        return prettyName;
    }
}
