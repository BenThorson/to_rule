package com.bthorson.torule.screens;

import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.persist.SaveAction;
import com.bthorson.torule.screens.component.Menu;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/4/12
 * Time: 10:16 AM
 */
public class SaveScreen implements Screen {

    private Screen previous;
    private Menu choose;
    private String[] choices;
    private InputDialog saveName;
    private Menu confirm;
    private String name;
    private StartScreen startScreen = new StartScreen();

    public SaveScreen(Screen previous){
        this.previous = previous;
        choices = getChoices();
        choose = new Menu("Save and Quit?", (String)null, choices);
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        previous.displayOutput(terminal);
        if (saveName != null){
            saveName.displayOutput(terminal, 10, 10);
        } else if (confirm != null){
            confirm.displayOutput(terminal, 10, 10);
        } else {
            choose.displayOutput(terminal, 10, 10);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (saveName != null){
            name = saveName.respondToUserInput(key);
            if (name != null){
                saveName = null;
                new SaveAction().save(name);
                return startScreen;
            }
        } else if (confirm != null) {
            int num = confirm.respondToUserInput(key);
            switch (num){
                case -2:
                case 1:
                    confirm = null;
                    return this;
                case -1:
                    return this;
                case 0:
                    new SaveAction().save(name);
                    return startScreen;

            }

        } else {
            int num = choose.respondToUserInput(key);
            switch (num){
                case -2:
                    return previous;
                case -1:
                    return this;
                default:
                    String val = choices[num];
                    if ("new".equalsIgnoreCase(val)){
                        saveName = new InputDialog("Name of save");
                    } else if ("cancel".equalsIgnoreCase(val)){
                        return previous;
                    } else {
                        name = val;
                        confirm = new Menu("Overwrite?", (String)null, Menu.YES_NO);
                        return this;
                    }
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

    public String[] getChoices() {
        List<String> vals = new ArrayList<String>();
        File f = new File("save");
        if (f.exists()){
            File[] subs = f.listFiles();
            for (File file : subs){
                vals.add(file.getName());
            }
        }
        vals.add("new");
        vals.add("cancel");
        return vals.toArray(new String[vals.size()]);
    }
}
