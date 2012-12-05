package com.bthorson.torule.worldgen;

import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.LocalBuilder;
import com.bthorson.torule.map.LocalType;
import com.bthorson.torule.map.World;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.BuildingType;
import com.bthorson.torule.town.Town;
import com.bthorson.torule.town.TownBuilder;
import com.bthorson.torule.town.WealthLevel;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.bthorson.torule.map.MapConstants.*;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 12:39 AM
 */
public class WorldGenerator implements WorldLoader {


    private Point seCorner;
    private Local[][] locals;
    private int width;
    private int height;
    private Point playerHomeLocal;

    private List<Town> towns = new ArrayList<Town>();
    private List<Faction> factions = new ArrayList<Faction>();


    public void generateWorld(WorldGenParams params){

        EntityManager.destroy();
        createMap(params);
        World.destroy();
        World.getInstance().startWorld(this);
        populateWorld(params);


    }

    private void setupFactions() {
        Faction aggressiveAnimalFaction = new Faction("aggressiveAnimal");
        Faction passiveAnimalFaction = new Faction("passiveAnimal");
        Faction goblinFaction = new Faction("goblin");
        factions.add(aggressiveAnimalFaction);
        factions.add(passiveAnimalFaction);
        factions.add(goblinFaction);

        for (Town town : towns){
            Faction faction = town.getFaction();
            aggressiveAnimalFaction.addEnemyFaction(town.getFaction());
            town.getFaction().addEnemyFaction(aggressiveAnimalFaction);
            town.getFaction().addEnemyFaction(goblinFaction);
            goblinFaction.addEnemyFaction(town.getFaction());
            factions.add(faction);
        }
        aggressiveAnimalFaction.addEnemyFaction(goblinFaction);
        goblinFaction.addEnemyFaction(aggressiveAnimalFaction);
    }


    private void populateWorld(WorldGenParams params) {CreatureGenerator generator = new CreatureGenerator();
        setupFactions();
        EntityManager.getInstance().setFactions(factions);
        for (Town town : towns){

            EntityManager.getInstance().addTown(town.getRegionalPosition(), town);

            if (town.getRegionalPosition().equals(playerHomeLocal)){
                generator.createPlayer(params.getPlayerName(), town);
            }

            for (Building building : town.getBuildings()){
                if (BuildingType.isShop(building.getBuildingType())){
                    generator.createShopOwners(town, building);
                }
            }
            generator.makeTownsmen(town, 20);
            generator.makePuppies(town, 10);
        }

        generator.createHostileMobs();
    }

    private void createMap(WorldGenParams params) {
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
    }

    private void initWorld(WorldGenParams params) {
        Set<Point> townPoints = new HashSet<Point>();

        outer:
        for (int i = 0; townPoints.size() < params.getNumCities() && i < 10000; i++) {

            Point candidate = PointUtil.randomPoint(World.NW_CORNER, seCorner.divide(LOCAL_SIZE_POINT));

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
            town.getLocal().setType(LocalType.TOWN);
            towns.add(town);
        }

        getMSTPath(townPoints);
    }

    private Local getLocal(Point localGridPosition) {
        return locals[localGridPosition.x()][localGridPosition.y()];
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

    @Override
    public Local[][] getLocals() {
        return locals;
    }

    @Override
    public long getTurnCounter() {
        return 0L;
    }

    @Override
    public int worldWidth() {
        return width;
    }

    @Override
    public int worldHeight() {
        return height;
    }
}
