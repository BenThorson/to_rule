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
    G_SWORDSMAN(38),
    G_KNIGHT(36),
    G_SKELETON(10),
    PUPPY(1 + getCritterOffset()),
    DEAD_DOG(3 + getCritterOffset()),
    WOLF(0x1EB + getCritterOffset()),
    DEAD_WOLF(0x1EC + getCritterOffset()),
    COW(0xA0 + getCritterOffset()),
    DEAD_COW(0xA2 + getCritterOffset()),
    BOAR(0x184 + getCritterOffset()),
    DEAD_BOAR(0x187 + getCritterOffset()),
    BADGER(0xC4 + getCritterOffset()),
    DEAD_BADGER(0xC7 + getCritterOffset()),
    TROLL(0x1B3 + getCritterOffset()),
    DEAD_TROLL(0x1B7 + getCritterOffset()),
    DEMON(0x2F3 + getCritterOffset()),
    NONE(0x1B8);


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
