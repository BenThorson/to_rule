package com.bthorson.torule.entity;

/**
 * User: ben
 * Date: 9/9/12
 * Time: 9:25 PM
 */
public enum CreatureImage {

    H_KNIGHT(84),
    H_PEASANT(48),
    H_SWORDSMAN(74),
    H_MERCHANT(55),
    H_SKELETON(58),
    G_SWORDMAN(38),
    G_KNIGHT(36),
    G_SKELETON(10),
    PUPPY(1 + getCritterOffset()),
    DEAD_DOG(3 + getCritterOffset()),
    WOLF(0x1EB + getCritterOffset()),
    DEAD_WOLF(0x1EE + getCritterOffset());


    private int num;

    private CreatureImage(int num) {
        this.num = num;
    }

    public int num() {
        return num;
    }

    private static int getCritterOffset(){
        return 0x90;

    }
}
