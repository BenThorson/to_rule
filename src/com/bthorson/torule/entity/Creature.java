package com.bthorson.torule.entity;

import com.bthorson.torule.Message;
import com.bthorson.torule.entity.ai.CreatureAI;
import com.bthorson.torule.entity.ai.DeadAi;
import com.bthorson.torule.geom.Line;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:39 PM
 */
public class Creature extends Entity {

    private CreatureAI ai = null;

    private int visionRadius;

    private Point target;

    private int hitpoints;
    private int maxHitpoints;
    private int hpRegenCount;
    private static final int HP_REGEN_RESET = 10;

    private boolean dead = false;

    private List<Message> messages = new ArrayList<Message>();

    public Creature(World world, int x, int y, int glyph, int visionRadius, int hitpoints) {
        super(world, x, y, glyph, Color.WHITE);
        this.visionRadius = visionRadius;
        this.maxHitpoints = hitpoints;
        this.hitpoints = hitpoints;
    }

    public void move(int dx, int dy){

        if (x + dx < 0 || x + dx > getWorld().width() || y < 0 || y > getWorld().height()){
            return;
        }

        Creature other = getWorld().creature(x + dx, y + dy);
        if (other != null){
            ai.interact(other);
        } else if (getWorld().tile(x + dx, y + dy).passable()){
            x += dx;
            y += dy;
        }
    }

    public void update() {
        hpRegenCount = ++hpRegenCount % HP_REGEN_RESET;
        if (hpRegenCount == 0){
            adjustHitpoint(1);
        }
        ai.execute();
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

    public boolean canSee(int wx, int wy) {

        if ((x-wx)*(x-wx) + (y-wy)*(y-wy) > visionRadius*visionRadius)
            return false;

        for (Point p : new Line(x,y, wx, wy)){
            if (!getWorld().tile(p.x(), p.y()).blockSight() || p.x() == wx && p.y() == wy){
                continue;
            }

            return false;
        }

        return true;
    }

    public void goToTarget(int x, int y) {
        Point point = new Point(x,y,0);
        if (!point.equals(target)){
            target = point;
        }
        int dx = getDelta(this.x, x);
        int dy = getDelta(this.y, y);
        move(dx,dy);

    }

    private int getDelta(int x1, int x2) {
        int dx;
        if (x1 > x2){
            dx = -1;
        } else if (x1 < x2){
            dx = 1;
        } else {
            dx = 0;
        }
        return dx;
    }

    public void attack(Creature other){
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
        }
        if (hitpoints > maxHitpoints){
            hitpoints = maxHitpoints;
        }
    }

    private int determineDmg(Creature other) {
        return new Random().nextInt(10);
    }

    private boolean doesHit(Creature other) {
        return new Random().nextInt(10) > 7;
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
}
