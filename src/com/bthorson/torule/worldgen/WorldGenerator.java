package com.bthorson.torule.worldgen;

import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.*;
import com.bthorson.torule.town.*;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

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

        System.out.println("attempting to destroy the entity manager");
        EntityManager.destroy();
        System.out.println("building new map");
        createMap(params);
        System.out.println("attempting to destroy current world");
        World.destroy();
        System.out.println("loading new map into new world");
        World.getInstance().startWorld(this);
        System.out.println("creating creatures for world");
        populateWorld(params);
        System.out.println("starting the entity manager");
        EntityManager.getInstance().start();


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

            generator.createMayor(town);
            generator.createMilitia(town);

            for (Building building : town.getBuildings()){
                if (BuildingType.isShop(building.getBuildingType())){
                    generator.createShopOwners(town, building);
                }
            }
            generator.makeTownsmen(town, 20);
            generator.makePuppies(town, 10);
        }

        generator.spawnCreaturesByFerocity();
    }

    private void createMap(WorldGenParams params) {
        width = params.getWorldSize().x();
        height = params.getWorldSize().y();

        locals = new Local[params.getWorldSize().x()/LOCAL_SIZE_X][params.getWorldSize().y()/LOCAL_SIZE_Y];
        Random random = new Random();
        for (int x = 0; x < params.getWorldSize().x()/LOCAL_SIZE_X; x++){
            for (int y = 0; y < params.getWorldSize().y()/LOCAL_SIZE_Y; y++){
                locals[x][y] = new LocalBuilder(random.nextInt(5), random.nextInt(33), random.nextInt(15))
                        .makeGrassland().build(new Point(x * LOCAL_SIZE_X, y * LOCAL_SIZE_Y));
                locals[x][y].setType(LocalType.WILDERNESS);
            }
        }
        this.seCorner = params.getWorldSize();

        buildTowns(params);
        new GoblinCamp().build(locals[0][0]);
    }

    private void buildTowns(WorldGenParams params) {
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
        assignFerocityValues();
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
        
        for (Town town : towns){
            determineGates(town);
        }
    }

    private void determineGates(Town town) {
        
        Map<Direction, Point> directions = new EnumMap<Direction, Point>(Direction.class);
        Point westGate = new Point(0, LOCAL_SIZE_Y/2);
        Point eastGate = new Point(LOCAL_SIZE_X - 1, LOCAL_SIZE_Y/2);
        Point northGate = new Point(LOCAL_SIZE_X / 2, 0);
        Point southGate = new Point(LOCAL_SIZE_X / 2, LOCAL_SIZE_Y - 1);
        Local local = locals[town.getRegionalPosition().x()][town.getRegionalPosition().y()];
        if(local.getTiles()[westGate.x()][westGate.y()].equals(Tile.ROAD)){
            directions.put(Direction.WEST, westGate);
        }
        if(local.getTiles()[eastGate.x()][eastGate.y()].equals(Tile.ROAD)){
            directions.put(Direction.EAST, eastGate);
        }
        if(local.getTiles()[northGate.x()][northGate.y()].equals(Tile.ROAD)){
            directions.put(Direction.NORTH, northGate);
        }
        if(local.getTiles()[southGate.x()][southGate.y()].equals(Tile.ROAD)){
            directions.put(Direction.SOUTH, southGate);
        }
        town.setGates(directions);
        
    }

    private void buildConnectingRoad(Direction lastDirection, Direction cursorDirection, Point cursor) {
        Local local = getLocal(cursor);
        TownBuilder builder = new TownBuilder(local, WealthLevel.POOR);
        if (!LocalType.TOWN.equals(local.getType())){
            local.setType(LocalType.ROAD);
        }
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

    public void assignFerocityValues(){
        List<Local> toEvaluate = new ArrayList<Local>();
        Random random = new Random();
        for (Local[] row : locals){
            for (Local col : row) {
                determineDistanceScore(toEvaluate, random, col);
            }
        }

        Collections.sort(toEvaluate, new Comparator<Local>() {
            @Override
            public int compare(Local o1, Local o2) {
                return o1.getDistanceFromTown() - o2.getDistanceFromTown();
            }
        });

        doAssignmentOfFerocity(toEvaluate);
    }

    private void doAssignmentOfFerocity(List<Local> toEvaluate) {
        int divisor = toEvaluate.size() / Ferocity.values().length;

        for (int i = 0; i < divisor; i++){
            toEvaluate.get(i).setFerocity(Ferocity.CIVILIZED);
        }
        for (int i = divisor; i < divisor * 2; i++){
            toEvaluate.get(i).setFerocity(Ferocity.TAME);
        }
        for (int i = divisor * 2; i < divisor * 3; i++){
            toEvaluate.get(i).setFerocity(Ferocity.NEUTRAL);
        }
        for (int i = divisor * 3; i < divisor * 4; i++){
            toEvaluate.get(i).setFerocity(Ferocity.WILD);
        }
        for (int i = divisor * 4; i < divisor * 5; i++){
            toEvaluate.get(i).setFerocity(Ferocity.DANGEROUS);
        }
        for (int i = divisor * 5; i < toEvaluate.size(); i++){
            toEvaluate.get(i).setFerocity(Ferocity.EVIL);
        }
    }

    private void determineDistanceScore(List<Local> toEvaluate, Random random, Local col) {
        col.setDistanceFromTown(getTownDistance(col.getNwBoundWorldCoord().divide(LOCAL_SIZE_POINT)));
        if (col.getDistanceFromTown() == 0){
            col.setFerocity(Ferocity.CIVILIZED);
            return;
        } else {
            toEvaluate.add(col);
        }

        int variance = 0;
        int varianceChance = random.nextInt(10);
        switch (varianceChance){
            case 0:
                variance = -3; break;
            case 1:
                variance = -2; break;
            case 2:
                variance = -1; break;
            case 3:
            case 4:
            case 5:
            case 6:
                break;
            case 7:
                variance = 1; break;
            case 8:
                variance = 2; break;
            case 9:
                variance = 3;

        }
        col.setDistanceFromTown(col.getDistanceFromTown() - variance);
    }

    private int getTownDistance(Point local) {
        int min = Integer.MAX_VALUE;
        for (Town town : towns){
            int dist = PointUtil.manhattanDist(town.getRegionalPosition(), local);
            if (dist < min){
                min = dist;
            }
        }
        return min;
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
