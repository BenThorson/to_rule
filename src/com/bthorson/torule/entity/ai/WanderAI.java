package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.util.Random;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 8:51 PM
 */
public class WanderAI extends CreatureAI {

    private Point nwBound;
    private Point seBound;
    public WanderAI(AiControllable self, Point nwBound, Point seBound){
        super(self);
        this.nwBound = nwBound;
        this.seBound = seBound;
    }

    @Override
    public CreatureAI execute() {

        Creature toAggro = getTarget();
        if (toAggro != null){
            AggroAI aggroAI = new AggroAI(self, toAggro);
            aggroAI.execute();
            return aggroAI;
        }
        int check = new Random().nextInt(10);

        switch (check){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                break;
            case 6:
                if(self.position().add(new Point(1,0)).withinRect(nwBound, seBound))
                    self.move(new Point(1, 0));
                break;
            case 7:
                if(self.position().add(new Point(1,0)).withinRect(nwBound, seBound))
                    self.move(new Point(-1, 0));
                break;
            case 8:
                if(self.position().add(new Point(1,0)).withinRect(nwBound, seBound))
                    self.move(new Point(0, 1));
                break;
            case 9:
                if(self.position().add(new Point(1,0)).withinRect(nwBound, seBound))
                    self.move(new Point(0, -1));
                break;
        }
        return this;
    }

    @Override
    public void interact(Entity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
