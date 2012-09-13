package com.bthorson.torule.entity.ai.pathing;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * User: ben
 * Date: 9/13/12
 * Time: 1:14 PM
 */
public class AStarPathTo implements PathTo {
    @Override
    public List<Point> buildPath(World world, Point start, Point target) {
        List<Node> closedList = new ArrayList<Node>();
        PriorityQueue<Node> openList = new PriorityQueue<Node>(PointUtil.getDiagDist(start, target) * PointUtil.getDiagDist(start, target),
                new NodeCompare());
        List<Point> path = new ArrayList<Point>();

        openList.add(new Node(start));

        while (!openList.isEmpty()){

            Node current = openList.poll();
            if (current.equals(target)){
                return constructPath(closedList);
            }
            closedList.add(current);

            for (Point neighbor : getNeighbors(current.getPnt(), world)){
                boolean updateVals = false;
                Node n = new Node(neighbor);
                if(closedList.contains(n)){
                    continue;
                }
                int g = current.getG() + world.tile(n.getPnt().x(), n.getPnt().y()).moveCost();

                if (!openList.contains(n)){
                    openList.add(n);
                    updateVals = true;
                } else if (g < current.getG()){
                    updateVals = true;
                } else {
                    updateVals = false;
                }

                if (updateVals){
                    n.setParent(current);
                    n.setG(g);
                    n.setH(PointUtil.getDiagDist(n.getPnt(), target) * 2);
                }
            }
        }
        return null;
    }

    private List<Point> constructPath(Node current) {
        List<Point> path = new ArrayList<Point>();
        path.add(current.getPnt());
        while (current.getParent() != null){
            current = current.getParent();
            path.add(current.getPnt());
        }
        return path;
    }

    private List<Point> getNeighbors(Point current, World world) {
        List<Point> ret = new ArrayList<Point>();
        int x = current.x();
        int y = current.y();
        if (world.isTravelable(x-1,y)){
            ret.add(new Point(x-1,y));
        }
        if (world.isTravelable(x-1,y-1)) {
            ret.add(new Point(x - 1, y - 1));
        }
        if (world.isTravelable(x-1,y+1)){
            ret.add(new Point(x-1,y+1));
        }
        if (world.isTravelable(x+1,y)) {
            ret.add(new Point(x + 1, y));
        }
        if (world.isTravelable(x+1,y-1)) {
            ret.add(new Point(x + 1, y - 1));
        }
        if (world.isTravelable(x+1,y+1)) {
            ret.add(new Point(x + 1, y + 1));
        }
        if(world.isTravelable(x,y-1)) {
            ret.add(new Point(x, y - 1));
        }
        if (world.isTravelable(x, y+1)) {
            ret.add(new Point(x, y + 1));
        }
        return ret;
    }


}
