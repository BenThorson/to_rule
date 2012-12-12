package com.bthorson.torule.entity.ai.pathing;

import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.World;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * My implementation of the A star algorithm.  I have a feeling it's inefficient, or my use of it is, or java sucks.
 * User: ben
 * Date: 9/13/12
 * Time: 1:14 PM
 */
public class AStarPathTo implements PathTo {
    @Override
    public Stack<Point> buildPath(Point start, Point target, boolean ignoreTerrain) {
//        System.out.printf("Starting to build a path from x:%d,y:%d to x:%d,y:%d\n",start.x(), start.y(), target.x(), target.y());
        World world = World.getInstance();
        List<Node> closedList = new ArrayList<Node>();
        PriorityQueue<Node> openList = new PriorityQueue<Node>(PointUtil.getDiagDist(start, target) * PointUtil.getDiagDist(start, target) + 1,
                new NodeCompare());
        int maxOpenList = 0;
        openList.add(new Node(start));

        while (!openList.isEmpty()){

            if (closedList.size() > 10000){
                System.out.println("Possible error building path from " + start + " to " + target);
                return new Stack<Point>();
            }

            Node current = openList.poll();
            if (current.getPnt().equals(target)){
//                System.out.println(String.format("built path ending with %d,%d.  Open list size had a maximum of %d nodes", current.getPnt().x(), current.getPnt().y(), maxOpenList));
                return constructPath(current);
            }
            closedList.add(current);

            for (Point neighbor : getNeighbors(current.getPnt(), world, target, ignoreTerrain)){
                boolean updateVals = false;
                Node n = new Node(neighbor);
                if(closedList.contains(n)){
                    continue;
                }
                int g = current.getG() + (ignoreTerrain ? 1 : world.tile(n.getPnt()).moveCost());

                int h = PointUtil.diagMoves(n.getPnt(), target) * (ignoreTerrain ? 1 : 6) +
                        PointUtil.getDiagDist(n.getPnt(), target) * (ignoreTerrain ? 1 : 3);

                if (world.creature(n.getPnt()) != null && !n.getPnt().equals(target) && !ignoreTerrain){
                    h += 30;
                }


                if (!openList.contains(n)){
                    n.setParent(current);
                    n.setG(g);
                    n.setH(h);
//                    System.out.println(String.format("adding point = %d,%d g=%d h=%d parent %d,%d", n.getPnt().x(), n.getPnt().y(), n.getG(), n.getH(), n.getParent().getPnt().x(), n.getParent().getPnt().y() ));
                    openList.add(n);
                    if (openList.size() > maxOpenList){
                        maxOpenList = openList.size();
                    }
                } else if (g < current.getG()){
                    updateVals = true;
                } else {
                    updateVals = false;
                }

                if (updateVals){
                    System.out.println("this even happen?");
                    n.setParent(current);
                    n.setG(g);
                    n.setH(h);
//                    System.out.println(String.format("updating point = %d,%d g=%d h=%d parent %d,%d", n.getPnt().x(), n.getPnt().y(), n.getG(), n.getH(), n.getParent(), n.getParent().getPnt().y()));

                }
            }
        }
        return null;
    }

    private Stack<Point> constructPath(Node current) {
        Stack<Point> path = new Stack<Point>();
        while (current.getParent() != null){
            path.add(current.getPnt());
            current = current.getParent();
        }
        return path;
    }

    private List<Point> getNeighbors(Point current, World world, Point target, boolean ignoreTerrain) {
        List<Point> ret = new ArrayList<Point>();
        if (PointUtil.getDiagDist(target, current) == 1){
            ret.add(target);
            return ret;
        }
        for (Direction dir : Direction.values()){
            if (world.isTravelable(dir.point().add(current)) || ignoreTerrain){
                ret.add(dir.point().add(current));
            }
        }
        return ret;
    }


}
