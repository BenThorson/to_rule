package com.bthorson.torule.screens.component;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
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
    private Color textColor;
    private int firstDisplayed;
    private int lastDisplayed;

    private int width;
    private int height;
    private int totalWidth;
    private int totalHeight;
    private final Color foreground;
    private final Color background;

    private int currentChoice;

    public ScrollList(List<String> items, int width, int height, Color foreground, Color background, Color textColor){
        this.items = items;
        this.textColor = textColor;

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
        } else {
            lastDisplayed = items.size();
        }

    }

    public void displayOutput(AsciiPanel terminal, int x, int y) {
        Point pos = new Point(x,y);
        int row = 1;
        ScreenUtil.makeRect(terminal, x, y, totalWidth, totalHeight, foreground, background, true);
        for (int i = firstDisplayed; i < lastDisplayed; i++){

            String toWrite = items.get(i);
            if (toWrite.length() > width){
                toWrite = toWrite.substring(0,width);
            }
            if (i == currentChoice){
                terminal.writePopupText(toWrite,
                                        pos.add(new Point(1, row++)), textColor, AsciiPanel.red);
            } else {
                terminal.writePopupText(toWrite, pos.add(new Point(1, row++)), textColor, background);
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

        switch (key.getKeyCode()){
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_NUMPAD2:
                currentChoice = currentChoice < items.size()-1 ? currentChoice + 1 : currentChoice;
                if (currentChoice >= lastDisplayed){
                    lastDisplayed++;
                    firstDisplayed++;
                }
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_NUMPAD8:
                currentChoice = currentChoice > 0 ? currentChoice - 1 : currentChoice;
                if (currentChoice < firstDisplayed){
                    lastDisplayed--;
                    firstDisplayed--;
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                int temp = height;
                int actualMoved = temp + currentChoice< items.size()-1 ? temp : (items.size() -1) - currentChoice;
                currentChoice += actualMoved;
                int scrollAmount = lastDisplayed + actualMoved < items.size() ? actualMoved : items.size() - lastDisplayed;
                firstDisplayed += scrollAmount;
                lastDisplayed += scrollAmount;
                break;
            case KeyEvent.VK_PAGE_UP:
                int t = height;
                int actMoved = currentChoice - t > 0 ? t : currentChoice;
                currentChoice -= actMoved;
                int sAmount = firstDisplayed - actMoved > 0 ? actMoved : firstDisplayed;
                firstDisplayed -= sAmount;
                lastDisplayed -= sAmount;
                break;

            case KeyEvent.VK_ESCAPE:
                return -2;
            case KeyEvent.VK_ENTER:
                return currentChoice;
        }
        return -1;
    }

    public int getCurrentChoice() {
        return currentChoice;
    }

    public void updateList(List<String> items) {
        this.items = items;
        if (currentChoice >= items.size()){
            currentChoice--;
        }
        if (lastDisplayed >= items.size()){
            lastDisplayed--;
        }
    }
}
