package com.bthorson.torule.entity.group;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 9:32 PM
 */
public class RectangleFormation implements Formation {

    private static final int DEF_WIDTH_RATIO = 5;
    private static final int DEF_HEIGHT_RATIO = 3;

    private Rect rect;

    @Override
    public void buildPositions(List<Creature> creatures, Creature commander) {
        rect = new Rect(new Point(DEF_WIDTH_RATIO, DEF_HEIGHT_RATIO), creatures.size() + 1);
    }

    @Override
    public void wheelCW() {
        rect.rotateLeft();
    }

    @Override
    public void wheelCCW() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Point> getPositions() {
        return new ArrayList<Point>(Arrays.asList(rect.getPoints()));
    }

    @Override
    public Point getDimensions() {
        if (rect == null){
            return Point.BLANK;
        }
        return rect.getDimension();
    }
}
