package com.bthorson.torule.entity;

import com.bthorson.torule.entity.ai.AiControllable;
import com.bthorson.torule.entity.ai.CreatureAI;
import com.bthorson.torule.entity.ai.MeanderAI;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.LocalType;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.World;
import com.bthorson.torule.persist.SerializeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: Ben Thorson
 * Date: 12/10/12
 * Time: 1:43 PM
 */
public class Herd extends Entity implements AiControllable, Updatable {

    private Point nwPoint;
    private Point sePoint;
    private Point point;
    private int radius;
    private Direction heading;
    private List<Creature> creatures;
    private CreatureAI ai;
    private long lastUpdateCounter;
    private boolean hasMovedThisUpdate;


    public Herd(Point center, List<Creature> creatures){
        heading = Direction.random();
        this.creatures = creatures;
        this.radius = (int)Math.sqrt(creatures.size()*3);
        adjustBorders(center);
    }

    private boolean adjustBorders(Point point) {
        if (point.subtract(radius).isOutOfBounds() || LocalType.TOWN.equals(World.getInstance().getLocal(PointUtil.toRegional(point)).getType())){
            point = point.add(radius);
        } else if (point.add(radius).isOutOfBounds() || LocalType.TOWN.equals(World.getInstance().getLocal(PointUtil.toRegional(point)).getType())){
            point = point.subtract(radius);
        }

        this.point = point;
        nwPoint = point.subtract(radius);
        sePoint = point.add(radius);
        return true;

    }

    @Override
    public void update(long turnCounter){
        removeDead();
        if (lastUpdateCounter == turnCounter){
            throw new RuntimeException("already attempted an update this turn");
        }

        if (allCreaturesInRange()){
            ai.execute();
        }
        lastUpdateCounter = turnCounter;
        hasMovedThisUpdate = false;

    }

    @Override
    public long getLastUpdate() {
        return lastUpdateCounter;
    }

    private void removeDead() {
        List<Creature> alive = new ArrayList<Creature>();
        for (Creature creature : creatures){
            if (!creature.dead()){
                alive.add(creature);
            }
        }
        creatures = alive;
    }

    private boolean allCreaturesInRange() {
        for (Creature creature: creatures){
            if (!creature.position().withinRect(nwPoint, sePoint)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean move(Point delta) {


        if (hasMovedThisUpdate){
            System.out.println(getName() + "tried to move twice in one turn");
            return false;
        }
        if (!World.getInstance().isTravelable(delta.add(this.point))){
            return false;
        }

        heading = Direction.directionOf(delta);
        Point moveAttempt = point.add(delta);
        for (Point pt : PointUtil.getPointsInRange(moveAttempt.subtract(radius), moveAttempt.add(radius))) {
            if (pt.isOutOfBounds() || LocalType.TOWN.equals(World.getInstance().getLocal(PointUtil.toRegional(pt)).getType())) {
                delta = heading.opposite().point();
                heading = heading.opposite();
                break;
            }
        }

        hasMovedThisUpdate = true;
        return adjustBorders(delta.add(this.point));

    }

    public Point getNwPoint() {
        return nwPoint;
    }

    public Point getSePoint() {
        return sePoint;
    }

    @Override
    public Set<Faction> getFactionEnemies() {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public List<Creature> getVisibleCreatures() {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public boolean canSee(Point position) {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public void attack(Creature entity) {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public Point position() {
        return point;
    }

    @Override
    public boolean dead() {
        return false;
    }

    @Override
    public Creature getLeader() {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public Point getTarget() {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public Faction getFaction() {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public Direction getHeading() {
        return heading;
    }

    @Override
    public boolean isEnemy(Creature facing) {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public Creature closestVisibleHostile() {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public boolean isHostile(Creature other) {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    @Override
    public boolean isWithinRange(Point point, int range) {
        throw new UnsupportedOperationException("herd does not determine aggro");
    }

    public void setAi(CreatureAI ai) {
        this.ai = ai;
    }

    public void setCreatures(List<Creature> creats) {
        this.creatures = creats;
    }

    @Override
    public JsonElement serialize() {
        Gson gson = new Gson();
        JsonObject obj = super.serialize().getAsJsonObject();
        obj.add("nwPoint", gson.toJsonTree(nwPoint));
        obj.add("sePoint", gson.toJsonTree(sePoint));
        obj.add("point", gson.toJsonTree(point));
        obj.addProperty("radius", radius);
        obj.add("heading", gson.toJsonTree(heading));
        SerializeUtils.serializeRefCollection(creatures, obj, "creatures");
        obj.add("ai", ai.serialize());
        obj.addProperty("lastUpdateCounter", lastUpdateCounter);
        obj.addProperty("hasMovedThisUpdate", hasMovedThisUpdate);
        return obj;
    }
}
