package com.bthorson.torule.entity.group;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 9:32 PM
 */
public class RectangleFormation implements Formation {

    private static final int DEF_WIDTH_RATIO = 5;
    private static final int DEF_HEIGHT_RATIO = 2;

    private Rect rect;

    @Override
    public void buildPositions(List<Creature> creatures, Creature commander) {
        rect = new Rect(new Point(DEF_WIDTH_RATIO, DEF_HEIGHT_RATIO), creatures.size() + 1);
    }

    @Override
    public void wheelCW() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void wheelCCW() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Point> getPositions() {
        List<Point> ret = new ArrayList<Point>();
        for (int i = 0; i < rect.getPoints().length; i++){
            for (int j = 0; j < rect.getPoints()[0].length; j++){
                ret.add(rect.getPoints()[i][j]);
            }
        }
        return ret;
    }

    @Override
    public Point getDimensions() {
        if (rect == null){
            return new Point(0,0);
        }
        return new Point(rect.getPoints().length, rect.getPoints()[0].length);
    }
}
