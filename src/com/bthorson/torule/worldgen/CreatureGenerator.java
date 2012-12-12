package com.bthorson.torule.worldgen;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.CreatureFactory;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.ai.GuardAI;
import com.bthorson.torule.entity.ai.PatrolAI;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.*;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;
import com.bthorson.torule.util.RandomUtils;
import com.bthorson.torule.worldgen.spawn.SpawnFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bthorson.torule.map.MapConstants.LOCAL_SIZE_POINT;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 1:10 AM
 */
public class CreatureGenerator {

    public void spawnCreaturesByFerocity(){

        Point localAmounts = World.getInstance().seCorner().divide(LOCAL_SIZE_POINT);
        for (int x = 0; x < localAmounts.x(); x++){
            for (int y = 0; y < localAmounts.y(); y++) {
                Point localPos = new Point(x,y);
                Local local = World.getInstance().getLocal(localPos);
                if (Arrays.asList(LocalType.WILDERNESS, LocalType.ROAD).contains(local.getType())){
                    int spawns = RandomUtils.getRandomInRange(3,10);
                    for (int i = 0; i < spawns; i++){
                        SpawnFactory.INSTANCE.createRandomCritterSpawn(PointUtil.randomPoint(local.getNwBoundWorldCoord(),
                                                                                             local.getSeBoundWorldBound()),
                                                                       local);
                    }

                    if (PointUtil.manhattanDist(localPos, EntityManager.getInstance().getNearestTown(localPos).getRegionalPosition()) < 3){
                        SpawnFactory.INSTANCE.createPatrolSpawn("cityGuards", perimeterPoints(localPos));
                    }
                } else if (LocalType.GOBLIN_CAMP.equals(local.getType())){
                    createForGoblinCamp(local);
                }
            }
        }
    }

    private void createForGoblinCamp(Local local) {
        for (int i = 0; i < 50; i++){
            Point candidate = PointUtil.randomPoint(local.getNwBoundWorldCoord(), local.getSeBoundWorldBound());
            if (!World.getInstance().isOccupied(candidate) && !candidate.equals(new Point(50,50))){
                Creature creature = CreatureFactory.INSTANCE.createCreature("goblin", candidate);
                creature.setAi(new WanderAI(creature, local.getNwBoundWorldCoord(), local.getSeBoundWorldBound(), null, true));
            }
        }
        SpawnFactory.INSTANCE.createPatrolSpawn("goblinDefenders", perimeterPoints(local.getNwBoundWorldCoord().divide(LOCAL_SIZE_POINT)));
        for (Local neighbor : local.getAllNeighbors()){
            for (int i = 0; i < 4; i++){
                SpawnFactory.INSTANCE.createPatrolSpawn("goblinScout", randomPatrol(neighbor));
            }
        }
    }

    private List<Point> randomPatrol(Local local) {
        List<Point> points = new ArrayList<Point>();
        for (int i = 0; i < 4; i++){
            points.add(PointUtil.randomPoint(local.getNwBoundWorldCoord().add(10), local.getSeBoundWorldBound().subtract(10)));
        }
        return points;
    }

    private List<Point> perimeterPoints(Point localPos) {
        Point toWorld = localPos.multiply(MapConstants.LOCAL_SIZE_POINT);
        List<Point> patrolPoints = new ArrayList<Point>();
        patrolPoints.add(toWorld.add(10));
        patrolPoints.add(toWorld.add(10,90));
        patrolPoints.add(toWorld.add(90,90));
        patrolPoints.add(toWorld.add(90,10));
        return patrolPoints;
    }

    public void createPlayer(String playerName, Town town){
        Point placement = town.getRegionalPosition().multiply(new Point(MapConstants.LOCAL_SIZE_X, MapConstants.LOCAL_SIZE_Y));
        Player player = (Player)CreatureFactory.INSTANCE.createCreature("player", placement.add(new Point(50, 50)));
        player.setName(playerName);
        EntityManager.getInstance().setPlayer(player);
    }

    public void makeTownsmen(Town town, int numberOfTownsmen){
        for (int i = 0; i < numberOfTownsmen; i++){
            Point candidate = PointUtil.randomPoint(town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound());
            if (!World.getInstance().isOccupied(candidate) && !candidate.equals(new Point(50,50))){
                Creature villager = CreatureFactory.INSTANCE.createCreature("villager", candidate);
                villager.setAi(new WanderAI(villager, town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound(), null, true));
            } else {
                i--;
            }
        }
    }

