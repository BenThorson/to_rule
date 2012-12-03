package com.bthorson.torule.entity;

import com.bthorson.torule.Message;
import com.bthorson.torule.entity.ai.AiControllable;
import com.bthorson.torule.entity.ai.CreatureAI;
import com.bthorson.torule.entity.ai.DeadAi;
import com.bthorson.torule.entity.group.Group;
import com.bthorson.torule.exception.CannotEquipException;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Line;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.item.ItemType;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;
import com.bthorson.torule.town.Building;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:39 PM
 */
public class Creature extends Entity implements AiControllable {

    private CreatureAI ai = null;

    private int corpseGlyph;

    private Creature leader;
    private Group group;

    private Direction heading;

    private int visionRadius;

    private Point target;

    private static final int HP_REGEN_RESET = 10;
    private int hitpoints;
    private int maxHitpoints;
    private int hpRegenCount;
    private int hpRegenRate = 1;

    private boolean dead = false;
    private List<Message> messages = new ArrayList<Message>();

    private Faction faction;

    private int gold;

    private Profession profession;
    private Map<String, Building> properties = new HashMap<String, Building>();

    private List<Item> inventory = new ArrayList<Item>();
    private Map<String, EquipmentSlot> equipmentSlots = new HashMap<String, EquipmentSlot>();
    private Map<String, Integer> itemlessAttackVals = new HashMap<String, Integer>();
    private int innateArmor;
    
    private int strength;
    private int dexterity;
    private int constitution;

    public Creature(CreatureBuilder builder) {
        super(builder.position, builder.glyph, Color.WHITE, NameGenerator.getInstance().genName());
        this.visionRadius = builder.visionRadius;
        this.maxHitpoints = builder.hitPoints;
        this.hitpoints = builder.hitPoints;
        this.heading = Direction.SOUTH;
        this.profession = builder.profession;
        this.gold = builder.gold;
        this.inventory = builder.inventory;
        this.equipmentSlots = builder.equipmentSlots;
        this.corpseGlyph = builder.corpseImage;
        this.innateArmor = builder.innateArmor;
        this.strength = builder.strength;
        this.dexterity = builder.dexterity;
        this.constitution = builder.constitution;
        this.itemlessAttackVals = builder.itemlessAttackValues;
        this.templateName = builder.templateName;
        assignOwnershipOfItems();

    }

    private void assignOwnershipOfItems() {
        for (Item item : inventory){
            item.setOwnedBy(this);
        }
    }

    public static class CreatureBuilder{
        private Point position;
        private int glyph;
        private int visionRadius = 20;
        private int hitPoints;
        private Profession profession;
        private int gold = 0;
        private Map<String, EquipmentSlot> equipmentSlots = new HashMap<String, EquipmentSlot>();
        private List<Item> inventory = new ArrayList<Item>();
        private Map<String, Integer> itemlessAttackValues = new HashMap<String, Integer>();
        private int corpseImage;
        public int innateArmor = 1;
        public int strength = 5;
        public int dexterity = 5;
        public int constitution = 5;
        private String templateName;

        public CreatureBuilder position(Point position){
            this.position = position;
            return this;
        }

        public CreatureBuilder glyph(int glyph){
            this.glyph = glyph;
            return this;
        }

        public CreatureBuilder visionRadius(int visionRadius){
            this.visionRadius = visionRadius;
            return this;
        }

        public CreatureBuilder hitPoints(int hitPoints){
            this.hitPoints = hitPoints;
            return this;
        }

        public CreatureBuilder profession(Profession profession){
            this.profession = profession;
            return this;
        }

        public CreatureBuilder gold(int gold){
            this.gold = gold;
            return this;
        }

        public CreatureBuilder equipmentSlots(Map<String, EquipmentSlot> equipmentSlots){
            this.equipmentSlots = equipmentSlots;
            return this;
        }

        public CreatureBuilder inventory(List<Item> inventory){
            this.inventory = inventory;
            return this;
        }

        public CreatureBuilder itemlessAttackValues(Map<String, Integer> itemlessAttackValues){
            this.itemlessAttackValues = itemlessAttackValues;
            return this;
        }

        public CreatureBuilder corpseGlyph(int corpseGlyph){
            this.corpseImage = corpseGlyph;
            return this;
        }
        
        public CreatureBuilder innateArmor(int innateArmor){
            this.innateArmor = innateArmor;
            return this;
        }
        
        public CreatureBuilder strength(int strength){
            this.strength = strength;
            return this;
        }
        
        public CreatureBuilder dexterity(int dexterity){
            this.dexterity = dexterity;
            return this;
        }
        
        public CreatureBuilder constitution(int constitution){
            this.constitution = constitution;
            return this;
        }

        public Creature build(){
            return new Creature(this);
        }


