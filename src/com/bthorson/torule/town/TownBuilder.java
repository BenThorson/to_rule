package com.bthorson.torule.town;

import com.bthorson.torule.entity.CreatureFactory;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 11/10/12
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TownBuilder {
    private Local toBuildOn;
    private Town town;

    public TownBuilder(Local toBuildOn) {
        this.toBuildOn = toBuildOn;
        this.town = new Town();
    }

    public TownBuilder buildTownSquare(int size) {
        int startX = Local.WIDTH / 2 - (int) Math.floor((double) size / 2.0);
        int starty = Local.HEIGHT / 2 - (int) Math.floor((double) size / 2.0);
        int endX = Local.WIDTH / 2 + (int) Math.ceil((double) size / 2.0);
        int endY = Local.HEIGHT / 2 + (int) Math.ceil((double) size / 2.0);

        fillRect(startX, starty, endX - startX, endY - starty, Tile.ROAD);

        return this;
    }

    public TownBuilder buildWall(int x, int y, int w, int h) {
        toBuildOn.getTiles()[x][y] = Tile.WALL_NW;
        toBuildOn.getTiles()[x + w][y] = Tile.WALL_NE;
        toBuildOn.getTiles()[x][y + h] = Tile.WALL_SW;
        toBuildOn.getTiles()[x + w][y + h] = Tile.WALL_SE;
        for (int i = 1; i < w; i++) {
            toBuildOn.getTiles()[x + i][y] = Tile.WALL_HORIZ;
            toBuildOn.getTiles()[x + i][y + h] = Tile.WALL_HORIZ;
        }
        for (int i = 1; i < h; i++) {
            toBuildOn.getTiles()[x][y + i] = Tile.WALL_VERT;
            toBuildOn.getTiles()[x + w][y + i] = Tile.WALL_VERT;
        }
        return this;
    }

    public TownBuilder buildBuilding(int x, int y, int w, int h, Direction doorDir, BuildingType buildingType) {
        buildWall(x,y,w,h);
        fillRect(x + 1, y + 1, w - 1, h - 1, Tile.FLOOR);
        town.registerBuilding(new Building(new Point(x,y), new Point(x,y), buildingType));
        switch (doorDir) {
            case NORTH:
                toBuildOn.getTiles()[x + w / 2][y] = Tile.DOOR;
                break;
            case SOUTH:
                toBuildOn.getTiles()[x + w / 2][y + h] = Tile.DOOR;
                break;
            case WEST:
                toBuildOn.getTiles()[x][y + h / 2] = Tile.DOOR;
                break;
            case EAST:
                toBuildOn.getTiles()[x + w][y + h / 2] = Tile.DOOR;
                break;
        }
        return this;
    }

    private void fillRect(int x, int y, int w, int h, Tile tileType) {
        System.out.println("");
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                toBuildOn.getTiles()[x + i][y + j] = tileType;
            }
        }
    }

    public TownBuilder buildRoad(int width, int startX, int startY, int endX, int endY) {
        boolean isEastWest = startX - endX != 0;
        if (isEastWest) {
            fillRect(startX, startY - width / 2, endX - startX, width, Tile.ROAD);
        } else {
            fillRect(startX - width / 2, startY, width, endY - startY, Tile.ROAD);
        }
        return this;
    }

    public TownBuilder makeTownsmen(int numberOfTownsmen){
        for (int i = 0; i < numberOfTownsmen; i++){
            Point candidate = PointUtil.randomPoint(toBuildOn.getNwBoundWorldCoord(), toBuildOn.getSeBoundWorldBound());
            if (!World.getInstance().isOccupied(candidate)){
                CreatureFactory.buildVillager(World.getInstance(), candidate);

            } else {
                i--;
            }

        }
        return this;
    }

    public Town build(){
        return town;
    }

    public static Town buildPredefinedTown(Local local) {
        return new TownBuilder(local)
                .buildTownSquare(26)
                .buildRoad(4, 0, Local.HEIGHT / 2, Local.WIDTH, Local.HEIGHT / 2)
                .buildRoad(4, Local.WIDTH / 2, 0, Local.WIDTH / 2, Local.HEIGHT)
                .buildRoad(3, 84, 50, 84, 70)
                .buildRoad(3, 16, 16, 16, 85)
                .buildRoad(3, 8, 16, 86, 16)
                .buildRoad(3, 16, 83, 50, 83)
                .buildRoad(3, 84, 16, 84, 50)
                .buildWall(0, 0, 99, 99)

                .buildBuilding(8, 18, 6, 6, Direction.EAST, BuildingType.HOUSE)
                .buildBuilding(8, 25, 6, 6, Direction.EAST, BuildingType.HOUSE)
                .buildBuilding(8, 34, 6, 6, Direction.EAST, BuildingType.HOUSE)
                .buildBuilding(8, 41, 6, 6, Direction.EAST, BuildingType.HOUSE)
                .buildBuilding(8, 52, 6, 6, Direction.EAST, BuildingType.HOUSE)
                .buildBuilding(8, 59, 6, 6, Direction.EAST, BuildingType.HOUSE)
                .buildBuilding(8, 68, 6, 6, Direction.EAST, BuildingType.HOUSE)
                .buildBuilding(8, 75, 6, 6, Direction.EAST, BuildingType.HOUSE)


                .buildBuilding(18, 18, 6, 6, Direction.WEST, BuildingType.HOUSE)
                .buildBuilding(18, 25, 6, 6, Direction.WEST, BuildingType.HOUSE)
                .buildBuilding(18, 34, 6, 6, Direction.WEST, BuildingType.HOUSE)
                .buildBuilding(18, 41, 6, 6, Direction.WEST, BuildingType.HOUSE)
                .buildBuilding(18, 52, 6, 6, Direction.WEST, BuildingType.HOUSE)
                .buildBuilding(18, 59, 6, 6, Direction.WEST, BuildingType.HOUSE)
                .buildBuilding(18, 68, 6, 6, Direction.WEST, BuildingType.HOUSE)
                .buildBuilding(18, 75, 6, 6, Direction.WEST, BuildingType.HOUSE)

                .buildBuilding(8, 8, 6, 6, Direction.SOUTH , BuildingType.HOUSE)
                .buildBuilding(18, 8, 6, 6, Direction.SOUTH, BuildingType.HOUSE)
                .buildBuilding(25, 8, 6, 6, Direction.SOUTH, BuildingType.HOUSE)
                .buildBuilding(34, 8, 6, 6, Direction.SOUTH, BuildingType.HOUSE)
                .buildBuilding(41, 8, 6, 6, Direction.SOUTH, BuildingType.HOUSE)

                .buildBuilding(25, 18, 6, 6, Direction.NORTH, BuildingType.HOUSE)
                .buildBuilding(34, 18, 6, 6, Direction.NORTH, BuildingType.HOUSE)
                .buildBuilding(41, 18, 6, 6, Direction.NORTH, BuildingType.HOUSE)

                .buildBuilding(25, 75, 6, 6, Direction.SOUTH, BuildingType.HOUSE)
                .buildBuilding(34, 75, 6, 6, Direction.SOUTH, BuildingType.HOUSE)
                .buildBuilding(41, 75, 6, 6, Direction.SOUTH, BuildingType.HOUSE)

                .buildBuilding(8, 85, 6, 6, Direction.NORTH , BuildingType.HOUSE)
                .buildBuilding(18, 85, 6, 6, Direction.NORTH, BuildingType.HOUSE)
                .buildBuilding(25, 85, 6, 6, Direction.NORTH, BuildingType.HOUSE)
                .buildBuilding(34, 85, 6, 6, Direction.NORTH, BuildingType.HOUSE)
                .buildBuilding(41, 85, 6, 6, Direction.NORTH, BuildingType.HOUSE)


                .buildBuilding(28, 38, 8, 8, Direction.EAST, BuildingType.SHOP)
                .buildBuilding(28, 53, 8, 8, Direction.EAST, BuildingType.SHOP)
                .buildBuilding(38, 28, 8, 8, Direction.SOUTH, BuildingType.SHOP)
                .buildBuilding(53, 28, 8, 8, Direction.SOUTH, BuildingType.SHOP)
                .buildBuilding(63, 38, 8, 8, Direction.WEST, BuildingType.SHOP)
                .buildBuilding(63, 53, 8, 8, Direction.WEST, BuildingType.SHOP)
                .buildBuilding(38, 63, 8, 8, Direction.NORTH, BuildingType.SHOP)
                .buildBuilding(53, 63, 8, 8, Direction.NORTH, BuildingType.SHOP)
                .buildBuilding(70, 70, 28, 28, Direction.NORTH, BuildingType.KEEP)
                .makeTownsmen(20).build();
    }

}
