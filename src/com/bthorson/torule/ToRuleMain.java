package com.bthorson.torule;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.screens.Screen;
import com.bthorson.torule.screens.StartScreen;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 10:17 AM
 */
public class ToRuleMain extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;

    public ToRuleMain(){
        super();
        terminal = new AsciiPanel(Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT);
        add(terminal);
        pack();
        screen = new StartScreen();
        addKeyListener(this);
        repaint();
//        super();
//        terminal = new AsciiPanel(1,1);
//        add(terminal);
//        pack();
//        screen = new Screen() {
//            @Override
//            public void displayOutput(AsciiPanel terminal) {
//                terminal.write(Tile.TREE.glyph(), 0,0, Color.GREEN, Color.BLUE);
//            }
//
//            @Override
//            public Screen respondToUserInput(KeyEvent key) {
//                return this;
//            }
//        };
//        addKeyListener(this);
//        repaint();
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        ToRuleMain app = new ToRuleMain();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

}
