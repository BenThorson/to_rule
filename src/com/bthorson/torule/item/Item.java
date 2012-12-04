package com.bthorson.torule.item;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.PhysicalEntity;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 10:39 AM
 */
public class Item extends PhysicalEntity {

    private int price;
    private int weight;
    private String type;
    private String slotType;
    private Map<String, Integer> attributes;
    private boolean isEquipped;
    private Creature ownedBy;

    public Item(){
        super(PointUtil.POINT_ORIGIN, 0x5B, AsciiPanel.brown, "");
    }

    public Item(String name, int price, int weight, String type, String slotType, Map<String, Integer> attributes) {
        super(PointUtil.POINT_ORIGIN, 0x5B, AsciiPanel.brown, name);
        this.price = price;
        this.weight = weight;
        this.type = type;
        this.slotType = slotType;
        this.attributes = attributes;
    }

    public Item(Item item) {
        super(item.position, item.glyph(), item.color(), item.getName());
        this.price = item.price;
        this.weight = item.weight;
        this.type = item.type;
        this.slotType = item.slotType;
        this.attributes = item.attributes;
    }

    public int getPrice() {
        return price;
    }

    public int getWeight() {
        return weight;
    }

    public Map<String, Integer> getAttributes() {
        return attributes;
    }

    public String getType() {
        return type;
    }

    public String getSlotType() {
        return slotType;
    }

    public boolean isEquipped() {
        return isEquipped;
    }

    public void setEquipped(boolean equipped) {
        isEquipped = equipped;
    }

    public Creature getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Creature ownedBy) {
        this.ownedBy = ownedBy;
    }

    public Boolean isOwned(){
        return ownedBy != null;
    }

    public void setPosition(Point position){
        this.position = position;
    }

    @Override
    public List<String> getDetailedInfo() {
        List<String> toRet = new ArrayList<String>();
        toRet.add("Item:   " + getName());
        toRet.add("Price:  " + getPrice());
        toRet.add("Weight: " + getWeight());
        if (attributes != null){
            for (String key : getAttributes().keySet()){
                toRet.add(key + ":  " + attributes.get(key));
            }
        }
        return toRet;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.addProperty("price", price);
        obj.addProperty("weight", weight);
        obj.addProperty("type", type);
        obj.addProperty("slotType", slotType);
        if (attributes != null && !attributes.isEmpty()){
            obj.add("attributes", gson.toJsonTree(attributes));
        }
        obj.addProperty("isEquipped", isEquipped);
        if (ownedBy != null){
            obj.addProperty("ownedBy", ownedBy.id);
        }

        return obj;
    }
}
