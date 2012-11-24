package com.bthorson.torule.map;

import com.bthorson.torule.entity.*;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:07 PM
 */
public class World {

    public static final Point NW_CORNER = new Point(0,0);
    private Point seCorner;


    private Region[][] regions;

    List<Entity> items = new ArrayList<Entity>();
    List<Creature> toRemove = new ArrayList<Creature>();
    private Creature player;

    private List<Point> cities;

    private static World instance;

    private World(){}

    public static World getInstance(){
        if (instance == null){
            instance = new World();
        }
        return instance;
    }

    public void loadWorld(Point worldSize){
        regions = new Region[worldSize.x()/1000][worldSize.y()/1000];
        for (int x = 0; x < worldSize.x()/1000; x++){
            for (int y = 0; y < worldSize.y()/1000; y++){
                regions[x][y] = new Region(x,y);
            }
        }
        this.seCorner = worldSize;

        initWorld();
        populateSomeCreatures();
    }

    private void initWorld() {
        cities = new ArrayList<Point>();
        cities.add(new Point(0, 0));
        cities.add(new Point(2, 1));


        for (Point city : cities) {
            buildTown(getLocal(city));
        }

        connectCities(cities.get(0), cities.get(1));

    }

    private void connectCities(Point point, Point point1) {
        Point cursor = new Point(point);
        Direction cursorDirection = null;
        Direction lastDirection = null;
        while (!cursor.equals(point1)) {
            lastDirection = cursorDirection;

            if (cursor.x() > point1.x()) {
                cursorDirection = Direction.WEST;
            } else if (cursor.x() < point1.x()) {
                cursorDirection = Direction.EAST;
            } else if (cursor.y() > point1.y()) {
                cursorDirection = Direction.NORTH;
            } else if (cursor.y() < point1.y()) {
                cursorDirection = Direction.SOUTH;
            }

            buildConnectingRoad(lastDirection, cursorDirection, cursor);
            cursor = cursor.add(cursorDirection.point());
        }
        lastDirection = cursorDirection;
        cursorDirection = null;
        buildConnectingRoad(lastDirection, cursorDirection, cursor);
    }

    private void buildConnectingRoad(Direction lastDirection, Direction cursorDirection, Point cursor) {
        Local local = getLocal(cursor);
        TownBuilder builder = new TownBuilder(local);
        builder.buildRoad(4, Local.WIDTH / 2 - 2, Local.HEIGHT / 2, Local.WIDTH / 2 + 2, Local.HEIGHT);

        if (lastDirection != null){
            switch (lastDirection) {
                case NORTH:
                    builder.buildRoad(4, Local.WIDTH / 2, Local.HEIGHT / 2, Local.WIDTH / 2, Local.HEIGHT);
                    break;
                case SOUTH:
                    builder.buildRoad(4, Local.WIDTH / 2, 0, Local.WIDTH / 2, Local.HEIGHT / 2);
                    break;
                case EAST:
                    builder.buildRoad(4, 0, Local.HEIGHT / 2, Local.WIDTH / 2, Local.HEIGHT / 2);
                    break;
                case WEST:
                    builder.buildRoad(4, Local.WIDTH / 2, Local.HEIGHT / 2, Local.WIDTH, Local.HEIGHT / 2);
                    break;

            }
        }

        if (cursorDirection != null){
            switch (cursorDirection) {
                case NORTH:
                    builder.buildRoad(4, Local.WIDTH / 2, 0, Local.WIDTH / 2, Local.HEIGHT / 2);
                    break;
                case SOUTH:
                    builder.buildRoad(4, Local.WIDTH / 2, Local.HEIGHT / 2, Local.WIDTH / 2, Local.HEIGHT);
                    break;
                case EAST:
                    builder.buildRoad(4, Local.WIDTH / 2, Local.HEIGHT / 2, Local.WIDTH, Local.HEIGHT / 2);
                    break;
                case WEST:
                    builder.buildRoad(4, 0, Local.HEIGHT / 2, Local.WIDTH / 2, Local.HEIGHT / 2);
                    break;

            }
        }


    }

