package com.bthorson.torule.entity;

import com.bthorson.torule.Message;
import com.bthorson.torule.entity.ai.AggroAI;
import com.bthorson.torule.entity.ai.AiControllable;
import com.bthorson.torule.entity.ai.CreatureAI;
import com.bthorson.torule.entity.ai.DeadAi;
import com.bthorson.torule.entity.ai.FollowAI;
import com.bthorson.torule.entity.ai.PlayerAI;
import com.bthorson.torule.entity.group.Group;
import com.bthorson.torule.exception.CannotEquipException;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Line;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.item.ItemFactory;
import com.bthorson.torule.item.ItemType;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;
import com.bthorson.torule.persist.SerializeUtils;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:39 PM
 */
public class Creature extends PhysicalEntity implements AiControllable {

    private CreatureAI ai = null;

    private int corpseGlyph;
    private long lastUpdate;
    private boolean hasMovedThisUpdate;

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
        super(builder.position, builder.glyph, Color.WHITE, builder.name);
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

    public void setEquipmentSlots(Map<String, EquipmentSlot> equipmentSlots) {
        this.equipmentSlots = equipmentSlots;
    }

    public void setItemlessAttackVals(Map<String, Integer> itemlessAttackVals){
        this.itemlessAttackVals = itemlessAttackVals;
    }

