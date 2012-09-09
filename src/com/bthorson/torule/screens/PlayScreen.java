package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.CreatureFactory;
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

    public PlayScreen(World world) {
        this.world = world;
        player = CreatureFactory.buildPlayer(world, 10, 10);
        world.addCreature(player);
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x - Screen.SCREEN_WIDTH / 2,
                                    world.width() - Screen.SCREEN_WIDTH));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y - Screen.SCREEN_HEIGHT / 2,
                                    world.height() - Screen.SCREEN_HEIGHT));
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();
        displayTiles(terminal, left, top);

        terminal.writeHumanoid(71, player.x - left, player.y - top);
        terminal.writeCenter("abcdefghijklmnopqrstuvwxyz", SCREEN_HEIGHT - 1);
        terminal.writeCenter("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SCREEN_HEIGHT - 2);


    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        for (int x = 0; x < SCREEN_WIDTH; x++){
            for (int y = 0; y < SCREEN_HEIGHT; y++){
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy)){
                    Creature creature = world.creature(wx, wy);
                    if (creature != null){
                        terminal.writeHumanoid(64, creature.x - left, creature.y - top);
                    }
                    terminal.write(world.tile(wx, wy).glyph(), x, y, world.tile(wx, wy).color(), world.tile(wx, wy).color());
                } else {
                    terminal.write(world.tile(wx, wy).glyph(), x, y, ColorUtil.darken(world.tile(wx, wy).color(), 25), ColorUtil.darken(world.tile(wx, wy).color(), 25));
                }
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()){
            case KeyEvent.VK_ESCAPE: return new StartScreen();
            case KeyEvent.VK_ENTER: return new StartScreen();
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_NUMPAD4: player.move(-1, 0); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6: player.move(1, 0); break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_NUMPAD8: player.move(0, -1); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_NUMPAD2: player.move(0, 1); break;
            case KeyEvent.VK_NUMPAD7: player.move(-1, -1); break;
            case KeyEvent.VK_NUMPAD9: player.move(1, -1); break;
            case KeyEvent.VK_NUMPAD1: player.move(-1, 1); break;
            case KeyEvent.VK_NUMPAD3: player.move(1, 1); break;
        }

        world.update();

        return this;
    }
}
