package com.bthorson.torule.entity.conversation.actions;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.ai.FollowAI;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 2:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class JoinPlayer implements ConversationAction {
    @Override
    public void doAction(Creature creature) {
        World.getInstance().getPlayer().addFollower(creature);
        creature.setLeader(World.getInstance().getPlayer().getCreature());
        creature.setAi(new FollowAI(creature));
    }
}
