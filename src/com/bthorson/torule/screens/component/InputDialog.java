package com.bthorson.torule.screens.component;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.screens.ScreenUtil;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * User: Ben Thorson
 * Date: 11/25/12
 * Time: 12:45 PM
 */
public class InputDialog {

    private static int MAX_WIDTH = 48;

    private String title;
    private int width;
    private int height;
    private StringBuilder input = new StringBuilder();

    private Color foreground;
    private Color background;
    private Color textColor;

    public InputDialog(String title){
        this(title, Color.YELLOW, Color.BLACK, Color.WHITE);
    }

    public InputDialog(String title, Color foreground, Color background, Color textColor) {
        this.title = title;
        this.foreground = foreground;
        this.background = background;
        this.textColor = textColor;

        width = 20;
        height = 5;

    }


    public void displayOutput(AsciiPanel terminal, int x, int y) {
        Point pos = new com.bthorson.torule.geom.Point(x,y);
        int row = 1;
        ScreenUtil.makeRect(terminal, x, y, width, height, foreground, background, true);
        terminal.writePopupText(title, pos.add(new com.bthorson.torule.geom.Point(1, row++)), textColor, background);
        terminal.writePopup(makeDivider(), pos.add(new Point(0,row++)), foreground, background);
        terminal.writePopupText(input.toString(), pos.add(new Point(1, row)), textColor, background);
        terminal.writePopup((char) 27, pos.add(new Point(1 + input.length(), row++)), Color.GREEN, Color.BLACK);

    }

    private String makeDivider() {
        StringBuilder sb = new StringBuilder();
        sb.append((char)204);
        for (int i = 1; i < width; i++){
            sb.append((char)205);
        }
        sb.append((char)185);
        return sb.toString();
    }

    public String respondToUserInput(KeyEvent key) {

        int keyCode = key.getKeyCode();
        if (keyCode == KeyEvent.VK_BACK_SPACE){
            if (input.length() > 0){
                input.deleteCharAt(input.length() - 1);
            }
        } else if (keyCode == KeyEvent.VK_ENTER){
            return input.toString();
        } else if (keyCode == KeyEvent.VK_ESCAPE){
            if (input.length() > 0){
                input = new StringBuilder();
            } else {
                return (char)27 + "";
            }

        } else if (keyCode >= 48 && keyCode <= 112 ) {
            input.append(new String("" + key.getKeyChar()).toUpperCase());
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
