package com.bthorson.torule.entity;

import com.bthorson.torule.entity.ai.PlayerAI;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.item.ItemFactory;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.ExploredMap;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:07 PM
 */
public enum CreatureFactory {

    INSTANCE;

    private Map<String, JsonObject> equipmentSlotMap = new HashMap<String, JsonObject>();
    private Map<String, JsonObject> creatureEquipmentFormat = new HashMap<String, JsonObject>();
    private Map<String, JsonObject> creatureTemplate = new HashMap<String, JsonObject>();

    private CreatureFactory(){
        load();
    }

    private void load() {
        try {
            String equipSlots = FileUtils.readFileToString(new File("resources/creature/equipmentSlots.json"));
            JsonObject jo = new JsonParser().parse(equipSlots).getAsJsonObject();
            JsonArray items = jo.get("slotTypes").getAsJsonArray();
            for (JsonElement item : items){
                equipmentSlotMap.put(item.getAsJsonObject().get("slotName").getAsString(), item.getAsJsonObject());
            }

            String creatureSlots = FileUtils.readFileToString(new File("resources/creature/creatureEquipmentFormat.json"));
            jo = new JsonParser().parse(creatureSlots).getAsJsonObject();
            items = jo.get("creatures").getAsJsonArray();
            for (JsonElement item : items){
                creatureEquipmentFormat.put(item.getAsJsonObject().get("creature").getAsString(), item.getAsJsonObject());
            }

            String creatureTempl = FileUtils.readFileToString(new File("resources/creature/creatureTemplate.json"));
            jo = new JsonParser().parse(creatureTempl).getAsJsonObject();
            items = jo.get("templates").getAsJsonArray();
            for (JsonElement item : items){
                creatureTemplate.put(item.getAsJsonObject().get("templateName").getAsString(), item.getAsJsonObject());
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Creature createCreature(String templateName, Point pos){
        Gson gson = new Gson();
        JsonObject template = creatureTemplate.get(templateName);
        JsonObject format = creatureEquipmentFormat.get(template.get("creatureType").getAsString());
        Map<String, EquipmentSlot> map = new HashMap<String, EquipmentSlot>();
        for (JsonElement slotKey : format.getAsJsonArray("slots")){
            String key = slotKey.getAsString();
            EquipmentSlot slot = gson.fromJson(equipmentSlotMap.get(key), EquipmentSlot.class);
            map.put(key, slot);
        }

        List<Item> equipment = new ArrayList<Item>();
        for (JsonElement equips : template.get("startingEquipment").getAsJsonArray()){
            String itemId = equips.getAsString();
            equipment.add(ItemFactory.INSTANCE.createItemOfId(itemId));
        }

        Map<String, Integer> itemlessAttack = new HashMap<String, Integer>();
        JsonElement element = template.get("itemlessAttack");
        if (element != null){
            JsonArray array = element.getAsJsonArray();
            for (JsonElement e2 : array){
                JsonObject obj = e2.getAsJsonObject();
                itemlessAttack.put(obj.get("slotType").getAsString(), obj.get("value").getAsInt());
            }
        }

        String name = NameGenerator.getInstance().genName();
        if (template.has("name")){
            name = template.get("name").getAsString();
        }

        Creature creature = new Creature.CreatureBuilder()
                .position(pos)
                .name(name)
                .templateName(templateName)
                .glyph(CreatureImage.valueOf(template.get("image").getAsString()).num())
                .hitPoints(template.get("hitPoints").getAsInt())
                .gold(template.get("startingGold").getAsInt())
                .profession(Profession.valueOf(template.get("profession").getAsString()))
                .equipmentSlots(map)
                .inventory(equipment)
                .itemlessAttackValues(itemlessAttack)
                .corpseGlyph(CreatureImage.valueOf(template.get("corpseImage").getAsString()).num())
                .innateArmor(template.get("innateArmor").getAsInt())
                .build();

        EntityManager.getInstance().addCreature(creature);
        creature.optimizeEquippedItems();
        return creature;

    }

    public List<Item> getLootDropsForCreature(String templateName){
        JsonObject template = creatureTemplate.get(templateName);
        JsonArray loots = template.get("loot").getAsJsonArray();
        List<Item> toReturn = new ArrayList<Item>();
        Random random = new Random();
        for (JsonElement element : loots){
            JsonObject objItem = element.getAsJsonObject();
            if (random.nextInt(100) < objItem.get("chance").getAsInt()){
                int quantity = objItem.get("quantity").getAsInt();
                String itemId = objItem.get("item").getAsString();
                int toAdd = 1 == quantity ? 1 : 1 + random.nextInt(quantity - 1);
                for (int i = 0; i < toAdd; i++){
                    toReturn.add(ItemFactory.INSTANCE.createItemOfId(itemId));
                }
            }
        }
        return toReturn;

    }

    public static void main(String[] args) {
        Creature creature = CreatureFactory.INSTANCE.createCreature("villager", new Point(3,3));
        Creature creature2 = CreatureFactory.INSTANCE.createCreature("player", new Point(3,3));
        System.out.println("hi");
    }
}
