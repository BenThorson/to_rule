package com.bthorson.torule.entity.ai.pathing;

import java.util.Comparator;

/**
 * User: ben
 * Date: 9/13/12
 * Time: 1:33 PM
 */
public class NodeCompare implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        return (o1.getG() + o1.getH()) - (o2.getG() + o2.getH());
    }

}
