package com.bthorson.torule.screens;

import com.bthorson.torule.debug.DebugUtil;
import com.bthorson.torule.entity.Corpse;
import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.PhysicalEntity;
import com.bthorson.torule.entity.ai.pathing.AStarPathTo;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.graphics.asciiPanel.AsciiPanel;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Screen in which the game is played.  It handles the rendering of the game viewport and the commands therein
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
        this.player = EntityManager.getInstance().getPlayer();
        this.statusScreen = new StatusScreen(xBorder, yBorder);
        this.messageScreen = new MessageScreen(world, yBorder);
    }

    /**
     * Returns the difference between the origin(0,0) of the world, and the origin of the play screen
     * @return {@link Point} representing the offset to transform world coodrinates to screen coordinates
     */
    public Point getOffset() {
        return new Point(Math.max(0, Math.min(player.position().x() - xBorder / 2,
                                    world.width() - xBorder)),
                         Math.max(0, Math.min(player.position().y() - yBorder / 2,
                                    world.height() - yBorder)));
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

                    List<PhysicalEntity> items = EntityManager.getInstance().item(worldPoint);
                    if (items.size() > 0){
                        PhysicalEntity item = items.get(0);
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
        if (player.dead()){
            return new DeadScreen(this);
        }
        switch (key.getKeyCode()){
            case KeyEvent.VK_Q: return new SaveScreen(this);
            case KeyEvent.VK_J:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_NUMPAD4: player.move(Direction.WEST.point()); break;
            case KeyEvent.VK_L:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6: player.move(Direction.EAST.point()); break;
            case KeyEvent.VK_I:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_NUMPAD8: player.move(Direction.NORTH.point()); break;
            case KeyEvent.VK_COMMA:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_NUMPAD2: player.move(Direction.SOUTH.point()); break;
            case KeyEvent.VK_U:
            case KeyEvent.VK_NUMPAD7: player.move(Direction.NORTH_WEST.point()); break;
            case KeyEvent.VK_O:
            case KeyEvent.VK_NUMPAD9: player.move(Direction.NORTH_EAST.point()); break;
            case KeyEvent.VK_M:
            case KeyEvent.VK_NUMPAD1: player.move(Direction.SOUTH_WEST.point()); break;
            case KeyEvent.VK_PERIOD:
            case KeyEvent.VK_NUMPAD3: player.move(Direction.SOUTH_EAST.point()); break;
            case KeyEvent.VK_K:
            case KeyEvent.VK_NUMPAD5: break;
            case KeyEvent.VK_R: return new InventoryManagementScreen(this, player);
//            case KeyEvent.VK_PERIOD: player.getGroup().rotateTest(); break;
            case KeyEvent.VK_B: return new FollowerListScreen(this);
            case KeyEvent.VK_F: return new FollowerCommandScreen(this, player.position().subtract(getOffset()));
            case KeyEvent.VK_T: return new ConversationScreen(this, player.position().subtract(getOffset()));
            case KeyEvent.VK_H: World.getInstance().skipTurns(500); break;
            case KeyEvent.VK_V: return new ViewScreen(this, player.position().subtract(getOffset()));
            case KeyEvent.VK_G:
                List<Item> items = getItems(player.position());
                if (items.size() > 0){
                    return new LootScreen(this, items);
                }
                break;
            case KeyEvent.VK_W: return new QuestScreen(this);
            case KeyEvent.VK_8:
                DebugUtil.teleportPlayer();
                break;
            case KeyEvent.VK_9:
                DebugUtil.debugSpawnGoblin();
                break;
            case KeyEvent.VK_0:
                while (true){
                    Scanner scanner = new Scanner(System.in);
                    System.out.println(String.format("player position = %d, %d.  Enter position to path to", player.position().x(), player.position().y()));
                    final int p1 = scanner.nextInt();
                    final int p2 = scanner.nextInt();
                    if (!World.getInstance().isTravelable(new Point(p1, p2))){
                        System.out.println("not travelable");
                        continue;
                    }
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            new AStarPathTo().buildPath(player.position(), new Point(p1, p2), false);
                        }
                    }).start();

                }



//            case KeyEvent.VK_SEMICOLON:
//                player.addGold(5000);
            default:
                return this;
        }

        world.update();

        return this;
    }

    /**
     * Gets items to loot, should be moved to the {@link LootScreen} //todo do this someday
     * @param position coordinate of the loot
     * @return All items containing the loot
     */
    private List<Item> getItems(Point position) {
        List<Item> items = new ArrayList<Item>();
        List<PhysicalEntity> entities = EntityManager.getInstance().item(position);
        for (PhysicalEntity entity : entities){
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
