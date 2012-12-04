package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Collection;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/4/12
 * Time: 1:18 PM
 */
public class SerializeUtils {

    public static <S extends Entity> void serializeRefCollection(Collection<S> entities, JsonObject toAdd, String name){
        if (entities != null && !entities.isEmpty()){
            JsonArray array = new JsonArray();
            for (S entity : entities) {
                array.add(new JsonPrimitive(entity.id));
            }
            toAdd.add(name, array);
        }
    }

    public static <S extends Entity> void serializeRefMap(Map<String, S> map, JsonObject obj, String name){
        if (map != null && !map.isEmpty()){
            JsonObject props = new JsonObject();
            for (Map.Entry<String, S> item : map.entrySet()) {
                props.addProperty(item.getKey(), item.getValue().id);
            }
            obj.add(name, props);
        }
    }
}
