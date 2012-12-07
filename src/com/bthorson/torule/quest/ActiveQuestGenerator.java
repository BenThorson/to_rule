package com.bthorson.torule.quest;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.CreatureFactory;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.bthorson.torule.map.MapConstants.LOCAL_SIZE_POINT;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 5:36 PM
 */
public class ActiveQuestGenerator {
    public void generate(Quest quest, Creature giver) {
        Player player = EntityManager.getInstance().getPlayer();

        ActiveQuest activeQuest = new ActiveQuest(quest.getName(), quest.getText(), giver, createQuestCreatures(quest, giver), quest.getReward());
        player.addQuest(activeQuest);
    }

    private List<Creature> createQuestCreatures(Quest quest, Creature giver) {
        Point giverLoc = giver.position().divide(LOCAL_SIZE_POINT);
        Point offset = quest.getDirection().point().multiply(quest.getDistance()).add(giverLoc).multiply(LOCAL_SIZE_POINT);
        Point placement = PointUtil.randomPoint(LOCAL_SIZE_POINT);
        List<Creature> creatures = new ArrayList<Creature>();
        for (ScriptedSpawn spawn : quest.getSpawns()){
            int number = 0;

            if (spawn.getMin() != spawn.getMax()){
                number = new Random().nextInt(spawn.getMax() - spawn.getMin()) + spawn.getMin() + 1;
            }
            else number = spawn.getMin();
            List<Point> points = getPlaceablePointsInRegion(number, offset, placement);
            for (int i = 0; i < number; i++){
                Creature creature = CreatureFactory.INSTANCE.createCreature(spawn.getType(), points.get(i));
                creature.setAi(new WanderAI(creature, null));
                creature.setFaction(EntityManager.getInstance().getGoblinFaction());
                creatures.add(creature);
            }

        }
        return creatures;
    }

    private List<Point> getPlaceablePointsInRegion(int number, Point offset, Point placement) {
        for (int i = 1; i < 100; i++) {
            List<Point> attempt = new ArrayList<Point>();
            for (Point p : getValidPointsinRegion(i, placement)) {
                if (!World.getInstance().isOccupied(p.add(offset))){
                    attempt.add(p.add(offset));
                }
            }
            if (attempt.size() > number){
                return attempt;
            }
        }
        return new ArrayList<Point>();
    }

    private List<Point> getValidPointsinRegion(int i, Point placement) {
        List<Point> validPoints = new ArrayList<Point>();
        for (Point p : PointUtil.getPointsInRange(placement.subtract(i), placement.add(i))) {
            if (p.withinRect(LOCAL_SIZE_POINT)){
                validPoints.add(p);
            }
        }
        return validPoints;
    }

}
