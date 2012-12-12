package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;
import com.bthorson.torule.worldgen.WorldGenParams;
import com.bthorson.torule.worldgen.WorldGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/4/12
 * Time: 10:33 AM
 */
public class SaveAction {


    public void save(String name) {

        try {
            File directory = new File("save");
            if (!directory.exists()){
                directory.mkdir();
            }
            File subDirectory = new File(directory.getPath() + "/" + name);
            if (subDirectory.exists()){
                FileUtils.deleteDirectory(subDirectory);
            }
            subDirectory.mkdir();

            PrintWriter writer = new PrintWriter(subDirectory.getPath() + "/world.wo");
            World.getInstance().serialize(writer);
            writer.close();

            Map<String, JsonArray> lists = EntityManager.getInstance().serialize();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            for (String key : lists.keySet()){
                writer = new PrintWriter(subDirectory.getPath() + "/" + key + ".eo");
                writer.append(gson.toJson(lists.get(key)));
                writer.close();
            }

            writer = new PrintWriter(subDirectory.getPath() + "/metadata.eo");
            JsonObject obj = new JsonObject();
            obj.addProperty("turn", World.getInstance().getTurnCounter());
            obj.addProperty("idSerialGen", Entity.getCurrentId());
            writer.append(gson.toJson(obj));
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) {
        WorldGenParams params = new WorldGenParams();
        params.setNumCities(5);
        params.setWorldSize(new Point(2000,2000));
        params.setPlayerName("Bob");
        new WorldGenerator().generateWorld(params);
        new SaveAction().save("blah");

    }
}
