package com.bthorson.torule.entity.group;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Point;

import java.util.List;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 8:42 PM
 */
public interface Formation {

    public void buildPositions(List<Creature> creatures, Creature commander);

    public void wheelCW();

    public void wheelCCW();

    public List<Point> getPositions();

    public Point getDimensions();

}
