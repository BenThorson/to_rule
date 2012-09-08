package com.bthorson.torule.entity;

import com.bthorson.torule.map.World;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 8:48 PM
 */
public interface CreatureAI {

    public void execute();

    public void interact(Entity entity);
}
