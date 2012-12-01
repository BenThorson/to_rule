package com.bthorson.torule;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.screens.Screen;
import com.bthorson.torule.screens.StartScreen;

import javax.swing.*;
import java.awt.event.*;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 10:17 AM
 */
public class ToRuleMain extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;
    private Timer timer;
    private boolean acceptInput;

    public ToRuleMain(){
        super();
        setTitle("Helloooo!");

        terminal = new AsciiPanel(Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT);
        add(terminal);
        pack();
        screen = new StartScreen();
        addKeyListener(this);
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptInput = true;
            }
        });
        timer.start();
        addMouseMotionListener(new MouseAdapter() {


            @Override
            public void mouseMoved(MouseEvent e) {
                int adjY = e.getY() - 25;
                Point point = new Point(e.getX(), adjY).divide(terminal.charBr());
                screen.respondToMouseInput(point);
                screen.displayOutput(terminal);
                repaint();
//                System.out.printf("Mouse clicked %d %d\n",e.getX(), adjY);
                int adjX = e.getX() / terminal.charBr().x();
                adjY= adjY / terminal.charBr().y();
//                System.out.printf("Tile is %d %d\n",adjX, adjY);
            }
        });
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
        if (acceptInput){
            screen = screen.respondToUserInput(e);
            timer.restart();
            acceptInput = false;
        }
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
