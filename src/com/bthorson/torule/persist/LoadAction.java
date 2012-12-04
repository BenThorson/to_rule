package com.bthorson.torule.persist;

import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.map.World;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/4/12
 * Time: 4:11 PM
 */
public class LoadAction {

    public void load(String name){
        try {
            String prefix = "save/" + name;

            WorldParser parser = new WorldParser();
            parser.parse(new BufferedReader(new FileReader(prefix + "/world.wo")));

//            JsonElement element = new JsonParser().parse(new JsonReader(new FileReader()))
//            Bu writer = new PrintWriter(directory.getPath() + "/" + name + ".wo");
//            World.getInstance().serialize(writer);
//            writer.close();
//
//            Map<String, JsonArray> lists = EntityManager.getInstance().serialize();
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//            for (String key : lists.keySet()){
//                writer = new PrintWriter(directory.getPath() + "\\" + key + ".eo");
//                writer.append(gson.toJson(lists.get(key)));
//                writer.close();
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static void main(String[] args) {
        new LoadAction().load("blah");
    }
}