    public void createMayor(Town town){
        Point nwCorner = town.getRegionalPosition().multiply(MapConstants.LOCAL_SIZE_POINT);
        Creature mayor = CreatureFactory.INSTANCE.createCreature("mayor", nwCorner.add(84, 97));
        mayor.setAi(new GuardAI(mayor, mayor.position(), null));
    }

    public void makePuppies(Town town, int numberOfTownsmen){
        for (int i = 0; i < numberOfTownsmen; i++){
            Point candidate = PointUtil.randomPoint(town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound());
            if (!World.getInstance().isOccupied(candidate) && !candidate.equals(new Point(50,50))){
                Creature puppy = CreatureFactory.INSTANCE.createCreature("puppy", candidate);
                puppy.setAi(new WanderAI(puppy, town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound(), null, true));
            } else {
                i--;
            }

        }
    }

    public void createShopOwners(Town town, Building building){
        Creature shopOwner = CreatureFactory.INSTANCE.createCreature("merchant", building.getNwCorner().add(new Point(1, 1)));
        shopOwner.setFaction(town.getFaction());
        shopOwner.setAi(new WanderAI(shopOwner,
                                     building.getNwCorner().add(Direction.SOUTH_EAST.point()),
                                     building.getSeCorner().add(Direction.NORTH_WEST.point()), null, true));
        shopOwner.addOwnedProperty("shop", building);
        building.setOwner(shopOwner);
        for (Item item : building.getInventory()){
            item.setOwnedBy(shopOwner);
        }
    }

    public void createMilitia(Town town){

        Point nwCorner = town.getRegionalPosition().multiply(MapConstants.LOCAL_SIZE_POINT);
        String templateName = "cityGuard";

        for (Direction d : Direction.values()){
            if (town.getGates().get(d) != null){
                createGateGuards(town.getGates().get(d).add(nwCorner),
                                 templateName,
                                 Arrays.asList(Direction.EAST, Direction.WEST).contains(d));
            }
        }
        createPatrollers(town, templateName);

        createKeepGuards(templateName, nwCorner);

    }

    private void createKeepGuards(String templateName, Point offset) {
        Point row1 = offset.add(82, 74);
        Point row2 = offset.add(86, 74);

        for (int i = 0; i < 20; i+= 2){
            Creature guard1 = CreatureFactory.INSTANCE.createCreature(templateName, row1.add(0, i));
            Creature guard2 = CreatureFactory.INSTANCE.createCreature(templateName, row2.add(0,i));
            guard1.setAi(new GuardAI(guard1, guard1.position(), null));
            guard2.setAi(new GuardAI(guard2, guard2.position(), null));
        }
    }

    private void createGateGuards(Point gate, String templateName, boolean eastWest) {
        Point offset1 = eastWest ? new Point(0,-2) : new Point(-2, 0);
        Point offset2 = eastWest ? new Point(0,1) : new Point(1, 0);
        Creature guard1 = CreatureFactory.INSTANCE.createCreature(templateName, gate.add(offset1));
        Creature guard2 = CreatureFactory.INSTANCE.createCreature(templateName, gate.add(offset2));
        guard1.setAi(new GuardAI(guard1, guard1.position(), null));
        guard2.setAi(new GuardAI(guard2, guard2.position(), null));
    }

    private void createPatrollers(Town town, String templateName){
        Point nwCorner = town.getRegionalPosition().multiply(MapConstants.LOCAL_SIZE_POINT);
        List<Point> point = Arrays.asList(
                nwCorner.add(new Point(50, 83)),
                nwCorner.add(new Point(16, 83)),
                nwCorner.add(new Point(16, 16)),
                nwCorner.add(new Point(50, 16)),
                nwCorner.add(new Point(84, 16)),
                nwCorner.add(new Point(84, 50)),
                nwCorner.add(new Point(60, 50)),
                nwCorner.add(new Point(60, 60)),
                nwCorner.add(new Point(39, 60)),
                nwCorner.add(new Point(39, 39)),
                nwCorner.add(new Point(60, 39)),
                nwCorner.add(new Point(48, 48)));
        Creature guard1 = CreatureFactory.INSTANCE.createCreature(templateName, point.get(0));
        guard1.setAi(new PatrolAI(guard1, point, 0, null));
        Creature guard2 = CreatureFactory.INSTANCE.createCreature(templateName, point.get(5));
        guard2.setAi(new PatrolAI(guard2, point, 4, null));


    }


}
