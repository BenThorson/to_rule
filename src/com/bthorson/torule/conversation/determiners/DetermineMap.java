package com.bthorson.torule.conversation.determiners;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 1:42 AM
 * To change this template use File | Settings | File Templates.
 */
public enum DetermineMap {

    INSTANCE;

    private HashMap<String, Determiner> determiners;

    private DetermineMap(){
        determiners = new HashMap<String, Determiner>();
        determiners.put("checkRecruit", new RecruitDetermine());
        determiners.put("hasInventory", new ShopHasInventory());
        determiners.put("questActive", new QuestActiveDeterminer());
        determiners.put("questComplete", new QuestCompleteDeterminer());
    }

    public Determiner get(String key){
        return determiners.get(key);
    }
}
