package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;

import java.awt.*;
import java.awt.event.KeyEvent;

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
        terminal.highlight(cursor, Color.GREEN, true);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                return controlParentScreen;
            case KeyEvent.VK_ENTER:
                controlParentScreen.positionSelected(cursor);
                return controlParentScreen;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_NUMPAD4: cursor = cursor.add(Direction.WEST.point()); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6: cursor = cursor.add(Direction.EAST.point()); break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_NUMPAD8: cursor = cursor.add(Direction.NORTH.point()); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_NUMPAD2: cursor = cursor.add(Direction.SOUTH.point()); break;
            case KeyEvent.VK_NUMPAD7: cursor = cursor.add(Direction.NORTHWEST.point()); break;
            case KeyEvent.VK_NUMPAD9: cursor = cursor.add(Direction.NORTHEAST.point()); break;
            case KeyEvent.VK_NUMPAD1: cursor = cursor.add(Direction.SOUTHWEST.point()); break;
            case KeyEvent.VK_NUMPAD3: cursor = cursor.add(Direction.SOUTHEAST.point()); break;
            default:
                break;
        }
        return this;

    }

    @Override
    public Screen respondToMouseInput(Point key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
