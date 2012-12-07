package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.PhysicalEntity;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.screens.component.Menu;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/3/12
 * Time: 7:23 PM
 */
public class ViewScreen implements ControlCallbackScreen {

    private SelectScreen selectScreen;
    private PlayScreen previous;
    private boolean goBack = false;
    private boolean attemptedSelection = false;
    private Menu infoDialog;
    private Menu multiEntity;
    private List<PhysicalEntity> entities;

    public ViewScreen(PlayScreen previous, Point position) {
        this.previous = previous;
        selectScreen = new SelectScreen(previous, position, this);
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        previous.displayOutput(terminal);
        if (infoDialog != null){
            infoDialog.displayOutput(terminal, 9, 9);
        } else if (multiEntity != null){
            multiEntity.displayOutput(terminal, 10, 10);
        } else {
            selectScreen.displayOutput(terminal);
        }
    }


    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (goBack){
            return previous;
        }

        if (infoDialog != null){
            if (infoDialog.respondToUserInput(key) != -1){
                infoDialog = null;
                return this;
            }
        }

        if (multiEntity != null){
            int val = multiEntity.respondToUserInput(key);
            switch (val){
                case -2:
                    multiEntity = null;
                    return previous;
                case -1:
                    return this;
                default:
                    infoDialog = new Menu("Info", entities.get(val).getDetailedInfo(), new String[]{"OK"});
                    return this;
            }
        } else if (attemptedSelection){
            return previous;
        } else {
            selectScreen.respondToUserInput(key);
        }
        return this;
    }

    @Override
    public void positionSelected(Point point) {
        if (point == null){
            goBack = true;
            return;
        }
        attemptedSelection = true;
        entities = EntityManager.getInstance().getAllEntities(point.add(previous.getOffset()));
        if (entities.isEmpty()){
            return;
        }
        if (entities.size() == 1){
            infoDialog = new Menu("Info", entities.get(0).getDetailedInfo(), new String[]{"OK"});
        } else {
            multiEntity = new Menu("Look at what?", (String)null, getNames(entities));
        }
    }

    private String[] getNames(List<PhysicalEntity> entities) {
        List<String> names = new ArrayList<String>();
        for (Entity entity : entities){
            names.add(entity.getName());
        }
        return names.toArray(new String[names.size()]);
    }

    @Override
    public Screen respondToMouseInput(Point translatedPoint) {
        if (infoDialog == null){
            return attemptedSelection ? previous : selectScreen.respondToMouseInput(translatedPoint);
        }
        return null;
    }

    @Override
    public Screen respondToMouseClick(Point translatedPoint, int mouseButton) {
        return attemptedSelection ? previous : selectScreen.respondToMouseClick(translatedPoint, mouseButton);
    }


}
