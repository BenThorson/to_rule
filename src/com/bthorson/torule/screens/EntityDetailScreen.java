package com.bthorson.torule.screens;

import com.bthorson.torule.StringUtil;
import com.bthorson.torule.entity.Describable;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.screens.component.ScrollList;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 10:04 AM
 */
public class EntityDetailScreen<T extends Entity & Describable> {

    private ScrollList<T> list;
    private List<T> entities;
    private String title;


    public EntityDetailScreen(List<T> entities, String title, EntityRenderer<T> entityRenderer){
        this.entities = entities;

        list = new ScrollList<T>(entities, 20, Screen.SCREEN_HEIGHT-1, entityRenderer);
        this.title = title;
    }

    public void displayOutput(AsciiPanel terminal) {

        list.displayOutput(terminal, 0, 0);
        Point detailsStart = new Point(22, 2);
        terminal.writePopupText(title, new Point(22, 0), Color.WHITE, Color.BLACK);
        int detailsRow = 1;
        if (!entities.isEmpty()){
            T item = entities.get(list.getCurrentChoice());
            List<String> details = item.getDetailedInfo();
            for (String detail : details){
                List<String> formatted = StringUtil.splitStringByLength(detail, Screen.SCREEN_WIDTH - 24);
                for (String format: formatted){
                    terminal.writePopupText(format, detailsStart.add(new Point(0, detailsRow++)), Color.WHITE, Color.BLACK);
                }
                detailsRow++;
            }
        }
    }

    public int respondToUserInput(KeyEvent key) {
        return list.respondToUserInput(key);
    }

    public void updateList(List<T> entities) {
        this.entities = entities;
        list.updateList(entities);
    }

}
