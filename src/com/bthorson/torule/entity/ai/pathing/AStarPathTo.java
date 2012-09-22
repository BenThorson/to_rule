package com.bthorson.torule.entity.ai.pathing;

import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * User: ben
 * Date: 9/13/12
 * Time: 1:14 PM
 */
public class AStarPathTo implements PathTo {
    @Override
    public Stack<Point> buildPath(World world, Point start, Point target) {
//        System.out.printf("Starting to build a path from x:%d,y:%d to x:%d,y:%d\n",start.x(), start.y(), target.x(), target.y());
        List<Node> closedList = new ArrayList<Node>();
        PriorityQueue<Node> openList = new PriorityQueue<Node>(PointUtil.getDiagDist(start, target) * PointUtil.getDiagDist(start, target) + 1,
                new NodeCompare());

        openList.add(new Node(start));

        while (!openList.isEmpty()){

            Node current = openList.poll();
            if (current.getPnt().equals(target)){
//                System.out.println("built path");
                return constructPath(current);
            }
            closedList.add(current);

            for (Point neighbor : getNeighbors(current.getPnt(), world, target)){
                boolean updateVals = false;
                Node n = new Node(neighbor);
                if(closedList.contains(n)){
                    continue;
                }
                int g = current.getG() + world.tile(n.getPnt()).moveCost();
                if (world.creature(n.getPnt()) != null){
                    g += 30;
                }

                if (!openList.contains(n)){
                    n.setParent(current);
                    n.setG(g);
                    n.setH(PointUtil.diagMoves(n.getPnt(), target) * 4);
                    openList.add(n);
                } else if (g < current.getG()){
                    updateVals = true;
                } else {
                    updateVals = false;
                }

                if (updateVals){
                    n.setParent(current);
                    n.setG(g);
                    n.setH(PointUtil.diagMoves(n.getPnt(), target) * 4);
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

    private List<Point> getNeighbors(Point current, World world, Point target) {
        List<Point> ret = new ArrayList<Point>();
        if (PointUtil.getDiagDist(target, current) == 1){
            ret.add(target);
            return ret;
        }
        for (Direction dir : Direction.values()){
            if (world.isTravelable(dir.point().add(current))){
                ret.add(dir.point().add(current));
            }
        }
        return ret;
    }


}
