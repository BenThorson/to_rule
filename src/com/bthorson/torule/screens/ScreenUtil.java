package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;

import java.awt.Color;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:58 AM
 */
public class ScreenUtil {

    public static void makeRect(AsciiPanel terminal, int x, int y, int w, int h, Color foreground, Color background){
        char tl = (char)201;
        char tr = (char)187;
        char bl = (char)200;
        char br = (char)188;
        char horizontal = (char)205;
        char vertical = (char)186;

        StringBuilder topLine = new StringBuilder();
        StringBuilder bottomLine = new StringBuilder();
        StringBuilder vertLines = new StringBuilder();
        topLine.append(tl);
        bottomLine.append(bl);
        vertLines.append(vertical);
        for (int i = 1; i < w; i++){
            topLine.append(horizontal);
            bottomLine.append(horizontal);
            vertLines.append(' ');
        }
        topLine.append(tr);
        bottomLine.append(br);
        vertLines.append(vertical);

        terminal.write(topLine.toString(), x, y, foreground, background);
        for (int i = 1; i < h; i++ ){
            terminal.write(vertLines.toString(), x,y + i, foreground, background);
        }
        terminal.write(bottomLine.toString(), x, y + h, foreground, background);


    }
}
