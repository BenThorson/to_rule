package com.bthorson.torule.entity.group.ai;

import com.bthorson.torule.entity.ai.CreatureAI;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.entity.group.Group;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

/**
 * User: ben
 * Date: 9/17/12
 * Time: 7:34 PM
 */
public class GroupAI {

    Group self;
    CreatureAI underlyingAI;

    public GroupAI(Group self) {
        this.self = self;
        underlyingAI = new WanderAI(self,World.NW_CORNER, World.getInstance().seCorner(), null, true);
    }

    public void execute() {
        if (self.inFormation()){
            underlyingAI = underlyingAI.execute();
        }
        else {
            self.move(Point.BLANK);
        }
    }
}
