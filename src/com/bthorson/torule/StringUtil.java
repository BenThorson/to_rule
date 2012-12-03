package com.bthorson.torule;

/**
 * User: Ben Thorson
 * Date: 12/2/12
 * Time: 11:50 PM
 */
public class StringUtil {

    public static boolean isNullOrEmpty(String string){
        return (string == null || string.trim().length() == 0);
    }
}
