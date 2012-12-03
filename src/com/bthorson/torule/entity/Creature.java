package com.bthorson.torule.entity;

import com.bthorson.torule.Message;
import com.bthorson.torule.entity.ai.AiControllable;
import com.bthorson.torule.entity.ai.CreatureAI;
import com.bthorson.torule.entity.ai.DeadAi;
import com.bthorson.torule.entity.group.Group;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Line;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
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

    private String name;

    private int gold;

    private Profession profession;
    private Map<String, Building> properties = new HashMap<String, Building>();

    private List<Item> inventory = new ArrayList<Item>();
    private Map<String, EquipmentSlot> equipmentSlots = new HashMap<String, EquipmentSlot>();

    public Creature(CreatureBuilder builder) {
        super(builder.position, builder.glyph, Color.WHITE);
        this.visionRadius = builder.visionRadius;
        this.maxHitpoints = builder.hitPoints;
        this.hitpoints = builder.hitPoints;
        this.heading = Direction.SOUTH;
        this.profession = builder.profession;
        this.gold = builder.gold;
        name = NameGenerator.getInstance().genName();
        this.inventory = builder.inventory;
        this.equipmentSlots = builder.equipmentSlots;
    }

    public static class CreatureBuilder{
        private Point position;
        private int glyph;
        private int visionRadius = 30;
        private int hitPoints;
        private Profession profession;
        private int gold = 0;
        private Map<String, EquipmentSlot> equipmentSlots = new HashMap<String, EquipmentSlot>();
        private List<Item> inventory = new ArrayList<Item>();

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

        public Creature build(){
            return new Creature(this);
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
            super.setGlyph(10);
            getWorld().creatureDead(this);
        }
        if (hitpoints > maxHitpoints){
            hitpoints = maxHitpoints;
        }
    }

    private int determineDmg(Creature other) {
        return new Random().nextInt(10);
    }

    private boolean doesHit(Creature other) {
        if(!(new Random().nextInt(10) > 7)) {
            messages.add(new Message(this, String.format("You missed %s completely.", other.getName())));
            other.getMessages().add(new Message(this, String.format("%s missed you completely.", getName())));
            return false;
        }
        return true;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return inventory;
    }

    public Map<String, EquipmentSlot> getEquipmentSlots() {
        return equipmentSlots;
    }
}
