package com.bthorson.torule;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;

/**
 * User: ben
 * Date: 9/12/12
 * Time: 1:42 PM
 */
public class Message {

    int originatingEntityId;
    String message;

    public Message(Creature origin, String message) {
        this.originatingEntityId = origin.id;
        this.message = message;
    }

    public Creature getOrigin() {
        return (Creature)EntityManager.getInstance().getById(originatingEntityId);
    }

    public String getMessage() {
        return message;
    }
}
