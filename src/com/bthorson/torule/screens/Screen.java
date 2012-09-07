package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:47 AM
 */
public interface Screen {

    public static final int SCREEN_HEIGHT = 30;
    public static final int SCREEN_WIDTH = 100;

    public void displayOutput(AsciiPanel terminal);
    public Screen respondToUserInput(KeyEvent key);
}
