package com.bthorson.torule.map;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.PhysicalEntity;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.worldgen.WorldGenParams;
import com.bthorson.torule.worldgen.WorldGenerator;
import com.bthorson.torule.worldgen.WorldLoader;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
    }

    public void startWorld(WorldLoader loader) {
        this.locals = loader.getLocals();
        this.width = loader.worldWidth();
        this.height = loader.worldHeight();
        seCorner = new Point(width, height);
        this.turnCounter = loader.getTurnCounter();
    }



    public Local getLocal(Point localGridPosition) {
        if (localGridPosition.x() >= 0 && localGridPosition.x() < locals.length && localGridPosition.y() >= 0  && localGridPosition.y() < locals[0].length){
            return locals[localGridPosition.x()][localGridPosition.y()];
        } else {
            return null;
        }
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
        writer.println("numberOfChunks " + locals.length + " " + locals[0].length);
        for (int x = 0; x < locals.length; x++){
            for (int y = 0; y < locals[0].length; y++){
                writer.println(locals[x][y].getType().name() + " " + x + " " + y);
                locals[x][y].serialize(writer);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        WorldGenParams params = new WorldGenParams();
        params.setNumCities(5);
        params.setWorldSize(new Point(1000,1000));
        params.setPlayerName("Bob");
        new WorldGenerator().generateWorld(params);
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
        setTile(position, Tile.OPEN_DOOR);
        openDoors.add(position);
    }

    private void closeDoor(Point position){
        setTile(position, Tile.DOOR);
    }

    public long getTurnCounter() {
        return turnCounter;
    }

    public void skipTurns(int turnsSkipped) {
        for (int i = 0; i < turnsSkipped; i++){
            if (EntityManager.getInstance().getPlayer().getHitpoints() < EntityManager.getInstance().getPlayer().getMaxHitpoints() &&
                                EntityManager.getInstance().getPlayer().closestVisibleHostile() == null) {
                update();
            } else {
                return;
            }
        }
    }

    public void setTile(Point position, Tile tile) {
        Local local = getLocal(position.divide(LOCAL_SIZE_POINT));
        Point offset = position.subtract(local.getNwBoundWorldCoord());
        local.getTiles()[offset.x()][offset.y()] = tile;
    }
}
