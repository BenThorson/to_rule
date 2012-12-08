package com.bthorson.torule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/2/12
 * Time: 11:50 PM
 */
public class StringUtil {

    public static boolean isNullOrEmpty(String string){
        return (string == null || string.trim().length() == 0);
    }
    
    public static List<String> splitStringByLength(String input, int maxSize){
        List<String> output = new ArrayList<String>();    
        
        if (input == null){
                return output;
            }
            String[] words = input.split(" ");
            StringBuilder sb = new StringBuilder();
            int counter = 0;
            for (String word : words){
                if (word.length() + counter + 1 < maxSize){
                    sb.append(word).append(" ");
                    counter += word.length() + 1;
                } else {
                    output.add(sb.toString());
                    sb = new StringBuilder(word).append(" ");
                    counter = word.length() + 1;
                }
            }
            if (sb.length() > 0){
                output.add(sb.toString());
            }
        return output;

    }
    
}
