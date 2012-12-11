package com.bthorson.torule.worldgen.spawn;

import com.bthorson.torule.StringUtil;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.Herd;
import com.bthorson.torule.entity.ai.HerdAI;
import com.bthorson.torule.entity.ai.MeanderAI;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.Ferocity;
import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.MapConstants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
public enum CritterSpawnFactory {

    INSTANCE;

    private Gson gson = new Gson();

    private List<CritterSpawn> spawns = new ArrayList<CritterSpawn>();

    private CritterSpawnFactory(){
        load();
    }

    private void load() {
        try {
            String spawnFile = FileUtils.readFileToString(new File("resources/creature/randomCritterGeneration.json"));
            JsonObject jo = new JsonParser().parse(spawnFile).getAsJsonObject();
            JsonArray spwns = jo.get("spawns").getAsJsonArray();
            for (JsonElement spwn : spwns){
                spawns.add(gson.fromJson(spwn, CritterSpawn.class));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateSpawn(Point point, Local local){
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

        for (CritterSpawn spawn : spawns){
            if (Arrays.asList(spawn.getFerocity()).contains(ferocity)){
                choices.add(spawn);
            }
        }
        return choices;
    }


}
