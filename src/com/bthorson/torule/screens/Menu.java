package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 11:05 AM
 */
public class Menu  {

    private static int MAX_WIDTH = 48;

    private String title;
    private List<String> splitDetails = new ArrayList<String>();
    private int width;
    private int height;
    private String[] choices;
    private int currentChoice;

    private Color foreground;
    private Color background;

    public Menu(String title, String additionalDetails, String[] choices, Color foreground, Color background) {
        this.title = title;
        this.choices = choices;
        this.foreground = foreground;
        this.background = background;

        currentChoice = 0;

        init(additionalDetails);
    }

    private void init(String additionalDetails) {
        splitDetails(additionalDetails);

        if (splitDetails.size() > 0){
            height = choices.length + splitDetails.size() + 4;
        } else {
            height = choices.length + 3;
        }
        width = Math.max(Math.max(longestStringLengthInList(Arrays.asList(choices)), longestStringLengthInList(splitDetails)), title.length()) + 2;

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
        terminal.writePopup(title, pos.add(new Point(1,row++)), foreground, background);
        terminal.writePopup(makeDivider(), pos.add(new Point(0,row++)), foreground, background);
        if (splitDetails.size() > 0){
            for (String detail : splitDetails){
                terminal.writePopup(detail, pos.add(new Point(1, row++)), foreground, background);
            }
            terminal.writePopup(makeDivider(), pos.add(new Point(0, row++)), foreground, background);
        }
        for (int i = 0; i < choices.length; i++){
            if (i == currentChoice){
                terminal.writePopup(choices[i], pos.add(new Point(1, row++)),foreground, AsciiPanel.red);
            } else {
                terminal.writePopup(choices[i], pos.add(new Point(1, row++)), foreground, background);
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
        }

        if (key.getKeyCode() == KeyEvent.VK_ENTER){
            return currentChoice;
        }
        System.out.println(currentChoice);
        return -1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
