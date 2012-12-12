package com.bthorson.torule.entity.ai.pathing;

import com.bthorson.torule.geom.Point;

/**
 * User: ben
 * Date: 9/13/12
 * Time: 2:16 PM
 */
public class Node {

    private Point pnt;
    private Node parent;

    private int g;
    private int h;

    public Node(Point pnt) {
        this.pnt = pnt;
    }

    public Point getPnt() {
        return pnt;
    }

    public void setPnt(Point pnt) {
        this.pnt = pnt;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;
        if (pnt != null){
            return (pnt.equals(node.pnt));
        } else {
            return node.pnt == null;
        }
    }

    @Override
    public int hashCode() {
        int result = pnt != null ? pnt.hashCode() : 0;
        return result;
    }
}
