package com.bthorson.torule.map;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Entity;

import java.awt.Color;
import java.util.Random;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:16 PM
 */
public enum Tile {

    GRASS_1('.', AsciiPanel.green, true, false, 4),
    GRASS_2('\'', AsciiPanel.green, true, false, 4),
    GRASS_3(',', AsciiPanel.green, true, false, 4),
    GRASS_4('`', AsciiPanel.green, true, false, 4),
    TREE((char)5, AsciiPanel.brightGreen, false, true, 1000),
    ROAD('~', Color.gray, true, false, 2),
    BOUNDS('x', AsciiPanel.brightBlack, false, true, 1000);



    private char glyph;
    private Color color;
    private boolean passable;
    private boolean blockSight;
    private int moveCost;

    private Tile(char glyph, Color color, boolean passable, boolean blockSight, int moveCost) {
        this.glyph = glyph;
        this.color = color;
        this.passable = passable;
        this.blockSight = blockSight;
        this.moveCost = moveCost;
    }

    public static Tile getGrass(){
        int rand = new Random().nextInt(4);
        switch (rand) {
            case 0:
                return GRASS_1;
            case 1:
                return GRASS_2;
            case 2:
                return GRASS_3;
            case 3:
                return GRASS_4;
        }
        return GRASS_4;
    }

    public void creatureEnter(Entity entity){}

    public char glyph() {
        return glyph;
    }

    public Color color() {
        return color;
    }

    public boolean passable() {
        return passable;
    }

    public boolean blockSight() {
        return blockSight;
    }

    public int moveCost() {
        return moveCost;
    }
}
