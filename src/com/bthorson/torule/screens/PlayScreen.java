package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.CreatureFactory;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.awt.Color;
import java.awt.event.KeyEvent;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:07 PM
 */
public class PlayScreen implements Screen{

    private World world;
    private Creature player;
    private StatusScreen statusScreen;
    private MessageScreen messageScreen;
    private int xBorder = 45;
    private int yBorder = 30;

    public PlayScreen(World world) {
        this.world = world;
        this.player = world.getPlayer();
        this.statusScreen = new StatusScreen(world, xBorder);
        this.messageScreen = new MessageScreen(world, yBorder);
    }

    public Point getOffset() {
        return new Point(Math.max(0, Math.min(player.position().x() - Screen.SCREEN_WIDTH / 2,
                                    world.width() - Screen.SCREEN_WIDTH)),
                         Math.max(0, Math.min(player.position().y() - Screen.SCREEN_HEIGHT / 2,
                                    world.height() - Screen.SCREEN_HEIGHT)));
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        Point offset = getOffset();
        displayTiles(terminal, offset);

        terminal.writeHumanoid(player.glyph(), player.position().subtract(offset), world.tile(player.position()));

        statusScreen.displayOutput(terminal);
        messageScreen.displayOutput(terminal);
    }

    private void displayTiles(AsciiPanel terminal, Point offset) {
        for (int x = 0; x < xBorder; x++){
            for (int y = 0; y < yBorder; y++){
                Point viewPort = new Point(x,y);
                Point worldPoint = viewPort.add(offset);
                if (player.canSee(worldPoint)){
                    player.explore(worldPoint);
                    terminal.writeTile(world.tile(worldPoint), viewPort);

                    Entity item = world.item(worldPoint);
                    if (item != null){
                        terminal.writeHumanoid(item.glyph(), item.position().subtract(offset), world.tile(worldPoint));
                    }

                    Creature creature = world.creature(worldPoint);
                    if (creature != null){
                        terminal.writeHumanoid(creature.glyph(), creature.position().subtract(offset), world.tile(worldPoint));
                    }
                } else if (player.hasExplored(worldPoint)) {
                    terminal.writeDarkTile(world.tile(worldPoint), viewPort, 25);
                }
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (player.dead()){
            return new DeadScreen(this);
        }
        switch (key.getKeyCode()){
            case KeyEvent.VK_ESCAPE: return new StartScreen();
            case KeyEvent.VK_ENTER: return new StartScreen();
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_NUMPAD4: player.move(new Point(-1,0)); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6: player.move(new Point(1, 0)); break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_NUMPAD8: player.move(new Point(0, -1)); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_NUMPAD2: player.move(new Point(0, 1)); break;
            case KeyEvent.VK_NUMPAD7: player.move(new Point(-1, -1)); break;
            case KeyEvent.VK_NUMPAD9: player.move(new Point(1, -1)); break;
            case KeyEvent.VK_NUMPAD1: player.move(new Point(-1, 1)); break;
            case KeyEvent.VK_NUMPAD3: player.move(new Point(1, 1)); break;
        }

        world.update();

        return this;
    }
}
