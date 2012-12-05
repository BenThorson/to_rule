package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonObject;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/4/12
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonObjectEntityPair<T extends Entity> {

    private JsonObject jsonObject;
    private T entity;

    public JsonObjectEntityPair(JsonObject jsonObject, T entity) {
        this.jsonObject = jsonObject;
        this.entity = entity;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public T getEntity() {
        return entity;
    }
}