    private Local getLocal(Point LocalGridPosition) {
        return regions[LocalGridPosition.x()/10][LocalGridPosition.y()/10].getLocal(LocalGridPosition.x() % 10, LocalGridPosition.y() % 10);
    }


    private void buildTown(Local local) {
        new TownBuilder(local)
                .buildTownSquare(26)
                .buildRoad(4, 0, Local.HEIGHT / 2, Local.WIDTH, Local.HEIGHT / 2)
                .buildRoad(4, Local.WIDTH / 2, 0, Local.WIDTH / 2, Local.HEIGHT)
                .buildRoad(3, 84, 50, 84, 70)
                .buildRoad(3, 16, 16, 16, 85)
                .buildRoad(3, 8, 16, 86, 16)
                .buildRoad(3, 16, 83, 50, 83)
                .buildRoad(3, 84, 16, 84, 50)
                .buildWall(0, 0, 99, 99)

                .buildBuilding(8, 18, 6, 6, Direction.EAST)
                .buildBuilding(8, 25, 6, 6, Direction.EAST)
                .buildBuilding(8, 34, 6, 6, Direction.EAST)
                .buildBuilding(8, 41, 6, 6, Direction.EAST)
                .buildBuilding(8, 52, 6, 6, Direction.EAST)
                .buildBuilding(8, 59, 6, 6, Direction.EAST)
                .buildBuilding(8, 68, 6, 6, Direction.EAST)
                .buildBuilding(8, 75, 6, 6, Direction.EAST)


                .buildBuilding(18, 18, 6, 6, Direction.WEST)
                .buildBuilding(18, 25, 6, 6, Direction.WEST)
                .buildBuilding(18, 34, 6, 6, Direction.WEST)
                .buildBuilding(18, 41, 6, 6, Direction.WEST)
                .buildBuilding(18, 52, 6, 6, Direction.WEST)
                .buildBuilding(18, 59, 6, 6, Direction.WEST)
                .buildBuilding(18, 68, 6, 6, Direction.WEST)
                .buildBuilding(18, 75, 6, 6, Direction.WEST)

                .buildBuilding(8, 8, 6, 6, Direction.SOUTH)
                .buildBuilding(18, 8, 6, 6, Direction.SOUTH)
                .buildBuilding(25, 8, 6, 6, Direction.SOUTH)
                .buildBuilding(34, 8, 6, 6, Direction.SOUTH)
                .buildBuilding(41, 8, 6, 6, Direction.SOUTH)

                .buildBuilding(25, 18, 6, 6, Direction.NORTH)
                .buildBuilding(34, 18, 6, 6, Direction.NORTH)
                .buildBuilding(41, 18, 6, 6, Direction.NORTH)

                .buildBuilding(25, 75, 6, 6, Direction.SOUTH)
                .buildBuilding(34, 75, 6, 6, Direction.SOUTH)
                .buildBuilding(41, 75, 6, 6, Direction.SOUTH)

                .buildBuilding(8, 85, 6, 6, Direction.NORTH)
                .buildBuilding(18, 85, 6, 6, Direction.NORTH)
                .buildBuilding(25, 85, 6, 6, Direction.NORTH)
                .buildBuilding(34, 85, 6, 6, Direction.NORTH)
                .buildBuilding(41, 85, 6, 6, Direction.NORTH)


                .buildBuilding(28, 38, 8, 8, Direction.EAST)
                .buildBuilding(28, 53, 8, 8, Direction.EAST)
                .buildBuilding(38, 28, 8, 8, Direction.SOUTH)
                .buildBuilding(53, 28, 8, 8, Direction.SOUTH)
                .buildBuilding(63, 38, 8, 8, Direction.WEST)
                .buildBuilding(63, 53, 8, 8, Direction.WEST)
                .buildBuilding(38, 63, 8, 8, Direction.NORTH)
                .buildBuilding(53, 63, 8, 8, Direction.NORTH)
                .buildBuilding(70, 70, 28, 28, Direction.NORTH)
                .makeTownsmen(20);
    }


