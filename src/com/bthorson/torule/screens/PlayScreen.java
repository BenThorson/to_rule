package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.CreatureFactory;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;

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

        terminal.write(player.glyph(), player.x - left, player.y - top, player.color());
        terminal.writeCenter("-- press [escape] to lose or [enter] to win --", SCREEN_HEIGHT - 1);

        for (Creature creature: world.getCreatures()){
            if (creature.x >= left && creature.x < left + SCREEN_WIDTH && creature.y >= top && creature.y < top + SCREEN_HEIGHT){
                terminal.write(creature.glyph(), creature.x - left, creature.y - top, creature.color());
            } else {
            }
        }
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        for (int x = 0; x < Screen.SCREEN_WIDTH; x++){
            for (int y = 0; y < Screen.SCREEN_HEIGHT; y++){
                int wx = x + left;
                int wy = y + top;
                Tile tile = world.tile(wx, wy);
                terminal.write(tile.glyph(), x, y, tile.color());
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
