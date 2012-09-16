package com.bthorson.torule.entity.group;

import com.bthorson.torule.geom.Point;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 9:00 PM
 */
public interface MovementCommander {

    public void move(Group group, Point point);
}
