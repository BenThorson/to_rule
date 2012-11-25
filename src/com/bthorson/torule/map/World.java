package com.bthorson.torule.map;

import com.bthorson.torule.entity.*;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.town.Town;
import com.bthorson.torule.town.TownBuilder;
import com.bthorson.torule.worldgen.WorldGenParams;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.bthorson.torule.map.MapConstants.*;

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

    private List<Point> openDoors = new ArrayList<Point>();

    private List<Town> towns = new ArrayList<Town>();

    private static World instance;

    private int width;
    private int height;

    private World(){}

    public static World getInstance(){
        if (instance == null){
            instance = new World();
        }
        return instance;
    }


    public static void destroy() {
        instance = null;
    }

    public void loadWorld(WorldGenParams params){

        width = params.getWorldSize().x();
        height = params.getWorldSize().y();
        regions = new Region[params.getWorldSize().x()/REGION_SIZE_X][params.getWorldSize().y()/REGION_SIZE_Y];
        for (int x = 0; x < params.getWorldSize().x()/REGION_SIZE_X; x++){
            for (int y = 0; y < params.getWorldSize().y()/REGION_SIZE_Y; y++){
                regions[x][y] = new Region(x,y);
            }
        }
        this.seCorner = params.getWorldSize();

        initWorld(params);
        createPlayer(params.getPlayerName());
    }

    private void initWorld(WorldGenParams params) {
        Set<Point> townPoints = new HashSet<Point>();
        for (; townPoints.size() < params.getNumCities();){
            townPoints.add(PointUtil.randomPoint(World.NW_CORNER, seCorner.divide(LOCAL_SIZE_POINT)));
        }



        for (Point city : townPoints) {
            towns.add(TownBuilder.buildPredefinedTown(getLocal(city)));
        }

        getMSTPath(townPoints);
    }

    private void getMSTPath(Set<Point> townPoints) {
        SimpleWeightedGraph<Point, DefaultWeightedEdge> g = new SimpleWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        for (Point p : townPoints){
            g.addVertex(p);
        }
        for (Point p : townPoints){

            for (Point p2 : townPoints){
                if (p.equals(p2)){
                    continue;
                }
                DefaultWeightedEdge edge = g.addEdge(p, p2);
                if (edge != null){
                    g.setEdgeWeight(edge, (double)PointUtil.manhattanDist(p, p2));
                }
            }
        }

        KruskalMinimumSpanningTree<Point, DefaultWeightedEdge> mstAlg = new KruskalMinimumSpanningTree<Point, DefaultWeightedEdge>(g);
        Set<DefaultWeightedEdge> edges = mstAlg.getEdgeSet();
        for (DefaultWeightedEdge edge : edges){
            connectCities(g.getEdgeSource(edge), g.getEdgeTarget(edge));
        }


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
        builder.buildRoad(4, LOCAL_SIZE_X / 2 - 2, LOCAL_SIZE_Y / 2, LOCAL_SIZE_X / 2 + 2, LOCAL_SIZE_Y);

        if (lastDirection != null){
            switch (lastDirection) {
                case NORTH:
                    builder.buildRoad(4, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y / 2, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y);
                    break;
                case SOUTH:
                    builder.buildRoad(4, LOCAL_SIZE_X / 2, 0, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y / 2);
                    break;
                case EAST:
                    builder.buildRoad(4, 0, LOCAL_SIZE_Y / 2, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y / 2);
                    break;
                case WEST:
                    builder.buildRoad(4, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y / 2, LOCAL_SIZE_X, LOCAL_SIZE_Y / 2);
                    break;

            }
        }

        if (cursorDirection != null){
            switch (cursorDirection) {
                case NORTH:
                    builder.buildRoad(4, LOCAL_SIZE_X / 2, 0, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y / 2);
                    break;
                case SOUTH:
                    builder.buildRoad(4, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y / 2, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y);
                    break;
                case EAST:
                    builder.buildRoad(4, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y / 2, LOCAL_SIZE_X, LOCAL_SIZE_Y / 2);
                    break;
                case WEST:
                    builder.buildRoad(4, 0, LOCAL_SIZE_Y / 2, LOCAL_SIZE_X / 2, LOCAL_SIZE_Y / 2);
                    break;

            }
        }


    }

    private Local getLocal(Point LocalGridPosition) {
        return regions[LocalGridPosition.x()/REGION_X_IN_LOCALS][LocalGridPosition.y()/REGION_Y_IN_LOCALS]
                .getLocal(LocalGridPosition.x() % REGION_X_IN_LOCALS, LocalGridPosition.y() % REGION_Y_IN_LOCALS);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }


    public Tile tile(Point tilePoint){
        return regions[tilePoint.x()/REGION_SIZE_X][tilePoint.y()/REGION_SIZE_Y].tile(tilePoint.x() % REGION_SIZE_X, tilePoint.y() % REGION_SIZE_Y);
    }

    public Creature creature(Point position){
        return EntityManager.getInstance().creatureAt(position);
    }

    public void createPlayer(String playerName){
        Faction human = new Faction("Human");
        Faction goblin = new Faction("Goblin");
        human.addEnemyFaction(goblin);
        goblin.addEnemyFaction(human);
        player = CreatureFactory.buildPlayer(this, new Point(50, 50));
        player.setFaction(human);
        player.setName(playerName);
        EntityManager.getInstance().setPlayer(player);
        EntityManager.getInstance().addCreature(player);
    }

    public void update() {
        EntityManager.getInstance().update();
        for (Creature dead : toRemove){
            EntityManager.getInstance().remove(dead);
        }
        toRemove.clear();
        List<Point> updated = new ArrayList<Point>();
        for (Point openDoor : openDoors){
            if (!isOccupied(openDoor)){
                closeDoor(openDoor);
            } else {
                updated.add(openDoor);
            }
        }
        openDoors = updated;
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

    public void openDoor(Point position){
        Local local = getLocal(position.divide(LOCAL_SIZE_POINT));
        Point offset = position.subtract(local.getNwBoundWorldCoord());
        local.getTiles()[offset.x()][offset.y()] = Tile.OPEN_DOOR;
        openDoors.add(position);
    }

    private void closeDoor(Point position){
        Local local = getLocal(position.divide(LOCAL_SIZE_POINT));
        Point offset = position.subtract(local.getNwBoundWorldCoord());
        local.getTiles()[offset.x()][offset.y()] = Tile.DOOR;
    }
}
