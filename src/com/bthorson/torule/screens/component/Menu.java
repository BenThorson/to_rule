package com.bthorson.torule.screens.component;

import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.screens.ScreenUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 11:05 AM
 */
public class Menu {

    private static int MAX_WIDTH = 48;
    public static final String[] YES_NO = new String[]{"Yes", "No"};

    private String title;
    private List<String> splitDetails = new ArrayList<String>();
    private int width;
    private int height;
    private String[] choices;
    private int currentChoice;

    private Color foreground;
    private Color background;
    private Color textColor;

    public Menu(String title, String[] choices){
        this(title, (String)null, choices);
    }

    public Menu(String title, String additionalDetails, String[] choices) {
        this (title, additionalDetails, choices, Color.YELLOW, Color.BLACK, Color.WHITE);
    }

    public Menu(String title, String additionalDetails, String[] choices, Color foreground, Color background, Color textColor) {
        this.title = title;
        this.choices = choices;
        this.foreground = foreground;
        this.background = background;
        this.textColor = textColor;

        currentChoice = 0;
        splitDetails(additionalDetails);

        init();
    }

    public Menu(String title, List<String> detailedInfo, String[] choices){
        this (title, detailedInfo, choices, Color.YELLOW, Color.BLACK, Color.WHITE);
    }

    public Menu(String title, List<String> detailedInfo, String[] choices, Color foreground, Color background, Color textColor) {
        this.title = title;
        this.splitDetails = detailedInfo;
        this.choices = choices;
        this.foreground = foreground;
        this.background = background;
        this.textColor = textColor;

        currentChoice = 0;
        init();
    }

    private void init() {

        if (splitDetails.size() > 0){
            height = choices.length + splitDetails.size() + 4;
        } else {
            height = choices.length + 3;
        }
        width = Math.max(Math.max(longestStringLengthInList(Arrays.asList(choices)),
                                           longestStringLengthInList(splitDetails)), title.length()) + 2;

    }

    private void splitDetails(String additionalDetails) {

        if (additionalDetails == null){
            return;
        }
        String[] words = additionalDetails.split(" ");
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (String word : words){
            if (word.length() + counter + 1 < MAX_WIDTH){
                sb.append(word).append(" ");
                counter += word.length() + 1;
            } else {
                splitDetails.add(sb.toString());
                sb = new StringBuilder(word).append(" ");
                counter = word.length() + 1;
            }
        }
        if (sb.length() > 0){
            splitDetails.add(sb.toString());
        }
    }

    private int longestStringLengthInList(List<String> strings){
        int max = 0;
        for (String choice : strings){
            if (choice.length() > max){
                max = choice.length();
            }
        }
        return max;
    }


    public void displayOutput(AsciiPanel terminal, int x, int y) {
        Point pos = new Point(x,y);
        int row = 1;
        ScreenUtil.makeRect(terminal, x, y, width, height, foreground, background, true);
        terminal.writePopupText(title, pos.add(new Point(1, row++)), textColor, background);
        terminal.writePopup(makeDivider(), pos.add(new Point(0,row++)), foreground, background);
        if (splitDetails.size() > 0){
            for (String detail : splitDetails){
                terminal.writePopupText(detail, pos.add(new Point(1, row++)), textColor, background);
            }
            terminal.writePopup(makeDivider(), pos.add(new Point(0, row++)), foreground, background);
        }
        for (int i = 0; i < choices.length; i++){
            if (i == currentChoice){
                terminal.writePopupText(choices[i], pos.add(new Point(1, row++)), textColor, AsciiPanel.red);
            } else {
                terminal.writePopupText(choices[i], pos.add(new Point(1, row++)), textColor, background);
            }
        }
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

    private Color highlightColor(Color foreground) {
        int red = foreground.getRed() + 80 < 255 ? foreground.getRed() + 80 : 255;
        int green = foreground.getGreen() + 80 < 255 ? foreground.getGreen() + 80 : 255;
        int blue = foreground.getBlue() + 80 < 255 ? foreground.getBlue() + 80 : 255;

        return new Color(red, green, blue);
    }

    public int respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN
                || key.getKeyCode() == KeyEvent.VK_NUMPAD2){
            currentChoice = (currentChoice + 1) % choices.length;
        } else if (key.getKeyCode() == KeyEvent.VK_UP
                || key.getKeyCode() == KeyEvent.VK_NUMPAD8){
            currentChoice = Math.abs((currentChoice - 1 + choices.length) % choices.length);
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE){
            return -2;
        }

        if (key.getKeyCode() == KeyEvent.VK_ENTER){
            return currentChoice;
        }
        return -1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
