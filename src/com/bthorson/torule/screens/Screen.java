package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:47 AM
 */
public interface Screen {

    public static final int SCREEN_HEIGHT = 35;
    public static final int SCREEN_WIDTH = 60;

    public void displayOutput(AsciiPanel terminal);
    public Screen respondToUserInput(KeyEvent key);
    public Screen respondToMouseInput(Point key);
}
