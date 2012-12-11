package com.bthorson.torule.entity;

import com.bthorson.torule.geom.Point;

/**
 * User: Ben Thorson
 * Date: 12/10/12
 * Time: 10:38 PM
 */
public interface Updatable {

    public void update(long updateVal);

    public long getLastUpdate();

    public Point position();

    public boolean dead();
}
