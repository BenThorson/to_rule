package com.bthorson.torule.map;

import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:10 PM
 */
public class Region {

    private Local[][] locals;
    private List<Point> cities;

    public Region(int worldX, int worldY) {
        cities = new ArrayList<Point>();
        cities.add(new Point(0, 0));
        cities.add(new Point(2, 1));
        locals = new Local[10][10];


        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                locals[x][y] = new LocalBuilder().makeGrassland().build(worldX, worldY, x, y);
            }
        }
        for (Point city : cities) {
            buildTown(city.x(), city.y());
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
        TownBuilder builder = new TownBuilder(locals[cursor.x()][cursor.y()]);
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

    private void buildTown(int localX, int localY) {
        new TownBuilder(locals[localX][localY])
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
                .buildBuilding(70, 70, 28, 28, Direction.NORTH);
    }


    public Tile tile(int x, int y) {
        return locals[x / 100][y / 100].tile(x % 100, y % 100);

    }

    public void serialize(PrintWriter writer) {
        writer.println("\tRegion metadata:  Dimension width:" + locals.length + " height: " + locals[0].length);
        for (int x = 0; x < locals.length; x++) {
            for (int y = 0; y < locals[0].length; y++) {
                writer.println("\tL x:" + x + " y:" + y + ";");
                locals[x][y].serialize(writer);
            }
        }
    }
}
