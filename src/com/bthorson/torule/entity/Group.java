package com.bthorson.torule.entity;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.Rect;

import java.util.Collections;
import java.util.List;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 1:31 AM
 */
public class Group {

    private static final int DEF_WIDTH_RATIO = 5;
    private static final int DEF_HEIGHT_RATIO = 3;


    private List<Creature> members;
    private Creature squadCommander;
    private Point commanderPoint;


    public Rect rect;

    public Group(List<Creature> members, Creature squadCommander){
        this.members = members;
        Collections.sort(members, new CreatureComparator());
        this.squadCommander = squadCommander;
        buildRect();
    }

    private void buildRect() {
        rect = new Rect(new Point(DEF_WIDTH_RATIO, DEF_HEIGHT_RATIO), members.size(), squadCommander.position());
    }

    private void move(Point point){
        Rect rect2 = rect.add(point);
        for (Creature member : members){
            move();
        }
    }
}
