package com.bthorson.torule.entity.conversation;

import com.bthorson.torule.entity.Profession;
import com.bthorson.torule.entity.conversation.model.ConversationScript;
import com.bthorson.torule.entity.conversation.model.Conversations;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * User: Ben Thorson
 * Date: 11/30/12
 * Time: 9:16 AM
 */
public class ConversationTree {

    Conversations conversations;

    public ConversationTree(){
        load();
    }

    private void load() {
        try {
            String jsonStr = FileUtils.readFileToString(new File("resources/dialogs/conversation.json"));
            conversations = new Gson().fromJson(jsonStr, Conversations.class);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public ConversationScript getDialogByProfession(Profession profession){
        for (ConversationScript convs: conversations.getProfessions()){
            if (convs.getProfession().equalsIgnoreCase(profession.name())){
                return convs;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ConversationScript jo = new ConversationTree().getDialogByProfession(Profession.VILLAGER);
        System.out.println("HI");
    }
}