    public void follow(Creature leader) {
        if (this.leader != null && this.leader.equals(leader) && ai instanceof FollowAI){
            //do nothing
        } else {
            this.leader = leader;
            if (ai instanceof FollowAI){
                ai = new FollowAI(this, ai.getPrevious());
            } else {
                ai = new FollowAI(this, ai);
            }
        }
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public CreatureAI getAi() {

        return ai;
    }

    public static class CreatureBuilder{
        private Point position;
        private int glyph;
        private int visionRadius = 16;
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
        private String name;

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

        public CreatureBuilder name(String name) {
            this.name = name;
            return this;
        }
    }

    public void setGroup(Group group){
        this.group = group;
    }


    public boolean move(Point delta){

        if (hasMovedThisUpdate){
            System.out.println(getName() + "tried to move twice in one turn");
            return false;
        }

        if (Math.abs(delta.x()) > 1 || Math.abs(delta.y()) > 1){
            throw new RuntimeException("too large of a move");
        }

        if (Point.BLANK.equals(delta)){
            return false;
        }

        World world = World.getInstance();
        Point moveTo = position().add(delta);

        if (!moveTo.withinRect(world.topLeft(), world.bottomRight())){
            return false;
        }

        if (World.getInstance().tile(moveTo).equals(Tile.DOOR)){
            World.getInstance().openDoor(moveTo);
        }

        heading = Direction.directionOf(delta) != null ? Direction.directionOf(delta) : heading;

        Creature other = world.creature(moveTo);
        if (other != null && !other.equals(this)){
            hasMovedThisUpdate = ai.interact(other);
            return hasMovedThisUpdate;
        } else if (world.tile(moveTo).passable()){
            position = moveTo;
            hasMovedThisUpdate = true;
        }
        return hasMovedThisUpdate;
    }


    public void update(long turnCounter) {

        if (lastUpdate == turnCounter){
            throw new RuntimeException("already attempted an update this turn");
        }

        hpRegenCount = ++hpRegenCount % HP_REGEN_RESET;
        if (hpRegenCount == 0){
            adjustHitpoint(hpRegenRate);
        }
        lastUpdate = turnCounter;
        ai = ai.execute();
        hasMovedThisUpdate = false;
    }

    public long getLastUpdate() {
        return lastUpdate;
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
            if (!World.getInstance().tile(p).blockSight() || p.equals(positionPoint)){
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
            other.processAttack(this, -dmg);
            messages.add(new Message(this, String.format("You hit %s for %d damage", other.getName(), dmg)));
            if (other.dead()){
                messages.add(new Message(this, String.format("You killed %s!", other.getName())));
            }
        }
    }

    private void processAttack(Creature attacker, int damage){
        adjustHitpoint(damage);

        if (ai instanceof PlayerAI){
            return;
        }

        if (!dead){
            Random random = new Random();
            if (!(ai instanceof AggroAI) || random.nextInt(10) > 5){
                setAi(new AggroAI(this, attacker, ai.getPrevious()));
            }
        }
    }

    private void adjustHitpoint(int dHp) {
        if (dHp < 0){
            messages.add(new Message(this, String.format("You were hit for %d damage", -dHp)));
        }
        hitpoints += dHp;
        if (hitpoints <= 0){
            die();
        }
        if (hitpoints > maxHitpoints){
            hitpoints = maxHitpoints;
        }
    }

    private void die() {
        hitpoints = 0;
        dead = true;
        ai = new DeadAi();
        super.setGlyph(corpseGlyph);
        dropLoot();
        EntityManager.getInstance().creatureDead(this);
    }

    private void dropLoot() {
        Random random = new Random();

        if (inventory == null){
            inventory = new ArrayList<Item>();
        }

        if (gold > 0){
            Item goldDrop = ItemFactory.INSTANCE.createGoldDrop(gold);
            goldDrop.setPosition(position);
            gold = 0;
            EntityManager.getInstance().addFreeItem(goldDrop);
        }


        for (Item item: inventory){
            if (random.nextBoolean()){
                item.setPosition(position);
                EntityManager.getInstance().addFreeItem(item);
            } else {
                //destroys it
                EntityManager.getInstance().destroyItem(item);
            }
        }
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
        double rawValue = weaponDmg == 1 ? 1 : new Random().nextInt(weaponDmg - 1) + 1;

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
        List<Creature> inApproxRange = EntityManager.getInstance().getActiveCreaturesInRange(position().subtract(vis), position().add(vis));
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


    public void addItems(List<Item> items) {
        for (Item item : items){
            addItem(item);
        }
    }

    public void addItem(Item selectedItem) {

        if (selectedItem.getName().equals("gold")){
            gold += selectedItem.getAttributes().get("quantity");
            EntityManager.getInstance().destroyItem(selectedItem);
            return;
        }

        selectedItem.setOwnedBy(this);
        if (inventory == null){
            inventory = new ArrayList<Item>();
        }
        inventory.add(selectedItem);
    }

    public int purchaseItem(Item selectedItem, int price) {
        if (gold < price){
            return 0;
        }
        addItem(selectedItem);
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

    @Override
    public List<String> getDetailedInfo() {
        List<String> info = new ArrayList<String>();
        info.add("This is " + getName());
        int armoredWorth = getEquippedItemWorth();
        String armoredModifier = "";
        if (armoredWorth < 100){
            armoredModifier = "barely armed";
        } else if (armoredWorth < 500){
            armoredModifier = "lightly armed";
        } else if (armoredWorth < 1000){
            armoredModifier = "moderately armed";
        } else {
            armoredModifier = "heavily armed";
        }
        info.add(getName() + " looks " + armoredModifier + ",");
        String strengthDesc = "";
        if (strength < 4) {
            strengthDesc = "weak";
        } else if (strength < 7){
            strengthDesc = "of average strength";
        } else {
            strengthDesc = "pretty strong";
        }
        info.add(" looks like he is " + strengthDesc + ",");
        String dexterityDesc = "";
        if (dexterity < 4) {
            dexterityDesc = "clumsy";
        } else if (dexterity < 7){
            dexterityDesc = "of average coordination";
        } else {
            dexterityDesc = "pretty swift";
        }
        info.add(" looks like he is " + dexterityDesc + ",");
        String constitutionDesc = "";
        if (constitution < 4) {
            constitutionDesc = "fragile";
        } else if (constitution < 7){
            constitutionDesc = "of average sturdiness";
        } else {
            constitutionDesc = "pretty robust";
        }
        info.add(" looks like he is " + constitutionDesc + ",");

        String hitpointDesc = "";
        if (maxHitpoints < 20) {
            hitpointDesc = "flimsy";
        } else if (maxHitpoints < 35){
            hitpointDesc = "can take a hit";
        } else {
            hitpointDesc = "is a tank";
        }
        info.add(" looks like he is " + hitpointDesc + ".");
        return info;
    }

    private int getEquippedItemWorth() {
        int worth = 0;
        for (String key : equipmentSlots.keySet()){
            EquipmentSlot slot = equipmentSlots.get(key);
            Item item = slot.getItem();
            if (item != null){
                worth += item.getPrice();
            }
        }
        return worth;
    }

    public Creature closestVisibleHostile() {
        List<Creature> visibleCreatures = getVisibleCreatures();
        List<Creature> hostilable = new ArrayList<Creature>();
        for (Creature other: visibleCreatures){
            if (isHostile(other)){
                hostilable.add(other);
            }
        }
        if (hostilable.size() > 0){
            Collections.sort(hostilable, new NearestComparator(position()));
            return hostilable.get(0);
        }
        return null;
    }


    public boolean isHostile(Creature other) {
        return getFactionEnemies().contains(other.getFaction()) || other.getFactionEnemies().contains(getFactionEnemies());
    }




    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.add("ai", ai.serialize());
        obj.addProperty("corpseGlyph", corpseGlyph);
        if (leader != null){
            obj.addProperty("leader", leader.id);
        }
        if (group != null){
            obj.addProperty("group", group.id);
        }
        obj.add("heading", gson.toJsonTree(heading));
        obj.addProperty("visionRadius", visionRadius);
        obj.add("target", gson.toJsonTree(target));
        obj.addProperty("hitpoints", hitpoints);
        obj.addProperty("lastUpdate", lastUpdate);
        obj.addProperty("maxHitpoints", maxHitpoints);
        obj.addProperty("hpRegenCount", hpRegenCount);
        obj.addProperty("hpRegenRate", hpRegenRate);
        obj.addProperty("dead", dead);
        obj.add("messages", gson.toJsonTree(messages));
        obj.addProperty("faction", faction.id);
        obj.addProperty("gold",gold);
        obj.add("profession", gson.toJsonTree(profession));
        SerializeUtils.serializeRefMap(properties, obj, "properties");

        if (inventory != null && !inventory.isEmpty()){
            SerializeUtils.serializeRefCollection(inventory, obj, "inventory");
        }

        if (!equipmentSlots.isEmpty()){
            JsonObject props = new JsonObject();
            for (Map.Entry<String, EquipmentSlot> set : equipmentSlots.entrySet()){
                props.add(set.getKey(), set.getValue().serialize());
            }
            obj.add("equipmentSlots", props);                
        }

        if (!itemlessAttackVals.isEmpty()){
            obj.add("itemlessAttackVals", gson.toJsonTree(itemlessAttackVals));
        }
        obj.addProperty("innateArmor", innateArmor);
        obj.addProperty("strength", strength);
        obj.addProperty("dexterity", dexterity);
        obj.addProperty("constitution", constitution);
        return obj;
    }

}