    public int width() {
        return 1000;
    }

    public int height() {
        return 1000;
    }


    public Tile tile(Point tilePoint){
        return regions[tilePoint.x()/1000][tilePoint.y()/1000].tile(tilePoint.x() % 1000, tilePoint.y() % 1000);
    }

    public Creature creature(Point position){
        return EntityManager.getInstance().creatureAt(position);
    }

    public void populateSomeCreatures(){
        Faction human = new Faction("Human");
        Faction goblin = new Faction("Goblin");
        human.addEnemyFaction(goblin);
        goblin.addEnemyFaction(human);
        player = CreatureFactory.buildPlayer(this, new Point(50, 50));
        player.setFaction(human);
        EntityManager.getInstance().setPlayer(player);
        EntityManager.getInstance().addCreature(player);

//        Creature gobLeader = CreatureFactory.buildGoblinLeader(this, new Point(30, 32));
//        gobLeader.setFaction(goblin);
//        gobLeader.setAi(new GroupFollowAI(gobLeader));
//        List<Creature> group = new ArrayList<Creature>();
//        List<Creature> gobGroup = new ArrayList<Creature>();
//        gobGroup.add(gobLeader);
//
//        for (int i = 0; i < 14; i++) {
//            Creature villy = CreatureFactory.buildSoldier(this, new Point(30 + i, 21));
//            villy.setFaction(human);
//            villy.setLeader(player);
//            group.add(villy);
//            villy.setAi(new GroupFollowAI(villy));
//            Creature gobby = CreatureFactory.buildGoblin(this, new Point(30 + i, 31));
//            gobby.setFaction(goblin);
//            gobby.setAi(new GroupFollowAI(gobby));
//            gobGroup.add(gobby);
//        }
//        Group grp = new Group(group, player);
//        player.setGroup(grp);
//        EntityManager.getInstance().addGroup(grp);
//
//        Group gobGrp = new Group(gobGroup, gobLeader);
//        EntityManager.getInstance().addGroup(gobGrp);
    }

    public void update() {
        EntityManager.getInstance().update();
        for (Creature dead : toRemove){
            EntityManager.getInstance().remove(dead);
        }
        toRemove.clear();
    }

    public Creature getPlayer() {
        return player;
    }

    public List<Creature> getCreaturesInRange(Point p1, Point p2) {
        return EntityManager.getInstance().getCreaturesInRange(p1, p2);
    }

    public void creatureDead(Creature creature){

        toRemove.add(creature);
        items.add(new Corpse(this, creature));
    }

    public Entity item(Point itemPos) {
        for (Entity item : items){
            if (item.position().equals(itemPos)){
                return item;
            }
        }
        return null;
    }

    public boolean isTravelable(Point point) {
        return point.x() >= 0 && point.x() < width() && point.y() >= 0
                && point.y() < height()
                && tile(point).passable();
    }

    public Point topLeft() {
        return new Point(0,0);
    }

    public Point bottomRight() {
        return new Point(width(), height());
    }

    public void serialize(PrintWriter writer){
        writer.println("World file metadata:  Dimension width:" + regions.length + " height: " + regions[0].length);
        for (int x = 0; x < regions.length; x++){
            for (int y = 0; y < regions[0].length; y++){
                writer.println("R x:" + x + " y:" + y + ";");
                regions[x][y].serialize(writer);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        World world = new World();
        PrintWriter writer = new PrintWriter("testWorld.wo");
        world.serialize(writer);
    }

    public boolean isOccupied(Point candidate) {
        return !tile(candidate).passable() || creature(candidate) != null;
    }

    public Point seCorner() {
        return seCorner;
    }
}
