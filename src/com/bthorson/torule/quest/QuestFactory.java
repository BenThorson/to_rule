package com.bthorson.torule.quest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
