package com.bthorson.torule.worldgen.spawn;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.CreatureFactory;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.World;
import com.bthorson.torule.quest.Quest;
import com.bthorson.torule.quest.ScriptedSpawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.bthorson.torule.map.MapConstants.LOCAL_SIZE_POINT;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/8/12
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpawnAction {

    public List<Creature> createCreatures(List<? extends Spawn> spawns, Point point) {

        Point offset = PointUtil.floorToNearest100(point);
        Point placement = point.subtract(offset);

        List<Creature> creatures = new ArrayList<Creature>();
        for (Spawn spawn : spawns){
            int number = 0;

            if (spawn.getMin() != spawn.getMax()){
                number = new Random().nextInt(spawn.getMax() - spawn.getMin()) + spawn.getMin() + 1;
            }
            else number = spawn.getMin();
            List<Point> points = SpawnUtils.getPlaceablePointsInRegion(number, offset, placement);
            for (int i = 0; i < number; i++){
                Creature creature = CreatureFactory.INSTANCE.createCreature(spawn.getCreatureType(), points.get(i));
                creatures.add(creature);
            }

        }
        return creatures;
    }


}
