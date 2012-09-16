package com.bthorson.torule.entity.group;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;

import java.util.*;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 9:26 PM
 */
public class FormUpCommander implements MovementCommander {
    InPlaceCommander commander = new InPlaceCommander();
    @Override
    public void move(Group group, Point point) {
        final Point dimension = group.getFormation().getDimensions();
        List<Point> pnt = group.getFormation().getPositions();
        Collections.sort(pnt, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return Math.min(o1.x(), dimension.x() - o1.x()) + Math.min(o1.y(), dimension.y() - o1.y()) -
                        (Math.min(o2.x(), dimension.x() - o2.x()) + Math.min(o2.y(), dimension.y() - o2.y()));
            }
        });
        Collections.reverse(pnt);
        HashMap<Point, Creature>members = new HashMap<Point, Creature>();
        group.setMembers(members);
        for (Point pt : pnt){
            if (pt.equals(new Point((dimension.x() - 1) / 2, (dimension.y() - 1) / 2))){
                members.put(pt, group.getSquadCommander());
                group.setCommanderPoint(pt);
                break;
            }
        }
        Point offset = group.getSquadCommander().position().subtract(group.getCommanderPoint());

        for (Point pt : pnt){
            if (shouldBeEmpty(pt, dimension, group.getMemList().size() + 1)){
                continue;
            }
            if (!pt.equals(group.getCommanderPoint())){
                Creature creature = findNearestCreatureToPoint(pt, offset, group.getMemList(), members);
                members.put(pt, creature);
            }
//            group.updateForPoint(pt, offset);
        }
        commander.move(group, point);
    }

    private boolean shouldBeEmpty(Point pt, Point dimension, int groupSize) {
        return  ((pt.x() + 1) + (pt.y() * dimension.x()) > groupSize);
    }

    private Creature findNearestCreatureToPoint(Point pt, Point offset, List<Creature> memList, Map<Point, Creature> memMap) {
        Point combined = pt.add(offset);
        int minDist = Integer.MAX_VALUE;
        Creature nearest = null;

        for (Creature creature : memList){
            int creatureDist = PointUtil.getDiagDist(creature.position(), combined);
            if (creatureDist < minDist && !memMap.containsValue(creature)){
                nearest = creature;
                minDist = creatureDist;
            }
        }
        return nearest;
    }
}
