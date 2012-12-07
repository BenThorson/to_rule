package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.persist.LoadAction;
import com.bthorson.torule.screens.component.Menu;
import com.bthorson.torule.worldgen.SavedWorldLoader;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 9:16 AM
 */
public class LoadScreen implements Screen {

    private Menu choose;
    private String[] choices;

    public LoadScreen(){
        choices = getChoices();
        choose = new Menu("Load a game", (String)null, choices);
    }
    @Override
    public void displayOutput(AsciiPanel terminal) {
        choose.displayOutput(terminal, (SCREEN_WIDTH - choose.getWidth()) / 2,
                                        (SCREEN_HEIGHT - choose.getHeight()) / 2);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        int num = choose.respondToUserInput(key);
        switch (num){
            case -2:
                return new StartScreen();
            case -1:
                return this;
            default:
                String val = choices[num];
                if ("cancel".equalsIgnoreCase(val)){
                    return new StartScreen();
                } else {
                    SavedWorldLoader loader = new LoadAction().load(val);
                    loader.startWorld();
                    return new PlayScreen();
                }
        }
    }

    @Override
    public Screen respondToMouseInput(Point translatedPoint) {
        return this;
    }

    @Override
    public Screen respondToMouseClick(Point translatedPoint, int mouseButton) {
        return this;
    }

    public String[] getChoices() {
        List<String> vals = new ArrayList<String>();
        File f = new File("save");
        if (f.exists()){
            File[] subs = f.listFiles();
            for (File file : subs){
                vals.add(file.getName());
            }
        }
        vals.add("cancel");
        return vals.toArray(new String[vals.size()]);
    }

}
