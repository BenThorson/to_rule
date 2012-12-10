package com.bthorson.torule.util;

import java.util.Random;

public class RandomUtils {
    /**
     * generates random number in a range inclusive
     *
     * @param min minimum number
     * @param max maximum number
     * @return the generated number
     */
    public static int getRandomInRange(int min, int max) throws IllegalArgumentException {
        if (min > max) {
            throw new IllegalArgumentException("Minimum cannot be larger than maximum");
        }
        if (min == max){
            return min;
        }
        int ret = new Random().nextInt(max + 1 - min) + min;
        if (ret < min || ret > max) {
            throw new IllegalStateException(
                    "Programmer messed up:  Value " + ret + "is not within range of " + min + " and " + max);
        }
        return ret;
    }
}