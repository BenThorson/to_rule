package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;

import java.awt.Color;
import java.awt.event.KeyEvent;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 11:05 AM
 */
public class Menu  {

    private String title;
    private int width;
    private int height;
    private String[] choices;
    private int currentChoice;

    private Color foreground;
    private Color background;

    public Menu(String title, String[] choices, Color foreground, Color background) {
        this.title = title;
        this.choices = choices;
        this.foreground = foreground;
        this.background = background;

        currentChoice = 0;

        height = choices.length + 3;
        width = getMaxChoiceLength() + 2;
    }

    private int getMaxChoiceLength() {
        int max = 0;
        for (String choice : choices){
            if (choice.length() > max){
                max = choice.length();
            }
        }
        if (title.length() > max){
            max = title.length();
        }
        return max;
    }

    public void displayOutput(AsciiPanel terminal, int x, int y) {
        ScreenUtil.makeRect(terminal, x, y, width, height, foreground, background);
        terminal.write(title, x + 1, y + 1, foreground, background);
        terminal.write(makeDivider(), x, y + 2, foreground, background);
        for (int i = 0; i < choices.length; i++){
            if (i == currentChoice){
                terminal.write(choices[i], x + 1, y + i + 3, foreground, AsciiPanel.red);
            } else {
                terminal.write(choices[i], x + 1, y + i + 3, foreground, background);
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
