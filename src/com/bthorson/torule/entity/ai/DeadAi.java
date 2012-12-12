package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Entity;

/**
 * Stub object for dead creatures.
 * User: ben
 * Date: 9/11/12
 * Time: 9:36 PM
 */
public class DeadAi extends CreatureAI {

    public DeadAi() {
        super(null, null);
    }

    @Override
    public CreatureAI execute() {
        return this;
    }

    @Override
    public boolean interact(Entity entity) {
        return false;
    }
}