        public CreatureBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }
    }

    public void setGroup(Group group){
        this.group = group;
    }


    public boolean move(Point delta){

        if (Point.BLANK.equals(delta)){
            return false;
        }


        Point moveTo = position().add(delta);

        if (!moveTo.withinRect(getWorld().topLeft(), getWorld().bottomRight())){
            return false;
        }

        if (World.getInstance().tile(moveTo).equals(Tile.DOOR)){
            World.getInstance().openDoor(moveTo);
        }

        heading = Direction.directionOf(delta) != null ? Direction.directionOf(delta) : heading;

        Creature other = getWorld().creature(moveTo);
        if (other != null && !other.equals(this)){
            ai.interact(other);
            return false;
        } else if (getWorld().tile(moveTo).passable()){
            position = moveTo;
            return true;
        }
        return false;
    }


    public void update() {
        hpRegenCount = ++hpRegenCount % HP_REGEN_RESET;
        if (hpRegenCount == 0){
            adjustHitpoint(hpRegenRate);
        }
        ai = ai.execute();
    }

    public boolean dead() {
        return dead;
    }

    public void setAi(CreatureAI ai) {
        this.ai = ai;
    }

    public int visionRadius(){
        return visionRadius;
    }

    public boolean canSee(Point positionPoint) {
        Point product = position().subtract(positionPoint).squared();

        if (product.x() + product.y() > visionRadius*visionRadius)
            return false;

        for (Point p : new Line(position, positionPoint)){
            if (!getWorld().tile(p).blockSight() || p.equals(positionPoint)){
                continue;
            }

            return false;
        }

        return true;
    }

    public Point getTarget(){
        return target;
    }

    public void setTarget(Point target){
        this.target = target;
    }

    public void attack(Creature other){
        heading = Direction.directionOf(other.position().subtract(position()));
        if (doesHit(other)){
            int dmg = determineDmg(other);
            other.adjustHitpoint(-dmg);
            messages.add(new Message(this, String.format("You hit %s for %d damage", other.getName(), dmg)));
            if (other.dead()){
                messages.add(new Message(this, String.format("You killed %s!", other.getName())));
            }
        }
    }

    private void adjustHitpoint(int dHp) {
        if (dHp < 0){
            messages.add(new Message(this, String.format("You were hit for %d damage", -dHp)));
        }
        hitpoints += dHp;
        if (hitpoints <= 0){
            hitpoints = 0;
            dead = true;
            ai = new DeadAi();
            super.setGlyph(corpseGlyph);
            dropLoot();
            EntityManager.getInstance().creatureDead(this);
        }
        if (hitpoints > maxHitpoints){
            hitpoints = maxHitpoints;
        }
    }

    private void dropLoot() {
        List<Item> lootItems = CreatureFactory.INSTANCE.getLootDropsForCreature(templateName);
        for (Item item : lootItems){
            item.setPosition(position);
            EntityManager.getInstance().addFreeItem(item);
        }
    }

    private int determineDmg(Creature other) {
        int weaponDmg = 0;
        for (EquipmentSlot slot : equipmentSlots.values()){
            if (slot.getItemPurpose().equalsIgnoreCase("OFFENSE")){
                if (slot.getItem() != null){
                    weaponDmg += slot.getItem().getAttributes().get("damage");
                }
                else {
                    weaponDmg += itemlessAttackVals.get(slot.getSlotName());
                }
            }
        }
        double rawValue = new Random().nextInt(weaponDmg - 1) + 1;

        double multiplier = (double)strength * .1 + 1;
        return other.mitigateDamage(rawValue * multiplier);
    }

    private int mitigateDamage(double initialDamage) {
        int armorValue = innateArmor;
        for (EquipmentSlot slot : equipmentSlots.values()){
            if (slot.getItemPurpose().equalsIgnoreCase("DEFENSE")){
                if (slot.getItem() != null){
                    armorValue += slot.getItem().getAttributes().get("Armor Class");
                }
            }
        }
        double percentage = (double) armorValue / 50.0;
        percentage = percentage > 1 ? 1 : percentage;
        return (int)Math.round(initialDamage - initialDamage * percentage);



    }

    private boolean doesHit(Creature other) {
        Random random = new Random();
        if(!(random.nextInt(dexterity) < random.nextInt(other.dexterity))) {
            messages.add(new Message(this, String.format("You missed %s completely.", other.getName())));
            other.getMessages().add(new Message(this, String.format("%s missed you completely.", getName())));
            return false;
        }
        if (other.blocks()){
            messages.add(new Message(this, String.format("You hit %s but it was blocked.", other.getName())));
            other.getMessages().add(new Message(this, String.format("You blocked %s's attack.", getName())));
            return false;
        }
        return true;
    }

    private boolean blocks() {
        int blockChance = 0;
        for (EquipmentSlot slot : equipmentSlots.values()){
            if (slot.getItemType().equalsIgnoreCase("SHIELD")){
                if (slot.getItem() != null){
                    blockChance += slot.getItem().getAttributes().get("block chance");
                }
            }
        }
        return new Random().nextInt(10) < blockChance;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public int getMaxHitpoints() {
        return maxHitpoints;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Creature> getVisibleCreatures() {
        Point vis = new Point(visionRadius, visionRadius);
        List<Creature> inApproxRange = EntityManager.getInstance().getCreaturesInRange(position().subtract(vis), position().add(vis));
        List<Creature> visible = new ArrayList<Creature>();
        for (Creature c : inApproxRange){
            if (this != c && canSee(c.position())){
                visible.add(c);
            }
        }
        return visible;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public Set<Faction> getFactionEnemies() {
        return faction.getEnemies();
    }

    public Faction getFaction() {
        return faction;
    }

    public void setLeader(Creature leader) {
        this.leader = leader;
    }

    public Creature getLeader() {
        return leader;
    }

    public void swapPlaces(Creature other) {
        Point temp = this.position;
        this.position = other.position;
        other.position = temp;
    }

    public Direction getHeading() {
        return heading;
    }

    public void doMove(Point point) {
        if(group != null){
            group.move(point);
        } else {
            move(point);
        }
    }

    public boolean isEnemy(Creature creat) {
        return getFaction().getEnemies().contains(creat.getFaction());
    }

    public Group getGroup() {
        return group;
    }

    public Profession getProfession() {
        return profession;
    }

    public void addOwnedProperty(String key, Building building){
        properties.put(key, building);
    }

    public void removeOwnedProperty(String key){
        properties.remove(key);
    }

    public Building getOwnedProperties(String key){
       return properties.get(key);
    }

    public List<Item> getInventory() {
        return new ArrayList<Item>(inventory);
    }

    public Map<String, EquipmentSlot> getEquipmentSlots() {
        return new HashMap<String, EquipmentSlot>(equipmentSlots);
    }

    public void equip(int itemNumber) throws CannotEquipException{
        if (itemNumber >= 0 && itemNumber < inventory.size()){
            Item item = inventory.get(itemNumber);
            equip(item);
        }
    }

    private void equip(Item item) throws CannotEquipException {

        if (item == null){
            return;
        }

        if (equipmentSlots.get(item.getSlotType()) == null){
            System.out.println("cannot equip");
        }

        Item oldItem = equipmentSlots.get(item.getSlotType()).getItem();

        if (oldItem != null){
            oldItem.setEquipped(false);
        }

        equipmentSlots.get(item.getSlotType()).setItem(item);
        item.setEquipped(true);
    }

    public void unEquip(int i) {
        if (i >= 0 && i < inventory.size()){
            unEquip(inventory.get(i));
        }
    }

    private void unEquip(Item item) {
        if (item.isEquipped()){
            equipmentSlots.get(item.getSlotType()).setItem(null);
            item.setEquipped(false);
        }
    }


    public int getGold() {
        return gold;
    }

    public int purchaseItem(Item selectedItem, int price) {
        if (gold < price){
            return 0;
        }
        selectedItem.setOwnedBy(this);
        inventory.add(selectedItem);
        gold -= price;
        return price;
    }

    public void sellItem(Item selectedItem, int sellPrice) {
        if (!inventory.contains(selectedItem)){
            return;
        }
        selectedItem.setOwnedBy(null);
        gold += sellPrice;
        removeItem(selectedItem);
    }


    public void dropItem(Item selectedItem) {
        removeItem(selectedItem);
        EntityManager.getInstance().addFreeItem(selectedItem);
    }

    private void removeItem(Item selectedItem) {
        selectedItem.setOwnedBy(null);
        selectedItem.setPosition(position());
        if(selectedItem.isEquipped()){
            selectedItem.setEquipped(false);
            equipmentSlots.get(selectedItem.getSlotType()).setItem(null);
        }
        inventory.remove(selectedItem);
    }


    public void optimizeEquippedItems() {

        for (String key : equipmentSlots.keySet()){
            EquipmentSlot slot = equipmentSlots.get(key);
            Item toEquip = findBestEquipmentForSlot(slot);
            try {
                equip(toEquip);
            } catch (CannotEquipException e) {
                e.printStackTrace();
            }
        }

    }

    private Item findBestEquipmentForSlot(EquipmentSlot slot) {
        Item toEquip = null;
        for (Item item : inventory){
            if (item.getSlotType().equals(slot.getSlotName())){
                if (toEquip == null ||
                        ItemType.INSTANCE.getItemRelativeWorth(item) > ItemType.INSTANCE.getItemRelativeWorth(toEquip)){
                    toEquip = item;
                }
            }
        }
        return toEquip;
    }

    public int getCorpseGlyph() {
        return corpseGlyph;
    }
}
