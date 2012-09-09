package com.bthorson.torule.screens;

import java.awt.Color;

/**
 * User: ben
 * Date: 9/8/12
 * Time: 12:14 AM
 */
public class ColorUtil {

    private ColorUtil(){}

    public static Color darken(Color input, int amount){
        int red = input.getRed() - amount > 0 ? input.getRed() - amount: 0;
        int green = input.getGreen() - amount > 0 ? input.getGreen() - amount: 0;
        int blue = input.getBlue() - amount > 0 ? input.getBlue() - amount : 0;

        return new Color(red, green, blue);
    }
}
