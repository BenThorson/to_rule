package com.bthorson.torule.entity;

import com.bthorson.torule.map.World;

import java.awt.*;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:39 PM
 */
public class Creature extends Entity {

    private CreatureAI ai = null;

    public void setAi(CreatureAI ai) {
        this.ai = ai;
    }

    public Creature(World world, int x, int y, char glyph, Color color) {
        super(world, x, y, glyph, color);
    }

    public void move(int dx, int dy){

        if (x + dx < 0 || x + dx > getWorld().width() || y < 0 || y > getWorld().height()){
            return;
        }

        Creature other = getWorld().creature(x + dx, y + dy);
        if (other != null){

        } else if (getWorld().tile(x + dx, y + dy).passable()){
            x += dx;
            y += dy;
        }
    }

    public void update() {
        ai.execute();
    }
}
