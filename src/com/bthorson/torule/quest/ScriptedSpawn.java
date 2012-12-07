package com.bthorson.torule.quest;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 2:35 PM
 */
public class ScriptedSpawn {

    private String type;
    private int min;
    private int max;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
