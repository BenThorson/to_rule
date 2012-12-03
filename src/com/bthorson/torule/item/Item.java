package com.bthorson.torule.item;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;

import java.awt.*;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 10:39 AM
 */
public class Item extends Entity {

    private int price;
    private int weight;
    private String type;
    private String slotType;
    private Map<String, Integer> attributes;
    private boolean isEquipped;
    private Creature ownedBy;

    public Item(){
        super(new Point(0,0), 0x5B, AsciiPanel.brown, "");
    }

    public Item(String name, int price, int weight, String type, String slotType, Map<String, Integer> attributes) {
        super(new Point(0,0), 0x5B, AsciiPanel.brown, name);
        this.price = price;
        this.weight = weight;
        this.type = type;
        this.slotType = slotType;
        this.attributes = attributes;
    }

    public Item(Item item) {
        super();
        super.position = item.position;
        super.setGlyph(item.glyph());
        super.color = item.color();
        super.setName(item.getName());
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
}
