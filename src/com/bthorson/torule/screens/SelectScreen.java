package com.bthorson.torule.screens;

import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * User: ben
 * Date: 10/25/12
 * Time: 9:41 PM
 */
public class SelectScreen implements Screen{

    private final ControlCallbackScreen controlParentScreen;
    private final Screen displayParent;

    private Point cursor;

    public SelectScreen(Screen displayParent, Point cursor, ControlCallbackScreen controlParentScreen){

        this.controlParentScreen = controlParentScreen;
        this.displayParent = displayParent;
        this.cursor = cursor;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        displayParent.displayOutput(terminal);
        terminal.highlight(cursor, Color.GREEN);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                controlParentScreen.positionSelected(null);
                return controlParentScreen;
            case KeyEvent.VK_ENTER:
                return setPositionAndReturn();
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_NUMPAD4: cursor = cursor.add(Direction.WEST.point()); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6: cursor = cursor.add(Direction.EAST.point()); break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_NUMPAD8: cursor = cursor.add(Direction.NORTH.point()); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_NUMPAD2: cursor = cursor.add(Direction.SOUTH.point()); break;
            case KeyEvent.VK_NUMPAD7: cursor = cursor.add(Direction.NORTH_WEST.point()); break;
            case KeyEvent.VK_NUMPAD9: cursor = cursor.add(Direction.NORTH_EAST.point()); break;
            case KeyEvent.VK_NUMPAD1: cursor = cursor.add(Direction.SOUTH_WEST.point()); break;
            case KeyEvent.VK_NUMPAD3: cursor = cursor.add(Direction.SOUTH_EAST.point()); break;
            default:
                break;
        }
        return this;

    }

    @Override
    public Screen respondToMouseInput(Point translatedPoint) {
        cursor = translatedPoint;
        return this;
    }

    @Override
    public Screen respondToMouseClick(Point translatedPoint, int mouseButton) {
        switch (mouseButton){
            case MouseEvent.BUTTON1:
                return setPositionAndReturn();
        }
        return this;
    }

    private Screen setPositionAndReturn() {
        controlParentScreen.positionSelected(cursor);
        return controlParentScreen;
    }
}
