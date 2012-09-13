package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.NearestComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 8:51 PM
 */
public class WanderAI extends CreatureAI {

    public WanderAI(Creature self){
        super(self);
    }
    @Override
    public void execute() {

        List<Creature> visibleCreatures = self.getVisibleCreatures();
        List<Creature> hostilable = new ArrayList<Creature>();
        for (Creature other: visibleCreatures){
            if (shouldHostile(other)){
                hostilable.add(other);
            }
        }
        if (hostilable.size() > 0){
            Collections.sort(hostilable, new NearestComparator(self));
            Creature toAggro = hostilable.get(0);
            AggroAI aggroAI = new AggroAI(self, toAggro);
            aggroAI.execute();
            self.setAi(aggroAI);
            return;
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
                self.move(1, 0);
                break;
            case 7:
                self.move(-1, 0);
                break;
            case 8:
                self.move(0, 1);
                break;
            case 9:
                self.move(0, -1);
                break;
        }
    }

    @Override
    public void interact(Entity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
