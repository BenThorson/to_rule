package com.bthorson.torule.screens;

import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.quest.ActiveQuest;
import com.bthorson.torule.screens.component.DefaultRenderer;

import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/8/12
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuestScreen implements Screen {

    private Screen previous;
    private EntityDetailScreen<ActiveQuest> detailScreen;

    public QuestScreen(Screen previous){
        this.previous = previous;
        detailScreen = new EntityDetailScreen<ActiveQuest>(EntityManager.getInstance().getPlayer().getQuests(),
                                                           "Active Quests", new DefaultRenderer<ActiveQuest>());
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        detailScreen.displayOutput(terminal);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (detailScreen.respondToUserInput(key) == -2){
            return previous;
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
