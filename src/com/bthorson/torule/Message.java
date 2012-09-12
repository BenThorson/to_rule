package com.bthorson.torule;

import com.bthorson.torule.entity.Creature;

/**
 * User: ben
 * Date: 9/12/12
 * Time: 1:42 PM
 */
public class Message {

    Creature origin;
    String message;

    public Message(Creature origin, String message) {
        this.origin = origin;
        this.message = message;
    }

    public Creature getOrigin() {
        return origin;
    }

    public String getMessage() {
        return message;
    }
}
