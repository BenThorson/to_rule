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
import com.bthorson.torule.worldgen.SpawnAction;

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
        List<Creature> creatures = new SpawnAction().createCreatures(quest.getSpawns(), offset.add(placement));

        return creatures;
    }
}
