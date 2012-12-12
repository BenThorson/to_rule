package com.bthorson.torule.quest;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 2:33 PM
 */
public enum QuestFactory {

INSTANCE;

    private List<Quest> catalog = new ArrayList<Quest>();
    private Gson gson = new Gson();

    private QuestFactory(){
        load();
    }

    private void load() {
        try {
            String armor = FileUtils.readFileToString(new File("resources/quest/quest.json"));
            JsonObject jo = new JsonParser().parse(armor).getAsJsonObject();
            JsonArray items = jo.get("quests").getAsJsonArray();
            for (JsonElement item : items){
                catalog.add(gson.fromJson(item, Quest.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Quest getQuest(){
        return catalog.get(0);
    }

}
