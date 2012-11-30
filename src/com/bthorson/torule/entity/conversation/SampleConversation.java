package com.bthorson.torule.entity.conversation;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.conversation.model.ConversationNode;
import com.bthorson.torule.entity.conversation.model.ConversationScript;
import com.bthorson.torule.entity.conversation.model.ConversationTextAndOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 11/30/12
 * Time: 8:57 AM
 */
public class SampleConversation {

    private Creature creature;

    private ConversationScript script;

    private ConversationNode current;

    public SampleConversation(Creature creature){
        this.creature = creature;
        script = new ConversationTree().getDialogByProfession(creature.getProfession());
    }
    public ConversationTextAndOptions startConversation(Creature player){
        current = script.getIntroduction();
        List<String> responseText = buildResponseTextList();

        return new ConversationTextAndOptions(current.getText(), responseText.toArray(new String[responseText.size()]));
    }

    private List<String> buildResponseTextList() {
        List<String> responseText = new ArrayList<String>();
        for (ConversationNode response : current.getResponses()){
            responseText.add(response.getText());
        }
        responseText.add("Can we start over?");
        responseText.add("Ok bye!"); return responseText;
    }

    public ConversationTextAndOptions continueConversation(Creature player, int optionSelected){
        if (current.getResponses() != null && optionSelected < current.getResponses().size()){
            ConversationNode node = current.getResponses().get(optionSelected);
            if (node.getCheck() != null && node.getCheck().trim().length() != 0){
                current = node.getResponses().get(0);
                List<String> responses = buildResponseTextList();
                return new ConversationTextAndOptions(current.getText(), responses.toArray(new String[responses.size()]));
            } else {
                //todo add decision logic
                current = node.getResponses().get(0);
                List<String> responses = buildResponseTextList();
                return new ConversationTextAndOptions(current.getText(), responses.toArray(new String[responses.size()]));
            }

        } else {
            int newOpt = optionSelected - current.getResponses().size();
            if (newOpt == 0){
                return startConversation(player);
            } else {
                return null;
            }
        }
    }
}
