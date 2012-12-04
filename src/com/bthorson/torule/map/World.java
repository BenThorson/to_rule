package com.bthorson.torule.map;

import com.bthorson.torule.entity.*;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Town;
import com.bthorson.torule.town.TownBuilder;
import com.bthorson.torule.town.WealthLevel;
import com.bthorson.torule.worldgen.WorldGenParams;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import static com.bthorson.torule.map.MapConstants.*;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:07 PM
 */
public class World {

    private long turnCounter;

    public static final Point NW_CORNER = PointUtil.POINT_ORIGIN;
    private Point seCorner;


    private Local[][] locals;

    private Player player;
    private Point playerHomeLocal;

    private List<Point> openDoors = new ArrayList<Point>();

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
        EntityManager.destroy();
    }

    public void loadWorld(WorldGenParams params){

        width = params.getWorldSize().x();
        height = params.getWorldSize().y();
        locals = new Local[params.getWorldSize().x()/LOCAL_SIZE_X][params.getWorldSize().y()/LOCAL_SIZE_Y];
        Random random = new Random();
        for (int x = 0; x < params.getWorldSize().x()/LOCAL_SIZE_X; x++){
            for (int y = 0; y < params.getWorldSize().y()/LOCAL_SIZE_Y; y++){
                locals[x][y] = new LocalBuilder(random.nextInt(5), random.nextInt(5), random.nextInt(5))
                        .makeGrassland().build(new Point(x * LOCAL_SIZE_X, y * LOCAL_SIZE_Y));
                locals[x][y].setType(LocalType.WILDERNESS);
            }
        }
        this.seCorner = params.getWorldSize();

        initWorld(params);
        EntityManager.getInstance().setupFactions();
        placeHostileMobs();
        createPlayer(params.getPlayerName());
    }

    private void initWorld(WorldGenParams params) {
        Set<Point> townPoints = new HashSet<Point>();

        outer:
        for (int i = 0; townPoints.size() < params.getNumCities() && i < 10000; i++) {

            Point candidate = PointUtil.randomPoint(World.NW_CORNER, seCorner().divide(LOCAL_SIZE_POINT));

            if (townPoints.isEmpty()) {
                townPoints.add(candidate);
                playerHomeLocal = candidate;
                continue;
            }

            for (Point exist : townPoints) {
                if (PointUtil.manhattanDist(exist, candidate) < 3) {
                    continue outer;
                }
            }
            townPoints.add(candidate);

        }

        for (Point city : townPoints) {
            Random random = new Random();
            int dink = random.nextInt(3);
            WealthLevel level = null;
            switch (dink){
                case 0:
                    level = WealthLevel.POOR;
                    break;
                case 1:
                    level = WealthLevel.AVERAGE;
                    break;
                case 2:
                    level = WealthLevel.PROSPERING;
                    break;
            }
            Town town = TownBuilder.buildPredefinedTown(getLocal(city), city, city.equals(playerHomeLocal) ? WealthLevel.POOR : level);
            EntityManager.getInstance().addTown(city, town);

            town.getLocal().setType(LocalType.TOWN);
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
        TownBuilder builder = new TownBuilder(local, WealthLevel.POOR);
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

    private void placeHostileMobs(){
        Point localAmounts = seCorner().divide(LOCAL_SIZE_POINT);
        for (int x = 0; x < localAmounts.x(); x++){
            for (int y = 0; y < localAmounts.y(); y++) {
                Point local = new Point(x,y);
                if (LocalType.WILDERNESS.equals(getLocal(local).getType())) {
                    for (int i = 0; i < 20; i++){
                        Point transformedLocal = local.multiply(LOCAL_SIZE_POINT);
                        Point candidate = transformedLocal.add(PointUtil.randomPoint(LOCAL_SIZE_POINT));
                        if (!isOccupied(candidate)){
                            Creature wolf = CreatureFactory.INSTANCE.createCreature("wolf", candidate);
                            wolf.setAi(new WanderAI(wolf, transformedLocal, transformedLocal.add(LOCAL_SIZE_POINT)));
                            wolf.setFaction(EntityManager.getInstance().getAggressiveAnimalFaction());
                        }  else {
                            i--;
                        }
                    }
                }
            }
        }
    }

    public Local getLocal(Point localGridPosition) {
        return locals[localGridPosition.x()][localGridPosition.y()];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }


    public Tile tile(Point tilePoint){
        return locals[tilePoint.x()/LOCAL_SIZE_X][tilePoint.y()/LOCAL_SIZE_Y].tile(tilePoint.x() % LOCAL_SIZE_X, tilePoint.y() % LOCAL_SIZE_Y);
    }

    public Creature creature(Point position){
        return EntityManager.getInstance().creatureAt(position);
    }

    public List<PhysicalEntity> items(Point position){
        return EntityManager.getInstance().item(position);
    }

    public void createPlayer(String playerName){
        Town town = EntityManager.getInstance().town(playerHomeLocal);
        Point placement = town.getRegionalPosition().multiply(new Point(MapConstants.LOCAL_SIZE_X, MapConstants.LOCAL_SIZE_Y));
        player = (Player)CreatureFactory.INSTANCE.createCreature("player", placement.add(new Point(50, 50)));
        player.setFaction(town.getFaction());
        player.setName(playerName);
        EntityManager.getInstance().setPlayer(player);
    }

    public void update() {
        turnCounter++;
        EntityManager.getInstance().update();
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

    public Player getPlayer() {
        return player;
    }

    public boolean isTravelable(Point point) {
        return point.x() >= 0 && point.x() < width() && point.y() >= 0
                && point.y() < height()
                && tile(point).passable();
    }

    public Point topLeft() {
        return PointUtil.POINT_ORIGIN;
    }

    public Point bottomRight() {
        return new Point(width(), height());
    }

    public void serialize(PrintWriter writer){
        writer.println("numberOfChunks " + locals.length + ";" + locals[0].length);
        for (int x = 0; x < locals.length; x++){
            for (int y = 0; y < locals[0].length; y++){
                writer.println("L " + x + ";" + y);
                locals[x][y].serialize(writer);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        WorldGenParams params = new WorldGenParams();
        params.setNumCities(5);
        params.setWorldSize(new Point(1000,1000));
        params.setPlayerName("Bob");
        World.getInstance().loadWorld(params);
        PrintWriter writer = new PrintWriter("testWorld.wo");
        World.getInstance().serialize(writer);
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

    public long getTurnCounter() {
        return turnCounter;
    }

    public void skipTurns(int turnsSkipped) {
        for (int i = 0; i < turnsSkipped; i++){
            if (player.getHitpoints() < player.getMaxHitpoints()) {
                update();
            } else {
                return;
            }
        }
    }
}
