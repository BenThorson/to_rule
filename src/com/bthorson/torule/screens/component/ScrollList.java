package com.bthorson.torule.screens.component;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.screens.ScreenUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 1:37 PM
 */
public class ScrollList {

    private List<String> items;
    private int firstDisplayed;
    private int lastDisplayed;

    private int width;
    private int height;
    private int totalWidth;
    private int totalHeight;
    private final Color foreground;
    private final Color background;

    private int currentChoice;

    public ScrollList(List<String> items, int width, int height, Color foreground, Color background){
        this.items = items;

        this.width = width - 2;
        this.totalWidth = width;
        this.height = height - 2;
        this.totalHeight = height;
        this.foreground = foreground;
        this.background = background;
        init();
    }

    private void init() {
        firstDisplayed = 0;
        if (height < items.size()){
            lastDisplayed = height + 1;
//            for (int i = 0 ; i < height; i++){
//                displayedItems.add(items.get(i));
//            }
        } else {
            lastDisplayed = items.size();
//            displayedItems.addAll(items);
        }

    }

    public void displayOutput(AsciiPanel terminal, int x, int y) {
        Point pos = new Point(x,y);
        int row = 1;
        ScreenUtil.makeRect(terminal, x, y, totalWidth, totalHeight, Color.WHITE, Color.BLACK, true);
        for (int i = firstDisplayed; i < lastDisplayed; i++){

            String toWrite = items.get(i);
            if (toWrite.length() > width){
                toWrite = toWrite.substring(0,width);
            }
            if (i == currentChoice){
                terminal.writePopup(toWrite,
                                    pos.add(new Point(1, row++)),foreground, AsciiPanel.red);
            } else {
                terminal.writePopup(toWrite, pos.add(new Point(1, row++)), foreground, background);
            }
        }
        if (firstDisplayed > 0){
            terminal.writePopup((char)24, pos.add(new Point(totalWidth-1, 1)), foreground, background);
        }
        if (lastDisplayed < items.size()){
            terminal.writePopup((char)25, pos.add(new Point(totalWidth-1, totalHeight-1)), foreground, background);
        }
        if (lastDisplayed - firstDisplayed < items.size()){
            double fraction = (double)currentChoice / (double)items.size();
            int relativePosition = (int)(fraction*(height-1)) + 2;
            Point representativePostion = new Point(totalWidth-1, relativePosition);
            terminal.writePopup((char)32, pos.add(representativePostion), foreground, foreground);

        }
    }

    public int respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN
                || key.getKeyCode() == KeyEvent.VK_NUMPAD2){
            currentChoice = currentChoice < items.size()-1 ? currentChoice + 1 : currentChoice;
            if (currentChoice >= lastDisplayed){
                lastDisplayed++;
                firstDisplayed++;
            }
        } else if (key.getKeyCode() == KeyEvent.VK_UP
                || key.getKeyCode() == KeyEvent.VK_NUMPAD8){
            currentChoice = currentChoice > 0 ? currentChoice - 1 : currentChoice;
            if (currentChoice < firstDisplayed){
                lastDisplayed--;
                firstDisplayed--;
            }
        }

        if (key.getKeyCode() == KeyEvent.VK_ENTER){
            return currentChoice;
        }
        System.out.println(currentChoice);
        return -1;
    }

    public int getCurrentChoice() {
        return currentChoice;
    }
}
