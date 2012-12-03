package com.bthorson.torule.screens;

import asciiPanel.AsciiPanel;
import com.bthorson.torule.entity.Corpse;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:07 PM
 */
public class PlayScreen implements Screen {

    private World world = World.getInstance();
    private Player player;
    private StatusScreen statusScreen;
    private MessageScreen messageScreen;
    private int xBorder = 45;
    private int yBorder = 30;

    public PlayScreen() {
        this.player = world.getPlayer();
        this.statusScreen = new StatusScreen(xBorder, yBorder);
        this.messageScreen = new MessageScreen(world, yBorder);
    }

    public Point getOffset() {
        return new Point(Math.max(0, Math.min(player.getCreature().position().x() - xBorder / 2,
                                    world.width() - xBorder)),
                         Math.max(0, Math.min(player.getCreature().position().y() - yBorder / 2,
                                    world.height() - yBorder)));
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        Point offset = getOffset();
        displayTiles(terminal, offset);

        terminal.writeHumanoid(player.getCreature().glyph(), player.getCreature().position().subtract(offset), world.tile(player.getCreature().position()));

        statusScreen.displayOutput(terminal);
        messageScreen.displayOutput(terminal);
    }



    private void displayTiles(AsciiPanel terminal, Point offset) {
        for (int x = 0; x < xBorder; x++){
            for (int y = 0; y < yBorder; y++){
                Point viewPort = new Point(x,y);
                Point worldPoint = viewPort.add(offset);
                if (player.getCreature().canSee(worldPoint)){
                    player.explore(worldPoint);
                    terminal.writeTile(world.tile(worldPoint), viewPort);

                    List<Entity> items = EntityManager.getInstance().item(worldPoint);
                    if (items.size() > 0){
                        Entity item = items.get(0);
                        if (item instanceof Corpse){
                            terminal.writeHumanoid(item.glyph(), item.position().subtract(offset), world.tile(worldPoint));
                        } else {
                            terminal.write((char)item.glyph(), item.position().subtract(offset),
                                           item.color(), world.tile(worldPoint).colorBG());
                        }
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
        if (player.getCreature().dead()){
            return new DeadScreen(this);
        }
        switch (key.getKeyCode()){
            case KeyEvent.VK_Q:
                World.destroy();
                return new StartScreen();
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_NUMPAD4: player.getCreature().move(Direction.WEST.point()); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6: player.getCreature().move(Direction.EAST.point()); break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_NUMPAD8: player.getCreature().move(Direction.NORTH.point()); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_NUMPAD2: player.getCreature().move(Direction.SOUTH.point()); break;
            case KeyEvent.VK_NUMPAD7: player.getCreature().move(Direction.NORTHWEST.point()); break;
            case KeyEvent.VK_NUMPAD9: player.getCreature().move(Direction.NORTHEAST.point()); break;
            case KeyEvent.VK_NUMPAD1: player.getCreature().move(Direction.SOUTHWEST.point()); break;
            case KeyEvent.VK_NUMPAD3: player.getCreature().move(Direction.SOUTHEAST.point()); break;
            case KeyEvent.VK_NUMPAD5: break;
            case KeyEvent.VK_I: return new InventoryManagementScreen(this);
//            case KeyEvent.VK_PERIOD: player.getCreature().getGroup().rotateTest(); break;
            case KeyEvent.VK_T: return new ConversationScreen(this, player.getCreature().position().subtract(getOffset()));
            case KeyEvent.VK_H: World.getInstance().skipTurns(500); break;
            case KeyEvent.VK_G:
                List<Item> items = getItems(player.getCreature().position());
                if (items.size() > 0){
                    return new LootScreen(this, items);
                }
            default:
                return this;
        }

        world.update();

        return this;
    }

    private List<Item> getItems(Point position) {
        List<Item> items = new ArrayList<Item>();
        List<Entity> entities = EntityManager.getInstance().item(position);
        for (Entity entity : entities){
            if (entity instanceof Item){
                items.add((Item)entity);
            }
        }
        return items;
    }

    @Override
    public Screen respondToMouseInput(Point translatedPoint) {
        return this;
    }

    @Override
    public Screen respondToMouseClick(Point translatedPoint, int mouseButton) {
        return this;
    }
}
