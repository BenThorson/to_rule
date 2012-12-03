package com.bthorson.torule.town;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.item.ItemFactory;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 11:30 AM
 */
public enum BuildingInventoryFactory {

    INSTANCE;

    private EnumMap<BuildingType, JsonArray> catalog = new EnumMap<BuildingType, JsonArray>(BuildingType.class);

    private BuildingInventoryFactory(){
        load();
    }

    private void load() {
        try {
            String armor = FileUtils.readFileToString(new File("resources/merchant/armorMerchant.json"));
            JsonObject jo = new JsonParser().parse(armor).getAsJsonObject();
            JsonArray armorItems = jo.get("inventory").getAsJsonArray();
            catalog.put(BuildingType.ARMOR_SHOP, armorItems);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void createItemsForShop(Building building, WealthLevel wealthLevel){
        JsonArray array = catalog.get(building.getBuildingType());
        if (array == null){
            return;
        }
        for (JsonElement element : array){
            JsonObject obj = element.getAsJsonObject();
            if(obj.get("wealthLevel").getAsString().equalsIgnoreCase(wealthLevel.toString())){
                JsonArray itemsToGen = obj.get("inventory").getAsJsonArray();
                for (JsonElement item : itemsToGen){
                    JsonObject toGen = item.getAsJsonObject();
                    for(int i = 0; i < toGen.get("quantity").getAsInt(); i++){
                        building.addItem(ItemFactory.INSTANCE.createItemOfId(toGen.get("refId").getAsString()));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Building building = new Building(PointUtil.POINT_ORIGIN, new Point(10,10), BuildingType.ARMOR_SHOP);
        BuildingInventoryFactory.INSTANCE.createItemsForShop(building, WealthLevel.POOR);
    }
}
