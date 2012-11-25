package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;

import java.awt.Color;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 9:58 AM
 */
public class ScreenUtil {

    public static void makeRect(AsciiPanel terminal, int x, int y, int w, int h, Color foreground, Color background, boolean isPopup){
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

        if (isPopup){
            terminal.writePopup(topLine.toString(), new Point(x, y), foreground, background);
            for (int i = 1; i < h; i++ ){
                terminal.writePopup(vertLines.toString(), new Point(x,y + i), foreground, background);
            }
            terminal.writePopup(bottomLine.toString(), new Point(x, y + h), foreground, background);

        }
        else {
            terminal.write(topLine.toString(), new Point(x, y), foreground, background);
            for (int i = 1; i < h; i++ ){
                terminal.write(vertLines.toString(), new Point(x,y + i), foreground, background);
            }
            terminal.write(bottomLine.toString(), new Point(x, y + h), foreground, background);

        }
    }

    public static String blankString(int length) {
        return solidLine(length, (char)0);
    }

    public static String solidLine(int length, char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++){
            sb.append(ch);
        }
        return sb.toString();
    }
}
