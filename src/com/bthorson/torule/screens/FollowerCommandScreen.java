package com.bthorson.torule.screens;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.ai.AttackAI;
import com.bthorson.torule.entity.ai.GuardAI;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.map.World;
import com.bthorson.torule.screens.component.Menu;

import java.awt.event.KeyEvent;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/7/12
 * Time: 7:12 AM
 */
public class FollowerCommandScreen implements ControlCallbackScreen {

    private PlayScreen previous;
    private SelectScreen selectScreen;
    private boolean selectScreenActive;
    private boolean done;
    private Menu commandMenu;
    private int choice;

    public FollowerCommandScreen(PlayScreen previous, Point position) {
        this.previous = previous;
        this.selectScreen = new SelectScreen(previous, position, this);
        commandMenu = new Menu("Follower Commands", new String[]{"Guard this",
                                                                 "Follow me",
                                                                 "Attack Target"});
    }

    @Override
    public void positionSelected(Point point) {
        selectScreenActive = false;
        if (point != null) {
            List<Creature> followers = EntityManager.getInstance().getPlayer().getFollowersInCommandableRange();
            switch (choice){
                case 0:
                    for (Creature follower : followers){
                        follower.setAi(new GuardAI(follower, point.add(previous.getOffset()), follower.getAi().getPrevious()));
                    }
                    break;
                case 2:
                    for (Creature follower : followers){
                        Creature creature = World.getInstance().creature(point.add(previous.getOffset()));
                        if (creature != null && (follower.getFaction().getEnemies().contains(creature.getFaction()) ||
                                creature.getFaction().getEnemies().contains(follower.getFaction()))) {

                            follower.setAi(new AttackAI(follower, creature, follower.getAi()));
                        }
                    }
            }
            done = true;
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        previous.displayOutput(terminal);
        if (selectScreenActive){
            selectScreen.displayOutput(terminal);
        } else {
            commandMenu.displayOutput(terminal, 10,10);
        }

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (done){
            return previous;
        }
        if (selectScreenActive) {
            if (this == selectScreen.respondToUserInput(key)) {
                return previous;
            }
        } else {
            int val = commandMenu.respondToUserInput(key);
            switch (val) {
                case -2:
                    return previous;
                case -1:
                    return this;
                case 0:
                case 2:
                    choice = val;
                    selectScreenActive = true;
                    return this;
                case 1:
                    List<Creature> creatures = EntityManager.getInstance().getPlayer().getFollowersInCommandableRange();
                    for (Creature follower : creatures){
                        follower.follow(EntityManager.getInstance().getPlayer());
                    }
                    return previous;

            }
        }
        return this;
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
