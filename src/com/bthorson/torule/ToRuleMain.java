package com.bthorson.torule;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.screens.Screen;
import com.bthorson.torule.screens.StartScreen;

import javax.swing.JFrame;
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
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        ToRuleMain app = new ToRuleMain();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

}
