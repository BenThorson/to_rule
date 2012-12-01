package com.bthorson.torule.entity.conversation;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.conversation.actions.ActionMap;
import com.bthorson.torule.entity.conversation.actions.ConversationAction;
import com.bthorson.torule.entity.conversation.determiners.DetermineMap;
import com.bthorson.torule.entity.conversation.model.ConversationNode;
import com.bthorson.torule.entity.conversation.model.ConversationScript;
import com.bthorson.torule.entity.conversation.model.ConversationTextAndOptions;
import com.bthorson.torule.screens.ConversationScreen;

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
    public ConversationTextAndOptions startConversation(){
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

    public ConversationTextAndOptions continueConversation(ConversationScreen screen, int optionSelected){
        if (current.getResponses() != null && optionSelected < current.getResponses().size()){
            ConversationNode node = current.getResponses().get(optionSelected);

            if (node.getCheck() == null || node.getCheck().trim().length() == 0){
                current = node.getResponses().get(0);
            } else {
                int value = DetermineMap.INSTANCE.get(node.getCheck()).determine(creature);
                current = node.getResponses().get(value);
            }
            List<String> responses = buildResponseTextList();
            ConversationAction action = ActionMap.INSTANCE.get(current.getAction());
            if (action != null){
                action.doAction(screen, creature);
            }
            return new ConversationTextAndOptions(current.getText(), responses.toArray(new String[responses.size()]));

        } else {
            int newOpt = optionSelected - current.getResponses().size();
            if (newOpt == 0){
                return startConversation();
            } else {
                return null;
            }
        }
    }
}
