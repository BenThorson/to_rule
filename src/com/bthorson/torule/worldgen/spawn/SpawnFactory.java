package com.bthorson.torule.worldgen.spawn;

import com.bthorson.torule.StringUtil;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.Herd;
import com.bthorson.torule.entity.ai.HerdAI;
import com.bthorson.torule.entity.ai.MeanderAI;
import com.bthorson.torule.entity.ai.PatrolAI;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.Ferocity;
import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.MapConstants;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * User: Ben Thorson
 * Date: 12/10/12
 * Time: 3:05 PM
 */
public enum SpawnFactory {

    INSTANCE;

    private Gson gson = new Gson();

    private List<CritterSpawn> critterSpawns = new ArrayList<CritterSpawn>();
    private List<PatrolSpawn> patrolSpawns = new ArrayList<PatrolSpawn>();

    private SpawnFactory(){
        load();
    }

    private void load() {
        try {
            String spawnFile = FileUtils.readFileToString(new File("resources/creature/randomCritterGeneration.json"));
            JsonObject jo = new JsonParser().parse(spawnFile).getAsJsonObject();
            JsonArray spwns = jo.get("spawns").getAsJsonArray();
            for (JsonElement spwn : spwns){
                critterSpawns.add(gson.fromJson(spwn, CritterSpawn.class));
            }
            spawnFile = FileUtils.readFileToString(new File("resources/creature/patrols.json"));
            jo = new JsonParser().parse(spawnFile).getAsJsonObject();
            spwns = jo.get("patrols").getAsJsonArray();
            for (JsonElement spwn : spwns){
                patrolSpawns.add(gson.fromJson(spwn, PatrolSpawn.class));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createRandomCritterSpawn(Point point, Local local){
        List<CritterSpawn> spawnChoices = getSpawnTypeForFerocity(local.getFerocity());
        if (!spawnChoices.isEmpty()){
            CritterSpawn spawn = spawnChoices.get(new Random().nextInt(spawnChoices.size()));
            List<Creature> creatures = new SpawnAction().createCreatures(Arrays.asList(spawn), point);
            if (creatures.size() > 1) {
                Herd herd = new Herd(point, creatures);
                EntityManager.getInstance().addUpdatable(herd);
                herd.setAi(new MeanderAI(herd, null));
                for (Creature creature : creatures){
                    creature.setAi(new HerdAI(creature, null, herd));
                    if (!StringUtil.isNullOrEmpty(spawn.getFaction())){

                        creature.setFaction(EntityManager.getInstance().getFaction(creature, spawn.getFaction()));
                    }
                }
            } else {
                Creature creature = creatures.get(0);
                creature.setAi(new WanderAI(creature, PointUtil.floorToNearest100(creature.position()),
                                            PointUtil.floorToNearest100(creature.position()).add(MapConstants.LOCAL_SIZE_POINT),
                                            null, true ));
                if (!StringUtil.isNullOrEmpty(spawn.getFaction())){

                    creature.setFaction(EntityManager.getInstance().getFaction(creature, spawn.getFaction()));
                }
            }

        }
    }

    private List<CritterSpawn> getSpawnTypeForFerocity(Ferocity ferocity) {
        List<CritterSpawn> choices = new ArrayList<CritterSpawn>();

        for (CritterSpawn spawn : critterSpawns){
            if (Arrays.asList(spawn.getFerocity()).contains(ferocity)){
                choices.add(spawn);
            }
        }
        return choices;
    }

    public void createPatrolSpawn(String patrolType, List<Point> patrolPoints){
        for (PatrolSpawn spawn : patrolSpawns){
            if (spawn.getPatrolType().equalsIgnoreCase(patrolType)){
                List<Creature> creatures = new SpawnAction().createCreatures(spawn.getSpawns(), patrolPoints.get(0));
                Herd herd = new Herd(patrolPoints.get(0), creatures);
                for (Creature creature : creatures){
                    creature.setAi(new HerdAI(creature, null, herd));
                }
                herd.setAi(new PatrolAI(herd, makePatrolPointsTravellable(patrolPoints), 0, null));
                EntityManager.getInstance().addUpdatable(herd);

            }
        }
    }

    private List<Point> makePatrolPointsTravellable(List<Point> patrolPoints) {
        List<Point> alteredPoints = new ArrayList<Point>();
        for (Point point : patrolPoints) {
            alteredPoints.add(SpawnUtils.getPlaceablePointsInRegion(1, PointUtil.floorToNearest100(point),
                                                                    point.subtract(PointUtil.floorToNearest100(point))).get(0));
        }
        return alteredPoints;
    }


}
