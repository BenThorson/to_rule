package com.bthorson.torule.map;

import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:16 PM
 */
public enum Tile {


    GRASS_1('.', AsciiPanel.brightGreen, AsciiPanel.brightGreen, true, false, 4),
    GRASS_2('\'', AsciiPanel.brightGreen, AsciiPanel.brightGreen, true, false, 4),
    GRASS_3(',', AsciiPanel.brightGreen, AsciiPanel.brightGreen, true, false, 4),
    GRASS_4('`', AsciiPanel.brightGreen, AsciiPanel.brightGreen, true, false, 4),
    BRUSH_1((char)0x1D, AsciiPanel.brightGreen, AsciiPanel.brightGreen, true, false, 4),
    BRUSH_2((char)0xD, AsciiPanel.brightGreen, AsciiPanel.brightGreen, true, false, 4),
    BRUSH_3((char)0x12, AsciiPanel.brightGreen, AsciiPanel.brightGreen, true, false, 4),
    BRUSH_4((char)0xFC, AsciiPanel.brightGreen, AsciiPanel.brightGreen, true, false, 4),
    TREE_1((char)0x5, AsciiPanel.brightGreen, AsciiPanel.brightGreen, false, true, 1000),
    TREE_2((char)0x6, AsciiPanel.brightGreen, AsciiPanel.brightGreen, false, true, 1000),
    TREE_3((char)0x17, AsciiPanel.brightGreen, AsciiPanel.brightGreen, false, true, 1000),
    TREE_4((char)0xA0, AsciiPanel.brightGreen, AsciiPanel.brightGreen, false, true, 1000),
    ROAD((char)0x2C, AsciiPanel.brightGreen, new Color(250,250,250), true, false, 2),
    WALL_NW((char)201, Color.gray, Color.gray, false, true, 1000),
    WALL_NE((char)187, Color.gray, Color.gray, false, true, 1000),
    WALL_SE((char)188, Color.gray, Color.gray, false, true, 1000),
    WALL_SW((char)200, Color.gray, Color.gray, false, true, 1000),
    WALL_VERT((char)186, Color.gray, Color.gray, false, true, 1000),
    WALL_HORIZ((char)205, Color.gray, Color.gray, false, true, 1000),
    BOUNDS('x', AsciiPanel.brightBlack, AsciiPanel.brightBlack, false, true, 1000),
    DOOR((char)197, AsciiPanel.brightBlack, new Color(0x994444), true, true, 2),
    OPEN_DOOR((char)197, AsciiPanel.brightBlack, new Color(0x994444), true, false, 2),
    FLOOR((char)0x2B, new Color(0xAAAAAA), new Color(0xAAAAAA), true, false, 2),
    ROCK((char)0x7, AsciiPanel.white, Color.GREEN, false, false, 1000),
    WATER((char)0xF7, Color.BLUE, Color.BLUE, false, false, 1000),
    DIRT_1('.', AsciiPanel.brown, AsciiPanel.brown, true, false, 4),
    DIRT_2('\'', AsciiPanel.brown, AsciiPanel.brown, true, false, 4),
    DIRT_3(',', AsciiPanel.brown, AsciiPanel.brown, true, false, 4),
    DIRT_4('`', AsciiPanel.brown, AsciiPanel.brown, true, false, 4),    
    ROUGH_WALL((char)0xB2, Color.GRAY, Color.GRAY, false, true, 1000),
    DEAD_TREE((char)0xA0, Color.GRAY, AsciiPanel.brown, false, true, 1000),
    SKULL_TOTEM((char)0x87, AsciiPanel.white, AsciiPanel.brown, true, true, 4);



    private char glyph;
    private Color colorFG;
    private Color colorBG;
    private boolean passable;
    private boolean blockSight;
    private int moveCost;

    private Tile(char glyph, Color colorFG, Color colorBG, boolean passable, boolean blockSight, int moveCost) {
        this.glyph = glyph;
        this.colorFG = colorFG;
        this.colorBG = colorBG;
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
    
    public static boolean isGrass(Tile tile){
        return Arrays.asList(GRASS_1, GRASS_2, GRASS_3, GRASS_4).contains(tile);
    }

    public static Tile getDirt(){
        int rand = new Random().nextInt(4);
        switch (rand) {
            case 0:
                return DIRT_1;
            case 1:
                return DIRT_2;
            case 2:
                return DIRT_3;
            case 3:
                return DIRT_4;
        }
        return DIRT_4;
    }

    public static boolean isDirt(Tile tile){
        return Arrays.asList(DIRT_1, DIRT_2, DIRT_3, DIRT_4).contains(tile);
    }
    
    
    public static Tile getBrush(){
        int rand = new Random().nextInt(4);
        switch (rand) {
            case 0:
                return BRUSH_1;
            case 1:
                return BRUSH_2;
            case 2:
                return BRUSH_3;
            case 3:
                return BRUSH_4;
        }
        return BRUSH_4;
    }

    
    public static boolean isBrush(Tile tile){
        return Arrays.asList(BRUSH_1, BRUSH_2, BRUSH_3, BRUSH_4).contains(tile);
    }
    
    public static Tile getTree(){
        int rand = new Random().nextInt(4);
        switch (rand) {
            case 0:
                return TREE_1;
            case 1:
                return TREE_2;
            case 2:
                return TREE_3;
            case 3:
                return TREE_4;
        }
        return TREE_4;
    }
       
    public static boolean isTree(Tile tile){
        return Arrays.asList(TREE_1, TREE_2, TREE_3, TREE_4).contains(tile);
    }
    

    public char glyph() {
        return glyph;
    }

    public Color colorFG() {
        return colorFG;
    }

    public Color colorBG() {
        return colorBG;
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

    public static Tile valueOf(int i) {
        for (Tile tile : values()){
            if (tile.ordinal() == i){
                return tile;
            }
        }
        return null;
    }
}
