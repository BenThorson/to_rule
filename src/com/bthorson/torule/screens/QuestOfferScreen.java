package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.LocalType;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.World;
import com.bthorson.torule.quest.KillQuestGenerator;
import com.bthorson.torule.quest.Quest;
import com.bthorson.torule.screens.component.Confirmation;
import com.bthorson.torule.screens.component.Menu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 2:44 PM
 */
public class QuestOfferScreen implements Screen {

    private Menu menu;
    private ConversationScreen parent;
    private Creature creature;
    private Quest quest;
    private Confirmation confirmation;

    public QuestOfferScreen(ConversationScreen screen, Creature creature, Quest quest) {

        this.parent = screen;
        this.creature = creature;
        this.quest = quest;
        init();
        this.menu = new Menu(quest.getName(), formatText(quest), Menu.YES_NO);
    }

    private void init() {
        if (quest.getDirection() == null) {
            quest.setDirection(getDirectionOfQuest());

        }
    }

    private Direction getDirectionOfQuest() {
        List<Direction> possibleDirections = new ArrayList<Direction>();
        Point creatureLocal = creature.position().divide(MapConstants.LOCAL_SIZE_POINT);
        for (Direction d : Direction.values()){
            Local local = World.getInstance().getLocal(
                    d.point()
                     .multiply(quest.getDistance())
                     .add(creatureLocal));
            if (local != null && local.getType().equals(LocalType.WILDERNESS)){
                possibleDirections.add(d);
            }
        }
        int random = new Random().nextInt(possibleDirections.size());
        return possibleDirections.get(random);
    }

    private String formatText(Quest quest) {
        if (quest.getTextParams() != null && quest.getTextParams().size() > 0){
            List params = new ArrayList();
            for (String param : quest.getTextParams()){
                if (param.equals("direction")){
                    params.add(quest.getDirection().prettyName());
                }
            }
            return String.format(quest.getText(), params.toArray(new Object[params.size()]));
        }
        return quest.getText();
    }


    @Override
    public void displayOutput(AsciiPanel terminal) {
        if (confirmation != null){
            confirmation.displayOutput(terminal, 5,5);
        } else {
            menu.displayOutput(terminal, 1, 1);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (confirmation != null){
            int val = confirmation.respondToUserInput(key);
            switch (val){
                case -2:
                case 1:
                    confirmation = null;
                    return this;
                case -1:
                    return this;
                case 0:
                    new KillQuestGenerator().generate(quest, creature);
                    return parent;
            }
        } else {
            int val = menu.respondToUserInput(key);
            switch (val){
                case -2:
                case 1:
                    return parent;
                case -1:
                    return this;
                case 0:
                    confirmation = new Confirmation("Accept this quest?");
                    return this;
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
