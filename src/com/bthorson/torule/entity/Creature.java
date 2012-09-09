package com.bthorson.torule.entity;

import com.bthorson.torule.entity.ai.CreatureAI;
import com.bthorson.torule.geom.Line;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.awt.Color;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:39 PM
 */
public class Creature extends Entity {

    private CreatureAI ai = null;

    private int visionRadius;

    public Creature(World world, int x, int y, char glyph, Color color, int visionRadius) {
        super(world, x, y, glyph, color);
        this.visionRadius = visionRadius;
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

    public boolean dead() {
        return false; 
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
}
