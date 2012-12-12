package com.bthorson.torule.screens;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.screens.component.Menu;
import com.bthorson.torule.screens.component.ScrollList;

import java.awt.event.KeyEvent;

/**
 * User: Ben Thorson
 * Date: 12/7/12
 * Time: 2:40 PM
 */
public class FollowerListScreen implements Screen {

    private static final String REMOVE = "Remove Equipment";
    private static final String GIVE = "Give Item";
    private static final String OPTIMIZE = "Optimize Equip";
    private static final String DISBAND = "Disband Follower";

    public static final String[] CHOICES = {
            REMOVE,
            GIVE,
            OPTIMIZE,
            DISBAND
    };

    private EntityDetailScreen<Creature> followerScreen;
    private Menu followerActionMenu;
    private boolean menuActive;
    private ScrollList itemTransfer;
    private Screen previous;
    private Creature selectedFollower;
    private Player player = EntityManager.getInstance().getPlayer();
    EntityRenderer<Creature> renderer = new EntityRenderer<Creature>() {
        @Override
        public String render(Creature input) {
            String prefix = "";
            if (player.getFollowersInCommandableRange().contains(input)){
                prefix += "*";
            }
            return prefix + input.getName();
        }
    };

    public FollowerListScreen(Screen previous) {
        this.previous = previous;
        followerScreen = new EntityDetailScreen<Creature>(player.getFollowers(), "Followers", renderer);
        followerActionMenu = new Menu("Actions", CHOICES);
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        followerScreen.displayOutput(terminal);
        if (menuActive){
            followerActionMenu.displayOutput(terminal, 10,10);
        }

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        if (menuActive){
            int num = followerActionMenu.respondToUserInput(key);
            switch (num){
                case -2:
                    menuActive = false;
                case -1:
                    return this;
                default:
                    Screen transfer = doAction(CHOICES[num]);
                    if (transfer != null){
                        return transfer;
                    }
                    menuActive = false;
            }
        } else {
            int num = followerScreen.respondToUserInput(key);
            switch (num){
                case -2:
                    return previous;
                case -1:
                    return this;
                default:
                    if (!player.getFollowers().isEmpty()){
                        selectedFollower = player.getFollowers().get(num);
                        menuActive = true;
                    }
            }
        }
        return this;
    }

    private Screen doAction(String choice) {
        if (REMOVE.equalsIgnoreCase(choice)){
            if (player.getFollowersInCommandableRange().contains(selectedFollower)){
                return new ItemTransferScreen(this, selectedFollower, player);
            }
        } else if (GIVE.equalsIgnoreCase(choice)){
            if (player.getFollowersInCommandableRange().contains(selectedFollower)){
                return new ItemTransferScreen(this, player, selectedFollower);
            }
        } else if (OPTIMIZE.equalsIgnoreCase(choice)){
            if (player.getFollowersInCommandableRange().contains(selectedFollower)){
                selectedFollower.transferEquipmentForOptimize(player);
            }
        } else if (DISBAND.equalsIgnoreCase(choice)){
            selectedFollower.setLeader(null);
            player.removeFollower(selectedFollower);
            if (selectedFollower.getAi().getPrevious() != null){
                selectedFollower.setAi(selectedFollower.getAi().getPrevious());
            } else {
                selectedFollower.setAi(new WanderAI(selectedFollower, null, true));
            }
        }
        return null;
    }

    @Override
    public Screen respondToMouseInput(Point translatedPoint) {
        return this;
    }

    @Override
    public Screen respondToMouseClick(Point translatedPoint, int mouseButton) {
        return this;
    }
}
