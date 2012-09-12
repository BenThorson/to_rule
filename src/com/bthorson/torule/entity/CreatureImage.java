package com.bthorson.torule.entity;

/**
 * User: ben
 * Date: 9/9/12
 * Time: 9:25 PM
 */
public enum CreatureImage {

    H_KNIGHT(84),
    H_PEASANT(48),
    G_KNIGHT(36);

    private int num;

    private CreatureImage(int num) {
        this.num = num;
    }

    public int num() {
        return num;
    }
}
